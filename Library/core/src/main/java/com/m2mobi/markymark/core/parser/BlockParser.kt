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

import com.m2mobi.markymark.core.model.Block
import com.m2mobi.markymark.core.model.BlockResult
import com.m2mobi.markymark.core.model.FormattedText
import com.m2mobi.markymark.core.parser.rule.BlockRule

/*
 * TODO: Optimize to only iterate over rules once. Possible by using a similar structure formatting. The block rules
 *  would parse all instances of themselves in the text and return these. You need to mark what their place was in the
 *  document. Then after each rule has been parsed you remove the section from the lines. The same offset handling
 *  as in the formatting should be used.
 */

/**
 * Used for retrieving blocks from a [String] containing lines of text.
 *
 * This class only handles iterating over the lines and delegates parsing to the [rules].
 *
 * This class drops lines once they have been parsed. If the next first line is blank it gets removed. A [BlockRule]
 * should therefor never check if the first line is empty.
 */
class BlockParser(
    private val rules: List<BlockRule>,
    private val formattingParser: Parser<FormattedText>
) : Parser<List<Block>> {

    @Suppress("NestedBlockDepth")
    override fun parse(text: String): List<Block> {
        var lines = text.lines()
        val blocks = mutableListOf<Block>()

        outer@ while (lines.isNotEmpty()) {
            if (lines[0].isBlank()) {
                lines = lines.drop(1)
            } else {
                for (rule in rules) {
                    val result = rule.parse(lines, formattingParser)
                    if (result is BlockResult.BlockMatch) {
                        lines = handleParsingMatch(blocks, lines, result)
                        continue@outer
                    }
                }
                handleNoRulesApplied(lines[0])
            }
        }

        return blocks
    }

    private fun handleParsingMatch(
        items: MutableList<Block>,
        lines: List<String>,
        match: BlockResult.BlockMatch
    ): List<String> {
        items += match.block
        return lines.drop(match.linesParsed)
    }

    private fun handleNoRulesApplied(currentLine: String) {
        throw IllegalStateException(
            """
                No rules applied. Throwing to prevent infinite loop.
                Breaking line: 
                $currentLine
            """.trimIndent()
        )
    }
}
