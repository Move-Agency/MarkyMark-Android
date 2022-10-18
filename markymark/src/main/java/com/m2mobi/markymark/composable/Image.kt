/*
 * Copyright © 2022 M2mobi
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

package com.m2mobi.markymark.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.FillWidth
import coil.compose.AsyncImage
import com.m2mobi.markymark.composer.padding
import com.m2mobi.markymark.model.ComposableStableNode.Image
import com.m2mobi.markymark.theme.ImageStyle

@Composable
internal fun Image(
    modifier: Modifier,
    node: Image,
    style: ImageStyle,
) {
    if (node.title == null) {
        AsyncImage(
            modifier = modifier.padding(style.padding),
            model = node.url,
            contentDescription = node.altText,
            contentScale = FillWidth,
        )
    } else {
        CaptionedImage(
            modifier = modifier,
            node = node,
            title = node.title,
            style = style,
        )
    }
}

@Composable
private fun CaptionedImage(
    modifier: Modifier,
    node: Image,
    title: String,
    style: ImageStyle,
) {
    Column(modifier.padding(style.padding)) {
        AsyncImage(
            model = node.url,
            contentDescription = node.altText,
            contentScale = FillWidth,
        )

        Text(
            modifier = Modifier
                .padding(style.captionPadding)
                .align(style.captionAlignment),
            text = title,
            style = style.captionTextStyle,
        )
    }
}
