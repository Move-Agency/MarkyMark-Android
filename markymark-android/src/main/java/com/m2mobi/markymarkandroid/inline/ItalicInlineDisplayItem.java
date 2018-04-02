/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid.inline;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StyleSpan;

import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymark.InlineDisplayItem;
import com.m2mobi.markymarkcommon.markdownitems.inline.ItalicString;

/**
 * Converts an {@link ItalicString} to a {@link Spanned}.
 */
public class ItalicInlineDisplayItem implements InlineDisplayItem<Spanned, ItalicString> {

	@Override
	public Spanned create(final InlineConverter<Spanned> pInlineConverter, final ItalicString pMarkdownString) {
		final Spannable spannable = SpannableUtils.createSpannable(pInlineConverter, pMarkdownString);
		spannable.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}
}