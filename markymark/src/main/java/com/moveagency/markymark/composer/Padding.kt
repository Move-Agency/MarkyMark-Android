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

@file:Suppress("unused")

package com.moveagency.markymark.composer

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * MarkyMark version of Jetpack Compose's [PaddingValues][androidx.compose.foundation.layout.PaddingValues]. This is
 * needed as padding values only allows the retrieval of its values from a density scope.
 */
@Immutable
data class Padding(
    val start: Dp = 0.dp,
    val top: Dp = 0.dp,
    val end: Dp = 0.dp,
    val bottom: Dp = 0.dp,
)

/**
 * See [Padding].
 */
@Stable
fun Padding(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) = Padding(
    start = horizontal,
    top = vertical,
    end = horizontal,
    bottom = vertical,
)

/**
 * See [Padding].
 */
@Stable
fun Padding(all: Dp = 0.dp) = Padding(
    start = all,
    top = all,
    end = all,
    bottom = all,
)

/**
 * See [Padding].
 */
@Stable
fun Modifier.padding(padding: Padding) = padding(
    start = padding.start,
    top = padding.top,
    end = padding.end,
    bottom = padding.bottom,
)

/**
 * See [Padding].
 */
@Stable
fun Modifier.paddingStart(padding: Padding) = padding(start = padding.start)

/**
 * See [Padding].
 */
@Stable
fun Modifier.paddingTop(padding: Padding) = padding(top = padding.top)

/**
 * See [Padding].
 */
@Stable
fun Modifier.paddingEnd(padding: Padding) = padding(end = padding.end)

/**
 * See [Padding].
 */
@Stable
fun Modifier.paddingBottom(padding: Padding) = padding(bottom = padding.bottom)

/**
 * See [Padding].
 */
@Stable
fun Modifier.paddingHorizontal(padding: Padding) = padding(start = padding.start, end = padding.end)

/**
 * See [Padding].
 */
@Stable
fun Modifier.paddingVertical(padding: Padding) = padding(top = padding.top, bottom = padding.bottom)
