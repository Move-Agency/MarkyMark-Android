/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid;

import android.content.Context;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.m2mobi.markymark.DisplayItem;
import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymarkcommon.markdownitems.Header;

/**
 * Converts an {@link Header} to a {@link View}.
 */
public class HeaderDisplayItem implements DisplayItem<View, Header, Spanned> {

	protected final Context mContext;

	protected static final int[] attr = new int[] {
			R.attr.MarkDownHeader1Style,
			R.attr.MarkDownHeader2Style,
			R.attr.MarkDownHeader3Style,
			R.attr.MarkDownHeader4Style,
			R.attr.MarkDownHeader5Style,
			R.attr.MarkDownHeader6Style
	};

	public HeaderDisplayItem(final Context pContext) {
		mContext = pContext;
	}

	@Override
	public View create(final Header pMarkDownItem, final InlineConverter<Spanned> pInlineConverter) {
		TextView textView = new TextView(mContext, null, attr[pMarkDownItem.getHeader() - 1]);
		textView.setText(pInlineConverter.convert(pMarkDownItem.getContent()));
		return textView;
	}
}