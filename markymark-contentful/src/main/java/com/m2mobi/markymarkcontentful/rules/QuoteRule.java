/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules;

import com.m2mobi.markymark.item.MarkdownItem;
import com.m2mobi.markymark.rules.Rule;
import com.m2mobi.markymarkcommon.markdownitems.QuoteBlock;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link Rule} that matches quotes
 */
public class QuoteRule implements Rule {

	/** Pattern used to find quotes */
	private static final Pattern QUOTE_PATTERN = Pattern.compile("(^>+)(.*?)$");

	@Override
	public boolean conforms(final List<String> pMarkdownLines) {
		return QUOTE_PATTERN.matcher(pMarkdownLines.get(0)).matches();
	}

	@Override
	public int getLinesConsumed() {
		return 1;
	}

	@Override
	public MarkdownItem toMarkdownItem(final List<String> pMarkdownLines) {
		final Matcher matcher = QUOTE_PATTERN.matcher(pMarkdownLines.get(0));
		if (matcher.find()) {
			return new QuoteBlock(matcher.group(2));
		}
		return new QuoteBlock("");
	}
}