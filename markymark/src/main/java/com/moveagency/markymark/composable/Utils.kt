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

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import com.moveagency.markymark.MarkyMark
import com.moveagency.markymark.MarkyMarkOptions
import com.moveagency.markymark.model.AnnotatedStableNode
import kotlinx.collections.immutable.ImmutableList

/**
 * Utility function to convert [nodes] into an [AnnotatedString] using the [MarkyMarkOptions.annotator].
 */
@Composable
internal fun annotate(
    nodes: ImmutableList<AnnotatedStableNode>,
): AnnotatedString {
    val styles = MarkyMark.theme.annotatedStyles
    return MarkyMark.options.annotator.annotate(nodes = nodes, styles = styles)
}
