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

package com.m2mobi.markymark

import android.util.Log
import com.m2mobi.markymark.model.AnnotatedStableNode
import com.m2mobi.markymark.model.ComposableStableNode
import com.m2mobi.markymark.model.ComposableStableNode.Headline.Level.HEADING1
import com.m2mobi.markymark.model.ComposableStableNode.Headline.Level.HEADING2
import com.m2mobi.markymark.model.ComposableStableNode.Headline.Level.HEADING3
import com.m2mobi.markymark.model.ComposableStableNode.Headline.Level.HEADING4
import com.m2mobi.markymark.model.ComposableStableNode.Headline.Level.HEADING5
import com.m2mobi.markymark.model.ComposableStableNode.Headline.Level.HEADING6
import com.m2mobi.markymark.model.StableNode
import com.m2mobi.markymark.util.mapAsync
import com.m2mobi.markymark.util.mapAsyncIndexed
import com.vladsch.flexmark.ast.AutoLink
import com.vladsch.flexmark.ast.BlockQuote
import com.vladsch.flexmark.ast.Code
import com.vladsch.flexmark.ast.Emphasis
import com.vladsch.flexmark.ast.FencedCodeBlock
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.Image
import com.vladsch.flexmark.ast.IndentedCodeBlock
import com.vladsch.flexmark.ast.Link
import com.vladsch.flexmark.ast.LinkRef
import com.vladsch.flexmark.ast.ListBlock
import com.vladsch.flexmark.ast.ListItem
import com.vladsch.flexmark.ast.MailLink
import com.vladsch.flexmark.ast.OrderedListItem
import com.vladsch.flexmark.ast.Paragraph
import com.vladsch.flexmark.ast.SoftLineBreak
import com.vladsch.flexmark.ast.StrongEmphasis
import com.vladsch.flexmark.ast.Text
import com.vladsch.flexmark.ast.TextBase
import com.vladsch.flexmark.ast.ThematicBreak
import com.vladsch.flexmark.ext.gfm.strikethrough.Strikethrough
import com.vladsch.flexmark.ext.gfm.strikethrough.Subscript
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListItem
import com.vladsch.flexmark.ext.superscript.Superscript
import com.vladsch.flexmark.ext.tables.TableBlock
import com.vladsch.flexmark.ext.tables.TableBody
import com.vladsch.flexmark.ext.tables.TableCell
import com.vladsch.flexmark.ext.tables.TableCell.Alignment.CENTER
import com.vladsch.flexmark.ext.tables.TableCell.Alignment.LEFT
import com.vladsch.flexmark.ext.tables.TableCell.Alignment.RIGHT
import com.vladsch.flexmark.ext.tables.TableHead
import com.vladsch.flexmark.ext.tables.TableRow
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.sequence.BasedSequence
import com.vladsch.flexmark.util.sequence.Escaping
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * This object contains the logic for converting the AST produced by Flexmark into an presentation level representation
 * which is compatible with compose.
 */
object MarkyMarkConverter {

    private const val TAG = "Converter"

    /**
     * Convert [document] child [Node]s to [StableNode]s. This mapping happens as asynchronously as possible on the
     * [Dispatchers.Default] dispatcher. See [mapAsync] & [mapAsyncIndexed] for more details.
     */
    suspend fun convertToStableNodes(document: Document): List<StableNode> {
        return convertToStableNodes(document.children)
    }

    private suspend fun convertToStableNodes(nodes: Iterable<Node>): List<StableNode> {
        return nodes.mapAsync(::convertToStableNode)
            .filterNotNull()
    }

    private suspend fun convertToAnnotatedNodes(nodes: Iterable<Node>): List<AnnotatedStableNode> {
        return nodes.mapAsync(::convertToAnnotatedNode)
            .filterNotNull()
    }

