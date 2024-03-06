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

@file:OptIn(ExperimentalTextApi::class)

package com.moveagency.markymark.sample.ui.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Light
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.sp
import com.moveagency.markymark.sample.ui.theme.Font.SpaceGrotesk

object Typography {

    private val BaseStyle = TextStyle(fontFamily = SpaceGrotesk)

    val Heading1 = BaseStyle.copy(
        fontSize = 48.sp,
        lineHeight = 64.sp,
        fontWeight = Normal,
    )
    val Heading2 = BaseStyle.copy(
        fontSize = 40.sp,
        lineHeight = 56.sp,
        fontWeight = Normal,
    )
    val Heading3 = BaseStyle.copy(
        fontSize = 32.sp,
        lineHeight = 48.sp,
        fontWeight = Normal,
    )
    val Heading4 = BaseStyle.copy(
        fontSize = 24.sp,
        lineHeight = 40.sp,
        fontWeight = Bold,
    )
    val Heading5 = BaseStyle.copy(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = Bold,
    )
    val Heading6 = BaseStyle.copy(
        fontSize = 16.sp,
        lineHeight = 32.sp,
        fontWeight = Bold,
    )

    val Caption = BaseStyle.copy(
        fontSize = 12.sp,
        fontStyle = Italic,
        fontWeight = Light,
        lineHeight = 16.sp,
    )

    val Body = BaseStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
    )

    val TableHeader = BaseStyle.copy(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = SemiBold,
    )
}
