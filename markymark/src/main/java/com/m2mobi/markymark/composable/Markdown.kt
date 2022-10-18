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

package com.m2mobi.markymark.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.m2mobi.markymark.MarkyMark
import com.m2mobi.markymark.MarkyMark.theme
import com.m2mobi.markymark.converter.MarkyMarkConverter.convertToStableNodes
import com.m2mobi.markymark.model.StableNode
import com.vladsch.flexmark.parser.Parser
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun Markdown(
    markdown: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val options = MarkyMark.options
    val parser = remember(options) { Parser.Builder(options.flexmarkOptions).build() }
    val document = remember(parser, markdown) { parser.parse(markdown) }

    var nodes by remember { mutableStateOf<ImmutableList<StableNode>>(persistentListOf()) }
    LaunchedEffect(document) { nodes = convertToStableNodes(document) }

    val composer = MarkyMark.options.composer
    val styles = theme.composableStyles

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        composer.run {
            createNodes(
                modifier = Modifier,
                nodes = nodes,
                styles = styles,
            )
        }
    }
}
