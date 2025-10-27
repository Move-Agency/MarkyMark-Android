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

package com.moveagency.markymark.composer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moveagency.markymark.composable.*
import com.moveagency.markymark.model.annotated.AnnotatedStableNode
import com.moveagency.markymark.model.composable.*
import com.moveagency.markymark.model.composable.ListBlock.ListEntry.ListItem
import com.moveagency.markymark.model.composable.ListBlock.ListEntry.ListNode
import com.moveagency.markymark.theme.LocalMarkyMarkTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * The default implementation of [MarkyMarkComposer]. All functions are open to allow customizing the rendering process.
 */
open class DefaultMarkyMarkComposer : MarkyMarkComposer {

    @Composable
    @Suppress("ComplexMethod", "LongMethod")
    protected open fun CreateNode(node: ComposableStableNode) {
        // Doing this here allows us to exclude certain elements from the screen padding like a rule for example.
        val modifier = if (node.metadata.isRootLevel) {
            Modifier.paddingHorizontal(LocalMarkyMarkTheme.current.root.screenPadding)
        } else {
            Modifier
        }

        when (node) {
            is Headline -> Headline(
                node = node,
                modifier = modifier,
            )
            is Image -> Image(
                modifier = if (LocalMarkyMarkTheme.current.styles.composable.image.fullWidth) Modifier else modifier,
                node = node,
            )
            is Paragraph -> Paragraph(
                modifier = modifier,
            ) {
                CreateNodes(node.children)
            }
            is Rule -> Rule()
            is CodeBlock -> CodeBlock(
                node = node,
                modifier = modifier,
            )
            is BlockQuote -> BlockQuote(
                node = node,
                modifier = modifier,
            ) {
                CreateNodes(node.children)
            }
            is TableBlock -> TableBlock(
                node = node,
                modifier = modifier,
            )
            is ListBlock -> ListBlock(
                node = node,
                modifier = modifier,
            ) {
                for (child in node.children) {
                    when (child) {
                        is ListItem -> MarkyMarkListItem(
                            item = child,
                            indentLevel = node.metadata.listLevel,
                        )
                        is ListNode -> CreateNode(child.node)
                    }
                }
            }
            is TextNode -> TextNode(
                nodes = persistentListOf(node.text),
                modifier = modifier,
            )
        }
    }

    @Composable
    override fun CreateNodes(nodes: ImmutableList<ComposableStableNode>) {
        for (node in nodes) CreateNode(node)
    }

    @Composable
    protected open fun Headline(
        node: Headline,
        modifier: Modifier,
    ) = MarkyMarkHeadline(
        node = node,
        modifier = modifier.fillMaxWidth(),
    )

    @Composable
    protected open fun Image(
        node: Image,
        modifier: Modifier,
    ) = MarkyMarkImage(
        modifier = modifier.fillMaxWidth(),
        node = node,
    )

    @Composable
    protected open fun Paragraph(
        modifier: Modifier,
        children: @Composable () -> Unit,
    ) = MarkyMarkParagraph(
        modifier = modifier,
    ) {
        children()
    }

    @Composable
    protected open fun Rule() = MarkyMarkRule(
        modifier = Modifier.fillMaxWidth(),
    )

    @Composable
    protected open fun CodeBlock(
        node: CodeBlock,
        modifier: Modifier,
    ) = MarkyMarkCode(
        node = node,
        modifier = modifier,
    )

    @Composable
    protected open fun BlockQuote(
        node: BlockQuote,
        modifier: Modifier,
        children: @Composable () -> Unit,
    ) = MarkyMarkQuote(
        node = node,
        modifier = modifier,
    ) {
        children()
    }

    @Composable
    protected open fun TableBlock(
        node: TableBlock,
        modifier: Modifier,
    ) = MarkyMarkTable(
        node = node,
        modifier = modifier,
    )

    @Composable
    protected open fun ListBlock(
        node: ListBlock,
        modifier: Modifier,
        children: @Composable () -> Unit,
    ) = MarkyMarkList(
        node = node,
        modifier = modifier,
    ) {
        children()
    }

    @Composable
    protected open fun TextNode(
        nodes: ImmutableList<AnnotatedStableNode>,
        modifier: Modifier,
    ) = MarkyMarkText(
        nodes = nodes,
        modifier = modifier,
    )
}
