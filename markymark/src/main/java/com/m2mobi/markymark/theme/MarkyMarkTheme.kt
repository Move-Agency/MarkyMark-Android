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

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.m2mobi.markymark.theme

import androidx.annotation.FloatRange
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily.Companion.Monospace
import androidx.compose.ui.text.font.FontFamily.Companion.SansSerif
import androidx.compose.ui.text.font.FontFamily.Companion.Serif
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m2mobi.markymark.annotator.DefaultMarkyMarkAnnotator
import com.m2mobi.markymark.composer.DefaultMarkyMarkComposer
import com.m2mobi.markymark.composer.Padding
import com.m2mobi.markymark.model.AnnotatedStableNode
import com.m2mobi.markymark.model.AnnotatedStableNode.Bold
import com.m2mobi.markymark.model.AnnotatedStableNode.Code
import com.m2mobi.markymark.model.AnnotatedStableNode.Italic
import com.m2mobi.markymark.model.AnnotatedStableNode.Link
import com.m2mobi.markymark.model.AnnotatedStableNode.Strikethrough
import com.m2mobi.markymark.model.AnnotatedStableNode.Subscript
import com.m2mobi.markymark.model.AnnotatedStableNode.Superscript
import com.m2mobi.markymark.model.ComposableStableNode
import com.m2mobi.markymark.model.ComposableStableNode.BlockQuote
import com.m2mobi.markymark.model.ComposableStableNode.CodeBlock
import com.m2mobi.markymark.model.ComposableStableNode.Headline
import com.m2mobi.markymark.model.ComposableStableNode.Image
import com.m2mobi.markymark.model.ComposableStableNode.ListBlock
import com.m2mobi.markymark.model.ComposableStableNode.ListItemType
import com.m2mobi.markymark.model.ComposableStableNode.Paragraph
import com.m2mobi.markymark.model.ComposableStableNode.Rule
import com.m2mobi.markymark.model.ComposableStableNode.TableBlock
import com.m2mobi.markymark.model.ComposableStableNode.TableCell
import com.m2mobi.markymark.model.ImmutableList
import com.m2mobi.markymark.model.StableNode
import com.m2mobi.markymark.model.immutableListOf
import com.m2mobi.markymark.theme.ListItemStyle.Companion.DefaultListItemIndicatorPadding
import com.m2mobi.markymark.theme.ListItemStyle.Companion.DefaultListItemPadding
import com.m2mobi.markymark.theme.ListItemStyle.Companion.DefaultListItemTextStyle
import com.m2mobi.markymark.theme.TextNodeStyle.Companion.BodyTextStyle
import com.m2mobi.markymark.theme.UnorderedListItemStyle.Indicator.Shape.Oval
import com.m2mobi.markymark.theme.UnorderedListItemStyle.Indicator.Shape.Rectangle
import com.m2mobi.markymark.theme.UnorderedListItemStyle.Indicator.Shape.Triangle
import com.m2mobi.markymark.theme.UnorderedListItemStyle.Indicator.Style.Fill

val LocalMarkyMarkTheme = staticCompositionLocalOf { MarkyMarkTheme() }

/**
 * Theming attributes used when rendering [StableNode]s with [DefaultMarkyMarkComposer.createNodes]. The
 * [composableStyles] are used when rendering [ComposableStableNode]s. The [annotatedStyles] are used when rendering
 * [AnnotatedStableNode]s.
 */
@Immutable
data class MarkyMarkTheme(
    val composableStyles: ComposableStyles = ComposableStyles(),
    val annotatedStyles: AnnotatedStyles = AnnotatedStyles(),
)

/**
 * Theming attributes used when rendering [ComposableStableNode]s with [DefaultMarkyMarkComposer.createNode]. The
 * pairings of style to [ComposableStableNode] to default composer function are as follows:
 *
 * - [headings] -> [Headline] -> [createHeadline][DefaultMarkyMarkComposer.createHeadline]
 * - [image] -> [Image] -> [createImage][DefaultMarkyMarkComposer.createImage]
 * - [paragraph] -> [Paragraph] -> [createParagraph][DefaultMarkyMarkComposer.createParagraph]
 * - [rule] -> [Rule] -> [createRule][DefaultMarkyMarkComposer.createRule]
 * - [codeBlock] -> [CodeBlock] -> [createCodeBlock][DefaultMarkyMarkComposer.createCodeBlock]
 * - [blockQuote] -> [BlockQuote] -> [createBlockQuote][DefaultMarkyMarkComposer.createBlockQuote]
 * - [tableBlock] -> [TableBlock] -> [createTableBlock][DefaultMarkyMarkComposer.createTableBlock]
 * - [listBlock] -> [ListBlock] -> [createListEntries][DefaultMarkyMarkComposer.createListEntries]
 * - [textNode] -> root [AnnotatedStableNode] -> [createTextNode][DefaultMarkyMarkComposer.createTextNode]
 */
