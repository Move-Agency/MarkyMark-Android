/*
 * Copyright © 2025 Move
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

import android.content.res.Resources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.moveagency.markymark.composer.padding
import com.moveagency.markymark.model.NodeMetadata.Companion.Root
import com.moveagency.markymark.model.annotated.Text
import com.moveagency.markymark.model.composable.ListBlock.ListEntry.ListItem
import com.moveagency.markymark.model.composable.ListBlock.ListItemType.*
import com.moveagency.markymark.theme.LocalMarkyMarkTheme
import com.moveagency.markymark.theme.list.UnorderedListColors
import com.moveagency.markymark.theme.list.UnorderedListItemStyle.Indicator
import com.moveagency.markymark.theme.list.UnorderedListItemStyle.Indicator.IndicatorDrawStyle
import com.moveagency.markymark.theme.list.UnorderedListItemStyle.Indicator.Shape.*
import com.moveagency.markymark.theme.markyMarkTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MarkyMarkListItem(
    item: ListItem,
    indentLevel: Int,
    modifier: Modifier = Modifier,
) {
    val blockStyle = LocalMarkyMarkTheme.current.styles.composable.listBlock
    val style = when (item.type) {
        is Ordered -> blockStyle.ordered
        is Unordered -> blockStyle.unordered
        is Task -> blockStyle.task
    }
    val colors = when (item.type) {
        is Ordered -> LocalMarkyMarkColors.current.list.ordered
        is Unordered -> LocalMarkyMarkColors.current.list.unordered
        is Task -> LocalMarkyMarkColors.current.list.task
    }
    Row(modifier = modifier.padding(style.padding)) {
        val density = LocalDensity.current
        var firstLinePos by remember { mutableStateOf(0.dp to 0.dp) }

        when (item.type) {
            is Ordered -> OrderedIndicator(
                type = item.type,
            )
            is Unordered -> UnorderedIndicator(
                firstLinePos = firstLinePos,
                level = indentLevel,
            )
            is Task -> TaskIndicator(
                type = item.type,
            )
        }

        CompositionLocalProvider(LocalContentColor provides colors.text) {
            MarkyMarkText(
                nodes = item.children,
                style = style.textStyle,
            ) {
                density.run {
                    if (it.lineCount > 0) firstLinePos = it.getLineTop(0).toDp() to it.getLineBottom(0).toDp()
                }
            }
        }
    }
}

@Composable
private fun OrderedIndicator(type: Ordered) {
    val style = LocalMarkyMarkTheme.current.styles.composable.listBlock.ordered
    val colors = LocalMarkyMarkColors.current.list.ordered
    Text(
        modifier = Modifier.padding(style.indicatorPadding),
        text = "${type.index}.",
        color = colors.indicator,
        style = style.indicatorTextStyle,
    )
}

@Composable
private fun UnorderedIndicator(
    firstLinePos: Pair<Dp, Dp>,
    level: Int,
) {
    val style = LocalMarkyMarkTheme.current.styles.composable.listBlock.unordered
    val colors = LocalMarkyMarkColors.current.list.unordered
    val indicator = if (style.indicators.size > 0) {
        style.indicators[level % style.indicators.size]
    } else {
        Indicator.Builder()
            .apply {
                shape = Oval
                size = DpSize(6.dp, 6.dp)
                this.style = IndicatorDrawStyle.Fill
                rotation = 0F
            }
            .build()
    }
    val resources = LocalContext.current.resources
    Canvas(
        modifier = Modifier
            .padding(top = (firstLinePos.second - firstLinePos.first - indicator.size.height).coerceAtLeast(0.dp) / 2)
            .padding(style.indicatorPadding)
            .size(indicator.size),
    ) { drawIndicator(indicator, colors, resources) }
}

private fun DrawScope.drawIndicator(
    indicator: Indicator,
    colors: UnorderedListColors,
    resources: Resources,
) = rotate(indicator.rotation) {
    when (indicator.shape) {
        is Oval -> drawOval(
            color = colors.indicator,
            style = mapToDrawStyle(indicator.style),
        )
        is Rectangle -> drawRect(
            color = colors.indicator,
            style = mapToDrawStyle(indicator.style),
        )
        is Triangle -> drawPath(
            path = createTrianglePath(),
            color = colors.indicator,
            style = mapToDrawStyle(indicator.style),
        )
        is CustomBitmap -> drawImageBitmap(
            indicator = indicator,
            colors = colors,
            imageBitmap = indicator.shape.bitmap,
            tint = indicator.shape.tint,
        )
        is CustomImageResource -> drawImageBitmap(
            indicator = indicator,
            colors = colors,
            imageBitmap = ImageBitmap.imageResource(res = resources, id = indicator.shape.resId),
            tint = indicator.shape.tint,
        )
        is CustomPath -> drawPath(
            path = indicator.shape.creator(this),
            color = colors.indicator,
            style = mapToDrawStyle(indicator.style),
        )
        is CustomShape -> drawOutline(
            outline = indicator.shape.shape.createOutline(
                size = size,
                layoutDirection = layoutDirection,
                density = this,
            ),
            color = colors.indicator,
            style = mapToDrawStyle(indicator.style),
        )
    }
}

private fun DrawScope.drawImageBitmap(
    indicator: Indicator,
    colors: UnorderedListColors,
    imageBitmap: ImageBitmap,
    tint: Boolean,
) {
    scale(
        scaleX = size.width / imageBitmap.width.toFloat(),
        scaleY = size.height / imageBitmap.height.toFloat(),
        pivot = Offset.Zero,
    ) {
        drawImage(
            image = imageBitmap,
            style = mapToDrawStyle(indicator.style),
            colorFilter = if (tint) ColorFilter.tint(colors.indicator) else null,
        )
    }
}

private fun DrawScope.mapToDrawStyle(style: IndicatorDrawStyle) = when (style) {
    is IndicatorDrawStyle.Fill -> Fill
    is IndicatorDrawStyle.Stroke -> Stroke(
        width = style.width.toPx(),
        miter = style.miter,
        cap = style.cap,
        join = style.join,
        pathEffect = style.pathEffect,
    )
}

private fun DrawScope.createTrianglePath(): Path = Path().apply {
    moveTo(x = size.width / 2F, y = 0F)
    lineTo(x = size.width, y = size.height)
    lineTo(x = 0F, y = size.height)
    close()
}

@Composable
private fun TaskIndicator(type: Task) {
    val style = LocalMarkyMarkTheme.current.styles.composable.listBlock.task
    val colors = LocalMarkyMarkColors.current.list.task
    Checkbox(
        modifier = Modifier.padding(style.indicatorPadding),
        checked = type.completed,
        onCheckedChange = null,
        colors = CheckboxDefaults.colors(
            checkedColor = colors.checked,
            uncheckedColor = colors.unchecked,
            checkmarkColor = colors.checkmark,
        )
    )
}

@Preview
@Composable
private fun PreviewOrderedListItem() {
    MarkyMarkListItem(
        item = ListItem(
            type = Ordered(index = 1),
            children = persistentListOf(
                Text(
                    metadata = Root,
                    content = "Some list item",
                ),
            ),
        ),
        indentLevel = 0,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
    )
}

@Preview
@Composable
private fun PreviewUnorderedListItem() {
    Column {
        MarkyMarkListItem(
            item = ListItem(
                type = Unordered,
                children = persistentListOf(
                    Text(
                        metadata = Root,
                        content = "Some list item",
                    ),
                ),
            ),
            indentLevel = 0,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
        )
        MarkyMarkListItem(
            item = ListItem(
                type = Unordered,
                children = persistentListOf(
                    Text(
                        metadata = Root,
                        content = "Some list item",
                    ),
                ),
            ),
            indentLevel = 1,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
        )
        MarkyMarkListItem(
            item = ListItem(
                type = Unordered,
                children = persistentListOf(
                    Text(
                        metadata = Root,
                        content = "Some list item",
                    ),
                ),
            ),
            indentLevel = 2,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
        )
        MarkyMarkListItem(
            item = ListItem(
                type = Unordered,
                children = persistentListOf(
                    Text(
                        metadata = Root,
                        content = "Some list item",
                    ),
                ),
            ),
            indentLevel = 3,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
        )
    }
}

@Preview
@Composable
private fun PreviewUnorderedListItemCustom() {
    val theme = LocalMarkyMarkTheme.current
    val previewTheme = remember(theme) {
        markyMarkTheme {
            include(theme)

            styles {
                composable {
                    listBlock {
                        unordered {
                            indicators {
                                indicator {
                                    shape = Oval
                                    style = IndicatorDrawStyle.Fill
                                }
                                indicator {
                                    shape = Oval
                                    style = IndicatorDrawStyle.Stroke(width = 2.dp)
                                }
                                indicator {
                                    shape = Rectangle
                                    style = IndicatorDrawStyle.Fill
                                }
                                indicator {
                                    shape = Rectangle
                                    style = IndicatorDrawStyle.Stroke(width = 2.dp)
                                }
                                indicator {
                                    shape = Triangle
                                    style = IndicatorDrawStyle.Fill
                                }
                                indicator {
                                    shape = Triangle
                                    style = IndicatorDrawStyle.Stroke(width = 2.dp)
                                }
                                indicator {
                                    shape = CustomPath {
                                        Path().apply {
                                            moveTo(size.width * 0F, size.height * 0.5F)

                                            lineTo(x = size.width * 0.4F, size.height)
                                            lineTo(x = size.width, size.height * 0.3F)
                                            lineTo(x = size.width * 0.85F, y = size.height * 0.1F)
                                            lineTo(x = size.width * 0.4F, size.height * 0.7F)
                                            lineTo(x = size.width * 0.1F, size.height * 0.3F)

                                            close()
                                        }
                                    }
                                    style = IndicatorDrawStyle.Fill
                                }
                                indicator {
                                    shape = CustomPath {
                                        Path().apply {
                                            moveTo(size.width * 0.1F, size.height * 0.5F)
                                            lineTo(size.width * 0.4F, size.height * 0.8F)
                                            lineTo(size.width * 0.9F, size.height * 0.2F)
                                        }
                                    }
                                    style = IndicatorDrawStyle.Stroke(width = 2.dp)
                                }
                                indicator {
                                    shape = CustomShape(RoundedCornerShape(2.dp))
                                    style = IndicatorDrawStyle.Fill
                                }
                                indicator {
                                    shape = CustomShape(RoundedCornerShape(2.dp))
                                    style = IndicatorDrawStyle.Stroke(width = 2.dp)
                                }
                                indicator {
                                    shape = CustomImageResource(
                                        resId = android.R.drawable.ic_dialog_info,
                                        tint = true,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            colors {
                list {
                    unordered {
                        indicator = Color.Blue
                    }
                }
            }
        }
    }

    CompositionLocalProvider(
        LocalMarkyMarkTheme provides previewTheme,
        LocalMarkyMarkColors provides previewTheme.colors,
    ) {
        Column {
            MarkyMarkListItem(
                item = ListItem(
                    type = Unordered,
                    children = persistentListOf(
                        Text(
                            metadata = Root,
                            content = "Some list item",
                        ),
                    ),
                ),
                indentLevel = 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            )
            MarkyMarkListItem(
                item = ListItem(
                    type = Unordered,
                    children = persistentListOf(
                        Text(
                            metadata = Root,
                            content = "Some list item",
                        ),
                    ),
                ),
                indentLevel = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            )
            MarkyMarkListItem(
                item = ListItem(
                    type = Unordered,
                    children = persistentListOf(
                        Text(
                            metadata = Root,
                            content = "Some list item",
                        ),
                    ),
                ),
                indentLevel = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            )
            MarkyMarkListItem(
                item = ListItem(
                    type = Unordered,
                    children = persistentListOf(
                        Text(
                            metadata = Root,
                            content = "Some list item",
                        ),
                    ),
                ),
                indentLevel = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            )
            MarkyMarkListItem(
                item = ListItem(
                    type = Unordered,
                    children = persistentListOf(
                        Text(
                            metadata = Root,
                            content = "Some list item",
                        ),
                    ),
                ),
                indentLevel = 4,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            )
            MarkyMarkListItem(
                item = ListItem(
                    type = Unordered,
                    children = persistentListOf(
                        Text(
                            metadata = Root,
                            content = "Some list item",
                        ),
                    ),
                ),
                indentLevel = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            )
            MarkyMarkListItem(
                item = ListItem(
                    type = Unordered,
                    children = persistentListOf(
                        Text(
                            metadata = Root,
                            content = "Some list item",
                        ),
                    ),
                ),
                indentLevel = 6,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            )
            MarkyMarkListItem(
                item = ListItem(
                    type = Unordered,
                    children = persistentListOf(
                        Text(
                            metadata = Root,
                            content = "Some list item",
                        ),
                    ),
                ),
                indentLevel = 7,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            )
            MarkyMarkListItem(
                item = ListItem(
                    type = Unordered,
                    children = persistentListOf(
                        Text(
                            metadata = Root,
                            content = "Some list item",
                        ),
                    ),
                ),
                indentLevel = 8,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            )
            MarkyMarkListItem(
                item = ListItem(
                    type = Unordered,
                    children = persistentListOf(
                        Text(
                            metadata = Root,
                            content = "Some list item",
                        ),
                    ),
                ),
                indentLevel = 9,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            )
            MarkyMarkListItem(
                item = ListItem(
                    type = Unordered,
                    children = persistentListOf(
                        Text(
                            metadata = Root,
                            content = "Some list item",
                        ),
                    ),
                ),
                indentLevel = 10,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewTaskListItem() {
    Column {
        MarkyMarkListItem(
            item = ListItem(
                type = Task(completed = false),
                children = persistentListOf(
                    Text(
                        metadata = Root,
                        content = "Some list item",
                    ),
                ),
            ),
            indentLevel = 0,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
        )
        MarkyMarkListItem(
            item = ListItem(
                type = Task(completed = true),
                children = persistentListOf(
                    Text(
                        metadata = Root,
                        content = "Some list item",
                    ),
                ),
            ),
            indentLevel = 0,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
        )
    }
}
