package com.m2mobi.markymarkcontentful.rules.inline

import com.m2mobi.markymark.item.inline.MarkdownString
import com.m2mobi.markymark.rules.InlineRule
import com.m2mobi.markymarkcommon.markdownitems.inline.LinkString
import java.util.regex.Pattern

/**
 * Rule for the markdown inline link
 */
class LinkRule : InlineRule {

    /**
     * A Regex to detect links. Ignores if they start with a "!" as those are image links.
     */
    override fun getRegex(): Pattern = Pattern.compile(PATTERN)

    override fun toMarkdownString(content: String): MarkdownString {
        val matcher = regex.matcher(content)
        return if (matcher.find()) {
            LinkString(matcher.group(1), matcher.group(2), false)
        } else {
            LinkString("", "", false)
        }
    }

    companion object {

        private const val PATTERN = "(?<!!)\\[(.+?)]\\((.+?)\\)"
    }
}