/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid;

import android.content.Context;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.m2mobi.markymark.DisplayItem;
import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymarkcommon.markdownitems.Paragraph;

/**
 * Converts an {@link Paragraph} to a {@link View}.
 */
public class ParagraphDisplayItem implements DisplayItem<View, Paragraph, Spanned> {

	private Context mContext;

	public ParagraphDisplayItem(final Context pContext) {
		mContext = pContext;
	}

	@Override
	public View create(final Paragraph pMarkdownItem, InlineConverter<Spanned> pInlineConverter) {
		TextView textView = new TextView(mContext, null, R.attr.MarkdownParagraphStyle);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		final CharSequence convert = pInlineConverter.convert(pMarkdownItem.getContent());
		textView.setText(convert);
		return textView;
	}
}