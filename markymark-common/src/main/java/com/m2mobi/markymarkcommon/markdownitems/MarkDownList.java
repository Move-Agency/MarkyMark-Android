/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcommon.markdownitems;

import com.m2mobi.markymark.item.MarkDownItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link MarkDownItem} used for storing {@link ListItem} for a list
 */
public class MarkDownList implements MarkDownItem {

	private final boolean mIsOrdered;

	private List<ListItem> mListItems = new ArrayList<>();

	private int mNestedLevel;

	public MarkDownList(final boolean pIsOrdered, final int pNestedLevel) {
		mIsOrdered = pIsOrdered;
		mNestedLevel = pNestedLevel;
	}

	public void addListItem(final ListItem pListItem) {
		mListItems.add(pListItem);
	}

	public List<ListItem> getListItems() {
		return mListItems;
	}

	public boolean isOrdered() {
		return mIsOrdered;
	}

	public int getNestedLevel() {
		return mNestedLevel;
	}

	public ListItem getLastListItem() {
		return mListItems.get(mListItems.size() - 1);
	}
}