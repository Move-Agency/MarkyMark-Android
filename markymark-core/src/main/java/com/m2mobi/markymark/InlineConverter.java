/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymark;

import com.m2mobi.markymark.item.inline.MarkDownString;
import com.m2mobi.markymark.item.inline.TextString;
import com.m2mobi.markymark.rules.InlineRule;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Converter for inline markdown items to {@link T}
 */
public class InlineConverter<T> {

	/** HashMap that maps MarkDownString items to display items */
	private final Map<Class<? extends MarkDownString>, InlineDisplayItem<T, MarkDownString>> mMapping = new HashMap<>();

	/** List of InlineRules used to parse MarkDownStrings */
	private List<InlineRule> mInlineRules;

	/**
	 * Sets the inline rules used to parse MarkDownStrings
	 *
	 * @param pInlineRules
	 * 		List of inline rules
	 */
	public void setInlineRules(final List<InlineRule> pInlineRules) {
		mInlineRules = pInlineRules;
	}

	/**
	 * Adds an {@link InlineDisplayItem}
	 *
	 * @param pInlineDisplayItem
	 * 		the item to add.
	 */
	@SuppressWarnings("unchecked")
	public final void addMapping(final InlineDisplayItem pInlineDisplayItem) {
		final Type type = pInlineDisplayItem.getClass().getGenericInterfaces()[0];

		// Get the generic type from the interface
		final Class<T> tClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[1];
		mMapping.put((Class<? extends MarkDownString>) tClass, pInlineDisplayItem);
	}

	/**
	 * Creates a T from the given {@link MarkDownString} using the mapped InlineDisplayItem
	 *
	 * @param pMarkDownString
	 * 		The inline markdown string that will be rendered
	 * @return Returns a {@link T} using the mapped {@link InlineDisplayItem}
	 */
	public T convert(final MarkDownString pMarkDownString) {
		return getDisplayItemForClass(pMarkDownString.getClass()).create(this, pMarkDownString);
	}

	/**
	 * Gets the InlineDisplayItem that is mapped to a MarkDownString
	 *
	 * @param pClass
	 * 		A MarkdownString class
	 * @return Returns the InlineDisplayItem that should be used to convert the MarkDownString to a {@link T}
	 */
	@SuppressWarnings("unchecked")
	private InlineDisplayItem<T, MarkDownString> getDisplayItemForClass(final Class pClass) {
		InlineDisplayItem displayItem = mMapping.get(pClass);
		if (displayItem == null) {
			throw new IllegalStateException("MarkDownItem not mapped to DisplayItem, use Converter.addMapping to add a map for: " +
					pClass.toString());
		}
		return displayItem;
	}

	/**
	 * TODO: Documentation
	 * TODO: consider moving this to a more suitable class?
	 */
	public List<MarkDownString> parseContent(final String pContent) {
		final List<MarkDownString> markDownStrings = new ArrayList<>();

		String toCheck = pContent;
		while (!toCheck.isEmpty()) {
			MatchedRule matchedRule = findFirstMatch(toCheck);
			if (matchedRule != null) {
				if (matchedRule.mStart > 0) {
					markDownStrings.add(new TextString(toCheck.substring(0, matchedRule.mStart), false));
				}

				String content = toCheck.substring(matchedRule.mStart, matchedRule.mEnd);
				markDownStrings.add(matchedRule.mInlineRule.toMarkDownString(content));
				toCheck = toCheck.substring(matchedRule.mEnd, toCheck.length());
			} else if (!toCheck.isEmpty()) {
				markDownStrings.add(new TextString(toCheck, false));
				break;
			}
		}
		return markDownStrings;
	}

	/**
	 * Finds the first InlineRule that matches
	 *
	 * @param pMarkDown
	 * 		String containing markdown
	 * @return Returns a MatchedRule
	 */
	private MatchedRule findFirstMatch(String pMarkDown) {
		MatchedRule currentMatch = null;
		for (InlineRule inlineRule : mInlineRules) {
			final Matcher matcher = inlineRule.getRegex().matcher(pMarkDown);
			if (matcher.find()) {
				MatchedRule matchedRule = new MatchedRule(inlineRule, matcher.start(), matcher.end());
				if (currentMatch == null || currentMatch.isStartLower(matchedRule)) {
					currentMatch = matchedRule;
				}
			}
		}
		return currentMatch;
	}

	/**
	 * Class containing data about a matched rule
	 */
	private static class MatchedRule {

		/** start position of the rule */
		private int mStart;

		/** end position of the rule */
		private int mEnd;

		/** The matching rule */
		private InlineRule mInlineRule;

		private MatchedRule(final InlineRule pInlineRule, final int pStart, final int pEnd) {
			mInlineRule = pInlineRule;
			mStart = pStart;
			mEnd = pEnd;
		}

		/**
		 * TODO: Documentation
		 */
		private boolean isStartLower(final MatchedRule pMatchedRule) {
			return pMatchedRule.mStart < mStart;
		}
	}
}