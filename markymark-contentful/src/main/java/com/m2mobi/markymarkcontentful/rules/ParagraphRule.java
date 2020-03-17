/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules;

import com.m2mobi.markymark.item.MarkdownItem;
import com.m2mobi.markymark.rules.Rule;
import com.m2mobi.markymarkcommon.markdownitems.Paragraph;
import com.m2mobi.markymarkcommon.markdownitems.inline.InlineString;

import java.util.List;

/**
 * {@link Rule} that matches paragraphs
 */
public class ParagraphRule implements Rule {

	@Override
	public boolean conforms(final List<String> pMarkdownLines) {
		return true;
	}

	@Override
	public int getLinesConsumed() {
		return 1;
	}

	@Override
	public MarkdownItem toMarkdownItem(final List<String> pMarkdownLines) {
		String line = pMarkdownLines.get(0);
		return new Paragraph(new InlineString(line, true));
	}
}