/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkcontentful.rules;

import com.m2mobi.markymark.item.MarkDownItem;
import com.m2mobi.markymark.rules.Rule;
import com.m2mobi.markymarkcommon.markdownitems.ListItem;
import com.m2mobi.markymarkcommon.markdownitems.MarkDownList;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link Rule} that matches lists
 */
public class ListRule implements Rule {

	/** Amount of white space before a nested list item */
	private static final int INDENT_SIZE = 2;

	/** The amount of lines that has been parsed as a markdown list */
	private int mLinesConsumed = 0;

	/** int index of the current line used to keep track of the lines that need to be parsed */
	private int mCurrentLine = 0;

	/** List of markdown strings used to create MarkDownLists */
	private List<String> mMarkDownLines;

	/** Matches an ordered list */
	private static final Pattern ORDERED_LIST_PATTERN = Pattern.compile("^\\s{0,3}\\d+\\.\\s+(.+)$");

	/** Matches an unordered list */
	private static final Pattern UNORDERED_LIST_PATTERN = Pattern.compile("^\\s{0,3}\\-{1}\\s+([^\\*].+)$");

	/** Matches an ordered list and the leading spaces */
	private static final Pattern NESTED_ORDERED_LIST_PATTERN = Pattern.compile("^(\\s*)\\d+\\.\\s+(.+)$");

	/** Matches an unordered list and the leading spaces */
	private static final Pattern NESTED_UNORDERED_LIST_PATTERN = Pattern.compile("^(\\s*)\\-{1}\\s+([^\\*].+)$");

	@Override
	public boolean conforms(final List<String> pMarkDownLines) {
		if (!ORDERED_LIST_PATTERN.matcher(pMarkDownLines.get(0)).matches()
				&& !UNORDERED_LIST_PATTERN.matcher(pMarkDownLines.get(0)).matches()) {
			return false;
		}
		mLinesConsumed = 0;
		for (String line : pMarkDownLines) {
			if (isList(line)) {
				mLinesConsumed++;
			} else {
				return true;
			}
		}
		return true;
	}

	@Override
	public int getLinesConsumed() {
		return mLinesConsumed;
	}

	@Override
	public MarkDownItem toMarkDownItem(final List<String> pMarkDownLines) {
		mMarkDownLines = pMarkDownLines;
		mCurrentLine = 0;

		MarkDownList list = new MarkDownList(isOrdered(mMarkDownLines.get(mCurrentLine)), 0);
		parseMarkDownList(list, getNestingLevel(mMarkDownLines.get(mCurrentLine)));

		return list;
	}

	/**
	 * Recursive method that constructs a MarkDownList from markDown
	 *
	 * @param pMarkDownList
	 * 		Root MarkDownList item
	 * @param currentLevel
	 * 		current nesting level
	 * @return Returns a MarkDownList
	 */
	private void parseMarkDownList(final MarkDownList pMarkDownList, int currentLevel) {
		String line = mMarkDownLines.get(mCurrentLine);
		while (isList(line)) {
			final int level = getNestingLevel(line);
			if (level > currentLevel) {
				// Create sub-list
				final MarkDownList childMarkDownList = new MarkDownList(isOrdered(line), level);
				pMarkDownList.getLastListItem().addChild(childMarkDownList);
				parseMarkDownList(childMarkDownList, level);
			} else if (level == currentLevel) {
				// Add a list item
				pMarkDownList.addListItem(new ListItem(getTextForLine(line)));
				mCurrentLine++;
			} else {
				break;
			}
			if (mCurrentLine >= mMarkDownLines.size()) {
				break;
			}
			line = mMarkDownLines.get(mCurrentLine);
		}
	}

	/**
	 * Determines if the line is an ordered list
	 *
	 * @param pLine
	 * 		String containing a list item
	 * @return Returns true if pLine is an ordered list, false if not
	 */
	private boolean isOrdered(String pLine) {
		return NESTED_ORDERED_LIST_PATTERN.matcher(pLine).matches();
	}

	/**
	 * Determines if a string is part of a list
	 *
	 * @param pLine
	 * 		String containing a possible list
	 * @return Returns true if the string is a ordered or unordered list
	 */
	private boolean isList(String pLine) {
		return NESTED_ORDERED_LIST_PATTERN.matcher(pLine).matches()
				|| NESTED_UNORDERED_LIST_PATTERN.matcher(pLine).matches();
	}

	/**
	 * Gets the nesting level of a list item
	 *
	 * @param pLine
	 * 		String containing a list item
	 * @return Returns an integer representing the nesting level of the list item¶
	 */
	private int getNestingLevel(String pLine) {
		final Matcher matchingRegex = getMatchingMatcher(pLine);
		if (matchingRegex != null) {
			// 1st group contains the whitespace in front of the pattern
			final int level = matchingRegex.group(1).length();
			return level / INDENT_SIZE;
		}
		return 0;
	}

	/**
	 * Extracts the text of a list item using the correct regex group
	 *
	 * @param pLine
	 * 		String that will be matched
	 * @return Returns a String of the text group
	 */
	private String getTextForLine(String pLine) {
		final Matcher matchingRegex = getMatchingMatcher(pLine);
		if (matchingRegex != null) {
			return matchingRegex.group(2).trim();
		}
		return pLine;
	}

	/**
	 * Gets a Matcher that matches a regex pattern
	 *
	 * @param pLine
	 * 		String of the line to match
	 * @return Returns a matching Matcher or null
	 */
	private Matcher getMatchingMatcher(String pLine) {
		Matcher matcher = NESTED_ORDERED_LIST_PATTERN.matcher(pLine);
		if (matcher.matches()) {
			return matcher;
		}

		matcher = NESTED_UNORDERED_LIST_PATTERN.matcher(pLine);
		if (matcher.matches()) {
			return matcher;
		}
		return null;
	}
}