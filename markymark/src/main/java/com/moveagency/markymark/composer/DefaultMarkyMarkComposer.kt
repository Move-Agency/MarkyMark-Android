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

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import com.moveagency.markymark.composable.CodeBlock
import com.moveagency.markymark.composable.Headline
import com.moveagency.markymark.composable.Image
import com.moveagency.markymark.composable.ListItem
import com.moveagency.markymark.composable.Rule
import com.moveagency.markymark.composable.TableBlock
import com.moveagency.markymark.composable.TextNode
import com.moveagency.markymark.composable.blockQuoteItem
import com.moveagency.markymark.model.AnnotatedStableNode
import com.moveagency.markymark.model.ComposableStableNode.BlockQuote
import com.moveagency.markymark.model.ComposableStableNode.CodeBlock
import com.moveagency.markymark.model.ComposableStableNode.Headline
import com.moveagency.markymark.model.ComposableStableNode.Headline.Level.HEADING1
import com.moveagency.markymark.model.ComposableStableNode.Headline.Level.HEADING2
import com.moveagency.markymark.model.ComposableStableNode.Headline.Level.HEADING3
import com.moveagency.markymark.model.ComposableStableNode.Headline.Level.HEADING4
import com.moveagency.markymark.model.ComposableStableNode.Headline.Level.HEADING5
import com.moveagency.markymark.model.ComposableStableNode.Headline.Level.HEADING6
import com.moveagency.markymark.model.ComposableStableNode.Image
import com.moveagency.markymark.model.ComposableStableNode.ListBlock
import com.moveagency.markymark.model.ComposableStableNode.ListEntry
import com.moveagency.markymark.model.ComposableStableNode.ListEntry.ListItem
import com.moveagency.markymark.model.ComposableStableNode.ListEntry.ListNode
import com.moveagency.markymark.model.ComposableStableNode.Paragraph
import com.moveagency.markymark.model.ComposableStableNode.Rule
import com.moveagency.markymark.model.ComposableStableNode.TableBlock
import com.moveagency.markymark.model.StableNode
import com.moveagency.markymark.theme.ComposableStyles
import com.moveagency.markymark.theme.ListBlockStyle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * The default implementation of [MarkyMarkComposer]. All functions are open to allow customizing the rendering process.
 */
open class DefaultMarkyMarkComposer : MarkyMarkComposer {

    @Suppress("ComplexMethod", "LongMethod")
    protected open fun LazyListScope.createNode(
        modifier: Modifier,
        node: StableNode,
        styles: ComposableStyles,
    ) = when (node) {
        is Headline -> createHeadline(
            modifier = modifier,
            node = node,
            styles = styles,
        )
        is Image -> createImage(
            modifier = modifier,
            node = node,
            styles = styles,
        )
        is Paragraph -> createParagraph(
            modifier = modifier,
            node = node,
            styles = styles,
        )
        is Rule -> createRule(
            modifier = modifier,
            styles = styles,
        )
        is CodeBlock -> createCodeBlock(
            modifier = modifier,
            node = node,
            styles = styles,
        )
        is BlockQuote -> createBlockQuote(
            modifier = modifier,
            node = node,
            styles = styles,
        )
        is TableBlock -> createTableBlock(
            modifier = modifier,
            node = node,
            styles = styles,
        )
        is ListBlock -> createListEntries(
            modifier = modifier,
            entries = node.children,
            level = node.level,
            styles = styles,
        )
        is AnnotatedStableNode -> createTextNode(
            modifier = modifier,
            node = node,
            styles = styles,
        )
    }

    override fun LazyListScope.createNodes(
        modifier: Modifier,
        nodes: ImmutableList<StableNode>,
        styles: ComposableStyles,
    ) {
        for (node in nodes) createNode(modifier = modifier, node = node, styles = styles)
    }

    protected open fun LazyListScope.createHeadline(
        modifier: Modifier,
        node: Headline,
        styles: ComposableStyles,
    ) = item(contentType = "${Headline::class.qualifiedName}.${node.level.name}") {
        val headingStyles = styles.headings
        Headline(
            modifier = modifier.fillParentMaxWidth(),
            node = node,
            style = when (node.level) {
                HEADING1 -> headingStyles.heading1
                HEADING2 -> headingStyles.heading2
                HEADING3 -> headingStyles.heading3
                HEADING4 -> headingStyles.heading4
                HEADING5 -> headingStyles.heading5
                HEADING6 -> headingStyles.heading6
            },
        )
    }

    protected open fun LazyListScope.createImage(
        modifier: Modifier,
        node: Image,
        styles: ComposableStyles,
    ) = item(contentType = Image::class.qualifiedName) {
        Image(
            modifier = modifier.fillParentMaxWidth(),
            node = node,
            style = styles.image,
        )
    }

    protected open fun LazyListScope.createParagraph(
        modifier: Modifier,
        node: Paragraph,
        styles: ComposableStyles,
    ) {
        val style = styles.paragraph
        for ((index, child) in node.children.withIndex()) {
            val childModifier = when (index) {
                0 -> modifier.paddingTop(style.padding)
                node.children.lastIndex -> modifier.paddingBottom(style.padding)
                else -> modifier
            }
            createNode(
                modifier = childModifier.paddingHorizontal(style.padding),
                node = child,
                styles = styles,
            )
        }
    }

    protected open fun LazyListScope.createTextNode(
        modifier: Modifier,
        node: AnnotatedStableNode,
        styles: ComposableStyles,
    ) = item(contentType = AnnotatedStableNode::class.qualifiedName) {
        val style = styles.textNode
        TextNode(
            modifier = modifier
                .fillParentMaxWidth()
                .padding(style.padding),
            nodes = persistentListOf(node),
            style = style.textStyle,
        )
    }

