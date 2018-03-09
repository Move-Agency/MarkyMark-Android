/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcommon.markdownitems;

import com.m2mobi.markymark.item.inline.MarkDownString;
import com.m2mobi.markymarkcommon.markdownitems.inline.InlineString;

import java.util.ArrayList;
import java.util.List;

/**
 * Item in an {@link MarkDownList}
 */
public class ListItem {

	private MarkDownString mItemText;

	private List<MarkDownList> mMarkDownList = new ArrayList<>();

	public ListItem(final String pItemText) {
		mItemText = new InlineString(pItemText, true);
	}

	public boolean hasChild() {
		return mMarkDownList.size() > 0;
	}

	public void addChild(MarkDownList pMarkDownList) {
		mMarkDownList.add(pMarkDownList);
	}

	public List<MarkDownList> getChild() {
		return mMarkDownList;
	}

	public MarkDownString getText() {
		return mItemText;
	}

	@Override
	public String toString() {
		return mItemText.getContent();
	}
}