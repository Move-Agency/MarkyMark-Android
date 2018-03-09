/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules.inline;

import com.m2mobi.markymarkcommon.markdownitems.inline.LinkString;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for {@link LinkRule}
 */
public class LinkRuleTest {

	private static final String mUrl = "[Link](www.google.com)";

	private LinkRule mLinkRule = null;

	@Before
	public void init() {
		mLinkRule = new LinkRule();
	}

	@Test
	public void shouldCreateLinkString() {
		assertEquals("Link", mLinkRule.toMarkDownString(mUrl).getContent());
	}

	@Test
	public void shouldCreateLinkUrl() {
		assertEquals("www.google.com", ((LinkString) mLinkRule.toMarkDownString(mUrl)).getUrl());
	}

	@Test
	public void shouldNotCreateLinString() {
		assertNotEquals("Link", mLinkRule.toMarkDownString("www.google.com").getContent());
	}
}