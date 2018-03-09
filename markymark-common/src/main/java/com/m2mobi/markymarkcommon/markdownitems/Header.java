/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcommon.markdownitems;

import com.m2mobi.markymark.item.MarkDownItem;
import com.m2mobi.markymark.item.inline.MarkDownString;
import com.m2mobi.markymarkcommon.markdownitems.inline.InlineString;

/**
 * {@link MarkDownItem} used for storing text inside a header
 */
public class Header implements MarkDownItem {

	private final MarkDownString mContent;

	private final int mHeader;

	public Header(final String pContent, final int pHeader) {
		mContent = new InlineString(pContent, true);
		mHeader = pHeader;
	}

	public MarkDownString getContent() {
		return mContent;
	}

	public int getHeader() {
		return mHeader;
	}
}