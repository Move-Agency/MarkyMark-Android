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

import android.util.Log
import com.moveagency.markymark.converter.AnnotatedStableNodeConverter.convertToAnnotatedNode
import com.moveagency.markymark.converter.AnnotatedStableNodeConverter.unescapeHtml
import com.moveagency.markymark.converter.MarkyMarkConverter.convertToAnnotatedNodes
import com.moveagency.markymark.model.NodeMetadata
import com.moveagency.markymark.model.annotated.AnnotatedStableNode
import com.moveagency.markymark.model.annotated.ParagraphText
import com.moveagency.markymark.model.composable.*
import com.moveagency.markymark.model.composable.CodeBlock
import com.moveagency.markymark.util.mapAsync
import com.moveagency.markymark.util.mapAsyncIndexed
import com.vladsch.flexmark.ast.*
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListItem
import com.vladsch.flexmark.ext.tables.TableBody
import com.vladsch.flexmark.ext.tables.TableHead
import com.vladsch.flexmark.util.ast.Node
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import com.vladsch.flexmark.ast.BlockQuote as FlexBlockQuote
import com.vladsch.flexmark.ast.Image as FlexImage
import com.vladsch.flexmark.ast.ListBlock as FlexListBlock
import com.vladsch.flexmark.ast.Paragraph as FlexParagraph
import com.vladsch.flexmark.ext.tables.TableBlock as FlexTableBlock
import com.vladsch.flexmark.ext.tables.TableCell as FlexTableCell
import com.vladsch.flexmark.ext.tables.TableRow as FlexTableRow

@Suppress("TooManyFunctions")
object ComposableStableNodeConverter {

    @Suppress("ComplexMethod")
    internal suspend fun convertToStableNode(
        metadata: NodeMetadata,
        node: Node,
    ): ComposableStableNode? = when (node) {
        is Heading -> convertHeadingNode(metadata = metadata, heading = node)
        is ThematicBreak -> Rule(metadata = metadata)
        is FlexParagraph -> convertParagraphNode(metadata = metadata, paragraph = node)
        is FlexImage -> convertImageNode(metadata = metadata, image = node)
        is FencedCodeBlock -> convertFencedCodeBlockNode(metadata = metadata, fencedCodeBlock = node)
        is IndentedCodeBlock -> convertIndentedCodeBlockNode(metadata = metadata, indentedCodeBlock = node)
        is FlexBlockQuote -> convertBlockQuoteNode(metadata = metadata, blockQuote = node)
        is FlexTableBlock -> convertTableBlockNode(metadata = metadata, tableBlock = node)
        is FlexListBlock -> convertListBlockNode(metadata = metadata, listBlock = node)
        else -> convertTextNode(metadata = metadata, node = node)
    }

    @Suppress("MagicNumber")
    private suspend fun convertHeadingNode(
        metadata: NodeMetadata,
        heading: Heading,
    ): Headline {
        return Headline(
            metadata = metadata,
            children = convertToAnnotatedNodes(metadata = metadata, nodes = heading.children),
            headingLevel = when (heading.level) {
                1 -> Headline.Level.HEADING1
                2 -> Headline.Level.HEADING2
                3 -> Headline.Level.HEADING3
                4 -> Headline.Level.HEADING4
                5 -> Headline.Level.HEADING5
                else -> Headline.Level.HEADING6
            }
        )
    }

    private suspend fun convertParagraphNode(metadata: NodeMetadata, paragraph: FlexParagraph): Paragraph {
        return Paragraph(
            metadata = metadata,
            children = convertParagraphChildren(
                metadata = metadata.incParagraphLevel(),
                children = paragraph.children,
            )
        )
    }

    private suspend fun convertParagraphChildren(
        metadata: NodeMetadata,
        children: Iterable<Node>,
    ): ImmutableList<ComposableStableNode> {
        return children.mapAsync { convertToStableNode(metadata = metadata, node = it) }
            .filterNotNull()
            .bundleParagraphText(metadata)
    }