    private suspend fun convertToStableNode(node: Node): StableNode? = when (node) {
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

    private suspend fun convertToAnnotatedNode(node: Node) = when (node) {
        is Text -> convertTextNode(node)
        is Emphasis -> convertEmphasisNode(node)
        is StrongEmphasis -> convertStrongEmphasisNode(node)
        is Strikethrough -> convertStrikeThroughNode(node)
        is Code -> convertCodeNode(node)
        is Link -> convertLinkNode(node)
        is AutoLink -> convertAutoLinkNode(node)
        is LinkRef -> convertLinkRefNode(node)
        is MailLink -> convertMailLinkNode(node)
        is SoftLineBreak -> AnnotatedStableNode.SoftLineBreak
        is Subscript -> convertSubscriptNode(node)
        is Superscript -> convertSuperscriptNode(node)
        is TextBase -> convertTextBaseNode(node)
        else -> {
            Log.w(TAG, "Found unknown node, $node")
            null
        }
    }

    private suspend fun convertHeadingNode(heading: Heading): ComposableStableNode.Headline {
        return ComposableStableNode.Headline(
            children = convertToAnnotatedNodes(heading.children),
            level = when (heading.level) {
                1 -> HEADING1
                2 -> HEADING2
                3 -> HEADING3
                4 -> HEADING4
                5 -> HEADING5
                else -> HEADING6
            }
        )
    }

    private suspend fun convertParagraphNode(paragraph: Paragraph): ComposableStableNode.Paragraph {
        return ComposableStableNode.Paragraph(children = convertParagraphChildren(paragraph.children))
    }

    private suspend fun convertParagraphChildren(children: Iterable<Node>): List<StableNode> {
        return children.mapAsync(::convertToStableNode)
            .filterNotNull()
            .bundleParagraphText()
    }

    private fun List<StableNode>.bundleParagraphText(): List<StableNode> {
        val returnList = mutableListOf<StableNode>()
        var currentChildren = mutableListOf<AnnotatedStableNode>()
        for (node in this) {
            when (node) {
                is ComposableStableNode -> {
                    if (currentChildren.isNotEmpty()) {
                        returnList += AnnotatedStableNode.ParagraphText(currentChildren)
                        currentChildren = mutableListOf()
                    }
                    returnList += node
                }
                is AnnotatedStableNode -> currentChildren += node
            }
        }
        if (currentChildren.isNotEmpty()) {
            returnList += AnnotatedStableNode.ParagraphText(currentChildren)
        }
        return returnList
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
                var indentsDropped = 0
                if (index == 0) line else line.dropWhile {
                    // Because FlexMark only removes the first lines indent but not the others we have to do it here.
                    val shouldDrop = (it == '\t' && indentsDropped == 0) || (it == ' ' && indentsDropped < 4)
                    if (shouldDrop) indentsDropped++
                    shouldDrop
                }
            }.joinToString("\n").trimEnd(),
            language = null,
        )
    }

    private suspend fun convertBlockQuoteNode(blockQuote: BlockQuote): ComposableStableNode.BlockQuote {
        return ComposableStableNode.BlockQuote(children = convertToStableNodes(blockQuote.children))
    }

    private suspend fun convertTableBlockNode(tableBlock: TableBlock): ComposableStableNode {
        return withContext(Dispatchers.Default) {
            val head = async { convertToTableRow(tableBlock.firstChild as TableHead) }
            val body = async {
                (tableBlock.lastChild as? TableBody)?.let { convertToTableRows(it) } ?: emptyList()
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
    ): List<ComposableStableNode.TableRow> {
        return nodes.filterIsInstance(TableRow::class.java)
            .mapAsync(::convertToTableRow)
    }

    private suspend fun convertToTableRow(tableRow: TableRow): ComposableStableNode.TableRow {
        return ComposableStableNode.TableRow(
            tableRow.children
                .filterIsInstance(TableCell::class.java)
                .mapAsync(::convertToTableCell)
        )
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    private suspend fun convertToTableCell(tableCell: TableCell): ComposableStableNode.TableCell {
        return ComposableStableNode.TableCell(
            children = convertToAnnotatedNodes(tableCell.children),
            alignment = when (tableCell.alignment) {
                LEFT -> ComposableStableNode.TableCell.Alignment.START
                CENTER -> ComposableStableNode.TableCell.Alignment.CENTER
                RIGHT -> ComposableStableNode.TableCell.Alignment.END
            }
        )
    }

    private suspend fun convertListBlockNode(node: ListBlock, level: Int = 0): ComposableStableNode.ListBlock? {
        return convertListChildren(node.children, level)
            .takeUnless { it.isEmpty() }
            ?.let { ComposableStableNode.ListBlock(level, convertListChildren(node.children, level)) }
    }

    private suspend fun convertListChildren(nodes: Iterable<Node>, level: Int): List<ComposableStableNode.ListEntry> {
        return nodes.mapAsyncIndexed { index, item -> convertListItem(index + 1, item as ListItem, level) }
            .flatten()
    }

    private suspend fun convertListItem(index: Int, item: ListItem, level: Int): List<ComposableStableNode.ListEntry> {
        val firstChild = item.firstChild
        if (firstChild == null || !firstChild.hasChildren()) return emptyList()

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
        }
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

    private fun convertTextNode(text: Text): AnnotatedStableNode.Text {
        return AnnotatedStableNode.Text(content = text.chars.unescapeHtml())
    }

    private suspend fun convertEmphasisNode(emphasis: Emphasis): AnnotatedStableNode.Italic {
        return AnnotatedStableNode.Italic(children = convertToAnnotatedNodes(emphasis.children))
    }

    private suspend fun convertStrongEmphasisNode(strongEmphasis: StrongEmphasis): AnnotatedStableNode.Bold {
        return AnnotatedStableNode.Bold(children = convertToAnnotatedNodes(strongEmphasis.children))
    }

    private suspend fun convertStrikeThroughNode(strikethrough: Strikethrough): AnnotatedStableNode.Strikethrough {
        return AnnotatedStableNode.Strikethrough(children = convertToAnnotatedNodes(strikethrough.children))
    }

    private suspend fun convertCodeNode(code: Code): AnnotatedStableNode.Code {
        return AnnotatedStableNode.Code(children = convertToAnnotatedNodes(code.children))
    }

    private suspend fun convertLinkNode(link: Link): AnnotatedStableNode.Link {
        return AnnotatedStableNode.Link(
            children = convertToAnnotatedNodes(link.children),
            url = link.url.unescapeHtml(),
            title = link.title.unescapeHtml().takeUnless { it.isBlank() },
        )
    }

    private fun convertAutoLinkNode(autoLink: AutoLink): AnnotatedStableNode.Link {
        val url = autoLink.url.unescapeHtml()
        return AnnotatedStableNode.Link(
            children = listOf(AnnotatedStableNode.Text(url)),
            url = url,
            title = null,
        )
    }

    private fun convertLinkRefNode(linkRef: LinkRef): AnnotatedStableNode.Text {
        return AnnotatedStableNode.Text(linkRef.chars.unescapeHtml())
    }

    private fun convertMailLinkNode(emailLink: MailLink): AnnotatedStableNode.EmailLink {
        return AnnotatedStableNode.EmailLink(emailLink.text.unescapeHtml())
    }

    private suspend fun convertSubscriptNode(subscript: Subscript): AnnotatedStableNode.Subscript {
        return AnnotatedStableNode.Subscript(children = convertToAnnotatedNodes(subscript.children))
    }

    private suspend fun convertSuperscriptNode(superscript: Superscript): AnnotatedStableNode.Superscript {
        return AnnotatedStableNode.Superscript(children = convertToAnnotatedNodes(superscript.children))
    }

    private suspend fun convertTextBaseNode(textBase: TextBase): AnnotatedStableNode.ParagraphText {
        return AnnotatedStableNode.ParagraphText(children = convertToAnnotatedNodes(textBase.children))
    }

    /**
     * Unescape HTML. FlexMark escapes things for HTML by default which doesn't work for Jetpack Compose.
     */
    private fun BasedSequence.unescapeHtml(): String {
        return Escaping.unescapeHtml(toStringOrNull().orEmpty())
    }
}
