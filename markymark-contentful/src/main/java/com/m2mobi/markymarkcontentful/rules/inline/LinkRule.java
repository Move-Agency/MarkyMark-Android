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
		return Pattern.compile("(\\[(.*)\\]\\((.*)\\))");
	}

	@Override
	public MarkdownString toMarkdownString(final String pContent) {
		Matcher matcher = getRegex().matcher(pContent);
		if (matcher.find()) {
			System.out.println("Link content: " + matcher.group(2) + " url " + matcher.group(3));
			return new LinkString(matcher.group(2), matcher.group(3), true);
		}

		return new LinkString("", "", true);
	}
}