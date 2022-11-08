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

package com.moveagency.markymark.composer

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import com.moveagency.markymark.model.ComposableStableNode
import com.moveagency.markymark.model.StableNode
import com.moveagency.markymark.theme.ComposableStyles
import kotlinx.collections.immutable.ImmutableList

/**
 * The Composer is responsible for rendering [ComposableStableNode]s. See [DefaultMarkyMarkComposer] for the default
 * implementation.
 */
@Stable
interface MarkyMarkComposer {

    /**
     * Create [LazyListScope.item]s belonging to [nodes].
     */
    fun LazyListScope.createNodes(
        modifier: Modifier = Modifier,
        nodes: ImmutableList<StableNode>,
        styles: ComposableStyles,
    )
}
