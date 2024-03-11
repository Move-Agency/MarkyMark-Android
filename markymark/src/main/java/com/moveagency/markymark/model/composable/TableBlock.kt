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

package com.moveagency.markymark.model.composable

import androidx.compose.runtime.Immutable
import com.moveagency.markymark.model.NodeMetadata
import com.moveagency.markymark.model.annotated.AnnotatedStableNode
import kotlinx.collections.immutable.ImmutableList

/**
 * Represents a Github Flavoured Markdown table. Mapped from [TableBlock][com.vladsch.flexmark.ext.tables.TableBlock].
 *
 * __Syntax:__
 *
 * ```markdown
 * | First Header  | Second Header |
 * | ------------- | ------------- |
 * | Content Cell  | Content Cell  |
 * | Content Cell  | Content Cell  |
 * ```
 *
 * For details see the [Github Flavoured Markdown guide](https://docs.github.com/en/get-started/writing-on-github/working-with-advanced-formatting/organizing-information-with-tables).
 */
@Immutable
data class TableBlock(
    override val metadata: NodeMetadata,
    val head: TableRow,
    val body: ImmutableList<TableRow>,
) : ComposableStableNode() {

    /**
     * Represents a Markdown table row. Mapped from [TableRow][com.vladsch.flexmark.ext.tables.TableRow].
     *
     * __Syntax:__
     *
     * For details see [TableBlock].
     */
    @Immutable
    data class TableRow(val cells: ImmutableList<TableCell>)

    /**
     * Represents a Markdown table cell. Mapped from [TableCell][com.vladsch.flexmark.ext.tables.TableCell].
     *
     * __Syntax:__
     *
     * For details see [TableBlock].
     */
    @Immutable
    data class TableCell(
        val children: ImmutableList<AnnotatedStableNode>,
        val alignment: Alignment,
    ) {

        enum class Alignment {

            Start,
            Center,
            End,
        }
    }
}
