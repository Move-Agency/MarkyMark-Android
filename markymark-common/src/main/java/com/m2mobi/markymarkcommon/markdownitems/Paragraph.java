/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcommon.markdownitems;

import com.m2mobi.markymark.item.MarkDownItem;
import com.m2mobi.markymark.item.inline.MarkDownString;
import com.m2mobi.markymarkcommon.markdownitems.inline.InlineString;

/**
 * {@link MarkDownItem} used for storing text inside a paragraph
 */
public class Paragraph implements MarkDownItem {

	private InlineString mContent;

	public Paragraph(final InlineString pContent) {
		mContent = pContent;
	}

	public MarkDownString getContent() {
		return mContent;
	}
}