/*
 * Copyright © 2024 Move
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

@file:Suppress("DataClassPrivateConstructor")

package com.moveagency.markymark.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import com.moveagency.markymark.annotator.DefaultMarkyMarkAnnotator
import com.moveagency.markymark.model.annotated.*

/**
 * [SpanStyle]s used when rendering [AnnotatedStableNode]s. The pairings of [SpanStyle] to [AnnotatedStableNode] to
 * default annotator function are as follows:
 *
 * - [bold] -> [Bold] -> [annotateBold][DefaultMarkyMarkAnnotator.annotateBold]
 * - [code] -> [Code] -> [annotateCode][DefaultMarkyMarkAnnotator.annotateCode]
 * - [italic] -> [Italic] -> [annotateItalic][DefaultMarkyMarkAnnotator.annotateItalic]
 * - [strikethrough] -> [Strikethrough] -> [annotateStrikethrough][DefaultMarkyMarkAnnotator.annotateStrikethrough]
 * - [link] -> [BrowserLink] -> [annotateLink][DefaultMarkyMarkAnnotator.annotateLink]
 * - [subscript] -> [Subscript] -> [annotateSubscript][DefaultMarkyMarkAnnotator.annotateSubscript]
 * - [superscript] -> [Superscript] -> [annotateSuperScript][DefaultMarkyMarkAnnotator.annotateSuperscript]
 *
 * *Note: [LineThrough] and [Underline] are mutually exclusive so if you have a [link] that specifies [Underline]
 * and a link that's part of a [LineThrough] then only one of the two will be rendered.*
 */
@Immutable
data class AnnotatedStyles private constructor(
    val bold: SpanStyle,
    val code: SpanStyle,
    val italic: SpanStyle,
    val strikethrough: SpanStyle,
    val link: SpanStyle,
    val subscript: SpanStyle,
    val superscript: SpanStyle,
) {

    /**
     * Builder class for constructing an instance of [AnnotatedStyles].
     * This class allows you to define custom [SpanStyle]s for each text style, such as bold, italic, etc.
     *
     * Use the provided properties to customize the appearance of text in your annotated nodes.
     */
    @MarkyMarkThemeBuilderMarker
    class Builder {

        /**
         * [SpanStyle] to be applied to bold text.
         * The default value is an empty [SpanStyle].
         */
        var bold = SpanStyle()

        /**
         * [SpanStyle] to be applied to code text.
         * The default value is an empty [SpanStyle].
         */
        var code = SpanStyle()

        /**
         * [SpanStyle] to be applied to italic text.
         * The default value is an empty [SpanStyle].
         */
        var italic = SpanStyle()

        /**
         * [SpanStyle] to be applied to strikethrough text.
         * The default value is an empty [SpanStyle].
         */
        var strikethrough = SpanStyle()

        /**
         * [SpanStyle] to be applied to links.
         * The default value is an empty [SpanStyle].
         */
        var link = SpanStyle()

        /**
         * [SpanStyle] to be applied to subscript text.
         * The default value is an empty [SpanStyle].
         */
        var subscript = SpanStyle()

        /**
         * [SpanStyle] to be applied to superscript text.
         * The default value is an empty [SpanStyle].
         */
        var superscript = SpanStyle()

        /**
         * Copies all styles from another [Builder] instance into this builder.
         *
         * @param builder The [Builder] instance to copy styles from.
         */
        fun include(builder: Builder) {
            bold = builder.bold
            code = builder.code
            italic = builder.italic
            strikethrough = builder.strikethrough
            link = builder.link
            subscript = builder.subscript
            superscript = builder.superscript
        }

        /**
         * Copies all styles from an existing [AnnotatedStyles] instance into this builder.
         *
         * @param styles The [AnnotatedStyles] instance to copy styles from.
         */
        fun include(styles: AnnotatedStyles) {
            bold = styles.bold
            code = styles.code
            italic = styles.italic
            strikethrough = styles.strikethrough
            link = styles.link
            subscript = styles.subscript
            superscript = styles.superscript
        }

        /**
         * Builds and returns an [AnnotatedStyles] instance with the current styles.
         *
         * @return A new [AnnotatedStyles] instance with the specified styles.
         */
        internal fun build() = AnnotatedStyles(
            bold = bold,
            code = code,
            italic = italic,
            strikethrough = strikethrough,
            link = link,
            subscript = subscript,
            superscript = superscript,
        )
    }
}