@Immutable
data class ComposableStyles(
    val headings: HeadingsStyle = HeadingsStyle(),
    val image: ImageStyle = ImageStyle(),
    val paragraph: ParagraphStyle = ParagraphStyle(),
    val rule: RuleStyle = RuleStyle(),
    val codeBlock: CodeBlockStyle = CodeBlockStyle(),
    val blockQuote: BlockQuoteStyle = BlockQuoteStyle(),
    val tableBlock: TableBlockStyle = TableBlockStyle(),
    val listBlock: ListBlockStyle = ListBlockStyle(),
    val textNode: TextNodeStyle = TextNodeStyle(),
)

/**
 * Theming attributes used for rendering [Headline]s with [DefaultMarkyMarkComposer.createHeadline]. See
 * [HeadingLevelStyle] for more details.
 */
@Immutable
data class HeadingsStyle(
    val heading1: HeadingLevelStyle = HeadingLevelStyle(
        textStyle = BaseHeadingTextStyle.copy(fontSize = 52.sp),
    ),
    val heading2: HeadingLevelStyle = HeadingLevelStyle(
        textStyle = BaseHeadingTextStyle.copy(fontSize = 32.sp),
    ),
    val heading3: HeadingLevelStyle = HeadingLevelStyle(
        textStyle = BaseHeadingTextStyle.copy(fontSize = 26.sp),
    ),
    val heading4: HeadingLevelStyle = HeadingLevelStyle(
        textStyle = BaseHeadingTextStyle.copy(fontSize = 22.sp),
    ),
    val heading5: HeadingLevelStyle = HeadingLevelStyle(
        textStyle = BaseHeadingTextStyle.copy(fontSize = 18.sp),
    ),
    val heading6: HeadingLevelStyle = HeadingLevelStyle(
        textStyle = BaseHeadingTextStyle.copy(fontSize = 16.sp),
    ),
) {

    companion object {

        internal val BaseHeadingTextStyle = TextStyle(
            fontWeight = FontWeight.Bold,
            fontFamily = Serif,
        )
    }
}

/**
 * Theming attributes used when rendering [Headline.Level]s with [DefaultMarkyMarkComposer.createHeadline].
 */
@Immutable
data class HeadingLevelStyle(
    val padding: Padding = Padding(vertical = 4.dp),
    val textStyle: TextStyle,
)

/**
 * Theming attributes used when rendering [Image]s with [DefaultMarkyMarkComposer.createImage]. The bottom [padding]
 * will be applied below the caption. Use [captionPadding] to apply padding around the caption.
 *
 * *Note: Captions do not support inline formatting.*
 */
@Immutable
data class ImageStyle(
    val padding: Padding = Padding(),
    val captionPadding: Padding = Padding(top = 2.dp),
    val captionTextStyle: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontFamily = SansSerif,
        fontStyle = FontStyle.Italic,
    ),
    val captionAlignment: Alignment.Horizontal = Alignment.Start,
)

/**
 * Theming attributes used for rendering [Paragraph]s with [DefaultMarkyMarkComposer.createParagraph].
 */
@Immutable
data class ParagraphStyle(
    val padding: Padding = Padding(),
)

/**
 * Theming attributes used for rendering [Rule]s with [DefaultMarkyMarkComposer.createRule].
 */
@Immutable
data class RuleStyle(
    val color: Color = DarkGray,
    val thickness: Dp = 1.dp,
    val padding: Padding = Padding(vertical = 2.dp),
)

/**
 * Theming attributes used for rendering [CodeBlock]s with [DefaultMarkyMarkComposer.createCodeBlock].
 */
@Immutable
data class CodeBlockStyle(
    val innerPadding: Padding = Padding(all = 8.dp),
    val outerPadding: Padding = Padding(start = 8.dp),
    val background: Color = LightGray,
    val textStyle: TextStyle = TextStyle(fontFamily = Monospace),
)

/**
 * Theming attributes used for rendering [BlockQuote]s with [DefaultMarkyMarkComposer.createBlockQuote].
 *
 * *Note: The indicator is always draw at the __start__ of the quote, this is not configurable.*
 */
