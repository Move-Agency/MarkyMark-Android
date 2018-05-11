/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid;

import android.content.Context;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.m2mobi.markymark.DisplayItem;
import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymarkcommon.markdownitems.Image;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Converts an {@link Image} to a {@link View}.
 */
public class ImageDisplayItem implements DisplayItem<View, Image, Spanned> {

	private final Context mContext;

	private final ImageLoader mImageLoader;

	public ImageDisplayItem(Context pContext, ImageLoader imageLoader) {
		mContext = pContext;
		mImageLoader = imageLoader;
	}

	@Override
	public View create(final Image pMarkdownItem, final InlineConverter<Spanned> pInlineConverter) {
		final ImageView imageView = new ImageView(mContext, null, R.attr.MarkdownImageStyle);
		imageView.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		mImageLoader.loadImage(imageView, pMarkdownItem.getFilename());
		return imageView;
	}
}