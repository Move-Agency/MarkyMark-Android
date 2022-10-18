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

package com.m2mobi.markymark.converter

import com.m2mobi.markymark.converter.AnnotatedStableNodeConverter.convertToAnnotatedNode
import com.m2mobi.markymark.converter.AnnotatedStableNodeConverter.unescapeHtml
import com.m2mobi.markymark.converter.MarkyMarkConverter.convertToAnnotatedNodes
import com.m2mobi.markymark.model.AnnotatedStableNode
import com.m2mobi.markymark.model.ComposableStableNode
import com.m2mobi.markymark.model.StableNode
import com.m2mobi.markymark.util.mapAsync
import com.m2mobi.markymark.util.mapAsyncIndexed
import com.vladsch.flexmark.ast.BlockQuote
import com.vladsch.flexmark.ast.FencedCodeBlock
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.Image
import com.vladsch.flexmark.ast.IndentedCodeBlock
import com.vladsch.flexmark.ast.ListBlock
import com.vladsch.flexmark.ast.ListItem
import com.vladsch.flexmark.ast.OrderedListItem
import com.vladsch.flexmark.ast.Paragraph
import com.vladsch.flexmark.ast.ThematicBreak
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListItem
import com.vladsch.flexmark.ext.tables.TableBlock
import com.vladsch.flexmark.ext.tables.TableBody
import com.vladsch.flexmark.ext.tables.TableCell
import com.vladsch.flexmark.ext.tables.TableHead
import com.vladsch.flexmark.ext.tables.TableRow
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
    internal suspend fun convertToStableNode(node: Node): StableNode? = when (node) {
        is Heading -> convertHeadingNode(node)
        is ThematicBreak -> ComposableStableNode.Rule
        is Paragraph -> convertParagraphNode(node)
        is Image -> convertImageNode(node)
        is FencedCodeBlock -> convertFencedCodeBlockNode(node)
        is IndentedCodeBlock -> convertIndentedCodeBlockNode(node)
        is BlockQuote -> convertBlockQuoteNode(node)
        is TableBlock -> convertTableBlockNode(node)
        is ListBlock -> convertListBlockNode(node)
        else -> convertToAnnotatedNode(node)
    }

    @Suppress("MagicNumber")
    private suspend fun convertHeadingNode(heading: Heading): ComposableStableNode.Headline {
        return ComposableStableNode.Headline(
            children = convertToAnnotatedNodes(heading.children),
            level = when (heading.level) {
                1 -> ComposableStableNode.Headline.Level.HEADING1
                2 -> ComposableStableNode.Headline.Level.HEADING2
                3 -> ComposableStableNode.Headline.Level.HEADING3
                4 -> ComposableStableNode.Headline.Level.HEADING4
                5 -> ComposableStableNode.Headline.Level.HEADING5
                else -> ComposableStableNode.Headline.Level.HEADING6
            }
        )
    }

    private suspend fun convertParagraphNode(paragraph: Paragraph): ComposableStableNode.Paragraph {
        return ComposableStableNode.Paragraph(children = convertParagraphChildren(paragraph.children))
    }

    private suspend fun convertParagraphChildren(children: Iterable<Node>): ImmutableList<StableNode> {
        return children.mapAsync(::convertToStableNode)
            .filterNotNull()
            .bundleParagraphText()
    }

    @Suppress("NestedBlockDepth")
    private fun List<StableNode>.bundleParagraphText(): ImmutableList<StableNode> {
        val returnList = mutableListOf<StableNode>()
        val currentChildren = mutableListOf<AnnotatedStableNode>()
        for (node in this) {
            when (node) {
                is ComposableStableNode -> {
                    if (currentChildren.isNotEmpty()) {
                        returnList += AnnotatedStableNode.ParagraphText(currentChildren.toImmutableList())
                        currentChildren.clear()
                    }
                    returnList += node
                }
                is AnnotatedStableNode -> currentChildren += node
            }
        }
        if (currentChildren.isNotEmpty()) {
            returnList += AnnotatedStableNode.ParagraphText(currentChildren.toImmutableList())
        }
        return returnList.toImmutableList()
    }

    private fun convertImageNode(image: Image): ComposableStableNode.Image {
        return ComposableStableNode.Image(
            url = image.url.unescapeHtml(),
            altText = image.text.unescapeHtml().takeUnless { it.isBlank() },
            title = image.title.unescapeHtml().takeUnless { it.isBlank() },
        )
    }

    private fun convertFencedCodeBlockNode(fencedCodeBlock: FencedCodeBlock): ComposableStableNode.CodeBlock {
        return ComposableStableNode.CodeBlock(
            content = fencedCodeBlock.firstChild?.chars?.unescapeHtml().orEmpty().trimEnd(),
            language = fencedCodeBlock.info.unescapeHtml(),
        )
    }

    private fun convertIndentedCodeBlockNode(indentedCodeBlock: IndentedCodeBlock): ComposableStableNode.CodeBlock {
        return ComposableStableNode.CodeBlock(
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

    private suspend fun convertBlockQuoteNode(blockQuote: BlockQuote): ComposableStableNode.BlockQuote {
        return ComposableStableNode.BlockQuote(children = MarkyMarkConverter.convertToStableNodes(blockQuote.children))
    }

    private suspend fun convertTableBlockNode(tableBlock: TableBlock): ComposableStableNode {
        return withContext(Dispatchers.Default) {
            val head = async { convertToTableRow(tableBlock.firstChild as TableHead) }
            val body = async {
                (tableBlock.lastChild as? TableBody)?.let { convertToTableRows(it) }.orEmpty().toImmutableList()
            }
            ComposableStableNode.TableBlock(
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

    private suspend fun convertToTableRows(
        nodes: Iterable<Node>
    ): ImmutableList<ComposableStableNode.TableRow> {
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

    private suspend fun convertListBlockNode(node: ListBlock, level: Int = 0): ComposableStableNode.ListBlock? {
        return convertListChildren(node.children, level)
            .takeUnless { it.isEmpty() }
            ?.let { ComposableStableNode.ListBlock(level, convertListChildren(node.children, level)) }
    }

    private suspend fun convertListChildren(
        nodes: Iterable<Node>,
        level: Int,
    ): ImmutableList<ComposableStableNode.ListEntry> {
        return nodes.mapAsyncIndexed { index, item -> convertListItem(index + 1, item as ListItem, level) }
            .flatten()
            .toImmutableList()
    }

    private suspend fun convertListItem(
        index: Int,
        item: ListItem,
        level: Int,
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
                .mapAsync { convertListNode(node = it, level = level) }
                .filterNotNull()
                .let(::addAll)
        }.toPersistentList()
    }

    private fun convertListItemType(index: Int, item: ListItem) = when (item) {
        is OrderedListItem -> ComposableStableNode.ListItemType.Ordered(index)
        is TaskListItem -> ComposableStableNode.ListItemType.Task(item.isItemDoneMarker)
        else -> ComposableStableNode.ListItemType.Unordered
    }

    private suspend fun convertListNode(node: Node, level: Int) = when (node) {
        is ListBlock -> convertListBlockNode(node, level + 1)
            ?.let(ComposableStableNode.ListEntry::ListNode)
        else -> convertToStableNode(node)?.let(ComposableStableNode.ListEntry::ListNode)
    }
}