@Immutable
data class BlockQuoteStyle(
    val innerPadding: Padding = Padding(all = 8.dp),
    val outerPadding: Padding = Padding(start = 12.dp, top = 2.dp, bottom = 2.dp),
    val indicatorThickness: Dp = 2.dp,
    val indicatorTint: Color = Blue,
    val background: Color = LightGray,
)

/**
 * Theming attributes used for rendering [TableBlock]s with [DefaultMarkyMarkComposer.createTableBlock].
 *
 * The header is only ever the first row of the table as defined by the
 * [Github Flavoured Markdown](https://github.github.com/gfm/#tables-extension-) specification.
 *
 * The outline is the border __around__ the table. The header divider is the __horizontal__ divider __below__ the header
 * row. The body dividers are the horizontal and vertical dividers between cells.
 */
@Immutable
data class TableBlockStyle(
    val padding: Padding = Padding(),
    val cellMinWidth: Dp = 0.dp,
    val cellMaxWidth: Dp = Dp.Infinity,
    val cellMinHeight: Dp = 0.dp,
    val cellMaxHeight: Dp = Dp.Infinity,
    val headerStyle: TableCellStyle = TableCellStyle(background = LightGray),
    val bodyStyle: TableCellStyle = TableCellStyle(background = Transparent),
    val outlineStyle: TableDividerStyle = TableDividerStyle(
        thickness = 3.dp,
        color = Black,
    ),
    val headerDividerStyle: TableDividerStyle = TableDividerStyle(
        thickness = 2.dp,
        color = DarkGray,
    ),
    val bodyDividerStyle: TableDividerStyle = TableDividerStyle(
        thickness = 1.dp,
        color = Gray,
    ),
)

/**
 * Theming attributes used when rendering [TableCell]s. [padding] is the spacing around the text of a cell but with the
 * dividers around the cell.
 */
@Immutable
data class TableCellStyle(
    val padding: Padding = Padding(all = 4.dp),
    val textStyle: TextStyle = BodyTextStyle,
    val background: Color,
)

/**
 * Theming attributes used when rendering table dividers.
 */
@Immutable
data class TableDividerStyle(
    val thickness: Dp,
    val color: Color,
)

/**
 * Theming attributes used when rendering [ListBlock]s with [DefaultMarkyMarkComposer.createListEntries].
 */
@Immutable
data class ListBlockStyle(
    val padding: Padding = Padding(vertical = 2.dp),
    val levelIndent: Dp = 24.dp,
    val orderedStyle: OrderedListItemStyle = OrderedListItemStyle(),
    val unorderedStyle: UnorderedListItemStyle = UnorderedListItemStyle(),
    val taskStyle: TaskListItemStyle = TaskListItemStyle(),
)

/**
 * Theming attributes used when rendering [ListItemType]s with [DefaultMarkyMarkComposer.createListItem].
 */
@Immutable
interface ListItemStyle {

    val padding: Padding
    val textStyle: TextStyle
    val indicatorPadding: Padding

    companion object {

        internal val DefaultListItemPadding = Padding(start = 12.dp)
        internal val DefaultListItemTextStyle = BodyTextStyle
        internal val DefaultListItemIndicatorPadding = Padding(end = 12.dp)
    }
}

/**
 * Theming attributes used when rendering [ListItemType.Ordered].
 */
@Immutable
data class OrderedListItemStyle(
    override val padding: Padding = DefaultListItemPadding,
    override val textStyle: TextStyle = DefaultListItemTextStyle,
    override val indicatorPadding: Padding = DefaultListItemIndicatorPadding,
    val indicatorTextStyle: TextStyle = textStyle,
) : ListItemStyle

/**
 * Theming attributes used when rendering [ListItemType.Unordered].
 */