    protected open fun LazyListScope.createRule(
        modifier: Modifier,
        styles: ComposableStyles,
    ) = item(contentType = Rule::class.qualifiedName) {
        Rule(
            modifier = modifier.fillParentMaxWidth(),
            style = styles.rule,
        )
    }

    protected open fun LazyListScope.createCodeBlock(
        modifier: Modifier,
        node: CodeBlock,
        styles: ComposableStyles,
    ) = item(contentType = CodeBlock::class.qualifiedName) {
        CodeBlock(
            modifier = modifier,
            node = node,
            style = styles.codeBlock,
        )
    }

    protected open fun LazyListScope.createBlockQuote(
        modifier: Modifier,
        node: BlockQuote,
        styles: ComposableStyles,
    ) {
        for ((index, child) in node.children.withIndex()) {
            if (child is ListBlock) {
                createQuoteListBlock(
                    modifier = modifier,
                    entries = child.children,
                    styles = styles,
                    isTop = index == 0,
                    isBottom = index == node.children.lastIndex,
                )
            } else {
                createQuoteChild(
                    modifier = modifier,
                    node = child,
                    styles = styles,
                    isTop = index == 0,
                    isBottom = index == node.children.lastIndex,
                )
            }
        }
    }

    // Because of how lists and quotes interact we need to handle lists inside of a quote block separately. If we don't
    // do so, the list will break the quote indicator.
    @Suppress("ComplexMethod", "NestedBlockDepth")
    private fun LazyListScope.createQuoteListBlock(
        modifier: Modifier,
        entries: ImmutableList<ListEntry>,
        styles: ComposableStyles,
        isTop: Boolean,
        isBottom: Boolean,
    ) {
        val listStyle = styles.listBlock
        val quoteStyle = styles.blockQuote
        for ((index, entry) in entries.withIndex()) {
            val childModifier = modifier
                .run { if (isTop && index == 0) paddingTop(quoteStyle.outerPadding) else this }
                .run { if (isBottom && index == entries.lastIndex) paddingBottom(quoteStyle.outerPadding) else this }
                .blockQuoteItem(quoteStyle)
                .run { if (isTop && index == 0) paddingTop(quoteStyle.innerPadding) else this }
                .run { if (isBottom && index == entries.lastIndex) paddingBottom(quoteStyle.innerPadding) else this }
                .run { if (index == 0) paddingTop(listStyle.padding) else this }
                .run { if (index == entries.lastIndex) paddingBottom(listStyle.padding) else this }

            when (entry) {
                is ListItem -> createListItem(
                    modifier = childModifier
                        .paddingHorizontal(quoteStyle.innerPadding)
                        .paddingHorizontal(listStyle.padding),
                    node = entry,
                    level = 0,
                    style = listStyle,
                )
                is ListNode -> createNode(
                    modifier = childModifier
                        .paddingHorizontal(quoteStyle.innerPadding)
                        .padding(start = listStyle.levelIndent)
                        .paddingHorizontal(listStyle.padding),
                    node = entry.node,
                    styles = styles,
                )
            }
        }
    }

    private fun LazyListScope.createQuoteChild(
        modifier: Modifier,
        node: StableNode,
        styles: ComposableStyles,
        isTop: Boolean,
        isBottom: Boolean,
    ) {
        val style = styles.blockQuote
        val childModifier = modifier
            .run { if (isTop) paddingTop(style.outerPadding) else this }
            .run { if (isBottom) paddingBottom(style.outerPadding) else this }
            .blockQuoteItem(style)
            .run { if (isTop) paddingTop(style.innerPadding) else this }
            .run { if (isBottom) paddingBottom(style.innerPadding) else this }

        createNode(
            modifier = childModifier,
            node = node,
            styles = styles,
        )
    }

    protected open fun LazyListScope.createTableBlock(
        modifier: Modifier,
        node: TableBlock,
        styles: ComposableStyles,
    ) = item(contentType = TableBlock::class.qualifiedName) {
        TableBlock(
            modifier = modifier.fillParentMaxWidth(),
            node = node,
            style = styles.tableBlock,
        )
    }

    protected open fun LazyListScope.createListEntries(
        modifier: Modifier,
        entries: ImmutableList<ListEntry>,
        level: Int,
        styles: ComposableStyles,
    ) {
        for ((index, entry) in entries.withIndex()) {
            createListEntry(
                modifier = modifier,
                entry = entry,
                level = level,
                isFirst = index == 0,
                isLast = index == entries.lastIndex,
                styles = styles,
            )
        }
    }

    private fun LazyListScope.createListEntry(
        modifier: Modifier,
        entry: ListEntry,
        level: Int,
        isFirst: Boolean,
        isLast: Boolean,
        styles: ComposableStyles,
    ) {
        val style = styles.listBlock
        val childModifier = modifier
            .run { if (level == 0 && isFirst) paddingTop(style.padding) else this }
            .run { if (level == 0 && isLast) paddingBottom(style.padding) else this }

        when (entry) {
            is ListItem -> createListItem(
                modifier = childModifier.paddingHorizontal(style.padding),
                node = entry,
                level = level,
                style = style,
            )
            is ListNode -> createNode(
                modifier = childModifier
                    .padding(start = style.levelIndent)
                    .paddingHorizontal(style.padding),
                node = entry.node,
                styles = styles,
            )
        }
    }

    protected open fun LazyListScope.createListItem(
        modifier: Modifier,
        node: ListItem,
        level: Int,
        style: ListBlockStyle,
    ) = item(contentType = node.type::class.qualifiedName) {
        ListItem(
            modifier = modifier.fillParentMaxWidth(),
            type = node.type,
            children = node.children,
            blockStyle = style,
            level = level,
        )
    }
}
