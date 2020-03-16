/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules.inline;

import com.m2mobi.markymark.item.inline.MarkdownString;
import com.m2mobi.markymark.rules.InlineRule;
import com.m2mobi.markymarkcommon.markdownitems.inline.CodeString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link InlineRule} that matches inline text
 */
public class InlineCodeRule implements InlineRule {

	@Override
	public Pattern getRegex() {
		return Pattern.compile("`(.*?)`");
	}

	@Override
	public MarkdownString toMarkdownString(final String content) {
		final Matcher matcher = getRegex().matcher(content);
		if (matcher.find()) {
			return new CodeString(matcher.group(1), false);
		}
		return new CodeString("", false);
	}
}