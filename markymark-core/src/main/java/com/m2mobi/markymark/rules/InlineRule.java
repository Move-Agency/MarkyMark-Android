/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymark.rules;

import com.m2mobi.markymark.item.inline.MarkDownString;

import java.util.regex.Pattern;

/**
 * Rule for inline Markdown
 */
public interface InlineRule {

	Pattern getRegex();

	MarkDownString toMarkDownString(String pContent);
}