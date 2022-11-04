/*
 * Copyright © 2022 Move
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

package com.moveagency.markymark.sample.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily.Companion.Monospace
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moveagency.markymark.sample.ui.theme.ColorPalette.M2Black
import com.moveagency.markymark.sample.ui.theme.ColorPalette.M2Blue
import com.moveagency.markymark.sample.ui.theme.ColorPalette.M2Gray
import com.moveagency.markymark.sample.ui.theme.ColorPalette.M2LightGray
import com.moveagency.markymark.sample.ui.theme.ColorPalette.M2Orange
import com.moveagency.markymark.sample.ui.theme.Typography.Body
import com.moveagency.markymark.sample.ui.theme.Typography.Heading1
import com.moveagency.markymark.sample.ui.theme.Typography.Heading2
import com.moveagency.markymark.sample.ui.theme.Typography.Heading3
import com.moveagency.markymark.sample.ui.theme.Typography.Heading4
import com.moveagency.markymark.sample.ui.theme.Typography.Heading5
import com.moveagency.markymark.sample.ui.theme.Typography.Heading6
import com.moveagency.markymark.theme.AnnotatedStyles
import com.moveagency.markymark.theme.BlockQuoteStyle
import com.moveagency.markymark.theme.CodeBlockStyle
import com.moveagency.markymark.theme.ComposableStyles
import com.moveagency.markymark.theme.HeadingLevelStyle
import com.moveagency.markymark.theme.HeadingsStyle
import com.moveagency.markymark.theme.ImageStyle
import com.moveagency.markymark.theme.ListBlockStyle
import com.moveagency.markymark.theme.LocalMarkyMarkTheme
import com.moveagency.markymark.theme.MarkyMarkTheme
import com.moveagency.markymark.theme.OrderedListItemStyle
import com.moveagency.markymark.theme.RuleStyle
import com.moveagency.markymark.theme.TableBlockStyle
import com.moveagency.markymark.theme.TableCellStyle
import com.moveagency.markymark.theme.TableDividerStyle
import com.moveagency.markymark.theme.TaskListItemStyle
import com.moveagency.markymark.theme.TaskListItemStyle.Tints
import com.moveagency.markymark.theme.TextNodeStyle
import com.moveagency.markymark.theme.UnorderedListItemStyle
import com.moveagency.markymark.theme.UnorderedListItemStyle.Indicator
import com.moveagency.markymark.theme.UnorderedListItemStyle.Indicator.Shape.Oval
import com.moveagency.markymark.theme.UnorderedListItemStyle.Indicator.Shape.Rectangle
import com.moveagency.markymark.theme.UnorderedListItemStyle.Indicator.Shape.Triangle
import com.moveagency.markymark.theme.UnorderedListItemStyle.Indicator.Style.Stroke
import kotlinx.collections.immutable.persistentListOf

private val SampleMarkyMarkTheme = MarkyMarkTheme(
    composableStyles = ComposableStyles(
        headings = HeadingsStyle(
            heading1 = HeadingLevelStyle(textStyle = Heading1),
            heading2 = HeadingLevelStyle(textStyle = Heading2),
            heading3 = HeadingLevelStyle(textStyle = Heading3),
            heading4 = HeadingLevelStyle(textStyle = Heading4),
            heading5 = HeadingLevelStyle(textStyle = Heading5),
            heading6 = HeadingLevelStyle(textStyle = Heading6),
        ),
        image = ImageStyle(captionTextStyle = Body.copy(fontSize = 12.sp, fontStyle = Italic)),
        rule = RuleStyle(color = M2Orange),
        codeBlock = CodeBlockStyle(
            background = M2LightGray,
            textStyle = Body.copy(fontFamily = Monospace),
        ),
        blockQuote = BlockQuoteStyle(
            indicatorTint = M2Orange,
            background = Color.Transparent,
        ),
        tableBlock = TableBlockStyle(
            headerStyle = TableCellStyle(background = M2LightGray),
            outlineStyle = TableDividerStyle(thickness = 3.dp, color = M2Black),
            headerDividerStyle = TableDividerStyle(thickness = 2.dp, color = M2Orange),
            bodyDividerStyle = TableDividerStyle(thickness = 1.dp, color = M2Gray),
        ),
        listBlock = ListBlockStyle(
            orderedStyle = OrderedListItemStyle(textStyle = Body),
            unorderedStyle = UnorderedListItemStyle(
                textStyle = Body,
                indicators = persistentListOf(
                    Indicator(
                        // Diamond
                        color = M2Orange,
                        shape = Rectangle,
                        rotation = 45F,
                    ),
                    Indicator(
                        color = M2Orange,
                        shape = Oval,
                        style = Stroke(thickness = 1.dp),
                    ),
                    Indicator(
                        color = M2Orange,
                        shape = Triangle,
                    ),
                    Indicator(
                        // Line
                        color = M2Orange,
                        shape = Rectangle,
                        size = DpSize(width = 6.dp, height = 2.dp),
                    ),
                )
            ),
            taskStyle = TaskListItemStyle(
                tints = Tints(
                    checkedColor = M2Orange,
                    uncheckedColor = M2Black,
                )
            ),
        ),
        textNode = TextNodeStyle(textStyle = Body),
    ),
    annotatedStyles = AnnotatedStyles(
        linkStyle = SpanStyle(color = M2Blue),
        codeStyle = SpanStyle(fontFamily = Monospace, background = M2LightGray),
    )
)

@Composable
fun SampleTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalMarkyMarkTheme provides SampleMarkyMarkTheme) {
        content()
    }
}
