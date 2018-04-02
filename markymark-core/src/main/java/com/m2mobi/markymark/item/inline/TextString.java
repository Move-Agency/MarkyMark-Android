/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymark.item.inline;

/**
 * {@link MarkdownString} implementation used for text
 */
public class TextString extends MarkdownString {

	public TextString(final String pContent, final boolean pCanHasChildItems) {
		super(pContent, pCanHasChildItems);
	}
}