/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcommon.markdownitems.inline;

import com.m2mobi.markymark.item.inline.MarkDownString;

/**
 * {@link MarkDownString} used for storing italic text
 */
public class ItalicString extends MarkDownString {

	public ItalicString(final String pContent, final boolean pCanHasChildItems) {
		super(pContent, pCanHasChildItems);
	}
}