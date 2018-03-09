/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules.inline;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for {@link ItalicRule}
 */
public class ItalicRuleTest {

	private ItalicRule mItalicRule = null;

	@Before
	public void init() {
		mItalicRule = new ItalicRule();
	}

	@Test
	public void shouldCreateItalicString() {
		assertEquals("text", mItalicRule.toMarkDownString("*text*").getContent());
	}

	@Test
	public void shouldNotCreateItalicString() {
		assertNotEquals("text", mItalicRule.toMarkDownString("$$text$$").getContent());
	}
}