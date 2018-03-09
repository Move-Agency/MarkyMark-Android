/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules;

import com.m2mobi.markymarkcommon.markdownitems.MarkDownList;

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
		List<String> strings = MarkDownUtil.createSingleMarkDownString("1. Number 1");
		assertEquals(true, mListRule.conforms(strings));

		strings = MarkDownUtil.createSingleMarkDownString("- Number 1");
		assertEquals(true, mListRule.conforms(strings));
	}

	@Test
	public void shouldCreateOrderedList() {
		List<String> strings = MarkDownUtil.createMarkDownBlock("1. Number 1", "2. Number 2", "3. Number 3");
		assertEquals(true, mListRule.conforms(strings));

		assertEquals(true, ((MarkDownList) mListRule.toMarkDownItem(strings)).isOrdered());
	}

	@Test
	public void shouldCreateUnOrderedList() {
		List<String> strings = MarkDownUtil.createMarkDownBlock("- Number 1", "- Number 2", "- Number 3");
		assertEquals(true, mListRule.conforms(strings));

		assertEquals(false, ((MarkDownList) mListRule.toMarkDownItem(strings)).isOrdered());
	}

	@Test
	public void shouldCreateCorrectLengthList() {
		List<String> strings = MarkDownUtil.createMarkDownBlock("- Number 1", "- Number 2", "- Number 3");
		assertEquals(true, mListRule.conforms(strings));

		assertEquals(3, ((MarkDownList) mListRule.toMarkDownItem(strings)).getListItems().size());
	}

	@Test
	public void shouldCreateNestedList() {
		List<String> strings = MarkDownUtil.createMarkDownBlock("- Number 1", "- Number 2", "  - Nest 1", "  - Nest 2");
		assertEquals(true, mListRule.conforms(strings));

		assertEquals(true, ((MarkDownList) mListRule.toMarkDownItem(strings)).getLastListItem().hasChild());
	}

	@Test
	public void shouldNotCreateNestedList() {
		List<String> strings = MarkDownUtil.createMarkDownBlock("- Number 1", "- Number 2");
		assertEquals(true, mListRule.conforms(strings));

		assertEquals(false, ((MarkDownList) mListRule.toMarkDownItem(strings)).getLastListItem().hasChild());
	}

	@Test
	public void shouldCreateNestedOrderedList() {
		List<String> strings = MarkDownUtil.createMarkDownBlock("- Number 1", "- Number 2", "  1. Nest 1", "  2. Nest 2");
		assertEquals(true, mListRule.conforms(strings));

		assertEquals(true, ((MarkDownList) mListRule.toMarkDownItem(strings)).getLastListItem().getChild().get(0).isOrdered());
	}

	@Test
	public void shouldCreateNestedUnorderedList() {
		List<String> strings = MarkDownUtil.createMarkDownBlock("- Number 1", "- Number 2", "  - Nest 1", "  - Nest 2");
		assertEquals(true, mListRule.conforms(strings));

		assertEquals(false, ((MarkDownList) mListRule.toMarkDownItem(strings)).getLastListItem().getChild().get(0).isOrdered());
	}

	@Test
	public void shouldNotConformToList() {
		List<String> strings = MarkDownUtil.createSingleMarkDownString("#. Number 1");
		assertEquals(false, mListRule.conforms(strings));
	}
}