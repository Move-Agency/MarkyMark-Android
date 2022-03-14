package com.m2mobi.basemarkdown.rule.inline

import com.m2mobi.markymarkcommon.markdownitems.inline.LinkString
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ShortLinkRuleTest {

    private val linkRule = ShortLinkRule(enableUrlCheck = false)

    private val linkRuleForUrl = ShortLinkRule(enableUrlCheck = true)

    @ParameterizedTest(name = "Given a short link for {0}, when parsed to markdown string, a link string is returned")
    @MethodSource("validLinks")
    fun `Given a short link without a title, when parsed to a markdown string, a link string is returned for the link`(
        link: String
    ) {
        // Given
        val mdLink = "<$link>"

        // When
        val result = linkRule.toMarkdownString(mdLink)

        // Then
        val expected = LinkString(
            url = link,
            title = link,
            content = link,
            isChildrenEnabled = false
        )
        assertEquals(expected = expected, actual = result)
    }

    @ParameterizedTest(name = "Given link {0} is surrounded by text, when parsed to a markdown string, a link string is returned for the link")
    @MethodSource("validLinks")
    fun `Given a text with a link surrounded by text, when parsed to a markdown string, a link string is returned for the link`(
        link: String
    ) {
        // Given
        val mdText = "Some thing interesting <$link> some thing else interesting"

        // When
        val result = linkRule.toMarkdownString(mdText)

        // Then
        val expected = LinkString(
            url = link,
            title = link,
            content = link,
            isChildrenEnabled = false
        )
        assertEquals(expected = expected, actual = result)
    }

    @ParameterizedTest(name = "Given short link {0} is surrounded by text, when parsed to markdown string, a link string is returned")
    @MethodSource("validLinks")
    fun `Given a short link to a valid url, when checking the regex url check enabled, a match is found`(
        validLink: String
    ) {
        // Given
        val mdLink = "<$validLink>"

        // When
        val result = linkRuleForUrl.regex.matcher(mdLink).find()

        // Then
        assertTrue(result)
    }

    @ParameterizedTest(name = "Given a short link to {0}, when checking the regex with url check enabled, no match is found")
    @MethodSource("invalidLinks")
    fun `Given a short link to an invalid url, when checking the regex with url check enabled, no match is found`(
        invalidLink: String
    ) {
        // Given
        val mdLink = "<$invalidLink>"

        // When
        val result = linkRuleForUrl.regex.matcher(mdLink).find()

        // Then
        assertFalse(result)
    }

    @ParameterizedTest(name = "Given a short link to {0},  when checking the regex without url check, no match is found")
    @MethodSource("invalidLinks")
    fun `Given a short link to an invalid url, when checking the regex without url check, a match is found`(
        invalidLink: String
    ) {
        // Given
        val mdLink = "<$invalidLink>"

        // When
        val result = linkRule.regex.matcher(mdLink).find()

        // Then
        assertTrue(result)
    }

    @Suppress("unused")
    companion object {

        @JvmStatic
        fun validLinks() = listOf(
            Arguments.of("https://www.google.com"),
            Arguments.of("https://www.m2mobi.com/about/"),
            Arguments.of("ftp://something.idk"),
            Arguments.of("tel://1234567890"),
            Arguments.of("myapp://feature/deeplink"),
            Arguments.of("spotify://user/playlist/1234")
        )

        @JvmStatic
        fun invalidLinks() = listOf(
            Arguments.of("google.com"),
            Arguments.of("https:// google.com"),
            Arguments.of("just some text"),
            Arguments.of("123567890")
        )
    }
}
