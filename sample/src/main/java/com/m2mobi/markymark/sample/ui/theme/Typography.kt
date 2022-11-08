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

package com.m2mobi.markymark.sample.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily.Companion.SansSerif
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.sp
import com.m2mobi.markymark.sample.ui.theme.ColorPalette.M2Black
import com.m2mobi.markymark.sample.ui.theme.ColorPalette.M2Gray
import com.m2mobi.markymark.sample.ui.theme.Font.Monteserrat

object Typography {

    private val HeadingBase = TextStyle(fontFamily = Monteserrat)
    val Heading1 = HeadingBase + TextStyle(
        fontSize = 52.sp,
        color = M2Black,
        fontWeight = Bold,
    )
    val Heading2 = HeadingBase + TextStyle(
        fontSize = 32.sp,
        color = M2Black,
        fontWeight = Bold,
    )
    val Heading3 = HeadingBase + TextStyle(
        fontSize = 26.sp,
        color = M2Black,
        fontWeight = Bold,
    )
    val Heading4 = HeadingBase + TextStyle(
        fontSize = 22.sp,
        color = M2Gray,
        fontWeight = SemiBold,
    )
    val Heading5 = HeadingBase + TextStyle(
        fontSize = 18.sp,
        color = M2Gray,
        fontWeight = SemiBold,
    )
    val Heading6 = HeadingBase + TextStyle(
        fontSize = 16.sp,
        color = M2Gray,
        fontWeight = SemiBold,
    )

    val Body = TextStyle(
        fontSize = 18.sp,
        fontFamily = SansSerif,
        color = M2Black,
    )
}
