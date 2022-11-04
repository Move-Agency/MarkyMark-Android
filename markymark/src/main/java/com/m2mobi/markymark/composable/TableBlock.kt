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

package com.m2mobi.markymark.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextAlign.Companion.End
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import com.m2mobi.markymark.composer.padding
import com.m2mobi.markymark.model.ComposableStableNode.TableBlock
import com.m2mobi.markymark.model.ComposableStableNode.TableCell
import com.m2mobi.markymark.model.ComposableStableNode.TableCell.Alignment.CENTER
import com.m2mobi.markymark.model.ComposableStableNode.TableCell.Alignment.END
import com.m2mobi.markymark.model.ComposableStableNode.TableCell.Alignment.START
import com.m2mobi.markymark.model.ComposableStableNode.TableRow
import com.m2mobi.markymark.theme.TableBlockStyle
import com.m2mobi.markymark.theme.TableCellStyle
import com.m2mobi.markymark.theme.TableDividerStyle
import kotlin.math.roundToInt

@Composable
internal fun TableBlock(
    node: TableBlock,
    style: TableBlockStyle,
    modifier: Modifier = Modifier,
) {
    val measurePolicy = remember(style) { TableBlockMeasurePolicy(style) }
    Layout(
        modifier = modifier
            .padding(style.padding)
            .horizontalScroll(rememberScrollState())
            .drawWithCache {
                onDrawBehind {
                    drawDividers(
                        columnWidths = measurePolicy.measuredColumnWidths,
                        rowHeights = measurePolicy.measuredRowHeights,
                        style = style,
                    )
                }
            },
        measurePolicy = measurePolicy,
        content = { TableContent(node = node, style = style) },
    )
}

private fun DrawScope.drawDividers(
    columnWidths: Map<Int, Int>,
    rowHeights: Map<Int, Int>,
    style: TableBlockStyle,
) {
    drawBodyDividers(
        columnWidths = columnWidths,
        rowHeights = rowHeights,
        style = style,
    )
    drawHeaderDivider(
        style = style.headerDividerStyle,
        yOffset = rowHeights.getValue(0) + style.outlineStyle.thickness.toPx(),
    )
    drawOutlines(style.outlineStyle)
}

private fun DrawScope.drawBodyDividers(
    columnWidths: Map<Int, Int>,
    rowHeights: Map<Int, Int>,
    style: TableBlockStyle,
) {
    drawColumnDividers(columnWidths = columnWidths, style = style)
    drawRowDividers(rowHeights = rowHeights, style = style)
}

@Suppress("NestedBlockDepth")
private fun DrawScope.drawColumnDividers(
    columnWidths: Map<Int, Int>,
    style: TableBlockStyle,
) {
    val outlineThickness = style.outlineStyle.thickness.toPx()
    val bodyDividerThickness = style.bodyDividerStyle.thickness.toPx()
    for (column in 1 until columnWidths.size) {
        var x = outlineThickness
        for (i in 0 until column) {
            if (i > 0) x += bodyDividerThickness
            x += columnWidths.getValue(i)
        }

        drawRect(
            color = style.bodyDividerStyle.color,
            topLeft = Offset(x = x, y = 0F),
            size = Size(width = bodyDividerThickness, height = size.height),
        )
    }
}

@Suppress("NestedBlockDepth")
private fun DrawScope.drawRowDividers(
    rowHeights: Map<Int, Int>,
    style: TableBlockStyle,
) {
    val outlineThickness = style.outlineStyle.thickness.toPx()
    val headerDividerThickness = style.headerDividerStyle.thickness.toPx()
    val bodyDividerThickness = style.bodyDividerStyle.thickness.toPx()
    for (row in 2 until rowHeights.size) {
        var y = outlineThickness + headerDividerThickness
        for (i in 0 until row) {
            if (i > 1) y += bodyDividerThickness
            y += rowHeights.getValue(i)
        }

        drawRect(
            color = style.bodyDividerStyle.color,
            topLeft = Offset(x = 0F, y = y),
            size = Size(width = size.width, height = bodyDividerThickness),
        )
    }
}

private fun DrawScope.drawHeaderDivider(style: TableDividerStyle, yOffset: Float) {
    drawRect(
        color = style.color,
        topLeft = Offset(x = 0F, y = yOffset),
        size = Size(
            width = size.width,
            height = style.thickness.toPx(),
        )
    )
}

private fun DrawScope.drawOutlines(style: TableDividerStyle) {
    val thickness = style.thickness.toPx()

    // left
    drawRect(
        color = style.color,
        topLeft = Offset(
            x = 0F,
            y = size.height - thickness,
        ),
        size = Size(
            width = size.width,
            height = thickness,
        ),
    )

    // top
    drawRect(
        color = style.color,
        size = Size(
            width = size.width,
            height = thickness,
        ),
    )

    // right
    drawRect(
        color = style.color,
        size = Size(
            width = thickness,
            height = size.height,
        ),
    )

    // bottom
    drawRect(
        color = style.color,
        topLeft = Offset(
            x = size.width - thickness,
            y = 0F,
        ),
        size = Size(
            width = thickness,
            height = size.height,
        ),
    )
}

