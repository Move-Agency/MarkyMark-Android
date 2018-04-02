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
			R.attr.MarkdownHeader1Style,
			R.attr.MarkdownHeader2Style,
			R.attr.MarkdownHeader3Style,
			R.attr.MarkdownHeader4Style,
			R.attr.MarkdownHeader5Style,
			R.attr.MarkdownHeader6Style
	};

	public HeaderDisplayItem(final Context pContext) {
		mContext = pContext;
	}

	@Override
	public View create(final Header pMarkdownItem, final InlineConverter<Spanned> pInlineConverter) {
		TextView textView = new TextView(mContext, null, attr[pMarkdownItem.getHeader() - 1]);
		textView.setText(pInlineConverter.convert(pMarkdownItem.getContent()));
		return textView;
	}
}