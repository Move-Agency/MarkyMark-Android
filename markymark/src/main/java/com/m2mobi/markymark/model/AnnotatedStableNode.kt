/*
 * Copyright © 2022 M2mobi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package com.m2mobi.markymark.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed class AnnotatedStableNode : StableNode {

    /**
     * Represents Markdown text containing other formatting.
     */
    @Immutable
    data class ParagraphText(val children: List<AnnotatedStableNode>) : AnnotatedStableNode()

    /**
     * Represents Markdown text without other formatting.
     */
    @Immutable
    data class Text(val content: String) : AnnotatedStableNode()

    /**
     * Represents Markdown text with bold formatting. Mapped from [StrongEmphasis][com.vladsch.flexmark.ast.StrongEmphasis].
     *
     * __Syntax:__
     *
     * ```markdown
     * some text __bold__ other text.
     * some text **bold** other text.
     * ```
     *
     * For more details see the [Markdown guide](https://www.markdownguide.org/basic-syntax#bold).
     */
    @Immutable
    data class Bold(val children: List<AnnotatedStableNode>) : AnnotatedStableNode()

    /**
     * Represents Markdown text with italic formatting. Mapped from [Emphasis][com.vladsch.flexmark.ast.Emphasis].
     *
     * __Syntax:__
     *
     * ```markdown
     * some text _italic_ other text.
     * some text *italic* other text.
     * ```
     *
     * For more details see the [Markdown guide](https://www.markdownguide.org/basic-syntax#italic).
     */
    @Immutable
    data class Italic(val children: List<AnnotatedStableNode>) : AnnotatedStableNode()

    /**
     * Represents Github Flavoured Markdown struck out text. Mapped from [Striketrough][com.vladsch.flexmark.ext.gfm.strikethrough.Strikethrough].
     *
     * __Syntax:__
     *
     * ```markdown
     * some text ~~struck out~~ other text.
     * ```
     *
     * For more details see the [Github Flavoured Markdown guide](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax#styling-text).
     */
    @Immutable
    data class Strikethrough(val children: List<AnnotatedStableNode>) : AnnotatedStableNode()

    /**
     * Represents Markdown text with inline code formatting. Mapped from [Code][com.vladsch.flexmark.ast.Code].
     *
     * __Syntax:__
     *
     * ```markdown
     * some text `code` other text.
     * ```
     *
     * For more details see the [Markdown guide](https://www.markdownguide.org/basic-syntax#code).
     */
    @Immutable
    data class Code(val children: List<AnnotatedStableNode>) : AnnotatedStableNode()

    /**
     * Represents Markdown text with link formatting. Mapped from [Link][com.vladsch.flexmark.ast.Link] or
     * [AutoLink][com.vladsch.flexmark.ast.AutoLink].
     *
     * __Syntax:__
     *
     * ```markdown
     * [text](https://www.m2mobi.com/ "title")
     * [text](https://www.m2mobi.com/)
     * <https://www.m2mobi.com/>
     * https://www.m2mobi.com
     * ```
     *
     * For more details see the [Markdown guide](https://www.markdownguide.org/basic-syntax#code).
     */
    @Immutable
    data class Link(
        val children: List<AnnotatedStableNode>,
        val url: String,
        val title: String?,
    ) : AnnotatedStableNode()

    /**
     * Represents Markdown text with email formatting. Mapped from [MailLink][com.vladsch.flexmark.ast.MailLink].
     *
     * __Syntax:__
     *
     * ```markdown
     * <info@m2mobi.com>
     * jobs@m2mobi.com
     * ```
     *
     * For more details see the [Markdown guide](https://www.markdownguide.org/basic-syntax#urls-and-email-addresses).
     */
    @Immutable
    data class EmailLink(val email: String) : AnnotatedStableNode()

    /**
     * Represents a soft break in Markdown text.
     */
    @Immutable
    object SoftLineBreak : AnnotatedStableNode()

    /**
     * Represents Markdown text with subscript formatting. Mapped from [Subscript][com.vladsch.flexmark.ext.gfm.strikethrough.Subscript].
     *
     * __Syntax:__
     *
     * ```markdown
     * some text ~subscript~ other text.
     * ```
     *
     * For more details see the [Markdown guide](https://www.markdownguide.org/extended-syntax/#subscript).
     */
    @Immutable
    data class Subscript(val children: List<AnnotatedStableNode>) : AnnotatedStableNode()

    /**
     * Represents Markdown text with superscript formatting. Mapped from [Superscript][com.vladsch.flexmark.ext.superscript.Superscript].
     *
     * __Syntax:__
     *
     * ```markdown
     * some text ^superscript^ other text.
     * ```
     *
     * For more details see the [Markdown guide](https://www.markdownguide.org/extended-syntax/#superscript).
     */
    @Immutable
    data class Superscript(val children: List<AnnotatedStableNode>) : AnnotatedStableNode()
}
