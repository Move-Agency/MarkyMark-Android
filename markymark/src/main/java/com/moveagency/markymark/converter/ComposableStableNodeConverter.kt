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

package com.moveagency.markymark.converter

import com.moveagency.markymark.converter.AnnotatedStableNodeConverter.convertToAnnotatedNode
import com.moveagency.markymark.converter.AnnotatedStableNodeConverter.unescapeHtml
import com.moveagency.markymark.converter.MarkyMarkConverter.convertToAnnotatedNodes
import com.moveagency.markymark.model.AnnotatedStableNode
import com.moveagency.markymark.model.ComposableStableNode
import com.moveagency.markymark.util.mapAsync
import com.moveagency.markymark.util.mapAsyncIndexed
import com.vladsch.flexmark.ast.*
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListItem
import com.vladsch.flexmark.ext.tables.*
import com.vladsch.flexmark.util.ast.Node
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

@Suppress("TooManyFunctions")
object ComposableStableNodeConverter {

    @Suppress("ComplexMethod")
    internal suspend fun convertToStableNode(node: Node, level: Int): ComposableStableNode? = when (node) {
        is Heading -> convertHeadingNode(heading = node, level = level)
        is ThematicBreak -> ComposableStableNode.Rule(level)
        is Paragraph -> convertParagraphNode(paragraph = node, level = level)
        is Image -> convertImageNode(image = node, level = level)
        is FencedCodeBlock -> convertFencedCodeBlockNode(fencedCodeBlock = node, level = level)
        is IndentedCodeBlock -> convertIndentedCodeBlockNode(indentedCodeBlock = node, level = level)
        is BlockQuote -> convertBlockQuoteNode(blockQuote = node, level = level)
        is TableBlock -> convertTableBlockNode(tableBlock = node, level = level)
        is ListBlock -> convertListBlockNode(listBlock = node, level = level)
        else -> convertTextNode(node = node, level = level)
    }

    @Suppress("MagicNumber")
    private suspend fun convertHeadingNode(heading: Heading, level: Int): ComposableStableNode.Headline {
        return ComposableStableNode.Headline(
            level = level,
            children = convertToAnnotatedNodes(heading.children),
            headingLevel = when (heading.level) {
                1 -> ComposableStableNode.Headline.Level.HEADING1
                2 -> ComposableStableNode.Headline.Level.HEADING2
                3 -> ComposableStableNode.Headline.Level.HEADING3
                4 -> ComposableStableNode.Headline.Level.HEADING4
                5 -> ComposableStableNode.Headline.Level.HEADING5
                else -> ComposableStableNode.Headline.Level.HEADING6
            }
        )
    }

    private suspend fun convertParagraphNode(paragraph: Paragraph, level: Int): ComposableStableNode.Paragraph {
        return ComposableStableNode.Paragraph(
            level = level,
            children = convertParagraphChildren(children = paragraph.children, level = level + 1)
        )
    }

    private suspend fun convertParagraphChildren(
        children: Iterable<Node>,
        level: Int,
    ): ImmutableList<ComposableStableNode> {
        return children.mapAsync { convertToStableNode(node = it, level = level) }
            .filterNotNull()
            .bundleParagraphText(level)
    }

    @Suppress("NestedBlockDepth")
    private fun List<ComposableStableNode>.bundleParagraphText(level: Int): ImmutableList<ComposableStableNode> {
        val returnList = mutableListOf<ComposableStableNode>()
        val currentChildren = mutableListOf<AnnotatedStableNode>()
        for (node in this) {
            if (node is ComposableStableNode.TextNode) {
                currentChildren += node.text
            } else {
                if (currentChildren.isNotEmpty()) {
                    returnList += currentChildren.bundleIntoTextNode(level)
                    currentChildren.clear()
                }
                returnList += node
            }
        }
        if (currentChildren.isNotEmpty()) {
            returnList += currentChildren.bundleIntoTextNode(level)
        }
        return returnList.toImmutableList()
    }

    private fun List<AnnotatedStableNode>.bundleIntoTextNode(level: Int): ComposableStableNode.TextNode {
        return ComposableStableNode.TextNode(
            level = level,
            text = AnnotatedStableNode.ParagraphText(toImmutableList()),
        )
    }

    private fun convertImageNode(image: Image, level: Int): ComposableStableNode.Image {
        return ComposableStableNode.Image(
            level = level,
            url = image.url.unescapeHtml(),
            altText = image.text.unescapeHtml().takeUnless { it.isBlank() },
            title = image.title.unescapeHtml().takeUnless { it.isBlank() },
        )
    }

    private fun convertFencedCodeBlockNode(
        level: Int,
        fencedCodeBlock: FencedCodeBlock,
    ): ComposableStableNode.CodeBlock {
        return ComposableStableNode.CodeBlock(
            level = level,
            content = fencedCodeBlock.firstChild?.chars?.unescapeHtml().orEmpty().trimEnd(),
            language = fencedCodeBlock.info.unescapeHtml(),
        )
    }

    private fun convertIndentedCodeBlockNode(
        level: Int,
        indentedCodeBlock: IndentedCodeBlock
    ): ComposableStableNode.CodeBlock {
        return ComposableStableNode.CodeBlock(
            level = level,
            content = indentedCodeBlock.chars.unescapeHtml().lines().mapIndexed { index, line ->
                if (index == 0) line else line.dropIndent()
            }.joinToString("\n").trimEnd(),
            language = null,
        )
    }

    private const val NUM_SPACES_IN_TAB = 4

