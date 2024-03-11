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
import androidx.compose.runtime.Stable
import com.moveagency.markymark.model.NodeMetadata
import com.moveagency.markymark.model.annotated.AnnotatedStableNode
import com.moveagency.markymark.model.composable.ListBlock.ListEntry.ListItem
import com.moveagency.markymark.model.composable.ListBlock.ListEntry.ListNode
import com.moveagency.markymark.model.composable.ListBlock.ListItemType
import com.moveagency.markymark.model.composable.ListBlock.ListItemType.*
import kotlinx.collections.immutable.ImmutableList

/**
 * Represents a Markdown list. Mapped from [ListBlock][com.vladsch.flexmark.ast.ListBlock].
 *
 * __Syntax:__
 *
 * For details see [ListItemType] and the [Markdown guide](https://www.markdownguide.org/basic-syntax#lists-1).
 */
@Immutable
data class ListBlock(
    override val metadata: NodeMetadata,
    val children: ImmutableList<ListEntry>,
) : ComposableStableNode() {

    /**
     * Represents a Markdown list entry.
     *
     * __Syntax:__
     *
     * For details see [ListItem], [ListNode], and the
     * [Markdown guide](https://www.markdownguide.org/basic-syntax#lists-1).
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
        data class ListNode(val node: ComposableStableNode) : ListEntry()
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
         * Represents an ordered Markdown list item. Mapped from
         * [OrderedListItem][com.vladsch.flexmark.ast.OrderedListItem].
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
         * Represents an unordered Markdown list item. Mapped from
         * [UnorderedListItem][com.vladsch.flexmark.ast.BulletListItem].
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
         * Represents a Github Flavoured Markdown task list item. Mapped from
         * [TaskListItem][com.vladsch.flexmark.ext.gfm.tasklist.TaskListItem].
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
}
