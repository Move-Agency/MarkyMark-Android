/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcommon.markdownitem;

import com.m2mobi.markymark.item.MarkdownItem;
import com.m2mobi.markymark.item.inline.MarkdownString;
import com.m2mobi.markymarkcommon.markdownitem.inline.InlineString;

/**
 * {@link MarkdownItem} used for storing text inside a paragraph
 */
public class Paragraph implements MarkdownItem {

	private InlineString mContent;

	public Paragraph(final InlineString pContent) {
		mContent = pContent;
	}

	public MarkdownString getContent() {
		return mContent;
	}
}