/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid.inline;

import android.text.Spannable;
import android.text.Spanned;
import android.text.SpannedString;

import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymark.InlineDisplayItem;
import com.m2mobi.markymark.item.inline.TextString;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Converts an {@link TextString} to a {@link Spanned}.
 */
public class TextDisplayItem implements InlineDisplayItem<Spanned, TextString> {

	@Override
	public Spanned create(final InlineConverter<Spanned> pInlineConverter, final TextString pMarkdownString) {
		final Spannable spannable = SpannableUtils.createSpannable(pInlineConverter, pMarkdownString);
		final String result = StringEscapeUtils.unescapeHtml4(String.valueOf(spannable));
		return new SpannedString(result);
	}
}