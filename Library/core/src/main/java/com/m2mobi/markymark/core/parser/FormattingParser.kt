/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 M2mobi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.m2mobi.markymark.core.parser

import com.m2mobi.markymark.core.model.FormattedText
import com.m2mobi.markymark.core.model.Formatting
import com.m2mobi.markymark.core.model.FormattingResult
import com.m2mobi.markymark.core.model.Styling
import com.m2mobi.markymark.core.parser.rule.FormattingRule

/**
 * Used for retrieving formatting from a line of text.
 *
 * This class transforms a line of text to a "clean" state and delegates the responsibility for parsing the formattings
 * to the [rules].
 */
class FormattingParser(private val rules: List<FormattingRule>) : Parser<FormattedText> {

    @Suppress("NestedBlockDepth")
    override fun parse(text: String): FormattedText {
        if (text.isNotBlank()) {
            var formattings: List<Formatting<Styling>> = emptyList()

            var remainingLine = text
            for (rule in rules) {
                var matches = rule.parse(remainingLine)
                while (matches.isNotEmpty()) {
                    val match = matches[0]

                    val content = remainingLine
                        .substring(match.formatting.range)
                        .drop(match.prefixLength)
                        .dropLast(match.suffixLength)
                    remainingLine = remainingLine.replaceRange(match.formatting.range, content)

                    matches = matches
                        .drop(1)
                        .map { it.copy(formatting = it.formatting.maybeOffset(match)) }
                    formattings = formattings
                        .plus(match.formatting)
                        .map { it.maybeOffset(match) }
                }
            }

            return FormattedText(cleanString = remainingLine, formattings = formattings)
        } else {
            return FormattedText(cleanString = text, formattings = emptyList())
        }
    }

    private fun Formatting<Styling>.maybeOffset(match: FormattingResult): Formatting<Styling> {
        return copy(range = range.maybeOffset(match))
    }

    private fun IntRange.maybeOffset(match: FormattingResult): IntRange {
        return first.maybeOffset(match)..last.maybeOffset(match)
    }

    private fun Int.maybeOffset(match: FormattingResult): Int {
        var newIndex = this
        if (this > match.formatting.range.first) {
            newIndex -= match.prefixLength
        }
        if (this > match.formatting.range.last) {
            newIndex -= match.suffixLength
        }
        return newIndex
    }
}
