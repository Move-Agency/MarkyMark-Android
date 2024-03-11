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
import com.moveagency.markymark.converter.MarkyMarkConverter.ConverterTag
import com.moveagency.markymark.converter.MarkyMarkConverter.convertToAnnotatedNodes
import com.moveagency.markymark.model.NodeMetadata
import com.moveagency.markymark.model.annotated.*
import com.vladsch.flexmark.ast.*
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.sequence.BasedSequence
import com.vladsch.flexmark.util.sequence.Escaping
import kotlinx.collections.immutable.persistentListOf
import com.vladsch.flexmark.ast.Code as FlexCode
import com.vladsch.flexmark.ast.Link as FlexLink
import com.vladsch.flexmark.ast.SoftLineBreak as FlexSoftLineBreak
import com.vladsch.flexmark.ast.Text as FlexText
import com.vladsch.flexmark.ext.gfm.strikethrough.Strikethrough as FlexStrikethrough
import com.vladsch.flexmark.ext.gfm.strikethrough.Subscript as FlexSubscript
import com.vladsch.flexmark.ext.superscript.Superscript as FlexSuperscript

@Suppress("TooManyFunctions")
object AnnotatedStableNodeConverter {

    @Suppress("ComplexMethod")
    internal suspend fun convertToAnnotatedNode(metadata: NodeMetadata, node: Node) = when (node) {
        is FlexText -> convertTextNode(metadata, node)
        is Emphasis -> convertEmphasisNode(metadata, node)
        is StrongEmphasis -> convertStrongEmphasisNode(metadata, node)
        is FlexStrikethrough -> convertStrikeThroughNode(metadata, node)
        is FlexCode -> convertCodeNode(metadata, node)
        is FlexLink -> convertLinkNode(metadata, node)
        is AutoLink -> convertAutoLinkNode(metadata, node)
        is LinkRef -> convertLinkRefNode(metadata, node)
        is MailLink -> convertMailLinkNode(metadata, node)
        is FlexSoftLineBreak -> SoftLineBreak(metadata)
        is FlexSubscript -> convertSubscriptNode(metadata, node)
        is FlexSuperscript -> convertSuperscriptNode(metadata, node)
        is TextBase -> convertTextBaseNode(metadata, node)
        else -> {
            Log.w(ConverterTag, "Found unknown node, $node")
            null
        }
    }

    private fun convertTextNode(metadata: NodeMetadata, text: FlexText): Text {
        return Text(metadata = metadata, content = text.chars.unescapeHtml())
    }

    private suspend fun convertEmphasisNode(metadata: NodeMetadata, emphasis: Emphasis): Italic {
        return Italic(
            metadata = metadata,
            children = convertToAnnotatedNodes(metadata = metadata, nodes = emphasis.children),
        )
    }

    private suspend fun convertStrongEmphasisNode(metadata: NodeMetadata, strongEmphasis: StrongEmphasis): Bold {
        return Bold(
            metadata = metadata,
            children = convertToAnnotatedNodes(metadata = metadata, strongEmphasis.children),
        )
    }

    private suspend fun convertStrikeThroughNode(
        metadata: NodeMetadata,
        strikethrough: FlexStrikethrough,
    ): Strikethrough {
        return Strikethrough(
            metadata = metadata,
            children = convertToAnnotatedNodes(metadata = metadata, nodes = strikethrough.children),
        )
    }

    private suspend fun convertCodeNode(metadata: NodeMetadata, code: FlexCode): Code {
        return Code(
            metadata = metadata,
            children = convertToAnnotatedNodes(metadata = metadata, nodes = code.children),
        )
    }

    private suspend fun convertLinkNode(metadata: NodeMetadata, link: FlexLink): Link {
        return Link(
            metadata = metadata,
            children = convertToAnnotatedNodes(metadata = metadata, nodes = link.children),
            url = link.url.unescapeHtml(),
            title = link.title.unescapeHtml().takeUnless { it.isBlank() },
        )
    }

    private fun convertAutoLinkNode(metadata: NodeMetadata, autoLink: AutoLink): Link {
        val url = autoLink.url.unescapeHtml()
        return Link(
            metadata = metadata,
            children = persistentListOf(Text(metadata = metadata, content = url)),
            url = url,
            title = null,
        )
    }

    private fun convertLinkRefNode(metadata: NodeMetadata, linkRef: LinkRef): Text {
        return Text(metadata = metadata, content = linkRef.chars.unescapeHtml())
    }

    private fun convertMailLinkNode(metadata: NodeMetadata, emailLink: MailLink): EmailLink {
        return EmailLink(metadata = metadata, email = emailLink.text.unescapeHtml())
    }

    private suspend fun convertSubscriptNode(metadata: NodeMetadata, subscript: FlexSubscript): Subscript {
        return Subscript(
            metadata = metadata,
            children = convertToAnnotatedNodes(metadata = metadata, nodes = subscript.children),
        )
    }

    private suspend fun convertSuperscriptNode(metadata: NodeMetadata, superscript: FlexSuperscript): Superscript {
        return Superscript(
            metadata = metadata,
            children = convertToAnnotatedNodes(metadata = metadata, nodes = superscript.children),
        )
    }

    private suspend fun convertTextBaseNode(metadata: NodeMetadata, textBase: TextBase): ParagraphText {
        return ParagraphText(
            metadata = metadata,
            children = convertToAnnotatedNodes(metadata = metadata, nodes = textBase.children),
        )
    }

    /**
     * Unescape HTML. FlexMark escapes things for HTML by default which doesn't work for Jetpack Compose.
     */
    internal fun BasedSequence.unescapeHtml(): String {
        return Escaping.unescapeHtml(toStringOrNull().orEmpty())
    }
}
