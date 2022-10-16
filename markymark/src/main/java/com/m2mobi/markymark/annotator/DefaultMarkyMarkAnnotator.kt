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

package com.m2mobi.markymark.annotator

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.m2mobi.markymark.composable.TAG_LINK
import com.m2mobi.markymark.model.AnnotatedStableNode
import com.m2mobi.markymark.model.AnnotatedStableNode.Bold
import com.m2mobi.markymark.model.AnnotatedStableNode.Code
import com.m2mobi.markymark.model.AnnotatedStableNode.EmailLink
import com.m2mobi.markymark.model.AnnotatedStableNode.Italic
import com.m2mobi.markymark.model.AnnotatedStableNode.Link
import com.m2mobi.markymark.model.AnnotatedStableNode.ParagraphText
import com.m2mobi.markymark.model.AnnotatedStableNode.SoftLineBreak
import com.m2mobi.markymark.model.AnnotatedStableNode.Strikethrough
import com.m2mobi.markymark.model.AnnotatedStableNode.Subscript
import com.m2mobi.markymark.model.AnnotatedStableNode.Superscript
import com.m2mobi.markymark.model.AnnotatedStableNode.Text
import com.m2mobi.markymark.theme.AnnotatedStyles

open class DefaultMarkyMarkAnnotator : MarkyMarkAnnotator {

    override fun annotate(nodes: List<AnnotatedStableNode>, styles: AnnotatedStyles): AnnotatedString {
        return buildAnnotatedString { annotateChildren(nodes = nodes, styles = styles) }
    }

    protected open fun AnnotatedString.Builder.annotateChildren(
        nodes: List<AnnotatedStableNode>,
        styles: AnnotatedStyles,
    ) {
        for (node in nodes) annotate(node = node, styles = styles)
    }

    protected open fun AnnotatedString.Builder.annotate(
        node: AnnotatedStableNode,
        styles: AnnotatedStyles,
    ) = when (node) {
        is Bold -> annotateBold(bold = node, styles = styles)
        is Code -> annotateCode(code = node, styles = styles)
        is Italic -> annotateItalic(italic = node, styles = styles)
        is Strikethrough -> annotateStrikethrough(strikethrough = node, styles)
        is Link -> annotateLink(link = node, styles = styles)
        is EmailLink -> annotateEmailLink(link = node, styles = styles)
        is Text -> append(node.content)
        is ParagraphText -> annotateChildren(nodes = node.children, styles = styles)
        is SoftLineBreak -> append("\n")
        is Subscript -> annotateSubscript(subscript = node, styles = styles)
        is Superscript -> annotateSuperscript(superscript = node, styles = styles)
    }

    protected open fun AnnotatedString.Builder.annotateBold(bold: Bold, styles: AnnotatedStyles) {
        pushStyle(styles.boldStyle)
        annotateChildren(nodes = bold.children, styles = styles)
        pop()
    }

    protected open fun AnnotatedString.Builder.annotateCode(code: Code, styles: AnnotatedStyles) {
        pushStyle(styles.codeStyle)
        annotateChildren(nodes = code.children, styles = styles)
        pop()
    }

    protected open fun AnnotatedString.Builder.annotateItalic(italic: Italic, styles: AnnotatedStyles) {
        pushStyle(styles.italicStyle)
        annotateChildren(nodes = italic.children, styles = styles)
        pop()
    }

    protected open fun AnnotatedString.Builder.annotateStrikethrough(
        strikethrough: Strikethrough,
        styles: AnnotatedStyles
    ) {
        pushStyle(styles.strikethroughStyle)
        annotateChildren(nodes = strikethrough.children, styles = styles)
        pop()
    }

    protected open fun AnnotatedString.Builder.annotateLink(link: Link, styles: AnnotatedStyles) {
        pushStyle(styles.linkStyle)
        pushStringAnnotation(tag = TAG_LINK, annotation = link.url)
        annotateChildren(nodes = link.children, styles = styles)
        pop()
        pop()
    }

    protected open fun AnnotatedString.Builder.annotateEmailLink(link: EmailLink, styles: AnnotatedStyles) {
        pushStyle(styles.linkStyle)
        pushStringAnnotation(tag = TAG_LINK, annotation = "$MAIL_TO_PREFIX${link.email}")
        append(link.email)
        pop()
        pop()
    }

    protected open fun AnnotatedString.Builder.annotateSubscript(subscript: Subscript, styles: AnnotatedStyles) {
        pushStyle(styles.subscriptStyle)
        annotateChildren(nodes = subscript.children, styles = styles)
        pop()
    }

    protected open fun AnnotatedString.Builder.annotateSuperscript(superscript: Superscript, styles: AnnotatedStyles) {
        pushStyle(styles.superscriptStyle)
        annotateChildren(nodes = superscript.children, styles = styles)
        pop()
    }

    companion object {

        private const val MAIL_TO_PREFIX = "mailto:"
    }
}
