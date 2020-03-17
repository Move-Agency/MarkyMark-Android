package com.m2mobi.markymarkcontentful.rules.inline

import com.m2mobi.markymark.item.inline.MarkdownString
import com.m2mobi.markymark.rules.InlineRule
import com.m2mobi.markymarkcommon.markdownitems.inline.LinkString
import java.util.regex.Pattern

/**
 * Rule for the markdown inline link
 */
object LinkRule : InlineRule {

    /**
     * A Regex to detect links. Ignores if they start with a "!" as those are image links.
     */
    override fun getRegex(): Pattern = Pattern.compile(PATTERN)

    override fun toMarkdownString(content: String): MarkdownString {
        val matcher = regex.matcher(content)
        return if (matcher.find()) {
            LinkString(
                url = matcher.group(2),
                title = matcher.group(3) ?: "",
                content = matcher.group(1)
            )
        } else {
            LinkString()
        }
    }

    private const val PATTERN = "(?<!!)\\[(.+?)]\\((.+?)(?: \"(.+?)\")?\\)"
}