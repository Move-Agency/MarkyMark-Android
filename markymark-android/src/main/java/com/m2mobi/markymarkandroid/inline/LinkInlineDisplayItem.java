/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid.inline;

import android.text.Spannable;
import android.text.Spanned;
import android.text.style.URLSpan;

import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymark.InlineDisplayItem;
import com.m2mobi.markymarkcommon.markdownitems.inline.LinkString;

/**
 * Converts a {@link LinkString} to a {@link Spanned}.
 */
public class LinkInlineDisplayItem implements InlineDisplayItem<Spanned, LinkString> {

	@Override
	public Spanned create(final InlineConverter<Spanned> pInlineConverter, final LinkString pMarkdownString) {
		final Spannable spannable = SpannableUtils.createSpannable(pInlineConverter, pMarkdownString);
		spannable.setSpan(new URLSpan(pMarkdownString.getUrl()), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}
}