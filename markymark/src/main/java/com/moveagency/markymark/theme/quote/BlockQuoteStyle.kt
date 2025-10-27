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

@file:Suppress("DataClassPrivateConstructor", "unused", "MemberVisibilityCanBePrivate")

package com.moveagency.markymark.theme.quote

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.moveagency.markymark.composable.MarkyMarkQuote
import com.moveagency.markymark.model.composable.BlockQuote
import com.moveagency.markymark.theme.ComposableStyles
import com.moveagency.markymark.theme.MarkyMarkThemeBuilderMarker
import com.moveagency.markymark.theme.Padding
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * Theming attributes used for rendering [BlockQuote]s with [MarkyMarkQuote].
 *
 * *Note: The indicator is always drawn at the __start__ of the quote, this is not configurable.*
 *
 * @property innerPadding The padding inside the block quote.
 * @property outerPadding The padding outside the block quote, around the entire block.
 * @property indicatorThickness The thickness of the block quote indicator, defined in [Dp].
 * @property themes A list of themes used for rendering the block quote's content.
 * @property shape The shape of the block quote, such as a rectangle or other custom shape.
 * @property contentStyle The composable style to be applied to the content inside the block quote.
 */
@Immutable
data class BlockQuoteStyle private constructor(
    val innerPadding: Padding,
    val outerPadding: Padding,
    val indicatorThickness: Dp,
    val themes: ImmutableList<BlockQuoteTheme>,
    val shape: Shape,
    val contentStyle: ((parent: ComposableStyles) -> ComposableStyles)? = null,
) {

    /**
     * Builder class for constructing instances of [BlockQuoteStyle].
     */
    @MarkyMarkThemeBuilderMarker
    class Builder {

        /**
         * Builder for configuring the inner padding inside the block quote.
         */
        private var innerPadding = Padding.Builder()

        /**
         * Builder for configuring the outer padding outside the block quote.
         */
        private var outerPadding = Padding.Builder()

        /**
         * The thickness of the block quote indicator, defined in [Dp]. Default is `0.dp`.
         */
        var indicatorThickness = 0.dp

        /**
         * Builder for configuring the themes used for rendering the block quote's content.
         */
        private val themes = BlockQuoteThemesBuilder()

        /**
         * The shape of the block quote. Default is [RectangleShape].
         */
        var shape: Shape = RectangleShape

        /**
         * The composable style applied to the content inside the code block. Default is an empty [ComposableStyles].
         */
        var contentStyle: ((ComposableStyles) -> ComposableStyles)? = null

        /**
         * Includes another [Builder] instance's configuration into `this` builder.
         *
         * This will override all properties with the ones from the provided [builder].
         *
         * @param builder The builder whose configuration should be included.
         */
        fun include(builder: Builder) {
            innerPadding = builder.innerPadding
            outerPadding = builder.outerPadding
            indicatorThickness = builder.indicatorThickness
            themes.include(builder.themes)
            shape = builder.shape
            contentStyle = builder.contentStyle
        }

        /**
         * Includes the configuration from an existing [BlockQuoteStyle] object into this builder.
         *
         * This will include the padding, indicator settings, themes, and shape from the provided [style].
         *
         * @param style The [BlockQuoteStyle] instance whose configuration should be included.
         */
        fun include(style: BlockQuoteStyle) {
            innerPadding.include(style.innerPadding)
            outerPadding.include(style.outerPadding)
            indicatorThickness = style.indicatorThickness
            themes.include(style.themes)
            shape = style.shape
            contentStyle = style.contentStyle
        }

        /**
         * Configures the inner padding inside the block quote.
         *
         * @param block A lambda to configure the [Padding.Builder].
         */
        fun innerPadding(block: Padding.Builder.() -> Unit) = block(innerPadding)

        /**
         * Configures the outer padding outside the block quote.
         *
         * @param block A lambda to configure the [Padding.Builder].
         */
        fun outerPadding(block: Padding.Builder.() -> Unit) = block(outerPadding)

        /**
         * Configures the themes used for rendering the block quote's content.
         *
         * @param block A lambda to configure the [BlockQuoteThemesBuilder].
         */
        fun themes(block: BlockQuoteThemesBuilder.() -> Unit) = block(themes)

        /**
         * Configures the content style for the block quote.
         *
         * @param overrides A lambda to override the [ComposableStyles.Builder] for the content.
         */
        fun contentStyle(overrides: ComposableStyles.Builder.() -> Unit) {
            contentStyle = { parent ->
                ComposableStyles.Builder().apply {
                    include(parent)
                    overrides()
                }.build()
            }
        }

        /**
         * Builds a new [BlockQuoteStyle] instance with the current configuration.
         *
         * @return A [BlockQuoteStyle] object with the set properties for the block quote.
         */
        internal fun build(): BlockQuoteStyle = BlockQuoteStyle(
            innerPadding = innerPadding.build(),
            outerPadding = outerPadding.build(),
            indicatorThickness = indicatorThickness,
            themes = themes.build(),
            shape = shape,
            contentStyle = contentStyle
        )
    }

    /**
     * Builder class for constructing a list of [BlockQuoteTheme] objects.
     */
    @MarkyMarkThemeBuilderMarker
    class BlockQuoteThemesBuilder {

        private val themes = mutableListOf<BlockQuoteTheme.Builder>()

        /**
         * Includes another [BlockQuoteThemesBuilder] instance's configuration into `this` builder.
         *
         * This will add all themes from the provided [builder].
         *
         * @param builder The builder whose themes should be included.
         */
        fun include(builder: BlockQuoteThemesBuilder) {
            themes.addAll(builder.themes)
        }

        /**
         * Includes the configuration from an existing list of [BlockQuoteTheme] objects into this builder.
         *
         * This will add all themes from the provided list.
         *
         * @param themes The list of [BlockQuoteTheme] instances to be included.
         */
        fun include(themes: List<BlockQuoteTheme>) {
            this.themes.addAll(
                themes.map {
                    BlockQuoteTheme.Builder().apply { include(it) }
                }
            )
        }

        /**
         * Remove all existing themes that may have been added by [include].
         */
        fun clear() = themes.clear()

        /**
         * Adds a new theme to the block quote.
         *
         * @param block A lambda to configure the [BlockQuoteTheme.Builder].
         */
        fun theme(block: BlockQuoteTheme.Builder.() -> Unit) {
            themes.add(BlockQuoteTheme.Builder().apply(block))
        }

        /**
         * Builds a list of [BlockQuoteTheme] objects with the current configuration.
         *
         * @return An immutable list of [BlockQuoteTheme] objects.
         */
        internal fun build() = themes.map { it.build() }.toImmutableList()
    }
}
