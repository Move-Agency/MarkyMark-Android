/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymark;

import com.m2mobi.markymark.item.MarkDownItem;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converter for markdown items to {@link T}
 */
public class Converter<T> {

	/** HashMap that maps MarkDownItem items to display items */
	private final Map<Class<? extends MarkDownItem>, DisplayItem<T, MarkDownItem, ?>> mMapping = new HashMap<>();

	/**
	 * Adds an {@link DisplayItem}
	 *
	 * @param pDisplayItem
	 * 		the item to add.
	 */
	@SuppressWarnings("unchecked")
	public final void addMapping(final DisplayItem pDisplayItem) {
		// amazing
		final Type type = pDisplayItem.getClass().getGenericInterfaces()[0];
		final Class<T> tClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[1];
		mMapping.put((Class<? extends MarkDownItem>) tClass, pDisplayItem);
	}

	/**
	 * Creates a List of {@link T} from the given list of {@link MarkDownItem} using the mapped DisplayItem
	 *
	 * @param pMarkDownItems
	 * 		The list of markdown items that will be converted to {@link T}
	 * @return Returns a List of {@link T} using the mapped {@link DisplayItem}
	 */
	@SuppressWarnings("unchecked")
	public List<T> convert(final List<MarkDownItem> pMarkDownItems, InlineConverter pInlineConverter) {
		final List<T> convertedItems = new ArrayList<>(pMarkDownItems.size());
		for (MarkDownItem markDownItem : pMarkDownItems) {
			convertedItems.add((T) getDisplayItemForClass(markDownItem.getClass()).create(markDownItem, pInlineConverter));
		}
		return convertedItems;
	}

	/**
	 * Gets the DisplayItem that is mapped to a MarkDownItem
	 *
	 * @param pClass
	 * 		A MarkDownItem class
	 * @return Returns the DisplayItem that should be used to convert the MarkDownItem to a {@link T}
	 */
	@SuppressWarnings("unchecked")
	private DisplayItem<T, MarkDownItem, ?> getDisplayItemForClass(final Class pClass) {
		DisplayItem displayItem = mMapping.get(pClass);
		if (displayItem == null) {
			throw new IllegalStateException("MarkDownItem not mapped to DisplayItem, use Converter.addMapping to add a map for: " +
					pClass.toString());
		}
		return displayItem;
	}
}