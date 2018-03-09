/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link HeaderRule}
 */
public class HeaderRuleTest {

	private HeaderRule mHeaderRule = null;

	@Before
	public void init() {
		mHeaderRule = new HeaderRule();
	}

	@Test
	public void shouldBeHeader() {
		List<String> strings = MarkDownUtil.createSingleMarkDownString("# Header");
		assertEquals(true, mHeaderRule.conforms(strings));

		strings = MarkDownUtil.createSingleMarkDownString("## Header");
		assertEquals(true, mHeaderRule.conforms(strings));

		strings = MarkDownUtil.createSingleMarkDownString("### Header");
		assertEquals(true, mHeaderRule.conforms(strings));

		strings = MarkDownUtil.createSingleMarkDownString("#### Header");
		assertEquals(true, mHeaderRule.conforms(strings));

		strings = MarkDownUtil.createSingleMarkDownString("##### Header");
		assertEquals(true, mHeaderRule.conforms(strings));

		strings = MarkDownUtil.createSingleMarkDownString("###### Header");
		assertEquals(true, mHeaderRule.conforms(strings));
	}

	@Test
	public void isNotHeader() {
		List<String> strings = MarkDownUtil.createSingleMarkDownString("Header#");
		assertEquals(false, mHeaderRule.conforms(strings));

		strings = MarkDownUtil.createSingleMarkDownString("Header #");
		assertEquals(false, mHeaderRule.conforms(strings));

		strings = MarkDownUtil.createSingleMarkDownString("Header # Test");
		assertEquals(false, mHeaderRule.conforms(strings));
	}
}