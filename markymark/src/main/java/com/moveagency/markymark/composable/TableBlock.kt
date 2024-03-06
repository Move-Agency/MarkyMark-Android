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

package com.moveagency.markymark.composable

import androidx.annotation.Px
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextAlign.Companion.End
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.*
import com.moveagency.markymark.composer.padding
import com.moveagency.markymark.model.composable.TableBlock
import com.moveagency.markymark.model.composable.TableBlock.TableCell
import com.moveagency.markymark.model.composable.TableBlock.TableCell.Alignment.*
import com.moveagency.markymark.model.composable.TableBlock.TableRow
import com.moveagency.markymark.theme.OutlineDividerStyles
import com.moveagency.markymark.theme.TableBlockStyle
import com.moveagency.markymark.theme.TableCellStyle
import com.moveagency.markymark.theme.TableDividerStyle
import kotlin.math.roundToInt

@Composable
internal fun TableBlock(
    node: TableBlock,
    style: TableBlockStyle,
    modifier: Modifier = Modifier,
) {
    val measurePolicy = remember(style) { TableBlockMeasurePolicy(style) }
    Layout(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .then(modifier)
            .padding(style.padding)
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
        yOffset = rowHeights.getValue(0) + toSafePx(style.outlineDividerStyles.top.thickness),
    )
    drawOutlines(style.outlineDividerStyles)
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
    val dividerStyle = style.bodyDividerStyles.vertical
    if (dividerStyle.isNotWorthDrawing) return

    val leftOutlineThickness = toSafePx(style.outlineDividerStyles.left.thickness)
    val bodyDividerThickness = toSafePx(dividerStyle.thickness)
    for (column in 1 until columnWidths.size) {
        var x = leftOutlineThickness
        for (i in 0 until column) {
            if (i > 0) x += bodyDividerThickness
            x += columnWidths.getValue(i)
        }

        drawRect(
            color = dividerStyle.color,
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
    val dividerStyle = style.bodyDividerStyles.horizontal
    if (dividerStyle.isNotWorthDrawing) return

    val topOutlineThickness = toSafePx(style.outlineDividerStyles.top.thickness)
    val headerDividerThickness = toSafePx(style.headerDividerStyle.thickness)
    val bodyDividerThickness = toSafePx(dividerStyle.thickness)
    for (row in 2 until rowHeights.size) {
        var y = topOutlineThickness + headerDividerThickness
        for (i in 0 until row) {
            if (i > 1) y += bodyDividerThickness
            y += rowHeights.getValue(i)
        }

        drawRect(
            color = dividerStyle.color,
            topLeft = Offset(x = 0F, y = y),
            size = Size(width = size.width, height = bodyDividerThickness),
        )
    }
}

private fun DrawScope.drawHeaderDivider(style: TableDividerStyle, yOffset: Float) {
    if (style.isNotWorthDrawing) return

    drawRect(
        color = style.color,
        topLeft = Offset(x = 0F, y = yOffset),
        size = Size(
            width = size.width,
            height = toSafePx(style.thickness),
        )
    )
}

private fun DrawScope.drawOutlines(style: OutlineDividerStyles) {
    // left
    val leftThickness = toSafePx(style.left.thickness)
    drawOutline(
        style = style.left,
        size = Size(
            width = size.width,
            height = leftThickness,
        ),
        topLeft = Offset(
            x = 0F,
            y = size.height - leftThickness,
        ),
    )

    // top
    val topThickness = toSafePx(style.top.thickness)
    drawOutline(
        style = style.top,
        size = Size(
            width = size.width,
            height = topThickness,
        ),
    )

    // right
    drawOutline(
        style = style.right,
        size = Size(
            width = toSafePx(style.right.thickness),
            height = size.height,
        ),
    )

    // bottom
    val bottomThickness = toSafePx(style.bottom.thickness)
    drawOutline(
        style = style.bottom,
        size = Size(
            width = bottomThickness,
            height = size.height,
        ),
        topLeft = Offset(
            x = size.width - bottomThickness,
            y = 0F,
        ),
    )
}

private fun DrawScope.drawOutline(
    style: TableDividerStyle,
    size: Size,
    topLeft: Offset = Offset.Zero,
) {
    if (style.isNotWorthDrawing) return

    drawRect(
        color = style.color,
        topLeft = topLeft,
        size = size,
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

        val leftOutlineThickness = toSafePx(style.outlineDividerStyles.left.thickness)
        val topOutlineThickness = toSafePx(style.outlineDividerStyles.top.thickness)
        val rightOutlineThickness = toSafePx(style.outlineDividerStyles.right.thickness)
        val bottomOutlineThickness = toSafePx(style.outlineDividerStyles.bottom.thickness)
        val headerDividerThickness = toSafePx(style.headerDividerStyle.thickness)
        val bodyHorizontalThickness = toSafePx(style.bodyDividerStyles.horizontal.thickness)
        val bodyVerticalThickness = toSafePx(style.bodyDividerStyles.vertical.thickness)

        val outlineHorizontalSum = topOutlineThickness + bottomOutlineThickness
        val outlineVerticalSum = leftOutlineThickness + rightOutlineThickness

        val columnSum = columnWidths.values.sum()
        val columnBodySum = bodyVerticalThickness * (columnWidths.size - 1)
        val width = columnSum + columnBodySum + outlineVerticalSum

        val rowSum = rowHeights.values.sum()
        val rowBodySum = bodyHorizontalThickness * (rowHeights.size - 2)
        val height = rowSum + rowBodySum + headerDividerThickness + outlineHorizontalSum

        return layout(width.roundToInt(), height.roundToInt()) {
            for ((spec, placeable) in placeableCells) {
                val column = spec.column
                val row = spec.row
                var x = leftOutlineThickness
                for (i in 0 until column) {
                    if (i < columnWidths.size - 1) {
                        x += bodyVerticalThickness
                    }
                    x += columnWidths.getValue(i)
                }
                var y = topOutlineThickness
                for (i in 0 until row) {
                    if (i == 0) {
                        y += headerDividerThickness
                    }
                    if (i > 0 && i < rowHeights.size - 1) {
                        y += bodyHorizontalThickness
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

@Px
private fun Density.toSafePx(dp: Dp): Float = if (dp == Dp.Unspecified) 0F else dp.toPx()

private val TableDividerStyle.isNotWorthDrawing
    get() = color == Transparent || color == Color.Unspecified || thickness == Dp.Unspecified || thickness == 0.dp
