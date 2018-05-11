/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid.inline;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.TypefaceSpan;

import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymark.InlineDisplayItem;
import com.m2mobi.markymarkcommon.markdownitems.inline.CodeString;

/**
 * Converts a {@link CodeString} to a {@link Spanned}.
 */
public class CodeInlineDisplayItem implements InlineDisplayItem<Spanned, CodeString> {

	@Override
	public Spanned create(final InlineConverter<Spanned> pInlineConverter, final CodeString pMarkdownString) {
		SpannableString spannable = new SpannableString(pMarkdownString.getContent());
		spannable.setSpan(new TypefaceSpan("monospace"), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new BackgroundColorSpan(Color.LTGRAY), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}
}