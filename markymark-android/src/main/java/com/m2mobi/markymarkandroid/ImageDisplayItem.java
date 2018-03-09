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
import com.squareup.picasso.Picasso;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Converts an {@link Image} to a {@link View}.
 */
public class ImageDisplayItem implements DisplayItem<View, Image, Spanned> {

	private static final String URL_PREFIX = "https:";

	private final Context mContext;

	public ImageDisplayItem(Context pContext) {
		mContext = pContext;
	}

	@Override
	public View create(final Image pMarkDownItem, final InlineConverter<Spanned> pInlineConverter) {
		final ImageView imageView = new ImageView(mContext, null, R.attr.MarkDownImageStyle);
		imageView.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		Picasso.with(mContext)
		       .load(URL_PREFIX + pMarkDownItem.getFilename())
		       .into(imageView);
		return imageView;
	}
}