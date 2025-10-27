/*
 * Copyright © 2025 Framna
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
 * Represents a Markdown headline (heading). Mapped from [Heading][com.vladsch.flexmark.ast.Heading].
 *
 * Headings are used to create section titles and establish document structure. They are rendered
 * with larger and/or bolder text, with size decreasing as heading level increases (Heading1 is
 * largest, Heading6 is smallest).
 *
 * __Basic Syntax:__
 *
 * Headings are created using hash characters (#) at the start of the line. The number of hash
 * characters determines the heading level (1-6):
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
 * __Alternative Syntax:__
 *
 * For level 1 and 2 headings only, an alternative "underline" syntax is also supported:
 *
 * ```markdown
 * Heading 1
 * =========
 *
 * Heading 2
 * ---------
 * ```
 *
 * __Best Practices:__
 *
 * - Always put a space between the # symbols and the heading text
 * - For better compatibility, add blank lines before and after headings
 * - Use heading levels in order (don't skip levels) for proper document structure
 * - Avoid using more than 6 # symbols as they won't be recognized as headings
 *
 * __Formatting in Headings:__
 *
 * Headings can contain inline formatting such as emphasis (bold/italic), links, and code:
 *
 * ```markdown
 * ## **Bold** Heading with [Link](https://example.com) and `code`
 * ```
 *
 * For more details, see the [Markdown guide](https://www.markdownguide.org/basic-syntax#headings).
 *
 * @property metadata Contains information about this heading node, including its position in the
 *   document hierarchy and any additional metadata.
 * @property children The list of child nodes contained within this heading, typically inline
 *   elements like text, emphasis, links, etc. These represent the content of the heading.
 * @property headingLevel The level of the heading (1-6), represented by the [Level] enum.
 *   This determines how the heading is styled and its hierarchical importance in the document.
 */
@Immutable
data class Headline(
    override val metadata: NodeMetadata,
    val children: ImmutableList<AnnotatedStableNode>,
    val headingLevel: Level,
) : ComposableStableNode() {

    /**
     * Represents the six available heading levels in Markdown.
     *
     * Each level has a specific semantic meaning in document structure, with lower numbers
     * indicating higher importance. In HTML output, these correspond to h1-h6 tags.
     */
    enum class Level {

        /**
         * The top-level heading, typically used for document titles or major section headings.
         *
         * __Syntax:__
         *
         * ```markdown
         * # Heading 1
         * ```
         *
         * Or alternatively:
         *
         * ```markdown
         * Heading 1
         * =========
         * ```
         *
         * In HTML, this corresponds to the `<h1>` tag. A document should typically have only
         * one Heading1 for proper document structure.
         */
        Heading1,

        /**
         * Second-level heading, used for major sections within the document.
         *
         * __Syntax:__
         *
         * ```markdown
         * ## Heading 2
         * ```
         *
         * Or alternatively:
         *
         * ```markdown
         * Heading 2
         * ---------
         * ```
         *
         * In HTML, this corresponds to the `<h2>` tag. These headings divide the document into
         * major sections.
         */
        Heading2,

        /**
         * Third-level heading, used for subsections within major sections.
         *
         * __Syntax:__
         *
         * ```markdown
         * ### Heading 3
         * ```
         *
         * In HTML, this corresponds to the `<h3>` tag. These headings are typically used for
         * subsections within Heading2 sections.
         */
        Heading3,

        /**
         * Fourth-level heading, used for smaller divisions within subsections.
         *
         * __Syntax:__
         *
         * ```markdown
         * #### Heading 4
         * ```
         *
         * In HTML, this corresponds to the `<h4>` tag. These are typically used for smaller
         * topic divisions within Heading3 sections.
         */
        Heading4,

        /**
         * Fifth-level heading, used for fine-grained content organization.
         *
         * __Syntax:__
         *
         * ```markdown
         * ##### Heading 5
         * ```
         *
         * In HTML, this corresponds to the `<h5>` tag. These are used for detailed topic
         * organization and are typically rendered with smaller text than higher-level headings.
         */
        Heading5,

        /**
         * Sixth-level heading, the lowest level available in Markdown.
         *
         * __Syntax:__
         *
         * ```markdown
         * ###### Heading 6
         * ```
         *
         * In HTML, this corresponds to the `<h6>` tag. These are the smallest headings and
         * are used for the most granular content divisions. They are typically rendered with
         * the smallest text size among all heading levels.
         */
        Heading6,
    }
}
