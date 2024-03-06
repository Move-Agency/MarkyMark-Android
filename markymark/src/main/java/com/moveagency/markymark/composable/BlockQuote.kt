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

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection.Ltr
import androidx.compose.ui.unit.LayoutDirection.Rtl
import androidx.compose.ui.unit.isSpecified
import com.moveagency.markymark.composer.paddingHorizontal
import com.moveagency.markymark.theme.BlockQuoteStyle

fun Modifier.blockQuoteItem(
    style: BlockQuoteStyle,
    isTop: Boolean,
    isBottom: Boolean,
    level: Int,
) = paddingHorizontal(style.outerPadding)
    .drawWithCache {
        val adjustedShape = style.shape.let {
            it.copy(
                topStart = if (isTop) it.topStart else CornerSize(0F),
                topEnd = if (isTop) it.topEnd else CornerSize(0F),
                bottomStart = if (isBottom) it.bottomStart else CornerSize(0F),
                bottomEnd = if (isBottom) it.bottomEnd else CornerSize(0F),
            )
        }
        onDrawBehind {
            // draw background
            val backgroundColor = style.backgrounds[level % style.backgrounds.size]
            if (backgroundColor.isSpecified) {
                drawOutline(
                    outline = adjustedShape.createOutline(size, layoutDirection, Density(density, fontScale)),
                    color = backgroundColor,
                )
            }

            // draw indicator
            if (style.indicatorThickness.isSpecified && style.indicatorTint.isSpecified) {
                val thicknessPx = style.indicatorThickness.toPx()
                val indicatorOffset = when (layoutDirection) {
                    Ltr -> Offset.Zero
                    Rtl -> Offset(x = size.width - thicknessPx, y = 0F)
                }
                drawRect(
                    color = style.indicatorTint,
                    topLeft = indicatorOffset,
                    size = Size(width = thicknessPx, height = size.height),
                )
            }
        }
    }
    .padding(
        start = if (style.indicatorThickness.isSpecified) {
            style.innerPadding.start + style.indicatorThickness
        } else {
            style.innerPadding.start
        },
        end = style.innerPadding.end,
    )
