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
import com.moveagency.markymark.model.LinkInteractionListener
import com.moveagency.markymark.model.NodeMetadata
import com.moveagency.markymark.model.annotated.Bold
import com.moveagency.markymark.model.annotated.Code
import com.moveagency.markymark.model.annotated.EmailLink
import com.moveagency.markymark.model.annotated.Italic
import com.moveagency.markymark.model.annotated.Link
import com.moveagency.markymark.model.annotated.ParagraphText
import com.moveagency.markymark.model.annotated.SoftLineBreak
import com.moveagency.markymark.model.annotated.Strikethrough
import com.moveagency.markymark.model.annotated.Subscript
import com.moveagency.markymark.model.annotated.Superscript
import com.moveagency.markymark.model.annotated.Text
import com.vladsch.flexmark.ast.AutoLink
import com.vladsch.flexmark.ast.Emphasis
import com.vladsch.flexmark.ast.LinkRef
import com.vladsch.flexmark.ast.MailLink
import com.vladsch.flexmark.ast.StrongEmphasis
import com.vladsch.flexmark.ast.TextBase
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
    internal suspend fun convertToAnnotatedNode(
        metadata: NodeMetadata,
        node: Node,
        linkInteractionListener: LinkInteractionListener?,
    ) = when (node) {
        is FlexText -> convertTextNode(metadata, node)
        is Emphasis -> convertEmphasisNode(metadata, node, linkInteractionListener)
        is StrongEmphasis -> convertStrongEmphasisNode(metadata, node, linkInteractionListener)
        is FlexStrikethrough -> convertStrikeThroughNode(metadata, node, linkInteractionListener)
        is FlexCode -> convertCodeNode(metadata, node)
        is FlexLink -> convertLinkNode(metadata, node, linkInteractionListener)
        is AutoLink -> convertAutoLinkNode(metadata, node, linkInteractionListener)
        is LinkRef -> convertLinkRefNode(metadata, node)
        is MailLink -> convertMailLinkNode(metadata, node)
        is FlexSoftLineBreak -> SoftLineBreak(metadata)
        is FlexSubscript -> convertSubscriptNode(metadata, node)
        is FlexSuperscript -> convertSuperscriptNode(metadata, node)
        is TextBase -> convertTextBaseNode(metadata, node, linkInteractionListener)
        else -> {
            Log.w(ConverterTag, "Found unknown node, $node")
            null
        }
    }

    private fun convertTextNode(metadata: NodeMetadata, text: FlexText): Text {
        return Text(metadata = metadata, content = text.chars.unescapeHtml())
    }

    private suspend fun convertEmphasisNode(
        metadata: NodeMetadata,
        emphasis: Emphasis,
        linkInteractionListener: LinkInteractionListener?,
    ): Italic {
        return Italic(
            metadata = metadata,
            children = convertToAnnotatedNodes(
                metadata = metadata,
                nodes = emphasis.children,
                linkInteractionListener = linkInteractionListener,
            ),
        )
    }

    private suspend fun convertStrongEmphasisNode(
        metadata: NodeMetadata,
        strongEmphasis: StrongEmphasis,
        linkInteractionListener: LinkInteractionListener?,
    ): Bold {
        return Bold(
            metadata = metadata,
            children = convertToAnnotatedNodes(
                metadata = metadata,
                nodes = strongEmphasis.children,
                linkInteractionListener = linkInteractionListener,
            ),
        )
    }

    private suspend fun convertStrikeThroughNode(
        metadata: NodeMetadata,
        strikethrough: FlexStrikethrough,
        linkInteractionListener: LinkInteractionListener?,
    ): Strikethrough {
        return Strikethrough(
            metadata = metadata,
            children = convertToAnnotatedNodes(
                metadata = metadata,
                nodes = strikethrough.children,
                linkInteractionListener = linkInteractionListener,
            ),
        )
    }

    private suspend fun convertCodeNode(metadata: NodeMetadata, code: FlexCode): Code {
        return Code(
            metadata = metadata,
            children = convertToAnnotatedNodes(
                metadata = metadata,
                nodes = code.children,
                linkInteractionListener = null,
            ),
        )
    }

    private suspend fun convertLinkNode(
        metadata: NodeMetadata,
        link: FlexLink,
        clickListener: LinkInteractionListener?,
    ): Link {
        return clickListener?.let { listener ->
            Link.CustomLink(
                metadata = metadata,
                children = convertToAnnotatedNodes(
                    metadata = metadata,
                    nodes = link.children,
                    linkInteractionListener = null,
                ),
                url = link.url.unescapeHtml(),
                title = link.title.unescapeHtml().takeUnless { it.isBlank() },
                clickListener = listener,
            )
        } ?: Link.BrowserLink(
            metadata = metadata,
            children = convertToAnnotatedNodes(
                metadata = metadata,
                nodes = link.children,
                linkInteractionListener = null,
            ),
            url = link.url.unescapeHtml(),
            title = link.title.unescapeHtml().takeUnless { it.isBlank() },
        )
    }

    private fun convertAutoLinkNode(
        metadata: NodeMetadata,
        autoLink: AutoLink,
        clickListener: LinkInteractionListener?,
    ): Link {
        val url = autoLink.url.unescapeHtml()
        return clickListener?.let {
            Link.CustomLink(
                metadata = metadata,
                children = persistentListOf(Text(metadata = metadata, content = url)),
                url = url,
                title = null,
                clickListener = it,
            )
        } ?: Link.BrowserLink(
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
            children = convertToAnnotatedNodes(
                metadata = metadata,
                nodes = subscript.children,
                linkInteractionListener = null,
            ),
        )
    }

    private suspend fun convertSuperscriptNode(metadata: NodeMetadata, superscript: FlexSuperscript): Superscript {
        return Superscript(
            metadata = metadata,
            children = convertToAnnotatedNodes(
                metadata = metadata,
                nodes = superscript.children,
                linkInteractionListener = null,
            ),
        )
    }

    private suspend fun convertTextBaseNode(
        metadata: NodeMetadata,
        textBase: TextBase,
        linkInteractionListener: LinkInteractionListener?,
    ): ParagraphText {
        return ParagraphText(
            metadata = metadata,
            children = convertToAnnotatedNodes(
                metadata = metadata,
                nodes = textBase.children,
                linkInteractionListener = linkInteractionListener,
            ),
        )
    }

    /**
     * Unescape HTML. FlexMark escapes things for HTML by default which doesn't work for Jetpack Compose.
     */
    internal fun BasedSequence.unescapeHtml(): String {
        return Escaping.unescapeHtml(toStringOrNull().orEmpty())
    }
}
