/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymark;

import com.m2mobi.markymark.item.inline.MarkDownString;
import com.m2mobi.markymark.rules.InlineRule;

/**
 * Interface used to create an {@link T} using the MarkDownString created by a {@link InlineRule}
 */
public interface InlineDisplayItem<T, O extends MarkDownString> {

	/**
	 * Creates a {@link T} from a MarkDownItem ({@link O}) e.g. (Create a Spannable(T) from a BoldString(O))
	 *
	 * @param pInlineConverter
	 * 		Converter used to convert possible child items
	 * @param pMarkDownString
	 * 		The MarkdownItem used to create a {@link T}
	 * @return Creates a {@link T} using the MarkDownString
	 */
	T create(InlineConverter<T> pInlineConverter, O pMarkDownString);
}