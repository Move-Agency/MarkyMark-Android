/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcommon.markdownitems.inline;

import com.m2mobi.markymark.item.inline.MarkDownString;

/**
 * {@link MarkDownString} used for storing bold text
 */
public class BoldString extends MarkDownString {

	public BoldString(final String pContent, final boolean pCanHasChildItems) {
		super(pContent, pCanHasChildItems);
	}
}