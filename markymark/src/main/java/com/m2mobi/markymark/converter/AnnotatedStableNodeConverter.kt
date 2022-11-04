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

package com.m2mobi.markymark.converter

import android.util.Log
import com.m2mobi.markymark.converter.MarkyMarkConverter.CONVERTER_TAG
import com.m2mobi.markymark.converter.MarkyMarkConverter.convertToAnnotatedNodes
import com.m2mobi.markymark.model.AnnotatedStableNode
import com.vladsch.flexmark.ast.AutoLink
import com.vladsch.flexmark.ast.Code
import com.vladsch.flexmark.ast.Emphasis
import com.vladsch.flexmark.ast.Link
import com.vladsch.flexmark.ast.LinkRef
import com.vladsch.flexmark.ast.MailLink
import com.vladsch.flexmark.ast.SoftLineBreak
import com.vladsch.flexmark.ast.StrongEmphasis
import com.vladsch.flexmark.ast.Text
import com.vladsch.flexmark.ast.TextBase
import com.vladsch.flexmark.ext.gfm.strikethrough.Strikethrough
import com.vladsch.flexmark.ext.gfm.strikethrough.Subscript
import com.vladsch.flexmark.ext.superscript.Superscript
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.sequence.BasedSequence
import com.vladsch.flexmark.util.sequence.Escaping
import kotlinx.collections.immutable.persistentListOf

@Suppress("TooManyFunctions")
object AnnotatedStableNodeConverter {

    @Suppress("ComplexMethod")
    internal suspend fun convertToAnnotatedNode(node: Node) = when (node) {
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
            Log.w(CONVERTER_TAG, "Found unknown node, $node")
            null
        }
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
            children = persistentListOf(AnnotatedStableNode.Text(url)),
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
    internal fun BasedSequence.unescapeHtml(): String {
        return Escaping.unescapeHtml(toStringOrNull().orEmpty())
    }
}
