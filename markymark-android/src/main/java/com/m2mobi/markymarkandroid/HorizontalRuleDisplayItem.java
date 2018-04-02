/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid;

import android.content.Context;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.m2mobi.markymark.DisplayItem;
import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymarkcommon.markdownitems.HorizontalLine;

/**
 * Converts an {@link HorizontalLine} to a {@link View}.
 */
public class HorizontalRuleDisplayItem implements DisplayItem<View, HorizontalLine, Spanned> {

	private Context mContext;

	public HorizontalRuleDisplayItem(final Context pContext) {
		mContext = pContext;
	}

	@Override
	public View create(final HorizontalLine pMarkdownItem, final InlineConverter<Spanned> pInlineConverter) {
		View view = new View(mContext, null, R.attr.MarkdownHorizontalRuleStyle);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		return view;
	}
}