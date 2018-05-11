/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid.inline;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymark.item.inline.MarkdownString;

/**
 * Utility methods related to {@link Spannable}s
 */
class SpannableUtils {

	private SpannableUtils() {
		// no instances
	}

	/**
	 * Creates a Spannable from a MarkdownString
	 *
	 * @param pInlineConverter
	 * 		InlineConverter used to convert {@link MarkdownString} to spannable
	 * @param pMarkdownString
	 * 		MarkdownString used to create the spannables
	 * @return Returns a Spannable
	 */
	static Spannable createSpannable(final InlineConverter<Spanned> pInlineConverter, final MarkdownString pMarkdownString) {
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
		if (pMarkdownString.hasChildItems()) {
			for (MarkdownString markdownString : pInlineConverter.parseContent(pMarkdownString.getContent())) {
				stringBuilder.append(pInlineConverter.convert(markdownString));
			}
		} else {
			stringBuilder.append(pMarkdownString.getContent());
		}
		return stringBuilder;
	}
}