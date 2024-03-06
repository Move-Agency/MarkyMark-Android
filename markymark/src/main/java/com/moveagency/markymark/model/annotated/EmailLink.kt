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

package com.moveagency.markymark.model.annotated

import androidx.compose.runtime.Immutable
import com.moveagency.markymark.model.NodeMetadata

/**
 * Represents Markdown text with email formatting. Mapped from [MailLink][com.vladsch.flexmark.ast.MailLink].
 *
 * __Syntax:__
 *
 * ```markdown
 * <info@moveagency.com>
 * jobs@moveagency.com
 * ```
 *
 * For more details see the [Markdown guide](https://www.markdownguide.org/basic-syntax#urls-and-email-addresses).
 */
@Immutable
data class EmailLink(
    override val metadata: NodeMetadata,
    val email: String,
) : AnnotatedStableNode()
