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

package com.m2mobi.markymarkcontentful;

import com.m2mobi.markymark.Flavor;
import com.m2mobi.markymark.rules.InlineRule;
import com.m2mobi.markymark.rules.Rule;
import com.m2mobi.markymarkcontentful.rules.CodeBlockRule;
import com.m2mobi.markymarkcontentful.rules.HeaderRule;
import com.m2mobi.markymarkcontentful.rules.HorizontalLineRule;
import com.m2mobi.markymarkcontentful.rules.ImageRule;
import com.m2mobi.markymarkcontentful.rules.ListRule;
import com.m2mobi.markymarkcontentful.rules.ParagraphRule;
import com.m2mobi.markymarkcontentful.rules.QuoteRule;
import com.m2mobi.markymarkcontentful.rules.inline.BoldRule;
import com.m2mobi.markymarkcontentful.rules.inline.InlineCodeRule;
import com.m2mobi.markymarkcontentful.rules.inline.ItalicRule;
import com.m2mobi.markymarkcontentful.rules.inline.LinkRule;
import com.m2mobi.markymarkcontentful.rules.inline.StrikeRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Contentful markdown flavor
 */
public class ContentfulFlavor implements Flavor {

	@Override
	public List<Rule> getRules() {
		List<Rule> rules = new ArrayList<>();
		rules.add(new HeaderRule());
		rules.add(new HorizontalLineRule());
		rules.add(new QuoteRule());
		rules.add(new ListRule());
		rules.add(new CodeBlockRule());
		rules.add(new ImageRule());
		return rules;
	}

	@Override
	public List<InlineRule> getInlineRules() {
		List<InlineRule> rules = new ArrayList<>();
		rules.add(new StrikeRule());
		rules.add(new ItalicRule());
		rules.add(new BoldRule("*"));
		rules.add(new BoldRule("_"));
		rules.add(LinkRule.INSTANCE);
		rules.add(new InlineCodeRule());
		return rules;
	}

	@Override
	public Rule getDefaultRule() {
		return new ParagraphRule();
	}
}