/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ImageRule}
 */
public class ImageRuleTest {

	private ImageRule mImageRule = null;

	@Before
	public void init() {
		mImageRule = new ImageRule();
	}

	@Test
	public void shouldBeImage() {
		List<String> strings = MarkdownUtil.createSingleMarkdownString("![Image](www.google.com/images/cheese)");
		assertEquals(true, mImageRule.conforms(strings));
	}

	@Test
	public void shouldNotBeImage() {
		List<String> strings = MarkdownUtil.createSingleMarkdownString("[Image](www.google.com/images/cheese)");
		assertEquals(false, mImageRule.conforms(strings));
	}
}