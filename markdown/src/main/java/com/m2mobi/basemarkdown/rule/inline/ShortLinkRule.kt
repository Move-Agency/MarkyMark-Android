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

package com.m2mobi.basemarkdown.rule.inline

import com.m2mobi.markymark.item.inline.MarkdownString
import com.m2mobi.markymark.rules.InlineRule
import com.m2mobi.markymarkcommon.markdownitems.inline.LinkString
import java.util.regex.Pattern

/**
 * Rule for the markdown short links without
 * @param enableUrlCheck when enabled the rule will only be applied when linking to a valid URL
 */
class ShortLinkRule(enableUrlCheck: Boolean = true) : InlineRule {

    private val pattern = if (enableUrlCheck) {
        Pattern.compile(PATTERN_WITH_URL_CHECK)
    } else {
        Pattern.compile(SIMPLE_PATTERN)
    }

    /**
     * A Regex to detect short links containing a URL.
     */
    override fun getRegex(): Pattern = pattern

    override fun toMarkdownString(content: String): MarkdownString {
        val matcher = regex.matcher(content)
        return if (matcher.find()) {
            val url = matcher.group(1)
            LinkString(
                url = url,
                title = url,
                content = url
            )
        } else {
            LinkString()
        }
    }

    companion object {

        /**
         * Simple URL pattern checking for a scheme and some text. The goal is to prevent the
         * pattern from accidentally matching for non-links, not have a perfect URL validation.
         */
        private const val URL_PATTERN = "\\w+://[^\\s/\$.?#].[^\\s]*"

        /**
         * The markdown short link pattern with an additional restriction of checking for a URL.
         */
        private const val PATTERN_WITH_URL_CHECK = "<($URL_PATTERN)>"

        /**
         * The markdown short link pattern with an additional restriction of checking for a URL.
         */
        private const val SIMPLE_PATTERN = "<(.+?)>"
    }
}
