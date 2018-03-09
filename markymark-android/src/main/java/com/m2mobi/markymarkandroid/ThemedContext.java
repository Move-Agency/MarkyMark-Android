/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.support.v7.view.ContextThemeWrapper;

/**
 * Context wrapper that uses the <code>MarkyMarkTheme</code> theme
 */
public class ThemedContext extends ContextWrapper {

	public ThemedContext(Context pContext) {
		super(getWrapper(pContext));
	}

	/**
	 * Wraps a context with a ContextThemeWrapper with the MarkyMark theme
	 *
	 * @param pContext
	 * 		Context used by the wrapper
	 * @return Returns a ContextThemeWrapper with the MarkyMark theme
	 */
	private static ContextThemeWrapper getWrapper(Context pContext) {
		final TypedArray array = pContext.getTheme().obtainStyledAttributes(new int[] {R.attr.MarkyMarkTheme});
		final int id = array.getResourceId(0, R.style.MarkyMarkStyle);
		array.recycle();

		return new ContextThemeWrapper(pContext, id);
	}
}