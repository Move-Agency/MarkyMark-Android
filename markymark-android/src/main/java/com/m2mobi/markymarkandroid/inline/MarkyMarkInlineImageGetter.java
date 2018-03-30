/*
 * Copyright (C) M2mobi BV - All Rights Reserved
 */

package com.m2mobi.markymarkandroid.inline;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;

import com.m2mobi.markymarkandroid.InlineImageCallback;
import com.m2mobi.markymarkandroid.ImageLoader;

/**
 * ImageGetter that takes care of loading inline images
 */
public class MarkyMarkInlineImageGetter implements Html.ImageGetter {

    private BitmapDrawablePlaceHolder drawablePlaceHolder = new BitmapDrawablePlaceHolder();

    private ImageLoader mImageLoader;

    public MarkyMarkInlineImageGetter(ImageLoader pImageLoader) {
        mImageLoader = pImageLoader;
    }

    @Override
    public Drawable getDrawable(String source) {
        mImageLoader.loadDrawable(source, new InlineImageCallback() {
            @Override
            public void updateImage(@Nullable Drawable drawable) {
                drawablePlaceHolder.setDrawable(drawable);
            }
        });

        return drawablePlaceHolder;
    }

    private class BitmapDrawablePlaceHolder extends BitmapDrawable {

        private Drawable drawable;

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        public void setDrawable(@Nullable Drawable drawable) {
            this.drawable = drawable;

            if (drawable != null) {
                int width = drawable.getIntrinsicWidth();
                int height = drawable.getIntrinsicHeight();
                drawable.setBounds(0, 0, width, height);
                setBounds(0, 0, width, height);

                // TODO: Find a way to refresh the view
//                    if (textView != null) {
//                        Log.i("test", "refresh layout")
//                        textView.text = textView.text
//                    }
            }
        }
    }
}