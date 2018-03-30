/*
 * Copyright (C) M2mobi BV - All Rights Reserved
 */

package com.m2mobi.markymark

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.m2mobi.markymarkandroid.InlineImageCallback
import com.m2mobi.markymarkandroid.ImageLoader
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target as PicassoTarget

class PicassoImageLoader(private val context: Context) : ImageLoader {

    override fun loadImage(targetImageView: ImageView, url: String) {
        Picasso.with(context)
                .load(url)
                .into(targetImageView)
    }

    override fun loadDrawable(url: String, inlineImageCallback: InlineImageCallback) {
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.square)
                .into(InlineImageTarget(inlineImageCallback))
    }

    private inner class InlineImageTarget(private val inlineImageCallback: InlineImageCallback) : PicassoTarget {

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            inlineImageCallback.updateImage(placeHolderDrawable)
        }

        override fun onBitmapFailed(errorDrawable: Drawable?) {
            inlineImageCallback.updateImage(errorDrawable)
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            inlineImageCallback.updateImage(BitmapDrawable(context.resources, bitmap))
        }
    }
}