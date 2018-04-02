/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcommon.markdownitems.inline;

import com.m2mobi.markymark.item.inline.MarkdownString;

/**
 * {@link MarkdownString} used for storing strike through text
 */
public class StrikeString extends MarkdownString {

	public StrikeString(final String pContent, final boolean pCanHasChildItems) {
		super(pContent, pCanHasChildItems);
	}
}