/*
 * Copyright (C) M2mobi BV - All Rights Reserved
 */

package com.m2mobi.markymarkandroid;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * Interface that provides inline image updates
 */
public interface InlineImageCallback {

    /**
     * Called when the image has been updated
     *
     * @param drawable The new drawable
     */
    void updateImage(@Nullable Drawable drawable);
}
