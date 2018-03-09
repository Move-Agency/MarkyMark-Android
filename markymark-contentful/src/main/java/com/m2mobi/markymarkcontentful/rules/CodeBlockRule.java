/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules;

import com.m2mobi.markymark.item.MarkDownItem;
import com.m2mobi.markymark.rules.Rule;
import com.m2mobi.markymarkcommon.markdownitems.CodeBlock;

import java.util.List;
import java.util.regex.Pattern;

/**
 * {@link Rule} that matches code blocks
 */
public class CodeBlockRule implements Rule {

	/** Pattern used to find the start and end of the code block */
	private static final Pattern CODE_PATTERN_START = Pattern.compile("^`{3}");

	/** Pattern that indicates the code block is a single line */
	private static final Pattern CODE_PATTERN_END_SINGLE_LINE = Pattern.compile("(?<!^)(\\`{3})$");

	/** Pattern that ends the code block */
	private static final Pattern CODE_PATTERN_END = Pattern.compile("(\\`{3})$");

	/** The amount of lines used by this code block */
	private int mLinesConsumed;

	@Override
	public boolean conforms(final List<String> pMarkDownLines) {
		if (!CODE_PATTERN_START.matcher(pMarkDownLines.get(0)).matches()) {
			return false;
		}
		mLinesConsumed = 0;
		for (String line : pMarkDownLines) {
			mLinesConsumed += 1;
			if (mLinesConsumed == 1) {
				if (CODE_PATTERN_END_SINGLE_LINE.matcher(line).matches()) {
					return true;
				}
			} else {
				if (CODE_PATTERN_END.matcher(line).matches()) {
					return true;
				}
			}
		}
		return true;
	}

	@Override
	public int getLinesConsumed() {
		return mLinesConsumed;
	}

	@Override
	public MarkDownItem toMarkDownItem(final List<String> pMarkDownLines) {
		StringBuilder sb = new StringBuilder();
		for (String line : pMarkDownLines) {
			sb.append(line);
			sb.append("\n");
		}

		String content = sb.toString();
		content = content.replaceAll("\n```", "");
		content = content.replaceAll("```\n", "");
		content = content.replaceAll("```", "");

		return new CodeBlock(content);
	}
}