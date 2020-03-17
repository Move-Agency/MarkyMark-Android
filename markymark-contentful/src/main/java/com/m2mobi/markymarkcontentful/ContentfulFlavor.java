/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful;

import com.m2mobi.markymark.Flavor;
import com.m2mobi.markymark.rules.InlineRule;
import com.m2mobi.markymark.rules.Rule;
import com.m2mobi.markymarkcommon.rule.CodeBlockRule;
import com.m2mobi.markymarkcommon.rule.HeaderRule;
import com.m2mobi.markymarkcommon.rule.HorizontalLineRule;
import com.m2mobi.markymarkcommon.rule.ImageRule;
import com.m2mobi.markymarkcommon.rule.ListRule;
import com.m2mobi.markymarkcommon.rule.ParagraphRule;
import com.m2mobi.markymarkcommon.rule.QuoteRule;
import com.m2mobi.markymarkcommon.rule.inline.BoldRule;
import com.m2mobi.markymarkcommon.rule.inline.InlineCodeRule;
import com.m2mobi.markymarkcommon.rule.inline.ItalicRule;
import com.m2mobi.markymarkcommon.rule.inline.LinkRule;
import com.m2mobi.markymarkcommon.rule.inline.StrikeRule;

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
		rules.add(new LinkRule());
		rules.add(new InlineCodeRule());
		return rules;
	}

	@Override
	public Rule getDefaultRule() {
		return new ParagraphRule();
	}
}