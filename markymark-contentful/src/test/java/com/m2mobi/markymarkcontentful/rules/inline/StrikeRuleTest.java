/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules.inline;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for {@link StrikeRule}
 */
public class StrikeRuleTest {

	private StrikeRule mStrikeRule = null;

	@Before
	public void init() {
		mStrikeRule = new StrikeRule();
	}

	@Test
	public void shouldCreateStrikeString() {
		assertEquals("text", mStrikeRule.toMarkDownString("~~text~~").getContent());
	}

	@Test
	public void shouldNotCreateStrikeString() {
		assertNotEquals("text", mStrikeRule.toMarkDownString("$$text$$").getContent());
	}
}