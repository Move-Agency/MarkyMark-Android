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

package com.moveagency.markymark.sample.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily.Companion.Monospace
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moveagency.markymark.composer.Padding
import com.moveagency.markymark.sample.ui.theme.ColorPalette.Black
import com.moveagency.markymark.sample.ui.theme.ColorPalette.FirstAndBeyondRed
import com.moveagency.markymark.sample.ui.theme.ColorPalette.Grey
import com.moveagency.markymark.sample.ui.theme.ColorPalette.LekkerLila
import com.moveagency.markymark.sample.ui.theme.ColorPalette.LightGrey
import com.moveagency.markymark.sample.ui.theme.ColorPalette.OceanBlue
import com.moveagency.markymark.sample.ui.theme.ColorPalette.OrbYellow
import com.moveagency.markymark.sample.ui.theme.ColorPalette.WarmLightGrey
import com.moveagency.markymark.sample.ui.theme.ColorPalette.White
import com.moveagency.markymark.sample.ui.theme.Shape.CodeBlockShape
import com.moveagency.markymark.sample.ui.theme.Shape.QuoteShape
import com.moveagency.markymark.sample.ui.theme.Typography.Body
import com.moveagency.markymark.sample.ui.theme.Typography.Caption
import com.moveagency.markymark.sample.ui.theme.Typography.Heading1
import com.moveagency.markymark.sample.ui.theme.Typography.Heading2
import com.moveagency.markymark.sample.ui.theme.Typography.Heading3
import com.moveagency.markymark.sample.ui.theme.Typography.Heading4
import com.moveagency.markymark.sample.ui.theme.Typography.Heading5
import com.moveagency.markymark.sample.ui.theme.Typography.Heading6
import com.moveagency.markymark.sample.ui.theme.Typography.TableHeader
import com.moveagency.markymark.theme.*
import com.moveagency.markymark.theme.TaskListItemStyle.Tints
import com.moveagency.markymark.theme.UnorderedListItemStyle.Indicator
import com.moveagency.markymark.theme.UnorderedListItemStyle.Indicator.Shape.*
import com.moveagency.markymark.theme.UnorderedListItemStyle.Indicator.Style.Fill
import com.moveagency.markymark.theme.UnorderedListItemStyle.Indicator.Style.Stroke
import kotlinx.collections.immutable.persistentListOf

private val QuoteBackgrounds = persistentListOf(LekkerLila, FirstAndBeyondRed, OrbYellow)

private val LightMarkyMarkTheme = MarkyMarkTheme(
    composableStyles = ComposableStyles(
        headings = HeadingsStyle(
            heading1 = HeadingLevelStyle(
                textStyle = Heading1.copy(color = Black),
                padding = Padding(top = Spacing.x1, bottom = Spacing.x1_5),
            ),
            heading2 = HeadingLevelStyle(
                textStyle = Heading2.copy(color = Black),
                padding = Padding(top = Spacing.x1, bottom = Spacing.x1_5),
            ),
            heading3 = HeadingLevelStyle(
                textStyle = Heading3.copy(color = Black),
                padding = Padding(top = Spacing.x1, bottom = Spacing.x1_5),
            ),
            heading4 = HeadingLevelStyle(
                textStyle = Heading4.copy(color = Black),
                padding = Padding(top = Spacing.x0_5, bottom = Spacing.x1),
            ),
            heading5 = HeadingLevelStyle(
                textStyle = Heading5.copy(color = Black),
                padding = Padding(top = Spacing.x0_5, bottom = Spacing.x1),
            ),
            heading6 = HeadingLevelStyle(
                textStyle = Heading6.copy(color = Black),
                padding = Padding(top = Spacing.x0_5, bottom = Spacing.x1),
            ),
        ),
        image = ImageStyle(captionTextStyle = Caption.copy(color = Black)),
        rule = RuleStyle(
            color = Black,
            padding = Padding(),
        ),
        codeBlock = CodeBlockStyle(
            innerPadding = Padding(Spacing.x2),
            outerPadding = Padding(Spacing.x0_5),
            background = OceanBlue,
            textStyle = Body.copy(fontFamily = Monospace, color = Black),
            shape = CodeBlockShape,
        ),
        blockQuote = BlockQuoteStyle(
            innerPadding = Padding(horizontal = Spacing.x2, vertical = Spacing.x1),
            outerPadding = Padding(Spacing.x0_5),
            indicatorTint = Color.Unspecified,
            indicatorThickness = Dp.Unspecified,
            backgrounds = QuoteBackgrounds,
            shape = QuoteShape,
        ),
        tableBlock = TableBlockStyle(
            padding = Padding(vertical = Spacing.x1),
            headerStyle = TableCellStyle(
                padding = Padding(),
                textStyle = TableHeader.copy(color = Black),
            ),
            bodyStyle = TableCellStyle(
                padding = Padding(),
                textStyle = Body.copy(color = Black)
            ),
            outlineDividerStyles = OutlineDividerStyles(),
            headerDividerStyle = TableDividerStyle(thickness = Spacing.x0_25),
            bodyDividerStyles = BodyDividerStyles(
                horizontal = TableDividerStyle(thickness = Spacing.x0_25),
                vertical = TableDividerStyle(thickness = Spacing.x4),
            )
        ),
        listBlock = ListBlockStyle(
            orderedStyle = OrderedListItemStyle(
                textStyle = Body.copy(color = Black),
                indicatorTextStyle = Body.copy(color = Black, fontWeight = Medium),
            ),
            unorderedStyle = UnorderedListItemStyle(
                textStyle = Body.copy(color = Black),
                indicators = persistentListOf(
                    Indicator(
                        color = Black,
                        shape = Oval,
                        style = Fill,
                    ),
                    Indicator(
                        color = Black,
                        shape = Oval,
                        style = Stroke(thickness = 1.dp),
                    ),
                    Indicator(
                        color = Black,
                        shape = Rectangle,
                    ),
                )
            ),
            taskStyle = TaskListItemStyle(
                tints = Tints(
                    checkedColor = OrbYellow,
                    uncheckedColor = Grey,
                    checkmarkColor = Black,
                )
            ),
        ),
        textNode = TextNodeStyle(
            padding = Padding(vertical = Spacing.x1),
            textStyle = Body.copy(color = Black),
        ),
    ),
    annotatedStyles = AnnotatedStyles(
        boldStyle = SpanStyle(fontWeight = SemiBold),
        linkStyle = SpanStyle(textDecoration = Underline),
        codeStyle = SpanStyle(fontFamily = Monospace, background = OceanBlue, color = Black),
        superscriptStyle = SpanStyle(
            baselineShift = BaselineShift(0.4F),
            fontSize = 10.sp,
        ),
        subscriptStyle = SpanStyle(
            baselineShift = BaselineShift(-0.2F),
            fontSize = 10.sp,
        )
    )
)

