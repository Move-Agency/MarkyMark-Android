/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymark;

import com.m2mobi.markymark.item.MarkDownItem;
import com.m2mobi.markymark.rules.InlineRule;
import com.m2mobi.markymark.rules.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 * MarkyMark, use the {@link Builder} to create instances
 */
public class MarkyMark<T> {

	/** List of rules that should be used to initialize the parser */
	private List<Rule> mRules = new ArrayList<>();

	/** Rule that should be used as a default fallback */
	private Rule mDefaultRule;

	/** Converter used to convert MarkDownItems to {@link T} */
	private Converter<T> mConverter;

	/** InlineConverter used to convert MarkDownStrings */
	private InlineConverter mInlineConverter;

	/**
	 * Creates a new instance of MarkyMark using the Builders parameters
	 *
	 * @param pBuilder
	 * 		The Builder that should be used to initialise MarkyMark
	 */
	private MarkyMark(final Builder<T> pBuilder) {
		mRules.addAll(pBuilder.mRules);
		mDefaultRule = pBuilder.mDefaultRule;
		mConverter = pBuilder.mConverter;
		mInlineConverter = pBuilder.mInlineConverter;
		mInlineConverter.setInlineRules(pBuilder.mInlineRules);
	}

	/**
	 * Parse the MarkDown
	 *
	 * @param pMarkDownString
	 * 		The MarkDown String that should be parsed
	 * @return An array of MarkDown items
	 */
	public List<T> parseMarkDown(final String pMarkDownString) {
		MarkDownLines markDownLines = new MarkDownLines(pMarkDownString);
		List<MarkDownItem> markDownItems = new ArrayList<>();

		while (!markDownLines.isEmpty()) {
			Rule rule = getRuleForLines(markDownLines.getLines());
			markDownItems.add(rule.toMarkDownItem(markDownLines.getLines(rule.getLinesConsumed())));
			markDownLines.removeLinesForRule(rule);
		}

		return mConverter.convert(markDownItems, mInlineConverter);
	}

	/**
	 * Retrieves the first {@link Rule} that recognizes the syntax of the given lines
	 *
	 * @param pLines
	 * 		The lines that should be checked for syntax
	 * @return The rule that should be used to parse the lines
	 */
	private Rule getRuleForLines(final List<String> pLines) {
		for (Rule rule : mRules) {
			if (rule.conforms(pLines)) {
				return rule;
			}
		}
		return mDefaultRule;
	}

	/**
	 * Basic Builder class to build a MarkyMark object
	 */
	public static class Builder<T> {

		/** List of rules that should be used to initialize the parser */
		private List<Rule> mRules = new ArrayList<>();

		/** Rule that should be used as a default fallback */
		private Rule mDefaultRule;

		/** Converter used to convert MarkDownItems */
		private Converter<T> mConverter;

		/** InlineConverter used to convert MarkDownStrings */
		private InlineConverter mInlineConverter;

		/** InlineRules used to parse MarkDownStrings */
		private List<InlineRule> mInlineRules;

		/**
		 * Set the flavor that should be used to initialize the parser
		 *
		 * @param pFlavor
		 * 		The flavor with rules that should be added
		 * @return This Builder
		 */
		public Builder<T> addFlavor(final Flavor pFlavor) {
			mRules.addAll(pFlavor.getRules());
			mDefaultRule = pFlavor.getDefaultRule();
			mInlineRules = pFlavor.getInlineRules();
			return this;
		}

		/**
		 * Add a {@link Rule} that should be followed when parsing
		 *
		 * @param pRule
		 * 		The rule that should be added
		 * @return This Builder
		 */
		public Builder<T> addRule(final Rule pRule) {
			mRules.add(pRule);
			return this;
		}

		/**
		 * Set the default {@link Rule} that should be used when all other rules fail, Must be called after {@link #addFlavor}
		 *
		 * @param pRule
		 * 		The Rule that should be used as default rule
		 * @return This Builder
		 */
		public Builder<T> setDefaultRule(final Rule pRule) {
			mDefaultRule = pRule;
			return this;
		}

		/**
		 * Set the Converter that should be used to convert MarkDownItems to {@link T}
		 *
		 * @param pConverter
		 * 		The Converter used to convert MarkDownItems
		 * @return This Builder
		 */
		public Builder<T> setConverter(final Converter<T> pConverter) {
			mConverter = pConverter;
			return this;
		}

		/**
		 * Set the InlineConverter that should be used to convert MarkDownStrings
		 *
		 * @param pInlineConverter
		 * 		The Converter used to convert MarkDownStrings
		 * @return This Builder
		 */
		public Builder<T> setInlineConverter(final InlineConverter pInlineConverter) {
			mInlineConverter = pInlineConverter;
			return this;
		}

		/**
		 * Builds an instance of MarkyMark and verifies fields
		 *
		 * @return new instance of MarkyMark
		 */
		public MarkyMark<T> build() {
			if (mRules.isEmpty()) {
				throw new IllegalArgumentException("No rules set, use Builder.addRule() or addFlavor() to add rules");
			}

			if (mDefaultRule == null) {
				throw new IllegalArgumentException("Default rule not set, use Builder.setDefaultRule() to set the default rule.");
			}

			if (mConverter == null) {
				throw new IllegalArgumentException("No Converter set, use Builder.setConverter() to set the converter.");
			}

			if (mInlineConverter == null) {
				throw new IllegalArgumentException("No InlineConverter set, use Builder.setInlineConverter() to set the converter.");
			}

			return new MarkyMark<>(this);
		}
	}
}