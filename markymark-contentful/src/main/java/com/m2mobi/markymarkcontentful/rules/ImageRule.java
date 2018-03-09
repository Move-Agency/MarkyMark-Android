/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules;

import com.m2mobi.markymark.item.MarkDownItem;
import com.m2mobi.markymark.rules.RegexRule;
import com.m2mobi.markymarkcommon.markdownitems.Image;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link RegexRule} that matches image tags
 */
public class ImageRule extends RegexRule {

	/** Regex to match an image */
	public static final Pattern IMAGE_PATTERN = Pattern.compile("(!\\p{Z}?)\\[(.+?)\\]\\((.+?)\\)");

	@Override
	protected Pattern getRegex() {
		return IMAGE_PATTERN;
	}

	@Override
	public MarkDownItem toMarkDownItem(final List<String> pMarkDownLines) {
		final Matcher matcher = IMAGE_PATTERN.matcher(pMarkDownLines.get(0));
		if (matcher.find()) {
			return new Image(matcher.group(3), matcher.group(2));
		}
		return new Image("", "");
	}
}