class TableBlockMeasurePolicy(
    private val style: TableBlockStyle,
) : MeasurePolicy {

    var measuredColumnWidths by mutableStateOf(emptyMap<Int, Int>())
    var measuredRowHeights by mutableStateOf(emptyMap<Int, Int>())

    @Suppress("LongMethod")
    override fun MeasureScope.measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
        val columnWidths = mutableMapOf<Int, Int>()
        val rowHeights = mutableMapOf<Int, Int>()
        val placeableCells = mutableListOf<PlaceableTableCell>()

        for (measurable in measurables) {
            val spec = measurable.layoutId as TableCellSpec
            columnWidths[spec.column] = maxOf(
                columnWidths[spec.column] ?: style.cellMinWidth.roundToPx(),
                measurable.maxIntrinsicWidth(constraints.maxHeight),
            ).coerceAtMost(style.cellMaxWidth.roundToPx())
            rowHeights[spec.row] = maxOf(
                rowHeights[spec.row] ?: style.cellMinWidth.roundToPx(),
                measurable.maxIntrinsicHeight(constraints.maxWidth),
            ).coerceAtMost(style.cellMaxHeight.roundToPx())
        }

        for (measurable in measurables) {
            val spec = measurable.layoutId as TableCellSpec
            val width = columnWidths.getValue(spec.column)
            val height = rowHeights.getValue(spec.row)
            val placeable = measurable.measure(Constraints.fixed(width, height))
            placeableCells += PlaceableTableCell(
                spec = spec,
                placeable = placeable,
            )
        }

        measuredColumnWidths = columnWidths
        measuredRowHeights = rowHeights

        val outlineThickness = style.outlineStyle.thickness.toPx()
        val headerDividerThickness = style.headerDividerStyle.thickness.toPx()
        val bodyDividerThickness = style.bodyDividerStyle.thickness.toPx()

        val outlineSum = outlineThickness * 2

        val columnSum = columnWidths.values.sum()
        val columnBodySum = bodyDividerThickness * (columnWidths.size - 1)
        val width = columnSum + columnBodySum + outlineSum

        val rowSum = rowHeights.values.sum()
        val rowBodySum = bodyDividerThickness * (rowHeights.size - 2)
        val height = rowSum + rowBodySum + headerDividerThickness + outlineSum

        return layout(width.roundToInt(), height.roundToInt()) {
            for ((spec, placeable) in placeableCells) {
                val column = spec.column
                val row = spec.row
                var x = outlineThickness
                for (i in 0 until column) {
                    if (i < columnWidths.size - 1) {
                        x += bodyDividerThickness
                    }
                    x += columnWidths.getValue(i)
                }
                var y = outlineThickness
                for (i in 0 until row) {
                    if (i == 0) {
                        y += headerDividerThickness
                    }
                    if (i > 0 && i < rowHeights.size - 1) {
                        y += bodyDividerThickness
                    }
                    y += rowHeights.getValue(i)
                }
                placeable.placeRelative(IntOffset(x.roundToInt(), y.roundToInt()))
            }
        }
    }
}

@Composable
private fun TableContent(node: TableBlock, style: TableBlockStyle) {
    TableRow(rowIndex = 0, row = node.head, cellStyle = style.headerStyle)
    for ((index, row) in node.body.withIndex()) TableRow(rowIndex = index + 1, row = row, cellStyle = style.bodyStyle)
}

@Composable
private fun TableRow(
    rowIndex: Int,
    row: TableRow,
    cellStyle: TableCellStyle,
) {
    for ((index, cell) in row.cells.withIndex()) {
        TableCell(
            row = rowIndex,
            column = index,
            style = cellStyle,
            cell = cell,
        )
    }
}

@Composable
private fun TableCell(
    row: Int,
    column: Int,
    style: TableCellStyle,
    cell: TableCell,
) = TextNode(
    modifier = Modifier
        .layoutId(TableCellSpec(row = row, column = column))
        .background(style.background)
        .padding(style.padding),
    nodes = cell.children,
    style = style.textStyle.copy(
        textAlign = when (cell.alignment) {
            START -> Start
            CENTER -> Center
            END -> End
        },
    ),
)

/**
 * Helper class for identifying a table cell during measuring.
 */
@Immutable
private data class TableCellSpec(
    val row: Int,
    val column: Int,
)

/**
 * Helper class for laying out a table cell.
 */
@Immutable
private data class PlaceableTableCell(
    val spec: TableCellSpec,
    val placeable: Placeable,
)
