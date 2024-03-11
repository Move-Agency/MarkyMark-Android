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

package com.moveagency.markymark.converter

import com.moveagency.markymark.converter.AnnotatedStableNodeConverter.convertToAnnotatedNode
import com.moveagency.markymark.converter.ComposableStableNodeConverter.convertToStableNode
import com.moveagency.markymark.model.NodeMetadata
import com.moveagency.markymark.model.annotated.AnnotatedStableNode
import com.moveagency.markymark.model.composable.ComposableStableNode
import com.moveagency.markymark.util.mapAsync
import com.moveagency.markymark.util.mapAsyncIndexed
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers

/**
 * This object contains the logic for converting the AST produced by Flexmark into a presentation level representation
 * which is compatible with compose.
 */
@Suppress("TooManyFunctions")
object MarkyMarkConverter {

    internal const val CONVERTER_TAG = "Converter"

    /**
     * Convert [document] child [Node]s to [ComposableStableNode]s. This mapping happens as asynchronously as possible
     * on the [Dispatchers.Default] dispatcher. See [mapAsync] & [mapAsyncIndexed] for more details.
     */
    suspend fun convertToStableNodes(document: Document): ImmutableList<ComposableStableNode> {
        return convertToStableNodes(nodes = document.children, metadata = NodeMetadata.Root)
    }

    internal suspend fun convertToStableNodes(
        metadata: NodeMetadata,
        nodes: Iterable<Node>,
    ): ImmutableList<ComposableStableNode> {
        return nodes.mapAsync { convertToStableNode(metadata = metadata, node = it) }
            .filterNotNull()
            .toImmutableList()
    }

    internal suspend fun convertToAnnotatedNodes(
        metadata: NodeMetadata,
        nodes: Iterable<Node>,
    ): ImmutableList<AnnotatedStableNode> {
        return nodes.mapAsync { convertToAnnotatedNode(metadata = metadata, node = it) }
            .filterNotNull()
            .toImmutableList()
    }
}
