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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection.Ltr
import androidx.compose.ui.unit.LayoutDirection.Rtl
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.moveagency.markymark.composer.padding
import com.moveagency.markymark.model.NodeMetadata
import com.moveagency.markymark.model.NodeMetadata.Companion.Root
import com.moveagency.markymark.model.composable.BlockQuote
import com.moveagency.markymark.theme.LocalMarkyMarkTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MarkyMarkQuote(
    node: BlockQuote,
    modifier: Modifier = Modifier,
    children: (@Composable ColumnScope.() -> Unit),
) {
    val style = LocalMarkyMarkTheme.current.styles.composable.blockQuote
    val themeList = style.themes
    val theme = if (themeList.isNotEmpty()) {
        themeList[node.metadata.quoteLevel % themeList.size]
    } else {
        null
    }
    val background = theme?.background?.takeIf { it.isSpecified }

    Column(
        modifier = modifier
            .padding(style.outerPadding)
            .clip(style.shape)
            .then(if (background != null) Modifier.background(background) else Modifier)
            .drawBehind {
                if (style.indicatorThickness.isSpecified && theme?.indicator?.isSpecified == true) {
                    val thicknessPx = style.indicatorThickness.toPx()
                    val indicatorOffset = when (layoutDirection) {
                        Ltr -> Offset.Zero
                        Rtl -> Offset(x = size.width - thicknessPx, y = 0F)
                    }
                    val radius = theme.indicatorRadius.toPx()
                    drawRoundRect(
                        color = theme.indicator,
                        topLeft = indicatorOffset,
                        size = Size(width = thicknessPx, height = size.height),
                        cornerRadius = CornerRadius(x = radius, y = radius),
                    )
                }
            }
            .padding(style.innerPadding)
    ) {
        val parent = LocalMarkyMarkTheme.current.styles.composable
        val inner = style.contentStyle?.invoke(parent) ?: parent

        CompositionLocalProvider(
            LocalMarkyMarkTheme provides LocalMarkyMarkTheme.current.copy(
                styles = LocalMarkyMarkTheme.current.styles.copy(
                    composable = inner
                )
            )
        ) {
            if (theme?.colors != null) {
                CompositionLocalProvider(LocalMarkyMarkColors provides theme.colors) {
                    children()
                }
            } else {
                children()
            }
        }
    }
}

@Preview
@Composable
private fun PreviewBlockQuoteLevelOne() {
    val quote = BlockQuote(
        metadata = Root,
        children = persistentListOf(),
    )

    MarkyMarkQuote(
        node = quote,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Spacer(Modifier.height(200.dp))
    }
}

@Preview
@Composable
private fun PreviewBlockQuoteLevelTwo() {
    val quote = BlockQuote(
        metadata = NodeMetadata(
            level = 0,
            quoteLevel = 1,
            listLevel = 0,
            paragraphLevel = 0,
        ),
        children = persistentListOf(),
    )

    MarkyMarkQuote(
        node = quote,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Spacer(Modifier.height(200.dp))
    }
}

@Preview
@Composable
private fun PreviewBlockQuoteLevelThree() {
    val quote = BlockQuote(
        metadata = NodeMetadata(
            level = 0,
            quoteLevel = 2,
            listLevel = 0,
            paragraphLevel = 0,
        ),
        children = persistentListOf(),
    )

    MarkyMarkQuote(
        node = quote,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Spacer(Modifier.height(200.dp))
    }
}
