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

package com.moveagency.markymark.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.moveagency.markymark.composer.padding
import com.moveagency.markymark.model.AnnotatedStableNode
import com.moveagency.markymark.model.ComposableStableNode.ListItemType
import com.moveagency.markymark.model.ComposableStableNode.ListItemType.Ordered
import com.moveagency.markymark.model.ComposableStableNode.ListItemType.Task
import com.moveagency.markymark.model.ComposableStableNode.ListItemType.Unordered
import com.moveagency.markymark.theme.ListBlockStyle
import com.moveagency.markymark.theme.OrderedListItemStyle
import com.moveagency.markymark.theme.TaskListItemStyle
import com.moveagency.markymark.theme.UnorderedListItemStyle
import com.moveagency.markymark.theme.UnorderedListItemStyle.Indicator
import com.moveagency.markymark.theme.UnorderedListItemStyle.Indicator.Shape.Oval
import com.moveagency.markymark.theme.UnorderedListItemStyle.Indicator.Shape.Rectangle
import com.moveagency.markymark.theme.UnorderedListItemStyle.Indicator.Shape.Triangle
import com.moveagency.markymark.theme.UnorderedListItemStyle.Indicator.Style
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ListItem(
    type: ListItemType,
    children: ImmutableList<AnnotatedStableNode>,
    blockStyle: ListBlockStyle,
    level: Int,
    modifier: Modifier = Modifier,
) {
    val style = when (type) {
        is Ordered -> blockStyle.orderedStyle
        is Unordered -> blockStyle.unorderedStyle
        is Task -> blockStyle.taskStyle
    }
    Row(modifier = modifier.padding(style.padding)) {
        val density = LocalDensity.current
        var firstLinePos by remember { mutableStateOf(0.dp to 0.dp) }

        when (type) {
            is Ordered -> OrderedIndicator(
                type = type,
                style = blockStyle.orderedStyle,
            )
            is Unordered -> UnorderedIndicator(
                firstLinePos = firstLinePos,
                level = level,
                style = blockStyle.unorderedStyle,
            )
            is Task -> TaskIndicator(
                type = type,
                style = blockStyle.taskStyle,
            )
        }

        TextNode(
            nodes = children,
            style = style.textStyle,
        ) {
            density.run {
                if (it.lineCount > 0) firstLinePos = it.getLineTop(0).toDp() to it.getLineBottom(0).toDp()
            }
        }
    }
}

@Composable
private fun OrderedIndicator(
    type: Ordered,
    style: OrderedListItemStyle,
) {
    Text(
        modifier = Modifier.padding(style.indicatorPadding),
        text = "${type.index}.",
        style = style.indicatorTextStyle,
    )
}

@Composable
private fun UnorderedIndicator(
    firstLinePos: Pair<Dp, Dp>,
    level: Int,
    style: UnorderedListItemStyle,
) {
    val indicator = style.indicators[level % style.indicators.size]
    Canvas(
        modifier = Modifier
            .padding(top = (firstLinePos.second - firstLinePos.first - indicator.size.height).coerceAtLeast(0.dp) / 2)
            .padding(style.indicatorPadding)
            .size(indicator.size),
    ) { drawIndicator(indicator) }
}

private fun DrawScope.drawIndicator(indicator: Indicator) = rotate(indicator.rotation) {
    when (indicator.shape) {
        Oval -> drawOval(
            color = indicator.color,
            style = toDrawStyle(indicator.style),
        )
        Rectangle -> drawRect(
            color = indicator.color,
            style = toDrawStyle(indicator.style),
        )
        Triangle -> drawPath(
            color = indicator.color,
            path = createTrianglePath(),
        )
    }
}

private fun DrawScope.createTrianglePath(): Path = Path().apply {
    moveTo(x = size.width / 2F, y = 0F)
    lineTo(x = size.width, y = size.height)
    lineTo(x = 0F, y = size.height)
    close()
}

private fun DrawScope.toDrawStyle(style: Style) = when (style) {
    is Style.Fill -> Fill
    is Style.Stroke -> Stroke(width = style.thickness.toPx())
}

@Composable
private fun TaskIndicator(
    type: Task,
    style: TaskListItemStyle,
) {
    Checkbox(
        modifier = Modifier.padding(style.indicatorPadding),
        checked = type.completed,
        onCheckedChange = null,
        colors = CheckboxDefaults.colors(
            checkedColor = style.tints.checkedColor,
            uncheckedColor = style.tints.uncheckedColor,
            checkmarkColor = style.tints.checkmarkColor,
        )
    )
}
