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

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.moveagency.markymark.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily.Companion.Monospace
import androidx.compose.ui.text.font.FontFamily.Companion.SansSerif
import androidx.compose.ui.text.font.FontFamily.Companion.Serif
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moveagency.markymark.theme.list.UnorderedListItemStyle

object MarkyMarkThemeDefaults {

    private val BaseHeadingTextStyle
        get() = TextStyle(
            fontWeight = FontWeight.Bold,
            fontFamily = Serif,
        )

    private val BodyTextStyle
        get() = TextStyle(fontSize = 18.sp, fontFamily = SansSerif)

    val DefaultTheme
        get() = markyMarkTheme {
            root {
                padding { all = 16.dp }
            }

            styles {
                composable {
                    headings {
                        h1 {
                            padding { all = 4.dp }
                            textStyle = BaseHeadingTextStyle.copy(fontSize = 52.sp)
                        }

                        h2 {
                            padding { all = 4.dp }
                            textStyle = BaseHeadingTextStyle.copy(fontSize = 32.sp)
                        }

                        h3 {
                            padding { all = 4.dp }
                            textStyle = BaseHeadingTextStyle.copy(fontSize = 26.sp)
                        }

                        h4 {
                            padding { all = 4.dp }
                            textStyle = BaseHeadingTextStyle.copy(fontSize = 22.sp)
                        }

                        h5 {
                            padding { all = 4.dp }
                            textStyle = BaseHeadingTextStyle.copy(fontSize = 18.sp)
                        }

                        h6 {
                            padding { all = 4.dp }
                            textStyle = BaseHeadingTextStyle.copy(fontSize = 16.sp)
                        }
                    }

                    paragraph {
                        padding { all = 8.dp }
                    }

                    rule {
                        padding { all = 2.dp }
                    }

                    codeBlock {
                        innerPadding { all = 8.dp }
                        outerPadding { all = 8.dp }

                        shape = RoundedCornerShape(4.dp)

                        textStyle = TextStyle(fontFamily = Monospace)
                    }

                    blockQuote {
                        innerPadding { all = 8.dp }
                        outerPadding {
                            start = 12.dp
                            vertical = 2.dp
                        }

                        indicatorThickness = 2.dp

                        shape = RoundedCornerShape(8.dp)

                        themes {
                            theme {
                                background = LightGray
                                indicator = Color.Blue
                            }

                            theme {
                                background = Gray
                                indicator = Color.Blue
                            }

                            theme {
                                background = DarkGray
                                indicator = Color.Blue

                                colors {
                                    headings { all = White }

                                    image { caption = White }

                                    paragraph { text = White }

                                    rule { background = White }

                                    table {
                                        header {
                                            text = White
                                            divider = LightGray
                                        }

                                        body {
                                            text = White
                                            divider = Gray
                                        }

                                        outline { all = White }
                                    }

                                    list {
                                        ordered {
                                            text = White
                                            indicator = LightGray
                                        }

                                        unordered {
                                            text = White
                                            indicator = LightGray
                                        }

                                        task {
                                            text = White
                                            checked = White
                                            unchecked = White
                                            checkmark = Black
                                        }
                                    }

                                    codeBlock {
                                        background = Gray
                                        text = White
                                    }
                                }
                            }
                        }
                    }

                    tableBlock {
                        padding { all = 4.dp }

                        header {
                            cell {
                                padding { all = 4.dp }
                                textStyle = BodyTextStyle
                            }
                            divider { thickness = 1.dp }
                        }

                        body {
                            cell {
                                padding { all = 4.dp }
                                textStyle = BodyTextStyle
                            }

                            horizontalDivider { thickness = 1.dp }

                            verticalDivider { thickness = 1.dp }
                        }

                        outline {
                            all { thickness = 2.dp }
                        }
                    }

                    listBlock {
                        padding { all = 2.dp }
                        levelIndent = 24.dp

                        ordered {
                            padding { start = 12.dp }
                            textStyle = BodyTextStyle

                            indicatorPadding { end = 12.dp }
                            indicatorTextStyle = BodyTextStyle
                        }

                        unordered {
                            padding { start = 12.dp }
                            textStyle = BodyTextStyle

                            indicatorPadding { end = 12.dp }

                            indicators {
                                indicator { shape = UnorderedListItemStyle.Indicator.Shape.Oval }

                                indicator { shape = UnorderedListItemStyle.Indicator.Shape.Triangle }

                                indicator { shape = UnorderedListItemStyle.Indicator.Shape.Rectangle }

                                indicator { // Line
                                    shape = UnorderedListItemStyle.Indicator.Shape.Rectangle
                                    size = DpSize(width = 6.dp, height = 2.dp)
                                }
                            }
                        }

                        task {
                            padding { start = 12.dp }
                            textStyle = BodyTextStyle

                            indicatorPadding { end = 12.dp }
                        }
                    }
                }

                annotated {
                    bold = SpanStyle(fontWeight = FontWeight.Bold)
                    code = SpanStyle(fontFamily = Monospace, background = LightGray)
                    italic = SpanStyle(fontStyle = FontStyle.Italic)
                    strikethrough = SpanStyle(textDecoration = LineThrough)
                    link = SpanStyle(textDecoration = Underline, color = Color(0xFF66AAFF))
                    subscript = SpanStyle(baselineShift = BaselineShift.Subscript, fontSize = 14.sp)
                    superscript = SpanStyle(
                        baselineShift = BaselineShift.Superscript,
                        fontSize = 10.sp,
                    )
                }
            }

            colors {
                headings { all = Black }

                image { caption = Black }

                paragraph { text = Black }

                rule { background = Black }

                table {
                    header {
                        text = Black
                        divider = Gray
                    }

                    body {
                        text = Black
                        divider = LightGray
                    }

                    outline { all = DarkGray }
                }

                list {
                    ordered {
                        text = Black
                        indicator = DarkGray
                    }

                    unordered {
                        text = Black
                        indicator = DarkGray
                    }

                    task {
                        text = Black
                        checked = Black
                        unchecked = Black
                        checkmark = White
                    }
                }

                codeBlock {
                    background = LightGray
                    text = Black
                }
            }
        }
}
