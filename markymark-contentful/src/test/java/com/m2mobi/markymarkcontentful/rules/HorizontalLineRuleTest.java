/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Tests for {@link HorizontalLineRule}
 */
public class HorizontalLineRuleTest {

	@Test
	public void shouldBeHorizontalRule() {
		List<String> markdown = MarkdownUtil.createSingleMarkdownString("---");
		Assert.assertEquals(true, new HorizontalLineRule().conforms(markdown));

		markdown = MarkdownUtil.createSingleMarkdownString("----");
		Assert.assertEquals(true, new HorizontalLineRule().conforms(markdown));
	}

	@Test
	public void shouldNotBeHorizontalRule() {
		List<String> markdown = MarkdownUtil.createSingleMarkdownString("--");
		Assert.assertEquals(false, new HorizontalLineRule().conforms(markdown));

		markdown = MarkdownUtil.createSingleMarkdownString("===");
		Assert.assertEquals(false, new HorizontalLineRule().conforms(markdown));

		markdown = MarkdownUtil.createSingleMarkdownString("===Hoi");
		Assert.assertEquals(false, new HorizontalLineRule().conforms(markdown));
	}

}
