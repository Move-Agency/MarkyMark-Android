/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test utils to create lists of Markdown strings
 */
class MarkdownUtil {

	/**
	 * Create a list with a single {@link String} Containing markdown text
	 *
	 * @param pMarkdown
	 * 		the Markdown text
	 * @return List with one item
	 */
	static List<String> createSingleMarkdownString(final String pMarkdown) {
		List<String> strings = new ArrayList<>();
		strings.add(pMarkdown);
		return strings;
	}

	/**
	 * Create a list with multiple {@link String}s Containing markdown text
	 *
	 * @param pMarkdownStrings
	 * 		multiple strings containing Markdown text
	 * @return List filled with the argument strings
	 */
	static List<String> createMarkdownBlock(final String... pMarkdownStrings) {
		List<String> strings = new ArrayList<>();
		strings.addAll(Arrays.asList(pMarkdownStrings));
		return strings;
	}
}