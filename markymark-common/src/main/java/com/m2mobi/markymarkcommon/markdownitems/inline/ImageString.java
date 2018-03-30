/*
 * Copyright (C) M2mobi BV - All Rights Reserved
 */

package com.m2mobi.markymarkcommon.markdownitems.inline;

import com.m2mobi.markymark.item.inline.MarkdownString;

/**
 * {@link MarkdownString} used for storing inline images
 */
public class ImageString extends MarkdownString {

    private String mAltText;

    private String mImageUrl;

    public ImageString(String pContent, String altText, String pImageUrl, boolean pCanHaveChildItems) {
        super(pContent, pCanHaveChildItems);
        this.mAltText = altText;
        this.mImageUrl = pImageUrl;
    }

    public String getAltText() {
        return mAltText;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
