/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid;

import android.content.Context;
import android.text.Spanned;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.m2mobi.markymark.DisplayItem;
import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymarkcommon.markdownitems.CodeBlock;

/**
 * Converts an {@link CodeBlock} to a {@link View}.
 */
public class CodeBlockDisplayItem implements DisplayItem<View, CodeBlock, Spanned> {

	private Context mContext;

	public CodeBlockDisplayItem(final Context pContext) {
		mContext = pContext;
	}

	@Override
	public View create(final CodeBlock pMarkDownItem, final InlineConverter<Spanned> pInlineConverter) {
		TextView textView = new TextView(mContext, null, R.attr.MarkDownCodeStyle);
		textView.setHorizontallyScrolling(true);
		textView.setText(pMarkDownItem.getContent());

		HorizontalScrollView scrollView = new HorizontalScrollView(mContext);
		scrollView.addView(textView);
		return scrollView;
	}
}