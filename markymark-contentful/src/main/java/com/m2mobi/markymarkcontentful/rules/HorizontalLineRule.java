/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules;

import com.m2mobi.markymark.item.MarkDownItem;
import com.m2mobi.markymark.rules.RegexRule;
import com.m2mobi.markymarkcommon.markdownitems.HorizontalLine;

import java.util.List;
import java.util.regex.Pattern;

/**
 * {@link RegexRule} that matches horizontal lines
 */
public class HorizontalLineRule extends RegexRule {

	/** Pattern used to find horizontal lines */
	private static final Pattern HORIZONTAL_LINE_PATTERN = Pattern.compile("^-{3,}$");

	@Override
	protected Pattern getRegex() {
		return HORIZONTAL_LINE_PATTERN;
	}

	@Override
	public MarkDownItem toMarkDownItem(final List<String> pMarkDownLines) {
		return new HorizontalLine();
	}
}