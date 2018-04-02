/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid.inline;

import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;

import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymark.InlineDisplayItem;
import com.m2mobi.markymarkcommon.markdownitems.inline.StrikeString;

/**
 * Converts a {@link StrikeString} to a {@link Spanned}.
 */
public class StrikeInlineDisplayItem implements InlineDisplayItem<Spanned, StrikeString> {

	@Override
	public Spanned create(final InlineConverter<Spanned> pInlineConverter, final StrikeString pMarkdownString) {
		final Spannable spannable = SpannableUtils.createSpannable(pInlineConverter, pMarkdownString);
		spannable.setSpan(new StrikethroughSpan(), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}
}