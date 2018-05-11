/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid.inline;

import android.text.Spanned;

import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymark.InlineDisplayItem;
import com.m2mobi.markymarkcommon.markdownitems.inline.InlineString;

/**
 * Converts an {@link InlineString} to a {@link Spanned}.
 */
public class StringInlineDisplayItem implements InlineDisplayItem<Spanned, InlineString> {

	@Override
	public Spanned create(final InlineConverter<Spanned> pInlineConverter, final InlineString pMarkdownString) {
		return SpannableUtils.createSpannable(pInlineConverter, pMarkdownString);
	}
}