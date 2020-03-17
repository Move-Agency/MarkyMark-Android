/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules;

import com.m2mobi.markymark.item.MarkdownItem;
import com.m2mobi.markymark.rules.RegexRule;
import com.m2mobi.markymark.rules.Rule;
import com.m2mobi.markymarkcommon.markdownitems.Header;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link Rule} that matches headers from H1 to H6
 */
public class HeaderRule extends RegexRule {

	/** Pattern used to find headers and the header size */
	private static final Pattern HEADER_PATTERN = Pattern.compile("^(#{1,6})\\s*(.+)$");

	@Override
	protected Pattern getRegex() {
		return HEADER_PATTERN;
	}

	@Override
	public MarkdownItem toMarkdownItem(final List<String> pMarkdownLines) {
		String headerContent = "";
		int headerType = 1;
		Matcher headerMatcher = HEADER_PATTERN.matcher(pMarkdownLines.get(0));
		if (headerMatcher.matches()) {
			headerType = headerMatcher.group(1).length();
			headerContent = headerMatcher.group(2);
		}
		return new Header(headerContent, headerType);
	}
}