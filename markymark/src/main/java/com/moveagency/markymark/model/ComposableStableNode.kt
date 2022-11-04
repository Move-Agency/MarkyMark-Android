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

package com.moveagency.markymark.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.moveagency.markymark.model.ComposableStableNode.ListEntry.ListItem
import com.moveagency.markymark.model.ComposableStableNode.ListEntry.ListNode
import com.moveagency.markymark.model.ComposableStableNode.ListItemType.Ordered
import com.moveagency.markymark.model.ComposableStableNode.ListItemType.Task
import com.moveagency.markymark.model.ComposableStableNode.ListItemType.Unordered
import kotlinx.collections.immutable.ImmutableList

/**
 * Representation of a Markdown element which needs to be rendered as a Composable element. Marked [Stable] for compatibility
 * with Jetpack Compose.
 */
@Stable
@Suppress("MaxLineLength")
sealed class ComposableStableNode : StableNode {

    /**
     * Represents a Markdown headline. Mapped from [Heading][com.vladsch.flexmark.ast.Heading].
     *
     * __Syntax:__
     *
     * ```markdown
     * # Heading 1
     * ## Heading 2
     * ### Heading 3
     * #### Heading 4
     * ##### Heading 5
     * ###### Heading 6
     * ```
     *
     * For details see the [Markdown guide](https://www.markdownguide.org/basic-syntax#headings).
     */
    @Immutable
    data class Headline(
        val children: ImmutableList<AnnotatedStableNode>,
        val level: Level,
    ) : ComposableStableNode() {

        enum class Level {

            /**
             * __Syntax:__
             *
             * ```markdown
             * # Heading 1
             * ```
             */
            HEADING1,

            /**
             * __Syntax:__
             *
             * ```markdown
             * ## Heading 2
             * ```
             */
            HEADING2,

            /**
             * __Syntax:__
             *
             * ```markdown
             * ### Heading 3
             * ```
             */
            HEADING3,

            /**
             * __Syntax:__
             *
             * ```markdown
             * #### Heading 4
             * ```
             */
            HEADING4,

            /**
             * __Syntax:__
             *
             * ```markdown
             * ##### Heading 5
             * ```
             */
            HEADING5,

            /**
             * __Syntax:__
             *
             * ```markdown
             * ###### Heading 6
             * ```
             */
            HEADING6,
        }
    }

    /**
     * Represents a Markdown paragraph. Mapped from [Paragraph][com.vladsch.flexmark.ast.Paragraph].
     *
     * __Syntax:__
     *
     * See the [Markdown guide](https://www.markdownguide.org/basic-syntax#paragraphs-1).
     */
    @Immutable
    data class Paragraph(val children: ImmutableList<StableNode>) : ComposableStableNode()

    /**
     * Represents a Markdown image. Mapped from [Image][com.vladsch.flexmark.ast.Image].
     *
     * __Syntax:__
     *
     * ```markdown
     * ![alt text](https://www.sample.com/image "title")
     * ![alt text](https://www.sample.com/image)
     * ```
     *
     * For details see the [Markdown guide](https://www.markdownguide.org/basic-syntax#images-1).
     *
     * *Note: Inline images are not supported.*
     */
    @Immutable
    data class Image(
        val url: String,
        val altText: String?,
        val title: String?,
    ) : ComposableStableNode()

    /**
     * Represents a Markdown horizontal rule. Mapped from [Rule][com.vladsch.flexmark.ast.ThematicBreak]
     *
     * __Syntax:__
     *
     * ```markdown
     * ***
     * ---
     * ___
     * ```
     *
     * For details see the [Markdown guide](https://www.markdownguide.org/basic-syntax#horizontal-rules).
     */
    @Immutable
    object Rule : ComposableStableNode()

    /**
     * Represents a Markdown code block. Mapped from [CodeBlock][com.vladsch.flexmark.ast.CodeBlock].
     *
     * __Syntax:__
     *
     * *Indented:*
     *
     * ```markdown
     *     ./gradlew clean
     *     ./gradlew assembleDebug
     *     ./gradlew publishToMavenLocal
     * ```
     *
     * For details see the [Markdown guide](https://www.markdownguide.org/basic-syntax#code-blocks).
     *
     * *Fenced:*
     *
     * ````markdown
     * ```kotlin
     * function doSomething() {
     *     println("stuff and things")
     * }
     * ```
     * ````
     *
     * For details see the [Markdown guide](https://www.markdownguide.org/extended-syntax/#fenced-code-blocks).
     *
     * *Note: This is different from inline code.*
     *
     * *Note: While the language will be parsed we do not support any syntax highlighting.*
     */
    @Immutable
    data class CodeBlock(
        val content: String,
        val language: String?,
    ) : ComposableStableNode()

