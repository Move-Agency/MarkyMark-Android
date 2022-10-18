/*
 * Copyright © 2022 M2mobi
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

package com.m2mobi.markymark

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import com.m2mobi.markymark.annotator.DefaultMarkyMarkAnnotator
import com.m2mobi.markymark.annotator.MarkyMarkAnnotator
import com.m2mobi.markymark.composer.DefaultMarkyMarkComposer
import com.m2mobi.markymark.composer.MarkyMarkComposer
import com.vladsch.flexmark.ext.autolink.AutolinkExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughSubscriptExtension
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension
import com.vladsch.flexmark.ext.superscript.SuperscriptExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.parser.Parser.EXTENSIONS
import com.vladsch.flexmark.parser.Parser.ParserExtension
import com.vladsch.flexmark.util.data.DataHolder
import com.vladsch.flexmark.util.data.MutableDataSet

/**
 * Options for MarkyMark.
 *
 * [flexmarkOptions] Allows for customising the options passed to FlexMark. See [Parser.Builder] for more details.
 * See [createDefaultFlexmarkOptions] for the default options passed. When passing in a custom set of rules its best to
 * build upon the defaults as it adds all the needed [ParserExtension]s for the supported syntax.
 *
 * [composer] & [annotator] allows for passing in more customized Jetpack Compose renderers.
 */
@Stable
data class MarkyMarkOptions(
    val flexmarkOptions: DataHolder,
    val composer: MarkyMarkComposer,
    val annotator: MarkyMarkAnnotator,
)

val LocalMarkyMarkOptions = compositionLocalOf {
    MarkyMarkOptions(
        flexmarkOptions = createDefaultFlexmarkOptions(),
        composer = DefaultMarkyMarkComposer(),
        annotator = DefaultMarkyMarkAnnotator(),
    )
}

/**
 * Creates a [MutableDataSet] containing the default [ParserExtension] needed for all the syntax supported by MarkyMark.
 * - [AutolinkExtension], [docs](https://github.com/vsch/flexmark-java/wiki/Extensions#autolink)
 * - [StrikethroughSubscriptExtension], [docs](https://github.com/vsch/flexmark-java/wiki/Extensions#gfm-strikethroughsubscript)
 * - [TaskListExtension], [docs](https://github.com/vsch/flexmark-java/wiki/Extensions#gfm-tasklist)
 * - [SuperscriptExtension], [docs](https://github.com/vsch/flexmark-java/wiki/Extensions#superscript)
 * - [TablesExtension], [docs](https://github.com/vsch/flexmark-java/wiki/Extensions#tables)
 */
fun createDefaultFlexmarkOptions() = MutableDataSet().apply {
    set(
        AutolinkExtension.create(),
        StrikethroughSubscriptExtension.create(),
        TaskListExtension.create(),
        SuperscriptExtension.create(),
        TablesExtension.create(),
    )
    toImmutable()
}

/**
 * Utility function for adding [ParserExtension].
 */
fun MutableDataSet.set(vararg extensions: ParserExtension) = set(
    EXTENSIONS,
    extensions.toList(),
)
