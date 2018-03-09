/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcommon.markdownitems.inline;

import com.m2mobi.markymark.item.inline.MarkDownString;

/**
 * {@link MarkDownString} used for storing inline code
 */
public class CodeString extends MarkDownString {

	public CodeString(final String pContent, final boolean pCanHaveChildItems) {
		super(pContent, pCanHaveChildItems);
	}
}