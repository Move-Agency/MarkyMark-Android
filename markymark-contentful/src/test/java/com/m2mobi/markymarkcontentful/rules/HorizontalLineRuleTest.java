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
		List<String> markdown = MarkDownUtil.createSingleMarkDownString("---");
		Assert.assertEquals(true, new HorizontalLineRule().conforms(markdown));

		markdown = MarkDownUtil.createSingleMarkDownString("----");
		Assert.assertEquals(true, new HorizontalLineRule().conforms(markdown));
	}

	@Test
	public void shouldNotBeHorizontalRule() {
		List<String> markdown = MarkDownUtil.createSingleMarkDownString("--");
		Assert.assertEquals(false, new HorizontalLineRule().conforms(markdown));

		markdown = MarkDownUtil.createSingleMarkDownString("===");
		Assert.assertEquals(false, new HorizontalLineRule().conforms(markdown));

		markdown = MarkDownUtil.createSingleMarkDownString("===Hoi");
		Assert.assertEquals(false, new HorizontalLineRule().conforms(markdown));
	}

}
