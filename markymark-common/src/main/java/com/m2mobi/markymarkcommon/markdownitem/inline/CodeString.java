/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcommon.markdownitem.inline;

import com.m2mobi.markymark.item.inline.MarkdownString;

/**
 * {@link MarkdownString} used for storing inline code
 */
public class CodeString extends MarkdownString {

	public CodeString(final String pContent, final boolean pCanHaveChildItems) {
		super(pContent, pCanHaveChildItems);
	}
}