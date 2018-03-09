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
import com.m2mobi.markymarkcommon.markdownitems.inline.BoldString;

/**
 * Converts a {@link BoldString} to a {@link Spanned}.
 */
public class BoldInlineDisplayItem implements InlineDisplayItem<Spanned, BoldString> {

	@Override
	public Spanned create(final InlineConverter<Spanned> pInlineConverter, final BoldString pMarkDownString) {
		final Spannable spannable = SpannableUtils.createSpannable(pInlineConverter, pMarkDownString);
		spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}
}