    @Suppress("NestedBlockDepth")
    private fun List<ComposableStableNode>.bundleParagraphText(
        metadata: NodeMetadata,
    ): ImmutableList<ComposableStableNode> {
        val returnList = mutableListOf<ComposableStableNode>()
        val currentChildren = mutableListOf<AnnotatedStableNode>()
        for (node in this) {
            if (node is TextNode) {
                currentChildren += node.text
            } else {
                if (currentChildren.isNotEmpty()) {
                    returnList += currentChildren.bundleIntoTextNode(metadata)
                    currentChildren.clear()
                }
                returnList += node
            }
        }
        if (currentChildren.isNotEmpty()) {
            returnList += currentChildren.bundleIntoTextNode(metadata)
        }
        return returnList.toImmutableList()
    }

    private fun List<AnnotatedStableNode>.bundleIntoTextNode(metadata: NodeMetadata): TextNode {
        return TextNode(
            metadata = metadata,
            text = ParagraphText(
                metadata = metadata,
                children = toImmutableList(),
            ),
        )
    }

    private fun convertImageNode(metadata: NodeMetadata, image: FlexImage): Image {
        return Image(
            metadata = metadata,
            url = image.url.unescapeHtml(),
            altText = image.text.unescapeHtml().takeUnless { it.isBlank() },
            title = image.title.unescapeHtml().takeUnless { it.isBlank() },
        )
    }

    private fun convertFencedCodeBlockNode(
        metadata: NodeMetadata,
        fencedCodeBlock: FencedCodeBlock,
    ): CodeBlock {
        return CodeBlock(
            metadata = metadata,
            content = fencedCodeBlock.firstChild?.chars?.unescapeHtml().orEmpty().trimEnd(),
            language = fencedCodeBlock.info.unescapeHtml(),
        )
    }

    private fun convertIndentedCodeBlockNode(
        metadata: NodeMetadata,
        indentedCodeBlock: IndentedCodeBlock,
    ): CodeBlock {
        return CodeBlock(
            metadata = metadata,
            content = indentedCodeBlock.chars.unescapeHtml().lines().mapIndexed { index, line ->
                if (index == 0) line else line.dropIndent()
            }.joinToString("\n").trimEnd(),
            language = null,
        )
    }

    private const val NumSpacesInTab = 4

    private fun String.dropIndent(): String {
        var indentsDropped = 0
        return dropWhile {
            // Because FlexMark only removes the first line's indent but not the others we have to do it here.
            val shouldDrop = (it == '\t' && indentsDropped == 0) ||
                (it == ' ' && indentsDropped < NumSpacesInTab)
            if (shouldDrop) indentsDropped++
            shouldDrop
        }
    }

    private suspend fun convertBlockQuoteNode(
        metadata: NodeMetadata,
        blockQuote: FlexBlockQuote,
    ): BlockQuote {
        Log.d("[!!!]", blockQuote.chars.toString())
        Log.d("[!!!]", blockQuote.children.map { it::class.simpleName }.toString())
        return BlockQuote(
            metadata = metadata,
            children = MarkyMarkConverter.convertToStableNodes(
                nodes = blockQuote.children,
                metadata = metadata.incQuoteLevel(),
            )
        ).also {
            Log.d("[!!!]", it.toString())
        }
    }

    private suspend fun convertTableBlockNode(
        metadata: NodeMetadata,
        tableBlock: FlexTableBlock,
    ): ComposableStableNode {
        return withContext(Dispatchers.Default) {
            val head = async { convertToTableRow(metadata = metadata, tableHead = tableBlock.firstChild as TableHead) }
            val body = async {
                (tableBlock.lastChild as? TableBody)
                    ?.let { convertToTableRows(metadata = metadata, tableBody = it) }
                    .orEmpty()
                    .toImmutableList()
            }
            TableBlock(
                metadata = metadata,
                head = head.await(),
                body = body.await(),
            )
        }
    }

    private suspend fun convertToTableRow(
        metadata: NodeMetadata,
        tableHead: TableHead,
    ): TableBlock.TableRow {
        return convertToTableRow(metadata = metadata, tableRow = tableHead.firstChild as FlexTableRow)
    }

