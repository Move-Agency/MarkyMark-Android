/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link QuoteRule}
 */
public class QuoteRuleTest {

	private QuoteRule mQuoteRule = null;

	@Before
	public void init() {
		mQuoteRule = new QuoteRule();
	}

	@Test
	public void shouldBeQuote() {
		List<String> strings = MarkdownUtil.createSingleMarkdownString("> Quote");
		assertEquals(true, mQuoteRule.conforms(strings));
	}

	@Test
	public void shouldNotBeQuote() {
		List<String> strings = MarkdownUtil.createSingleMarkdownString("< Quote");
		assertEquals(false, mQuoteRule.conforms(strings));
	}
}