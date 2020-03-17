/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules.inline;

import com.m2mobi.markymark.item.inline.MarkdownString;
import com.m2mobi.markymark.rules.InlineRule;
import com.m2mobi.markymarkcommon.markdownitems.inline.ItalicString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link InlineRule} that matches italic text
 */
public class ItalicRule implements InlineRule {

	@Override
	public Pattern getRegex() {
		return Pattern.compile("(?<!\\*)(\\*{1})(?!\\*)(.+?)(?<!\\*)(\\*{1})(?!\\*)");
	}

	@Override
	public MarkdownString toMarkdownString(String pContent) {
		Matcher matcher = getRegex().matcher(pContent);
		String content = "";
		if (matcher.find()) {
			content = matcher.group(2);
		}
		return new ItalicString(content, true);
	}
}