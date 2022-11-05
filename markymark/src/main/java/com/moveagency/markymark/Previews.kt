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

package com.moveagency.markymark

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.moveagency.markymark.annotator.MarkyMarkAnnotator
import com.moveagency.markymark.composable.Markdown
import com.moveagency.markymark.composer.MarkyMarkComposer
import com.moveagency.markymark.theme.MarkyMarkTheme

@Preview
@Composable
internal fun Preview() {
    Markdown(
        modifier = Modifier.background(Color.White),
        markdown = PREVIEW_MARKDOWN,
    )
}

/**
 * Contains all syntax supported by MarkyMark. Made public so you can include it in a debug screen and verify your
 * custom [MarkyMarkTheme], [MarkyMarkComposer], or [MarkyMarkAnnotator].
 */
@Suppress("MaxLineLength")
const val PREVIEW_MARKDOWN =
    """
# Supported Markdown Showcase

## Headings

# Heading 1

## Heading 2

### Heading 3

#### Heading 4

##### Heading 5

###### Heading 6

### _Styling_ __in__ ~~headers~~

## Paragraphs

You can also just add normal, simple paragraphs to your hearts content.

Want blank lines? That's fine too!

### Superscript & Subscript

We can do superscript too see: 60m^2^, also subscript: H~2~O

### Emojis

You can use emojis 😀 (utf-8 encoded).

## Emphasis

### Asterisk

some *markdown* text *everybody*, cool stuff.

*Italic*

**Bold**

### Underscore

_Italic_

__Bold__

### Both

***Bold & Italic***

___Bold & Italic___

### Both *(Mixed)*

__*Bold & Italic*__

**_Bold & Italic_**

## Block Quotes

### Single `>`

> O Romeo, Romeo! wherefore art thou Romeo?
  Deny thy father, and refuse thy name;
  Or, if thou wilt not, be but sworn my love,
  And I'll no longer be a Capulet.
  ~ Juliet Capulet (Romeo and Juliet by William Shakespear)

### Multiple `>`

> Writing clean code is what you must do in order to call yourself a professional.
> There is no reasonable excuse for doing anything less then your best.
>
> ~ Clean Code by Robert C. Martin (title page)

### Line Breaks

> "Realy, Hagrid, if you are holding out for universal popularity, I'm afraid you will be in this cabin for a very long time" said Dumbledore, now peering sternly over his half-moon spectacles. "Not a week has passed, since I became Headmaster of this school, when I haven't had atleast one owl complaining about the way I run it. But what should I do? Barricade myself in my study and refuse to talk to anybody?"
> "Yeh - yeh're not half-giant!" said Hagrid croakily.
> "Hagrid, look what I've got for relatives!" Harry said furiously. "Look at the Dursleys!"
> "An excellent point" said Professor Dumbledore. "My own brother, Aberforth, was prosecuted for practising inappropriate charms on a goat. It was all over the papers, but did Aberforth hide? No, he did not! He held his head high and went about his business as usual! Of course, I'm not entirely sure he can read, so that may not have been bravery..."
>
> ~ Harry Potter and the Goblet of Fire by J.K. Rowling (Chapter 24. Rita Skeeter's Scoop)

### Inline Formatting

> *“A towel, [The Hitchhiker's Guide to the Galaxy] says, is about the most massively useful thing an interstellar hitchhiker can have. Partly it has great practical value. You can wrap it around you for warmth as you bound across the cold moons of Jaglan Beta; you can lie on it on the brilliant marble-sanded beaches of Santraginus V, inhaling the heady sea vapors; you can sleep under it beneath the stars which shine so redly on the desert world of Kakrafoon; use it to sail a miniraft down the slow heavy River Moth; wet it for use in hand-to-hand-combat; wrap it round your head to ward off noxious fumes or avoid the gaze of the Ravenous Bugblatter Beast of Traal (such a mind-boggingly stupid animal, it assumes that if you can't see it, it can't see you); you can wave your towel in emergencies as a distress signal, and of course dry yourself off with it if it still seems to be clean enough.”*
> 
> ~ Hitchhiker's Guide to the Galaxy by Douglas Adams

### Nested

> Svoboda slipped in through the door as Rudy left.
> "Hey Jazz" Svoboda pulled up a chair and sat beside the bed. "Doc says you're doing great!"
> "Hey Svobo. sorry about the chloroform
> "Eh, no biggy." He shrugged.
> "I'm guessing the rest of town isn't as forgiving?"
> "People don't seem that mad. Well, some are. But most aren't."
> "Seriously?" I said. "I knocked the whole town out."
> He wiggled his hand. "That wasn't just you. There were a lot of engineering failures. Like:
>
> > Why aren't there detectors in the air pipeline for complex toxins?
>
> > Why did Sanches store methane, oxygen, and chlorine in a room with an oven?
>
> > Why doesn't Life Support have its own separate air partition to make sure they'll stay awake if the rest of the city has a problem?
>
> > Why is Life Support centralized instead of having a separate zone for each bubble?
> 
> These are the questions people are asking."
> He put his hand on my arm. "I'm just glad you're okay."
> I put my hand on his. The effect was kinda lost with all the bandaging.
> "Anyway," he said. "The whole thing gave me a chance to bond with your dad"
> "Really?"
> "Yeah!" he said. "After we woke up we formed a two-man team to check on my neighbors. It was cool. He bought me a beer afterward."
> I widened my eyes. "Dad... bought a beer?"
> "For me, yeah. He drank juice. We spend an hour talking about metallurgy! Awesome guy."
> I tried to imagine Dad and Svoboda hanging out. I failed.
> "Awesome guy," Svoboda repeated, a little quieter this time. His smile faded.
> "Svobo?" I said.
> He looked down. "Are you... leaving Jazz? Are they going to deport you? I'd hate that."
> I put my mittened hand on his shoulder. "It'll be all right. I'm not going anywhere."
> "You sure?"
> "Yeah, I have a plan"
> "A plan?" He looked concerned. "Your plans are... uh... should I hide somewhere?"
> I laughed. "Not this time"
> "Okay..." He was clearly not convinced. "But how are you going to get out of this one? Like... you knocked out the *whole town*."
> I smiled at him. "Don't worry. I got this."
> "Okay, good." He leaned down and kissed my cheek, almost as an afterthought. I had no idea what possessed him to do that-- honestly I didn't think he had it in him. His bravery didn't last long though. Once he realized what he'd done, his face became a mask of terror. "Oh shit! I'm sorry! I wasn't thinking--"
> I laughed. The look in the poor guy's eyes... I couldn't help it. "Relax Svobo. It's just a peck on the cheek. It's nothing to get worked up about."
> "R-Right. Yeah."
> I put my hand on the nape of his neck, pulled his head to mine, and kissed him full on the lips. A good, long kiss with no ambiguity. When we disengaged, he looked hopelessly confused.
> "Now *that*," I said. "*That* you can get worked up about."
>
> ~ Artemis by Andy Weir

## Lists

### Unordered

#### Asterisk

* List Item 1
* List Item 2
* List Item 3

#### Dash

- List Item 1
- List Item 2
- List Item 3

#### Plus

+ List Item 1
+ List Item 2
+ List Item 3

#### Mixed

+ List Item 1
- List Item 2
* List Item 3

### Ordered

#### Sequential

1. List Item 1
2. List Item 2
3. List Item 3

#### Non Sequential

1. List Item 1
1. List Item 2
1. List Item 3

### Nested

#### Unordered

+ List Item 1 lorum ipsum dolor sit amet, consectetur adipiscing elit
+ List Item 2
    - Sub-List Item 1
    - Sub-List Item 2
    - Sub-List Item 3
        * Sub-Sub-List Item 1
        * Sub-Sub-List Item 2
        * Sub-Sub-List Item 3
        * Sub-Sub-List Item 4
    - Sub-List Item 3
    - Sub-List Item 3
+ List Item 3
+ List Item 4

#### Ordered

1. List Item 1
1. List Item 2
    1. Sub-List Item 1
    1. Sub-List Item 2
    1. Sub-List Item 3
1. List Item 3

#### Multiple Indent Backwards Jump

1. List Item 1
1. List Item 2
    1. Sub-List Item 1
    1. Sub-List Item 2
    1. Sub-List Item 3
        1. Sub-Sub-List Item 1
        1. Sub-Sub-List Item 2
        1. Sub-Sub-List Item 3
1. List Item 3

#### Mixed

1. List Item 1
1. List Item 2
    - Sub-List Item 1
    - Sub-List Item 2
    - Sub-List Item 3
        + Sub-Sub-List Item 1
        + Sub-Sub-List Item 2
        + Sub-Sub-List Item 3
            * Sub-Sub-Sub-List Item 1
            * Sub-Sub-Sub-List Item 2
            * Sub-Sub-Sub-List Item 3
1. List Item 3

#### Formatting

1. *List Item 1*
1. ~~List Item 2~~
    - *Sub-List Item 1*
    - __Sub-List Item 2__
    - Sub-List Item 3
        + Sub-Sub-List Item 1
        + __Sub-Sub-List Item 2__
        + Sub-Sub-List Item 3
1. List Item 3

### Task List

#### Ordered

1. [x] "x" completed item
2. [X] "X" completed item
3. [ ] Uncompleted item

#### Unordered

- [x] "x" completed item
- [X] "X" completed item
- [ ] Uncompleted item

### List Interruptions

1. First list item

    ```sh
    sh ./gradlew assemble
    ```

2. Second list item
3. Third list item

    > A Quote or something who knows

4. Fourth list item

### Inside a Quote

> Some text to start with
> 
> 1. List item 1
> 2. List item 2
>     - Sub item 1
> 3. List item 3

## Code

### Inline

Shell command to change directories: `cd '$'dir`

### Code Blocks

#### Indented

    function do_something () {
        cd '$'dir
    }

##### Blank Lines

    val text = "some text"

    for (char in text) {
        println(char)
    }

#### Fenced

```
cd '$'project_dir
mv file file.md
command
echo "done"
```

##### Language

```kotlin
fun doSomething(aThing: String): Something {
    return Something(aThing)
}
```

##### Tilde

~~~Kotlin
fun doNothing() {

}
~~~

## Horizontal rule

### Asterisk

***

### Dash

---

### Underscore

___

### Many

__________________________________

## Links

[link text](https://www.moveagency.com/)

### Titles

[link text](https://www.moveagency.com/ "link title")

### Quick

<https://www.moveagency.com/>

### Formatting

**[link text](https://www.moveagency.com/)**

*[link text](https://www.moveagency.com/)*

~~[link text](https://www.moveagency.com/)~~

### Email

#### Normal

info@moveagency.com

#### Quick

<jobs@moveagency.com>

## Images

![A photo showing the internal reveal of the new company name on a screen in the background, Move, in the foreground confetti and employees can be seen](https://global-uploads.webflow.com/6315fa597257024bb7fea6c4/635f950391fa6db43eea3a18_c7572a87-7f89-415a-923b-fefae1e85d7d.webp)

### Titles

![A photo of the outside of the Move Amsterdam office](https://global-uploads.webflow.com/6315fa597257021d73fea691/632c47a8e4e4c81d8c461997_contact-header.webp "Figure 1. The Move Amsterdam office (3rd floor)")

## Tables

| __Move Amsterdam__                           | __Move Lisbon__                                  | __Move Zwolle__                              |
|:---------------------------------------------|:-------------------------------------------------|:---------------------------------------------|
| Moermanskade 313                             | Av. Fontes Pereira de Melo 14                    | Hanzelaan 351                                |
| 1013 BC Amsterdam                            | 1050-121 Lisboa                                  | 8017 JM Zwolle                               |
| Netherlands                                  | Portugal                                         | Netherlands                                  |
| [+31 (0)20 - 354 0259](tel:+31(0)20-3540259) | [+31 (0)20 - 354 0259](tel:+31(0)20-3540259)     | [+31 (0)38 - 760 1750](tel:+31(0)38-7601750) |
| [Route](https://tinyurl.com/move-amsterdam)  | [Route](https://tinyurl.com/move-portugal)       | [Route](tinyurl.com/move-zwolle)             |

| __New Business__    |
|:--------------------|
| info@moveagency.com |

| __Press & Communication__ |
|:--------------------------|
| marketing@moveagency.com  |

| __Vacancies__       |
|:--------------------|
| jobs@moveagency.com |
    """
