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

package com.moveagency.markymark.sample.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontStyle.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.W100
import androidx.compose.ui.text.font.FontWeight.Companion.W200
import androidx.compose.ui.text.font.FontWeight.Companion.W300
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.font.FontWeight.Companion.W800
import androidx.compose.ui.text.font.FontWeight.Companion.W900
import com.moveagency.markymark.sample.R

object Font {

    val Monteserrat = FontFamily(
        Font(resId = R.font.montserrat_thin, weight = W100, style = Normal),
        Font(resId = R.font.montserrat_thin_italic, weight = W100, style = Italic),
        Font(resId = R.font.montserrat_extra_light, weight = W200, style = Normal),
        Font(resId = R.font.montserrat_extra_light_italic, weight = W200, style = Italic),
        Font(resId = R.font.montserrat_light, weight = W300, style = Normal),
        Font(resId = R.font.montserrat_light_italic, weight = W300, style = Italic),
        Font(resId = R.font.montserrat_regular, weight = W400, style = Normal),
        Font(resId = R.font.montserrat_italic, weight = W400, style = Italic),
        Font(resId = R.font.montserrat_medium, weight = W500, style = Normal),
        Font(resId = R.font.montserrat_medium_italic, weight = W500, style = Italic),
        Font(resId = R.font.montserrat_semi_bold, weight = W600, style = Normal),
        Font(resId = R.font.montserrat_semi_bold_italic, weight = W600, style = Italic),
        Font(resId = R.font.montserrat_bold, weight = W700, style = Normal),
        Font(resId = R.font.montserrat_bold_italic, weight = W700, style = Italic),
        Font(resId = R.font.montserrat_extra_bold, weight = W800, style = Normal),
        Font(resId = R.font.montserrat_extra_bold_italic, weight = W800, style = Italic),
        Font(resId = R.font.montserrat_black, weight = W900, style = Normal),
        Font(resId = R.font.montserrat_black_italic, weight = W900, style = Italic),
    )
}
