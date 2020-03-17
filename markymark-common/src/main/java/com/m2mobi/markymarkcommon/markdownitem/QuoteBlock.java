/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcommon.markdownitem;

import com.m2mobi.markymark.item.MarkdownItem;
import com.m2mobi.markymark.item.inline.MarkdownString;
import com.m2mobi.markymarkcommon.markdownitem.inline.InlineString;

/**
 * {@link MarkdownItem} used for storing text inside a quote block
 */
public class QuoteBlock implements MarkdownItem {

	private final String mContent;

	public QuoteBlock(final String pContent) {
		mContent = pContent;
	}

	public MarkdownString getContent() {
		return new InlineString(mContent, true);
	}
}