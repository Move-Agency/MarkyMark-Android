/* Copyright (C) M2mobi BV - All Rights Reserved
* Unauthorized copying of this file, via any medium is strictly prohibited
* Proprietary and confidential
* Written by Michiel Baneke <baneke@m2mobi.com>, February 2018
*/

package com.m2mobi.markymark.rules;

import com.m2mobi.markymark.item.MarkDownItem;

import java.util.List;

/**
 * Rules are used to identify a piece of markdown (header, list, paragraph) and convert it to a {@link MarkDownItem}
 */
public interface Rule {

	/**
	 * checks if the passed MarkDown conforms to this rule
	 *
	 * @return {@code true} if the MarkDown conforms to this rule, {@code false} otherwise.
	 */
	boolean conforms(final List<String> pMarkDownLines);

	/**
	 * @return the amount of lines consumed by this rule
	 */
	int getLinesConsumed();

	/**
	 * @return MarkDownItem that has been created from the MarkDown input
	 */
	MarkDownItem toMarkDownItem(final List<String> pMarkDownLines);
}