/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules.inline;

import com.m2mobi.markymark.item.inline.MarkdownString;
import com.m2mobi.markymark.rules.InlineRule;
import com.m2mobi.markymarkcommon.markdownitems.inline.LinkString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link InlineRule} that matches links
 */
public class LinkRule implements InlineRule {

	@Override
	public Pattern getRegex() {
		// Ignores links that start with '!'
		return Pattern.compile("(?<!!\\p{Z}{0,1})\\[{1}(.+?)\\]\\({1}(.+?)\\)");
	}

	@Override
	public MarkdownString toMarkdownString(final String pContent) {
		Matcher matcher = getRegex().matcher(pContent);
		if (matcher.find()) {
			return new LinkString(matcher.group(1), matcher.group(2), false);
		}

		return new LinkString("", "", false);
	}
}