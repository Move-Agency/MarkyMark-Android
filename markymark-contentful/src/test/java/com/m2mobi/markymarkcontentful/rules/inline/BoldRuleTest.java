/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules.inline;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for {@link BoldRule}
 */
public class BoldRuleTest {

	private BoldRule mBoldRule = null;

	@Before
	public void init() {
		mBoldRule = new BoldRule("_");
	}

	@Test
	public void shouldCreateBoldString() {
		assertEquals("text", mBoldRule.toMarkdownString("__text__").getContent());
	}

	@Test
	public void shouldNotCreateBoldString() {
		assertNotEquals("text", mBoldRule.toMarkdownString("$$text$$").getContent());
	}
}