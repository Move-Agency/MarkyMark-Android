/*
 * Copyright © 2025 Move
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

package com.moveagency.markymark.composable

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale.Companion.FillWidth
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.moveagency.markymark.composer.padding
import com.moveagency.markymark.model.NodeMetadata.Companion.Root
import com.moveagency.markymark.model.composable.Image
import com.moveagency.markymark.theme.LocalMarkyMarkTheme
import com.moveagency.markymark.theme.image.ImageStyle

@Composable
fun MarkyMarkImage(
    node: Image,
    modifier: Modifier = Modifier,
) {
    val style = LocalMarkyMarkTheme.current.styles.composable.image
    if (node.title == null) {
        AsyncImage(
            model = node.url,
            contentDescription = node.altText,
            modifier = modifier
                .padding(style.padding)
                .clip(style.shape),
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
    node: Image,
    title: String,
    style: ImageStyle,
    modifier: Modifier = Modifier,
) {
    val colors = LocalMarkyMarkColors.current.image
    Column(
        modifier = modifier
            .padding(style.padding)
            .focusGroup(),
    ) {
        AsyncImage(
            model = node.url,
            contentDescription = node.altText,
            modifier = Modifier.clip(style.shape),
            contentScale = FillWidth,
        )

        SelectionContainer(
            Modifier
                .padding(style.caption.padding)
                .align(style.caption.alignment)
        ) {
            Text(
                text = title,
                color = colors.caption,
                style = style.caption.textStyle,
            )
        }
    }
}

private const val previewImageUrl = "https://images.unsplash.com/photo-1459262838948-3e2de6c1ec80?q=80&w=3269&auto=format" +
    "&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"

@Preview
@Composable
private fun PreviewImage() {
    val node = Image(
        metadata = Root,
        url = previewImageUrl,
        altText = "A cute koala",
        title = null,
    )

    MarkyMarkImage(
        node = node,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview
@Composable
private fun PreviewImageCaptioned() {
    val node = Image(
        metadata = Root,
        url = previewImageUrl,
        altText = "A cute koala",
        title = "A cute koala in a tree",
    )

    MarkyMarkImage(
        node = node,
        modifier = Modifier.fillMaxWidth(),
    )
}
