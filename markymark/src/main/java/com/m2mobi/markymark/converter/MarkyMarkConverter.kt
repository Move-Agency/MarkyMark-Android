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

package com.m2mobi.markymark.converter

import com.m2mobi.markymark.converter.ComposableStableNodeConverter.convertToStableNode
import com.m2mobi.markymark.model.AnnotatedStableNode
import com.m2mobi.markymark.model.StableNode
import com.m2mobi.markymark.util.mapAsync
import com.m2mobi.markymark.util.mapAsyncIndexed
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers

/**
 * This object contains the logic for converting the AST produced by Flexmark into an presentation level representation
 * which is compatible with compose.
 */
@Suppress("TooManyFunctions")
object MarkyMarkConverter {

    internal const val CONVERTER_TAG = "Converter"

    /**
     * Convert [document] child [Node]s to [StableNode]s. This mapping happens as asynchronously as possible on the
     * [Dispatchers.Default] dispatcher. See [mapAsync] & [mapAsyncIndexed] for more details.
     */
    suspend fun convertToStableNodes(document: Document): ImmutableList<StableNode> {
        return convertToStableNodes(document.children)
    }

    internal suspend fun convertToStableNodes(nodes: Iterable<Node>): ImmutableList<StableNode> {
        return nodes.mapAsync(::convertToStableNode)
            .filterNotNull()
            .toImmutableList()
    }

    internal suspend fun convertToAnnotatedNodes(nodes: Iterable<Node>): ImmutableList<AnnotatedStableNode> {
        return nodes.mapAsync(AnnotatedStableNodeConverter::convertToAnnotatedNode)
            .filterNotNull()
            .toImmutableList()
    }
}