@Immutable
data class UnorderedListItemStyle(
    override val padding: Padding = DefaultListItemPadding,
    override val textStyle: TextStyle = DefaultListItemTextStyle,
    override val indicatorPadding: Padding = DefaultListItemIndicatorPadding,
    val indicators: ImmutableList<Indicator> = immutableListOf(
        Indicator(shape = Oval),
        Indicator(shape = Triangle),
        Indicator(shape = Rectangle),
        Indicator(shape = Rectangle, size = DpSize(width = 6.dp, height = 2.dp)), // Line
    ),
) : ListItemStyle {

    /**
     * Theming attributes used when rendering a [ListItemType.Unordered]'s indicator.
     */
    @Immutable
    data class Indicator(
        val shape: Shape,
        val color: Color = Black,
        val size: DpSize = DpSize(width = 6.dp, height = 6.dp),
        val style: Style = Fill,
        /**
         * A value between 0 and 360 (a.k.a the degrees of a circle).
         */
        @FloatRange(from = 0.0, to = 360.0) val rotation: Float = 0F,
    ) {

        /**
         * The shape of the indicator. Only supports ovals and rectangles.
         *
         * - Lines can be achieved by specifying an [Rectangle] with a height much smaller than its width.
         * - Diamonds can be achieved by specifying a [Rectangle] with a height and width that are equal (a square) and
         *   rotating it 45°.
         */
        enum class Shape {

            Oval,
            Rectangle,
            Triangle,
        }

        /**
         * MarkyMark implementation of [DrawStyle]. This is needed because
         * [Compose's Stroke][androidx.compose.ui.graphics.drawscope.Stroke] can only be created with px values.
         */
        @Stable
        sealed class Style {

            /**
             * Same as [Compose's Fill][androidx.compose.ui.graphics.drawscope.Fill]
             */
            @Immutable
            object Fill : Style()

            /**
             * Same as [Compose's Stroke][androidx.compose.ui.graphics.drawscope.Stroke] except it can be created with
             * [Dp] values.
             */
            @Immutable
            data class Stroke(val thickness: Dp = 1.dp) : Style()
        }
    }
}

/**
 * Theming attributes used when rendering [ListItemType.Task].
 */
@Immutable
data class TaskListItemStyle(
    override val padding: Padding = DefaultListItemPadding,
    override val textStyle: TextStyle = DefaultListItemTextStyle,
    override val indicatorPadding: Padding = DefaultListItemIndicatorPadding,
    val tints: Tints = Tints(),
) : ListItemStyle {

    /**
     * Similar to [Material3's CheckboxColors][androidx.compose.material3.CheckboxColors] which can only be created
     * inside a composable function. Only allows for specifying the relevant tints.
     */
    @Immutable
    data class Tints(
        val checkedColor: Color = Blue,
        val uncheckedColor: Color = Black,
        val checkmarkColor: Color = White,
    )
}

/**
 * Theming attributes used when rendering root [AnnotatedStableNode]s with [DefaultMarkyMarkComposer.createTextNode].
 */
@Immutable
data class TextNodeStyle(
    val padding: Padding = Padding(),
    val textStyle: TextStyle = BodyTextStyle,
) {

    companion object {

        internal val BodyTextStyle = TextStyle(fontSize = 18.sp, fontFamily = SansSerif)
    }
}

/**
 * [SpanStyle]s used when rendering [AnnotatedStableNode]s. The pairings of [SpanStyle] to [AnnotatedStableNode] to
 * default annotator function are as follows:
 *
 * - [boldStyle] -> [Bold] -> [annotateBold][DefaultMarkyMarkAnnotator.annotateBold]
 * - [codeStyle] -> [Code] -> [annotateCode][DefaultMarkyMarkAnnotator.annotateCode]
 * - [italicStyle] -> [Italic] -> [annotateItalic][DefaultMarkyMarkAnnotator.annotateItalic]
 * - [strikethroughStyle] -> [Strikethrough] -> [annotateStrikethrough][DefaultMarkyMarkAnnotator.annotateStrikethrough]
 * - [linkStyle] -> [Link] -> [annotateLink][DefaultMarkyMarkAnnotator.annotateLink]
 * - [subscriptStyle] -> [Subscript] -> [annotateSubscript][DefaultMarkyMarkAnnotator.annotateSubscript]
 * - [superscriptStyle] -> [Superscript] -> [annotateSuperScript][DefaultMarkyMarkAnnotator.annotateSuperscript]
 *
 * *Note: It's best not to add [Underline] to the [linkStyle] as [LineThrough] and [Underline] are mutually exclusive.*
 */
@Immutable
data class AnnotatedStyles(
    val boldStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.Bold),
    val codeStyle: SpanStyle = SpanStyle(fontFamily = Monospace, background = LightGray),
    val italicStyle: SpanStyle = SpanStyle(fontStyle = FontStyle.Italic),
    val strikethroughStyle: SpanStyle = SpanStyle(textDecoration = LineThrough),
    val linkStyle: SpanStyle = SpanStyle(color = DefaultLinkColor),
    val subscriptStyle: SpanStyle = SpanStyle(baselineShift = BaselineShift.Subscript, fontSize = DefaultSubscriptSize),
    val superscriptStyle: SpanStyle = SpanStyle(
        baselineShift = BaselineShift.Superscript,
        fontSize = DefaultSuperscriptSize,
    ),
) {

    companion object {

        private val DefaultLinkColor = Color(0xFF66AAFF)
        private val DefaultSubscriptSize = 14.sp
        private val DefaultSuperscriptSize = 10.sp
    }
}
