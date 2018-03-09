/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules.inline;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for {@link InlineCodeRule}
 */
public class InlineCodeRuleTest {

	private InlineCodeRule mInlineCodeRule = null;

	@Before
	public void init() {
		mInlineCodeRule = new InlineCodeRule();
	}

	@Test
	public void shouldCreateInlineCodeString() {
		assertEquals("text", mInlineCodeRule.toMarkDownString("`text`").getContent());
	}

	@Test
	public void shouldNotCreateInlineCodeString() {
		assertNotEquals("text", mInlineCodeRule.toMarkDownString("$$text$$").getContent());
	}
}