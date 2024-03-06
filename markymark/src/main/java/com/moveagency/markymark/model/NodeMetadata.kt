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

package com.moveagency.markymark.model

data class NodeMetadata(
    val level: Int = 0,
    val quoteLevel: Int = 0,
    val listLevel: Int = 0,
    val paragraphLevel: Int = 0,
) {

    val isRootLevel: Boolean
        get() = level == 0

    fun incQuoteLevel() = copy(
        level = level + 1,
        quoteLevel = quoteLevel + 1,
    )

    fun incListLevel() = copy(
        level = level + 1,
        listLevel = listLevel + 1,
    )

    fun incParagraphLevel() = copy(
        level = level + 1,
        paragraphLevel = paragraphLevel + 1,
    )

    fun incLevel() = copy(level = level + 1)

    companion object {

        val Root by lazy { NodeMetadata() }
    }
}
