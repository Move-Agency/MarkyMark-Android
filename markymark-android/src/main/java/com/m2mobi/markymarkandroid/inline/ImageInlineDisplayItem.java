/*
 * Copyright (C) M2mobi BV - All Rights Reserved
 */

package com.m2mobi.markymarkandroid.inline;

import android.text.Html;
import android.text.Spanned;

import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymark.InlineDisplayItem;
import com.m2mobi.markymarkandroid.ImageLoader;
import com.m2mobi.markymarkcommon.markdownitems.inline.ImageString;

/**
 * Converts a {@link ImageString} to a {@link Spanned} that contains an image.
 */
public class ImageInlineDisplayItem implements InlineDisplayItem<Spanned, ImageString> {

    private ImageLoader mImageLoader;

    public ImageInlineDisplayItem(final ImageLoader pImageLoader) {
        this.mImageLoader = pImageLoader;
    }

    @Override
    public Spanned create(InlineConverter<Spanned> pInlineConverter, ImageString pMarkDownString) {
        String content = String.format("<img src='%s'/>", pMarkDownString.getImageUrl());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY, new MarkyMarkInlineImageGetter(mImageLoader), null);
        } else {
            return Html.fromHtml(content, new MarkyMarkInlineImageGetter(mImageLoader), null);
        }
    }
}
