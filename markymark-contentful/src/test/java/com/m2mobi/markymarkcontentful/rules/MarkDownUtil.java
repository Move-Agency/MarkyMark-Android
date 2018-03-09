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
class MarkDownUtil {

	/**
	 * Create a list with a single {@link String} Containing markdown text
	 *
	 * @param pMarkDown
	 * 		the Markdown text
	 * @return List with one item
	 */
	static List<String> createSingleMarkDownString(final String pMarkDown) {
		List<String> strings = new ArrayList<>();
		strings.add(pMarkDown);
		return strings;
	}

	/**
	 * Create a list with multiple {@link String}s Containing markdown text
	 *
	 * @param pMarkdownStrings
	 * 		multiple strings containing Markdown text
	 * @return List filled with the argument strings
	 */
	static List<String> createMarkDownBlock(final String... pMarkdownStrings) {
		List<String> strings = new ArrayList<>();
		strings.addAll(Arrays.asList(pMarkdownStrings));
		return strings;
	}
}