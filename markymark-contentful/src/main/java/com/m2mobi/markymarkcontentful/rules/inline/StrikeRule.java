/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules.inline;

import com.m2mobi.markymark.item.inline.MarkDownString;
import com.m2mobi.markymark.rules.InlineRule;
import com.m2mobi.markymarkcommon.markdownitems.inline.StrikeString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link InlineRule} that matches strike through text
 */
public class StrikeRule implements InlineRule {

	@Override
	public Pattern getRegex() {
		return Pattern.compile("~{2}(.+?)~{2}");
	}

	@Override
	public MarkDownString toMarkDownString(final String pContent) {
		Matcher matcher = getRegex().matcher(pContent);
		String content = "";
		if (matcher.find()) {
			content = matcher.group(1);
		}
		return new StrikeString(content, true);
	}
}