    private suspend fun convertToTableRows(
        metadata: NodeMetadata,
        tableBody: TableBody,
    ): ImmutableList<TableBlock.TableRow> {
        return convertToTableRows(metadata = metadata, nodes = tableBody.children)
    }

    private suspend fun convertToTableRows(
        metadata: NodeMetadata,
        nodes: Iterable<Node>,
    ): ImmutableList<TableBlock.TableRow> {
        return nodes.filterIsInstance(FlexTableRow::class.java)
            .mapAsync { convertToTableRow(metadata = metadata, tableRow = it) }
            .toImmutableList()
    }

    private suspend fun convertToTableRow(
        metadata: NodeMetadata,
        tableRow: FlexTableRow,
    ): TableBlock.TableRow {
        return TableBlock.TableRow(
            tableRow.children
                .filterIsInstance(FlexTableCell::class.java)
                .mapAsync { convertToTableCell(metadata = metadata, tableCell = it) }
                .toImmutableList()
        )
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    private suspend fun convertToTableCell(
        metadata: NodeMetadata,
        tableCell: FlexTableCell,
    ): TableBlock.TableCell {
        return TableBlock.TableCell(
            children = convertToAnnotatedNodes(metadata = metadata, nodes = tableCell.children),
            alignment = when (tableCell.alignment) {
                FlexTableCell.Alignment.LEFT -> TableBlock.TableCell.Alignment.Start
                FlexTableCell.Alignment.CENTER -> TableBlock.TableCell.Alignment.Center
                FlexTableCell.Alignment.RIGHT -> TableBlock.TableCell.Alignment.End
            }
        )
    }

    private suspend fun convertListBlockNode(
        metadata: NodeMetadata,
        listBlock: FlexListBlock,
    ): ListBlock? {
        return convertListChildren(
            metadata = metadata,
            nodes = listBlock.children,
        ).takeUnless { it.isEmpty() }
            ?.let { ListBlock(metadata = metadata, children = it) }
    }

    private suspend fun convertListChildren(
        metadata: NodeMetadata,
        nodes: Iterable<Node>,
    ): ImmutableList<ListBlock.ListEntry> {
        return nodes.mapAsyncIndexed { index, item ->
            convertListItem(
                metadata = metadata,
                index = index + 1,
                item = item as ListItem,
            )
        }.flatten().toImmutableList()
    }

    private suspend fun convertListItem(
        metadata: NodeMetadata,
        index: Int,
        item: ListItem,
    ): ImmutableList<ListBlock.ListEntry> {
        val firstChild = item.firstChild
        if (firstChild == null || !firstChild.hasChildren()) return persistentListOf()

        val content = convertToAnnotatedNodes(metadata = metadata, nodes = firstChild.children)
        return buildList {
            // Enforce the first item to parsed as a list item. There is no way in the Markdown spec it won't be and
            // this greatly simplifies displaying lists later.
            add(
                ListBlock.ListEntry.ListItem(
                    type = convertListItemType(index = index, item = item),
                    children = content,
                )
            )

            item.children
                .drop(1) // Drop first child as we converted that to list item above
                .mapAsync { convertListNode(metadata = metadata, node = it) }
                .filterNotNull()
                .let(::addAll)
        }.toPersistentList()
    }

    private fun convertListItemType(index: Int, item: ListItem) = when (item) {
        is OrderedListItem -> ListBlock.ListItemType.Ordered(index)
        is TaskListItem -> ListBlock.ListItemType.Task(item.isItemDoneMarker)
        else -> ListBlock.ListItemType.Unordered
    }

    private suspend fun convertListNode(metadata: NodeMetadata, node: Node) = when (node) {
        is FlexListBlock -> convertListBlockNode(listBlock = node, metadata = metadata.incListLevel())
            ?.let(ListBlock.ListEntry::ListNode)
        else -> convertToStableNode(node = node, metadata = metadata.incLevel())
            ?.let(ListBlock.ListEntry::ListNode)
    }

    private suspend fun convertTextNode(node: Node, metadata: NodeMetadata): TextNode? {
        return convertToAnnotatedNode(metadata = metadata, node = node)
            ?.let { TextNode(metadata = metadata, text = it) }
    }
}