private val DarkMarkyMarkTheme = MarkyMarkTheme(
    composableStyles = ComposableStyles(
        headings = HeadingsStyle(
            heading1 = HeadingLevelStyle(
                textStyle = Heading1.copy(color = White),
                padding = Padding(top = Spacing.x0_5, bottom = Spacing.x1),
            ),
            heading2 = HeadingLevelStyle(
                textStyle = Heading2.copy(color = White),
                padding = Padding(top = Spacing.x0_5, bottom = Spacing.x1),
            ),
            heading3 = HeadingLevelStyle(
                textStyle = Heading3.copy(color = White),
                padding = Padding(top = Spacing.x0_5, bottom = Spacing.x1),
            ),
            heading4 = HeadingLevelStyle(
                textStyle = Heading4.copy(color = White),
                padding = Padding(top = Spacing.x0_5, bottom = Spacing.x1),
            ),
            heading5 = HeadingLevelStyle(
                textStyle = Heading5.copy(color = White),
                padding = Padding(top = Spacing.x0_5, bottom = Spacing.x1),
            ),
            heading6 = HeadingLevelStyle(
                textStyle = Heading6.copy(color = White),
                padding = Padding(top = Spacing.x0_5, bottom = Spacing.x1),
            ),
        ),
        image = ImageStyle(captionTextStyle = Caption.copy(color = WarmLightGrey)),
        rule = RuleStyle(color = White),
        codeBlock = CodeBlockStyle(
            background = OceanBlue,
            textStyle = Body.copy(fontFamily = Monospace, color = Black),
            shape = CodeBlockShape,
        ),
        blockQuote = BlockQuoteStyle(
            innerPadding = Padding(horizontal = Spacing.x2),
            outerPadding = Padding(Spacing.x0_5),
            indicatorTint = Color.Unspecified,
            indicatorThickness = Dp.Unspecified,
            backgrounds = QuoteBackgrounds,
            shape = QuoteShape,
        ),
        tableBlock = TableBlockStyle(
            padding = Padding(vertical = Spacing.x1),
            headerStyle = TableCellStyle(
                padding = Padding(),
                textStyle = Heading5.copy(color = White),
            ),
            bodyStyle = TableCellStyle(
                padding = Padding(),
                textStyle = Body.copy(color = WarmLightGrey)
            ),
            outlineDividerStyles = OutlineDividerStyles(),
            headerDividerStyle = TableDividerStyle(thickness = Spacing.x0_25),
            bodyDividerStyles = BodyDividerStyles(
                horizontal = TableDividerStyle(thickness = Spacing.x0_25),
                vertical = TableDividerStyle(thickness = Spacing.x4),
            )
        ),
        listBlock = ListBlockStyle(
            orderedStyle = OrderedListItemStyle(
                textStyle = Body.copy(color = WarmLightGrey),
                indicatorTextStyle = Body.copy(color = White, fontWeight = Medium),
            ),
            unorderedStyle = UnorderedListItemStyle(
                textStyle = Body.copy(color = WarmLightGrey),
                indicators = persistentListOf(
                    Indicator(
                        // Diamond
                        color = White,
                        shape = Rectangle,
                        rotation = 45F,
                    ),
                    Indicator(
                        color = White,
                        shape = Oval,
                        style = Stroke(thickness = 1.dp),
                    ),
                    Indicator(
                        color = White,
                        shape = Triangle,
                    ),
                    Indicator(
                        // Line
                        color = White,
                        shape = Rectangle,
                        size = DpSize(width = 6.dp, height = 2.dp),
                    ),
                )
            ),
            taskStyle = TaskListItemStyle(
                textStyle = Body.copy(color = WarmLightGrey),
                tints = Tints(
                    checkedColor = OrbYellow,
                    uncheckedColor = LightGrey,
                    checkmarkColor = Black,
                ),
            ),
        ),
        textNode = TextNodeStyle(
            padding = Padding(vertical = Spacing.x1),
            textStyle = Body.copy(color = WarmLightGrey),
        ),
    ),
    annotatedStyles = AnnotatedStyles(
        boldStyle = SpanStyle(fontWeight = SemiBold),
        linkStyle = SpanStyle(textDecoration = Underline),
        codeStyle = SpanStyle(fontFamily = Monospace, background = OceanBlue, color = Black),
    )
)

@Composable
fun SampleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val markdownTheme = remember(darkTheme) { if (darkTheme) DarkMarkyMarkTheme else LightMarkyMarkTheme }
    CompositionLocalProvider(
        LocalMarkyMarkTheme provides markdownTheme,
        content = content,
    )
}
