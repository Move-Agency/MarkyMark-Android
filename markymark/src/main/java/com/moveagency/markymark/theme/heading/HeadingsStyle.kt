/*
 * Copyright © 2025 Framna
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

package com.moveagency.markymark.theme.heading

import androidx.compose.runtime.Immutable
import com.moveagency.markymark.theme.MarkyMarkThemeBuilderMarker

/**
 * Collection of style configurations for all heading levels in markdown.
 *
 * This class aggregates styling for all six heading levels (h1-h6) used in markdown documents,
 * allowing for consistent and hierarchical typography across headings.
 *
 * @property h1 Styling configuration for level 1 headings (largest).
 * @property h2 Styling configuration for level 2 headings.
 * @property h3 Styling configuration for level 3 headings.
 * @property h4 Styling configuration for level 4 headings.
 * @property h5 Styling configuration for level 5 headings.
 * @property h6 Styling configuration for level 6 headings (smallest).
 */
@Immutable
data class HeadingsStyle(
    val h1: HeadingLevelStyle,
    val h2: HeadingLevelStyle,
    val h3: HeadingLevelStyle,
    val h4: HeadingLevelStyle,
    val h5: HeadingLevelStyle,
    val h6: HeadingLevelStyle,
) {

    /**
     * Builder class for constructing instances of [HeadingsStyle].
     *
     * This builder provides methods to configure styling for all six heading levels (h1-h6)
     * in a type-safe, fluent manner. It allows for incremental style configuration
     * and composition of styles from multiple sources.
     */
    @MarkyMarkThemeBuilderMarker
    class Builder {

        /**
         * Builder for configuring level 1 heading styles.
         */
        private var h1 = HeadingLevelStyle.Builder()

        /**
         * Builder for configuring level 2 heading styles.
         */
        private var h2 = HeadingLevelStyle.Builder()

        /**
         * Builder for configuring level 3 heading styles.
         */
        private var h3 = HeadingLevelStyle.Builder()

        /**
         * Builder for configuring level 4 heading styles.
         */
        private var h4 = HeadingLevelStyle.Builder()

        /**
         * Builder for configuring level 5 heading styles.
         */
        private var h5 = HeadingLevelStyle.Builder()

        /**
         * Builder for configuring level 6 heading styles.
         */
        private var h6 = HeadingLevelStyle.Builder()

        /**
         * Includes another [Builder] instance's configuration into `this` builder.
         *
         * This will override heading level builders with the ones from the provided [builder].
         * Note: This method currently does not include h6 from the provided builder.
         *
         * @param builder The builder whose configuration should be included.
         */
        fun include(builder: Builder) {
            h1 = builder.h1
            h2 = builder.h2
            h3 = builder.h3
            h4 = builder.h4
            h5 = builder.h5
            h6 = builder.h6
        }

        /**
         * Includes the configuration from an existing [HeadingsStyle] object into this builder.
         *
         * This will include all heading level styles from the provided [styles] into their
         * respective builders.
         *
         * @param styles The [HeadingsStyle] instance whose configuration should be included.
         */
        fun include(styles: HeadingsStyle) {
            h1.include(styles.h1)
            h2.include(styles.h2)
            h3.include(styles.h3)
            h4.include(styles.h4)
            h5.include(styles.h5)
            h6.include(styles.h6)
        }

        /**
         * Configures level 1 heading styles.
         *
         * @param block A lambda to configure the [HeadingLevelStyleBuilder] for h1.
         */
        fun h1(block: HeadingLevelStyle.Builder.() -> Unit) = block(h1)

        /**
         * Configures level 2 heading styles.
         *
         * @param block A lambda to configure the [HeadingLevelStyleBuilder] for h2.
         */
        fun h2(block: HeadingLevelStyle.Builder.() -> Unit) = block(h2)

        /**
         * Configures level 3 heading styles.
         *
         * @param block A lambda to configure the [HeadingLevelStyleBuilder] for h3.
         */
        fun h3(block: HeadingLevelStyle.Builder.() -> Unit) = block(h3)

        /**
         * Configures level 4 heading styles.
         *
         * @param block A lambda to configure the [HeadingLevelStyleBuilder] for h4.
         */
        fun h4(block: HeadingLevelStyle.Builder.() -> Unit) = block(h4)

        /**
         * Configures level 5 heading styles.
         *
         * @param block A lambda to configure the [HeadingLevelStyleBuilder] for h5.
         */
        fun h5(block: HeadingLevelStyle.Builder.() -> Unit) = block(h5)

        /**
         * Configures level 6 heading styles.
         *
         * @param block A lambda to configure the [HeadingLevelStyleBuilder] for h6.
         */
        fun h6(block: HeadingLevelStyle.Builder.() -> Unit) = block(h6)

        /**
         * Builds a new [HeadingsStyle] instance with the current configuration.
         *
         * @return A [HeadingsStyle] object with all the configured heading level styles.
         */
        internal fun build() = HeadingsStyle(
            h1 = h1.build(),
            h2 = h2.build(),
            h3 = h3.build(),
            h4 = h4.build(),
            h5 = h5.build(),
            h6 = h6.build(),
        )
    }
}
