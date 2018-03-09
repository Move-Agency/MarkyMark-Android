/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymark.item.inline;

/**
 * Wrapper containing content of a Markdown string
 */
public abstract class MarkDownString {

	/** String containing markdown */
	private String mContent;

	/** Boolean indicating if this item can have other markdown items */
	private boolean mCanHaveChildItems;

	public MarkDownString(final String pContent, final boolean pCanHaveChildItems) {
		mCanHaveChildItems = pCanHaveChildItems;
		mContent = pContent;
	}

	public boolean hasChildItems() {
		return mCanHaveChildItems;
	}

	public String getContent() {
		return mContent;
	}
}