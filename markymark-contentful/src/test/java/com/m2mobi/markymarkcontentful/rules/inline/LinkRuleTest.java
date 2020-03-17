/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 M2mobi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.m2mobi.markymarkcontentful.rules.inline;

import com.m2mobi.markymarkcommon.markdownitems.inline.LinkString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for {@link LinkRule}
 */
public class LinkRuleTest {

	private static final String mUrl = "[Link](www.google.com)";

	private LinkRule mLinkRule = null;

	@BeforeEach
	public void init() {
		mLinkRule = new LinkRule();
	}

	@Test
	public void shouldCreateLinkString() {
		assertEquals("Link", mLinkRule.toMarkdownString(mUrl).getContent());
	}

	@Test
	public void shouldCreateLinkUrl() {
		assertEquals("www.google.com", ((LinkString) mLinkRule.toMarkdownString(mUrl)).getUrl());
	}

	@Test
	public void shouldNotCreateLinString() {
		assertNotEquals("Link", mLinkRule.toMarkdownString("www.google.com").getContent());
	}
}