    /**
     * Represents a Markdown block quote. Mapped from [BlockQuote][com.vladsch.flexmark.ast.BlockQuote].
     *
     * __Syntax:__
     *
     * ```markdown
     * > Some quote
     * ```
     *
     * For details see the [Markdown guide](https://www.markdownguide.org/basic-syntax#blockquotes-1).
     */
    @Immutable
    data class BlockQuote(val children: ImmutableList<StableNode>) : ComposableStableNode()

    /**
     * Represents a Markdown list. Mapped from [ListBlock][com.vladsch.flexmark.ast.ListBlock].
     *
     * __Syntax:__
     *
     * For details see [ListItemType] and the [Markdown guide](https://www.markdownguide.org/basic-syntax#lists-1).
     */
    @Immutable
    data class ListBlock(
        val level: Int,
        val children: ImmutableList<ListEntry>,
    ) : ComposableStableNode()

    /**
     * Represents a Markdown list entry.
     *
     * __Syntax:__
     *
     * For details see [ListItem], [ListNode], and the [Markdown guide](https://www.markdownguide.org/basic-syntax#lists-1).
     */
    @Stable
    sealed class ListEntry {

        /**
         * Represents a Markdown list item. Mapped from [ListItem][com.vladsch.flexmark.ast.ListItem].
         *
         * __Syntax:__
         *
         * For details see [ListItemType] and the [Markdown guide](https://www.markdownguide.org/basic-syntax#lists-1).
         */
        @Immutable
        data class ListItem(
            val type: ListItemType,
            val children: ImmutableList<AnnotatedStableNode>,
        ) : ListEntry()

        /**
         * Represents a non list item Markdown element in a list.
         *
         * __Syntax:__
         *
         * For details see the [Markdown guide](https://www.markdownguide.org/basic-syntax#adding-elements-in-lists).
         */
        @Immutable
        data class ListNode(val node: StableNode) : ListEntry()
    }

    /**
     * Represents a type of Markdown list item. Mapped from [ListItem][com.vladsch.flexmark.ast.ListItem].
     *
     * __Syntax:__
     *
     * For details see [Ordered], [Unordered], and [Task].
     */
    @Stable
    sealed class ListItemType {

        /**
         * Represents an ordered Markdown list item. Mapped from [OrderedListItem][com.vladsch.flexmark.ast.OrderedListItem].
         *
         * __Syntax:__
         *
         * ```markdown
         * 1. some list item
         *   1. some other item
         * 1. some list item again
         * ```
         *
         * For details see the [Markdown guide](https://www.markdownguide.org/basic-syntax#ordered-lists).
         */
        @Immutable
        data class Ordered(val index: Int) : ListItemType()

        /**
         * Represents an unordered Markdown list item. Mapped from [UnorderedListItem][com.vladsch.flexmark.ast.BulletListItem].
         *
         * __Syntax:__
         *
         * ```markdown
         * - some list item
         *   * other list item
         *     + more list item
         *   * other list item again
         * - some list item again
         * ```
         *
         * For details see the [Markdown guide](https://www.markdownguide.org/basic-syntax#unordered-lists).
         */
        @Immutable
        object Unordered : ListItemType()

        /**
         * Represents a Github Flavoured Markdown task list item. Mapped from [TaskListItem][com.vladsch.flexmark.ext.gfm.tasklist.TaskListItem].
         *
         * __Syntax:__
         *
         * ```markdown
         * - [ ] Unfinished item
         * - [x] Finished item
         * - [X] Other finished item
         * ```
         *
         * For details see the [Github Flavoured Markdown guide](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax#task-lists).
         */
        @Immutable
        data class Task(val completed: Boolean) : ListItemType()
    }

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
        val head: TableRow,
        val body: ImmutableList<TableRow>,
    ) : ComposableStableNode()

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

            START,
            CENTER,
            END,
        }
    }
}
