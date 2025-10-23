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

package com.moveagency.markymark.converter

import com.moveagency.markymark.converter.AnnotatedStableNodeConverter.convertToAnnotatedNode
import com.moveagency.markymark.converter.AnnotatedStableNodeConverter.unescapeHtml
import com.moveagency.markymark.converter.MarkyMarkConverter.convertToAnnotatedNodes
import com.moveagency.markymark.model.LinkInteractionListener
import com.moveagency.markymark.model.NodeMetadata
import com.moveagency.markymark.model.annotated.AnnotatedStableNode
import com.moveagency.markymark.model.annotated.ParagraphText
import com.moveagency.markymark.model.composable.BlockQuote
import com.moveagency.markymark.model.composable.CodeBlock
import com.moveagency.markymark.model.composable.ComposableStableNode
import com.moveagency.markymark.model.composable.Headline
import com.moveagency.markymark.model.composable.Image
import com.moveagency.markymark.model.composable.ListBlock
import com.moveagency.markymark.model.composable.Paragraph
import com.moveagency.markymark.model.composable.Rule
import com.moveagency.markymark.model.composable.TableBlock
import com.moveagency.markymark.model.composable.TextNode
import com.moveagency.markymark.util.mapAsync
import com.moveagency.markymark.util.mapAsyncIndexed
import com.vladsch.flexmark.ast.FencedCodeBlock
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.IndentedCodeBlock
import com.vladsch.flexmark.ast.ListItem
import com.vladsch.flexmark.ast.OrderedListItem
import com.vladsch.flexmark.ast.ThematicBreak
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
        linkInteractionListener: LinkInteractionListener?,
    ): List<ComposableStableNode> = when (node) {
        is ThematicBreak -> listOf(Rule(metadata = metadata))
        is FlexImage -> listOf(convertImageNode(metadata = metadata, image = node))
        is FencedCodeBlock -> listOf(convertFencedCodeBlockNode(metadata = metadata, fencedCodeBlock = node))
        is IndentedCodeBlock -> listOf(convertIndentedCodeBlockNode(metadata = metadata, indentedCodeBlock = node))
        is FlexBlockQuote -> listOf(
            convertBlockQuoteNode(
                metadata = metadata,
                blockQuote = node,
                linkInteractionListener = linkInteractionListener,
            ),
        )
        is FlexTableBlock -> listOf(
            convertTableBlockNode(
                metadata = metadata,
                tableBlock = node,
                linkInteractionListener = linkInteractionListener,
            )
        )
        is Heading -> listOf(
            convertHeadingNode(
                metadata = metadata,
                heading = node,
                linkInteractionListener = linkInteractionListener,
            ),
        )
        is FlexParagraph -> {
            convertParagraphNode(
                metadata = metadata,
                paragraph = node,
                linkInteractionListener = linkInteractionListener
            )
        }
        is FlexListBlock -> {
            listOfNotNull(
                convertListBlockNode(
                    metadata = metadata,
                    listBlock = node,
                    linkInteractionListener = linkInteractionListener
                )
            )
        }
        else -> listOfNotNull(
            convertTextNode(
                metadata = metadata,
                node = node,
                linkInteractionListener = linkInteractionListener,
            )
        )
    }

    @Suppress("MagicNumber")
    private suspend fun convertHeadingNode(
        metadata: NodeMetadata,
        heading: Heading,
        linkInteractionListener: LinkInteractionListener?,
    ): Headline {
        return Headline(
            metadata = metadata,
            children = convertToAnnotatedNodes(
                metadata = metadata,
                nodes = heading.children,
                linkInteractionListener = linkInteractionListener,
            ),
            headingLevel = when (heading.level) {
                1 -> Headline.Level.Heading1
                2 -> Headline.Level.Heading2
                3 -> Headline.Level.Heading3
                4 -> Headline.Level.Heading4
                5 -> Headline.Level.Heading5
                else -> Headline.Level.Heading6
            }
        )
    }

    private suspend fun convertParagraphNode(
        metadata: NodeMetadata,
        paragraph: FlexParagraph,
        linkInteractionListener: LinkInteractionListener?,
    ): List<ComposableStableNode> {
        return paragraph.children
            .splitOnImage()
            .mapAsync {
                val singleNode = it.singleOrNull()
                if (singleNode is FlexImage) {
                    convertImageNode(metadata, singleNode)
                } else {
                    Paragraph(
                        metadata = metadata,
                        children = convertParagraphChildren(
                            metadata = metadata.incParagraphLevel(),
                            children = it,
                            linkInteractionListener = linkInteractionListener,
                        )
                    )
                }
            }
    }

    private fun Iterable<Node>.splitOnImage(): List<List<Node>> {
        val result = mutableListOf<List<Node>>()
        var currentList = mutableListOf<Node>()

        for (node in this) {
            if (node is FlexImage) {
                if (currentList.isNotEmpty()) {
                    result += currentList
                    currentList = mutableListOf()
                }

                result += listOf(node)
            } else {
                currentList += node
            }
        }

        if (currentList.isNotEmpty()) result += currentList

        return result
    }

    private suspend fun convertParagraphChildren(
        metadata: NodeMetadata,
        children: Iterable<Node>,
        linkInteractionListener: LinkInteractionListener?,
    ): ImmutableList<ComposableStableNode> {
        return children
            .mapAsync {
                convertToStableNode(metadata = metadata, node = it, linkInteractionListener = linkInteractionListener)
            }
            .flatten()
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
        linkInteractionListener: LinkInteractionListener?,
    ): BlockQuote {
        return BlockQuote(
            metadata = metadata,
            children = MarkyMarkConverter.convertToStableNodes(
                nodes = blockQuote.children,
                metadata = metadata.incQuoteLevel(),
                linkInteractionListener = linkInteractionListener,
            )
        )
    }

    private suspend fun convertTableBlockNode(
        metadata: NodeMetadata,
        tableBlock: FlexTableBlock,
        linkInteractionListener: LinkInteractionListener?,
    ): ComposableStableNode {
        return withContext(Dispatchers.Default) {
            val head = async {
                convertToTableRow(
                    metadata = metadata,
                    tableHead = tableBlock.firstChild as TableHead,
                    linkInteractionListener = linkInteractionListener,
                )
            }
            val body = async {
                (tableBlock.lastChild as? TableBody)
                    ?.let {
                        convertToTableRows(
                            metadata = metadata,
                            tableBody = it,
                            linkInteractionListener = linkInteractionListener,
                        )
                    }
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
        linkInteractionListener: LinkInteractionListener?,
    ): TableBlock.TableRow {
        return convertToTableRow(
            metadata = metadata,
            tableRow = tableHead.firstChild as FlexTableRow,
            linkInteractionListener = linkInteractionListener,
        )
    }

    private suspend fun convertToTableRows(
        metadata: NodeMetadata,
        tableBody: TableBody,
        linkInteractionListener: LinkInteractionListener?,
    ): ImmutableList<TableBlock.TableRow> {
        return convertToTableRows(
            metadata = metadata,
            nodes = tableBody.children,
            linkInteractionListener = linkInteractionListener,
        )
    }

    private suspend fun convertToTableRows(
        metadata: NodeMetadata,
        nodes: Iterable<Node>,
        linkInteractionListener: LinkInteractionListener?
    ): ImmutableList<TableBlock.TableRow> {
        return nodes.filterIsInstance(FlexTableRow::class.java)
            .mapAsync {
                convertToTableRow(
                    metadata = metadata,
                    tableRow = it,
                    linkInteractionListener = linkInteractionListener,
                )
            }
            .toImmutableList()
    }

    private suspend fun convertToTableRow(
        metadata: NodeMetadata,
        tableRow: FlexTableRow,
        linkInteractionListener: LinkInteractionListener?
    ): TableBlock.TableRow {
        return TableBlock.TableRow(
            tableRow.children
                .filterIsInstance(FlexTableCell::class.java)
                .mapAsync {
                    convertToTableCell(
                        metadata = metadata,
                        tableCell = it,
                        linkInteractionListener = linkInteractionListener,
                    )
                }
                .toImmutableList()
        )
    }

    private suspend fun convertToTableCell(
        metadata: NodeMetadata,
        tableCell: FlexTableCell,
        linkInteractionListener: LinkInteractionListener?,
    ): TableBlock.TableCell {
        return TableBlock.TableCell(
            children = convertToAnnotatedNodes(
                metadata = metadata,
                nodes = tableCell.children,
                linkInteractionListener = linkInteractionListener,
            ),
            alignment = when (tableCell.alignment) {
                FlexTableCell.Alignment.LEFT -> TableBlock.TableCell.Alignment.Start
                FlexTableCell.Alignment.CENTER -> TableBlock.TableCell.Alignment.Center
                FlexTableCell.Alignment.RIGHT -> TableBlock.TableCell.Alignment.End
                null -> TableBlock.TableCell.Alignment.Start
            }
        )
    }

    private suspend fun convertListBlockNode(
        metadata: NodeMetadata,
        listBlock: FlexListBlock,
        linkInteractionListener: LinkInteractionListener?,
    ): ListBlock? {
        return convertListChildren(
            metadata = metadata,
            nodes = listBlock.children,
            linkInteractionListener = linkInteractionListener,
        ).takeUnless { it.isEmpty() }
            ?.let { ListBlock(metadata = metadata, children = it) }
    }

    private suspend fun convertListChildren(
        metadata: NodeMetadata,
        nodes: Iterable<Node>,
        linkInteractionListener: LinkInteractionListener?,
    ): ImmutableList<ListBlock.ListEntry> {
        return nodes.mapAsyncIndexed { index, item ->
            convertListItem(
                metadata = metadata,
                index = index + 1,
                item = item as ListItem,
                linkInteractionListener = linkInteractionListener,
            )
        }.flatten().toImmutableList()
    }

    private suspend fun convertListItem(
        metadata: NodeMetadata,
        index: Int,
        item: ListItem,
        linkInteractionListener: LinkInteractionListener?,
    ): ImmutableList<ListBlock.ListEntry> {
        val firstChild = item.firstChild
        if (firstChild == null || !firstChild.hasChildren()) return persistentListOf()

        val content = convertToAnnotatedNodes(
            metadata = metadata,
            nodes = firstChild.children,
            linkInteractionListener = linkInteractionListener,
        )
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
                .mapAsync {
                    convertListNode(metadata = metadata, node = it, linkInteractionListener = linkInteractionListener)
                }
                .flatten()
                .let(::addAll)
        }.toPersistentList()
    }

    private fun convertListItemType(index: Int, item: ListItem) = when (item) {
        is OrderedListItem -> ListBlock.ListItemType.Ordered(index)
        is TaskListItem -> ListBlock.ListItemType.Task(item.isItemDoneMarker)
        else -> ListBlock.ListItemType.Unordered
    }

    private suspend fun convertListNode(
        metadata: NodeMetadata,
        node: Node,
        linkInteractionListener: LinkInteractionListener?,
    ) = when (node) {
        is FlexListBlock -> listOfNotNull(
            convertListBlockNode(
                listBlock = node,
                metadata = metadata.incListLevel(),
                linkInteractionListener = linkInteractionListener,
            )?.let(ListBlock.ListEntry::ListNode)
        )
        else -> convertToStableNode(
            node = node,
            metadata = metadata.incLevel(),
            linkInteractionListener = linkInteractionListener,
        ).map(ListBlock.ListEntry::ListNode)
    }

    private suspend fun convertTextNode(
        node: Node,
        metadata: NodeMetadata,
        linkInteractionListener: LinkInteractionListener?,
    ): TextNode? {
        return convertToAnnotatedNode(
            metadata = metadata,
            node = node,
            linkInteractionListener = linkInteractionListener,
        )?.let { TextNode(metadata = metadata, text = it) }
    }
}
