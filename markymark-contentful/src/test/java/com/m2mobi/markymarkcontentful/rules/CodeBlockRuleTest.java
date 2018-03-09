/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CodeBlockRule}
 */
public class CodeBlockRuleTest {

	private CodeBlockRule mCodeBlockRule = null;

	@Before
	public void init() {
		mCodeBlockRule = new CodeBlockRule();
	}

	@Test
	public void shouldConformToCodeBlock() {
		List<String> strings = MarkDownUtil.createMarkDownBlock("```", "code", "```");
		assertEquals(true, mCodeBlockRule.conforms(strings));
	}

	@Test
	public void shouldNotConformToCodeBlock() {
		List<String> strings = MarkDownUtil.createMarkDownBlock("***", "code", "***");
		assertEquals(false, mCodeBlockRule.conforms(strings));
	}
}