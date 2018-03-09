/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules.inline;

import com.m2mobi.markymark.item.inline.MarkDownString;
import com.m2mobi.markymark.rules.InlineRule;
import com.m2mobi.markymarkcommon.markdownitems.inline.BoldString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link InlineRule} that matches bold text
 */
public class BoldRule implements InlineRule {

	/** Regex pattern used to match bold strings */
	private final Pattern mBoldPattern;

	/**
	 * Creates a new BoldRule that matches on the given pattern e.g. ('*' or '_')
	 *
	 * @param pPattern
	 * 		The pattern to use
	 */
	public BoldRule(final String pPattern) {
		final String escaped = Pattern.quote(pPattern);
		mBoldPattern = Pattern.compile("(" + escaped + "{2})(.+?)(" + escaped + "{2})(?!" + escaped + ")");
	}

	@Override
	public Pattern getRegex() {
		return mBoldPattern;
	}

	@Override
	public MarkDownString toMarkDownString(String pContent) {
		Matcher matcher = getRegex().matcher(pContent);
		String content = "";
		if (matcher.find()) {
			content = matcher.group(2);
		}
		return new BoldString(content, true);
	}
}