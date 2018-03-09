/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymark;

import com.m2mobi.markymark.item.MarkDownItem;
import com.m2mobi.markymark.rules.Rule;

/**
 * Interface used to create an {@link T} using the MarkDownItem created by a {@link Rule}
 */
public interface DisplayItem<T, O extends MarkDownItem, INLINE> {

	/**
	 * Creates a {@link T} from a MarkDownItem ({@link O}) e.g. (Create a TextView(T) from a HeaderItem (O))
	 *
	 * @param pMarkDownItem
	 * 		The MarkdownItem used to create a {@link T}
	 * @param pInlineConverter
	 * @return Creates a {@link T} using the MarkDownItem
	 */
	T create(O pMarkDownItem, InlineConverter<INLINE> pInlineConverter);
}