    private fun String.dropIndent(): String {
        var indentsDropped = 0
        return dropWhile {
            // Because FlexMark only removes the first lines indent but not the others we have to do it here.
            val shouldDrop = (it == '\t' && indentsDropped == 0) ||
                (it == ' ' && indentsDropped < NUM_SPACES_IN_TAB)
            if (shouldDrop) indentsDropped++
            shouldDrop
        }
    }

    private suspend fun convertBlockQuoteNode(blockQuote: BlockQuote, level: Int): ComposableStableNode.BlockQuote {
        return ComposableStableNode.BlockQuote(
            level = level,
            children = MarkyMarkConverter.convertToStableNodes(nodes = blockQuote.children, level = level + 1)
        )
    }

    private suspend fun convertTableBlockNode(tableBlock: TableBlock, level: Int): ComposableStableNode {
        return withContext(Dispatchers.Default) {
            val head = async { convertToTableRow(tableBlock.firstChild as TableHead) }
            val body = async {
                (tableBlock.lastChild as? TableBody)?.let { convertToTableRows(it) }.orEmpty().toImmutableList()
            }
            ComposableStableNode.TableBlock(
                level = level,
                head = head.await(),
                body = body.await(),
            )
        }
    }

    private suspend fun convertToTableRow(tableHead: TableHead): ComposableStableNode.TableRow {
        return convertToTableRow(tableHead.firstChild as TableRow)
    }

    private suspend fun convertToTableRows(tableBody: TableBody): List<ComposableStableNode.TableRow> {
        return convertToTableRows(tableBody.children)
    }

    private suspend fun convertToTableRows(nodes: Iterable<Node>): ImmutableList<ComposableStableNode.TableRow> {
        return nodes.filterIsInstance(TableRow::class.java)
            .mapAsync(::convertToTableRow)
            .toImmutableList()
    }

    private suspend fun convertToTableRow(tableRow: TableRow): ComposableStableNode.TableRow {
        return ComposableStableNode.TableRow(
            tableRow.children
                .filterIsInstance(TableCell::class.java)
                .mapAsync(::convertToTableCell)
                .toImmutableList()
        )
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    private suspend fun convertToTableCell(tableCell: TableCell): ComposableStableNode.TableCell {
        return ComposableStableNode.TableCell(
            children = convertToAnnotatedNodes(tableCell.children),
            alignment = when (tableCell.alignment) {
                TableCell.Alignment.LEFT -> ComposableStableNode.TableCell.Alignment.START
                TableCell.Alignment.CENTER -> ComposableStableNode.TableCell.Alignment.CENTER
                TableCell.Alignment.RIGHT -> ComposableStableNode.TableCell.Alignment.END
            }
        )
    }

    private suspend fun convertListBlockNode(
        listBlock: ListBlock,
        level: Int,
        indentLevel: Int = 0,
    ): ComposableStableNode.ListBlock? {
        return convertListChildren(nodes = listBlock.children, level = level + 1, indentLevel = indentLevel)
            .takeUnless { it.isEmpty() }
            ?.let {
                ComposableStableNode.ListBlock(
                    level = level,
                    indentLevel = indentLevel,
                    children = it,
                )
            }
    }

    private suspend fun convertListChildren(
        nodes: Iterable<Node>,
        level: Int,
        indentLevel: Int,
    ): ImmutableList<ComposableStableNode.ListEntry> {
        return nodes.mapAsyncIndexed { index, item ->
            convertListItem(
                index = index + 1,
                item = item as ListItem,
                level = level,
                indentLevel = indentLevel,
            )
        }.flatten().toImmutableList()
    }

    private suspend fun convertListItem(
        index: Int,
        item: ListItem,
        level: Int,
        indentLevel: Int,
    ): ImmutableList<ComposableStableNode.ListEntry> {
        val firstChild = item.firstChild
        if (firstChild == null || !firstChild.hasChildren()) return persistentListOf()

        val content = convertToAnnotatedNodes(firstChild.children)
        return buildList {
            // Enforce the first item to parsed as a list item. There is no way in the Markdown spec it won't be and
            // this greatly simplifies displaying lists later.
            add(
                ComposableStableNode.ListEntry.ListItem(
                    type = convertListItemType(index = index, item = item),
                    children = content,
                )
            )

            item.children
                .drop(1) // Drop first child as we converted that to list item above
                .mapAsync { convertListNode(node = it, level = level, indentLevel = indentLevel) }
                .filterNotNull()
                .let(::addAll)
        }.toPersistentList()
    }

    private fun convertListItemType(index: Int, item: ListItem) = when (item) {
        is OrderedListItem -> ComposableStableNode.ListItemType.Ordered(index)
        is TaskListItem -> ComposableStableNode.ListItemType.Task(item.isItemDoneMarker)
        else -> ComposableStableNode.ListItemType.Unordered
    }

    private suspend fun convertListNode(node: Node, level: Int, indentLevel: Int) = when (node) {
        is ListBlock -> convertListBlockNode(listBlock = node, level = level, indentLevel = indentLevel + 1)
            ?.let(ComposableStableNode.ListEntry::ListNode)
        else -> convertToStableNode(node = node, level = level + 1)
            ?.let(ComposableStableNode.ListEntry::ListNode)
    }

    private suspend fun convertTextNode(node: Node, level: Int): ComposableStableNode.TextNode? {
        return convertToAnnotatedNode(node)?.let { ComposableStableNode.TextNode(level = level, text = it) }
    }
}
