/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules;

import com.m2mobi.markymarkcommon.markdownitems.MarkdownList;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ListRule}
 */
public class ListRuleTest {

	private ListRule mListRule = null;

	@Before
	public void init() {
		mListRule = new ListRule();
	}

	@Test
	public void shouldConformToList() {
		List<String> strings = MarkdownUtil.createSingleMarkdownString("1. Number 1");
		assertEquals(true, mListRule.conforms(strings));

		strings = MarkdownUtil.createSingleMarkdownString("- Number 1");
		assertEquals(true, mListRule.conforms(strings));
	}

	@Test
	public void shouldCreateOrderedList() {
		List<String> strings = MarkdownUtil.createMarkdownBlock("1. Number 1", "2. Number 2", "3. Number 3");
		assertEquals(true, mListRule.conforms(strings));

		assertEquals(true, ((MarkdownList) mListRule.toMarkdownItem(strings)).isOrdered());
	}

	@Test
	public void shouldCreateUnOrderedList() {
		List<String> strings = MarkdownUtil.createMarkdownBlock("- Number 1", "- Number 2", "- Number 3");
		assertEquals(true, mListRule.conforms(strings));

		assertEquals(false, ((MarkdownList) mListRule.toMarkdownItem(strings)).isOrdered());
	}

	@Test
	public void shouldCreateCorrectLengthList() {
		List<String> strings = MarkdownUtil.createMarkdownBlock("- Number 1", "- Number 2", "- Number 3");
		assertEquals(true, mListRule.conforms(strings));

		assertEquals(3, ((MarkdownList) mListRule.toMarkdownItem(strings)).getListItems().size());
	}

	@Test
	public void shouldCreateNestedList() {
		List<String> strings = MarkdownUtil.createMarkdownBlock("- Number 1", "- Number 2", "  - Nest 1", "  - Nest 2");
		assertEquals(true, mListRule.conforms(strings));

		assertEquals(true, ((MarkdownList) mListRule.toMarkdownItem(strings)).getLastListItem().hasChild());
	}

	@Test
	public void shouldNotCreateNestedList() {
		List<String> strings = MarkdownUtil.createMarkdownBlock("- Number 1", "- Number 2");
		assertEquals(true, mListRule.conforms(strings));

		assertEquals(false, ((MarkdownList) mListRule.toMarkdownItem(strings)).getLastListItem().hasChild());
	}

	@Test
	public void shouldCreateNestedOrderedList() {
		List<String> strings = MarkdownUtil.createMarkdownBlock("- Number 1", "- Number 2", "  1. Nest 1", "  2. Nest 2");
		assertEquals(true, mListRule.conforms(strings));

		assertEquals(true, ((MarkdownList) mListRule.toMarkdownItem(strings)).getLastListItem().getChild().get(0).isOrdered());
	}

	@Test
	public void shouldCreateNestedUnorderedList() {
		List<String> strings = MarkdownUtil.createMarkdownBlock("- Number 1", "- Number 2", "  - Nest 1", "  - Nest 2");
		assertEquals(true, mListRule.conforms(strings));

		assertEquals(false, ((MarkdownList) mListRule.toMarkdownItem(strings)).getLastListItem().getChild().get(0).isOrdered());
	}

	@Test
	public void shouldNotConformToList() {
		List<String> strings = MarkdownUtil.createSingleMarkdownString("#. Number 1");
		assertEquals(false, mListRule.conforms(strings));
	}
}