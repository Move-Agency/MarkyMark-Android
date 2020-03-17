/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcommon.markdownitems;

import com.m2mobi.markymark.item.MarkdownItem;

/**
 * {@link MarkdownItem} used for storing text inside a code block
 */
public class CodeBlock implements MarkdownItem {

	/** List of code lines */
	private final String mContent;

	public CodeBlock(final String pContent) {
		mContent = pContent;
	}

	public String getContent() {
		return mContent;
	}
}