/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid.inline;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymark.item.inline.MarkDownString;

/**
 * Utility methods related to {@link Spannable}s
 */
class SpannableUtils {

	private SpannableUtils() {
		// no instances
	}

	/**
	 * Creates a Spannable from a MarkDownString
	 *
	 * @param pInlineConverter
	 * 		InlineConverter used to convert {@link MarkDownString} to spannable
	 * @param pMarkDownString
	 * 		MarkDownString used to create the spannables
	 * @return Returns a Spannable
	 */
	static Spannable createSpannable(final InlineConverter<Spanned> pInlineConverter, final MarkDownString pMarkDownString) {
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
		if (pMarkDownString.hasChildItems()) {
			for (MarkDownString markDownString : pInlineConverter.parseContent(pMarkDownString.getContent())) {
				stringBuilder.append(pInlineConverter.convert(markDownString));
			}
		} else {
			stringBuilder.append(pMarkDownString.getContent());
		}
		return stringBuilder;
	}
}