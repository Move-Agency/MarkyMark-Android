/*
 * Copyright (C) M2mobi BV - All Rights Reserved
 */

package com.m2mobi.markymarkcontentful.rules.inline;

import com.m2mobi.markymarkcommon.markdownitems.inline.ImageString;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link BoldRule}
 */
public class InlineImageRuleTest {

    private InlineImageRule mInlineImageRule = null;

    @Before
    public void init() {
        mInlineImageRule = new InlineImageRule();
    }

    @Test
    public void shouldCreateInlineImageString() {
        assertThat(mInlineImageRule.toMarkDownString("![alt_text](image_url)"), instanceOf(ImageString.class));
    }

    @Test
    public void urlShouldBeSet() {
        assertThat(((ImageString) mInlineImageRule.toMarkDownString("![alt_text](image_url)")).getImageUrl(), is("image_url"));
    }

    @Test
    public void altTextShouldBeSet() {
        assertThat(((ImageString) mInlineImageRule.toMarkDownString("![alt_text](image_url)")).getAltText(), is("alt_text"));
    }
}