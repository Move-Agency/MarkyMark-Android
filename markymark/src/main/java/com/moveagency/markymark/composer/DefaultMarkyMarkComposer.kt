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

package com.moveagency.markymark.composer

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import com.moveagency.markymark.composable.*
import com.moveagency.markymark.model.annotated.AnnotatedStableNode
import com.moveagency.markymark.model.composable.*
import com.moveagency.markymark.model.composable.Headline.Level.*
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
        node: ComposableStableNode,
        styles: ComposableStyles,
        isFirst: Boolean = false,
        isLast: Boolean = false,
    ) {
        // Doing this here allows us to exclude certain elements from the screen padding like a rule for example.
        val screenPaddingModifier = Modifier.screenPadding(
            isRootLevel = node.metadata.isRootLevel,
            isFirst = isFirst,
            isLast = isLast,
            screenPadding = styles.screenPadding,
        )
        when (node) {
            is Headline -> createHeadline(
                modifier = modifier.then(screenPaddingModifier),
                node = node,
                styles = styles,
            )
            is Image -> createImage(
                modifier = modifier.then(screenPaddingModifier),
                node = node,
                styles = styles,
            )
            is Paragraph -> createParagraph(
                modifier = modifier.then(screenPaddingModifier),
                node = node,
                styles = styles,
            )
            is Rule -> createRule(
                modifier = modifier,
                styles = styles,
            )
            is CodeBlock -> createCodeBlock(
                modifier = modifier.then(screenPaddingModifier),
                node = node,
                styles = styles,
            )
            is BlockQuote -> createBlockQuote(
                modifier = modifier.then(screenPaddingModifier),
                node = node,
                styles = styles,
            )
            is TableBlock -> createTableBlock(
                modifier = modifier.then(screenPaddingModifier),
                node = node,
                styles = styles,
            )
            is ListBlock -> createListEntries(
                modifier = modifier.then(screenPaddingModifier),
                entries = node.children,
                indentLevel = node.metadata.listLevel,
                styles = styles,
            )
            is TextNode -> createTextNode(
                modifier = modifier.then(screenPaddingModifier),
                node = node,
                styles = styles,
            )
        }
    }

    override fun LazyListScope.createNodes(
        modifier: Modifier,
        nodes: ImmutableList<ComposableStableNode>,
        styles: ComposableStyles,
    ) {
        for ((index, node) in nodes.withIndex()) {
            createNode(
                modifier = modifier,
                node = node,
                styles = styles,
                isFirst = index == 0,
                isLast = index == nodes.lastIndex,
            )
        }
    }

    protected open fun LazyListScope.createHeadline(
        modifier: Modifier,
        node: Headline,
        styles: ComposableStyles,
    ) = item(contentType = "${Headline::class.qualifiedName}.${node.headingLevel.name}") {
        val headingStyles = styles.headings
        Headline(
            modifier = Modifier
                .fillParentMaxWidth()
                .then(modifier),
            node = node,
            style = when (node.headingLevel) {
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
            modifier = Modifier
                .fillParentMaxWidth()
                .then(modifier),
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
        node: TextNode,
        styles: ComposableStyles,
    ) = item(contentType = AnnotatedStableNode::class.qualifiedName) {
        val style = styles.textNode
        TextNode(
            modifier = Modifier
                .fillParentMaxWidth()
                .then(modifier)
                .padding(style.padding),
            nodes = persistentListOf(node.text),
            style = style.textStyle,
        )
    }

    protected open fun LazyListScope.createRule(
        modifier: Modifier,
        styles: ComposableStyles,
    ) = item(contentType = Rule::class.qualifiedName) {
        Rule(
            modifier = Modifier
                .fillParentMaxWidth()
                .then(modifier),
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
                    level = node.metadata.level,
                )
            } else {
                createQuoteChild(
                    modifier = modifier,
                    node = child,
                    styles = styles,
                    isTop = index == 0,
                    isBottom = index == node.children.lastIndex,
                    level = node.metadata.level,
                )
            }
        }
    }

    // Because of how lists and quotes interact we need to handle lists inside of a quote block separately. If we don't
    // do so, the list will break the quote indicator.
    @Suppress("ComplexMethod", "NestedBlockDepth")
    private fun LazyListScope.createQuoteListBlock(
        modifier: Modifier,
        entries: ImmutableList<ListBlock.ListEntry>,
        styles: ComposableStyles,
        isTop: Boolean,
        isBottom: Boolean,
        level: Int,
    ) {
        val listStyle = styles.listBlock
        val quoteStyle = styles.blockQuote
        for ((index, entry) in entries.withIndex()) {
            val childModifier = modifier
                .run { if (isTop && index == 0) paddingTop(quoteStyle.outerPadding) else this }
                .run { if (isBottom && index == entries.lastIndex) paddingBottom(quoteStyle.outerPadding) else this }
                .blockQuoteItem(
                    style = quoteStyle,
                    isTop = isTop && index == 0,
                    isBottom = isBottom && index == entries.lastIndex,
                    level = level,
                )
                .run { if (isTop && index == 0) paddingTop(quoteStyle.innerPadding) else this }
                .run { if (isBottom && index == entries.lastIndex) paddingBottom(quoteStyle.innerPadding) else this }
                .run { if (index == 0) paddingTop(listStyle.padding) else this }
                .run { if (index == entries.lastIndex) paddingBottom(listStyle.padding) else this }

            when (entry) {
                is ListBlock.ListEntry.ListItem -> createListItem(
                    modifier = childModifier
                        .paddingHorizontal(quoteStyle.innerPadding)
                        .paddingHorizontal(listStyle.padding),
                    node = entry,
                    indentLevel = 0,
                    style = listStyle,
                )
                is ListBlock.ListEntry.ListNode -> createNode(
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
        node: ComposableStableNode,
        styles: ComposableStyles,
        isTop: Boolean,
        isBottom: Boolean,
        level: Int,
    ) {
        val style = styles.blockQuote
        val childModifier = modifier
            .run { if (isTop) paddingTop(style.outerPadding) else this }
            .run { if (isBottom) paddingBottom(style.outerPadding) else this }
            .blockQuoteItem(
                style = style,
                isTop = isTop,
                isBottom = isBottom,
                level = level,
            )
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
            modifier = modifier,
            node = node,
            style = styles.tableBlock,
        )
    }

    protected open fun LazyListScope.createListEntries(
        modifier: Modifier,
        entries: ImmutableList<ListBlock.ListEntry>,
        indentLevel: Int,
        styles: ComposableStyles,
    ) {
        for ((index, entry) in entries.withIndex()) {
            createListEntry(
                modifier = modifier,
                entry = entry,
                indentLevel = indentLevel,
                isFirst = index == 0,
                isLast = index == entries.lastIndex,
                styles = styles,
            )
        }
    }

    private fun LazyListScope.createListEntry(
        modifier: Modifier,
        entry: ListBlock.ListEntry,
        indentLevel: Int,
        isFirst: Boolean,
        isLast: Boolean,
        styles: ComposableStyles,
    ) {
        val style = styles.listBlock
        val childModifier = modifier
            .run { if (indentLevel == 0 && isFirst) paddingTop(style.padding) else this }
            .run { if (indentLevel == 0 && isLast) paddingBottom(style.padding) else this }

        when (entry) {
            is ListBlock.ListEntry.ListItem -> createListItem(
                modifier = childModifier.paddingHorizontal(style.padding),
                node = entry,
                indentLevel = indentLevel,
                style = style,
            )
            is ListBlock.ListEntry.ListNode -> createNode(
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
        node: ListBlock.ListEntry.ListItem,
        indentLevel: Int,
        style: ListBlockStyle,
    ) = item(contentType = node.type::class.qualifiedName) {
        ListItem(
            type = node.type,
            children = node.children,
            blockStyle = style,
            indentLevel = indentLevel,
            modifier = Modifier
                .fillParentMaxWidth()
                .then(modifier),
        )
    }

    private fun Modifier.screenPadding(
        isRootLevel: Boolean,
        isFirst: Boolean,
        isLast: Boolean,
        screenPadding: Padding,
    ): Modifier {
        return if (isRootLevel) {
            paddingHorizontal(screenPadding)
                .run { if (isFirst) paddingTop(screenPadding) else this }
                .run { if (isLast) paddingBottom(screenPadding) else this }
        } else {
            this
        }
    }
}
