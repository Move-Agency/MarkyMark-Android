/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.LeadingMarginSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

import com.m2mobi.markymark.DisplayItem;
import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymarkcommon.markdownitems.ListItem;
import com.m2mobi.markymarkcommon.markdownitems.MarkdownList;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Creates a view containing list items created from a {@link MarkdownList}
 * <p>
 * User: M2mobi
 * Date 04/05/2016
 * Time 11:31
 */
public class ListDisplayItem implements DisplayItem<View, MarkdownList, Spanned> {

	private Context mContext;

	private final int mListMargin;

	@DrawableRes
	private final int[] mBulletDrawables;

	public ListDisplayItem(final Context pContext, @DimenRes final int pListMargin, @DrawableRes final int[] pBulletDrawables) {
		mContext = pContext;
		mListMargin = mContext.getResources().getDimensionPixelSize(pListMargin);
		mBulletDrawables = pBulletDrawables;
	}

	@Override
	public View create(final MarkdownList pMarkDownItem, final InlineConverter<Spanned> pInlineConverter) {
		return createLayout(pMarkDownItem, pInlineConverter);
	}

	private LinearLayout createLayout(final MarkdownList pMarkDownItem, final InlineConverter<Spanned> pInlineConverter) {
		LinearLayout layout = new LinearLayout(mContext);
		layout.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
		layout.setOrientation(LinearLayout.VERTICAL);

		final List<ListItem> items = pMarkDownItem.getListItems();
		for (int i = 0; i < items.size(); i++) {
			final ListItem item = items.get(i);
			final View view = getTextView(pMarkDownItem, pInlineConverter.convert(item.getText()), i);
			layout.addView(view);

			if (item.hasChild()) {
				for (MarkdownList list : item.getChild()) {
					final LinearLayout nestedLayout = createLayout(list, pInlineConverter);
					nestedLayout.setPadding(mListMargin, 0, 0, 0);
					layout.addView(nestedLayout);
				}
			}
		}

		return layout;
	}

	/**
	 * Create a drawable resource for the nesting level of this list
	 * When there are 3 drawable resources provided it will start with the first again
	 * when the nesting level is more than 2 etc.
	 *
	 * @param pNestedLevel
	 * 		nested level of this list
	 * @return item from the array of drawables resources that was provided
	 */
	@DrawableRes
	private int getDrawable(final int pNestedLevel) {
		return mBulletDrawables[pNestedLevel % mBulletDrawables.length];
	}

	/**
	 * Creates a TextView that represents a list item
	 *
	 * @param pMarkDownList
	 * 		MarkDownList used to determine the kind of list
	 * @param pText
	 * 		Content of the list item
	 * @param pIndex
	 * 		Index of the item in the list
	 * @return Returns a TextView
	 */
	private TextView getTextView(final MarkdownList pMarkDownList, final CharSequence pText, final int pIndex) {
		final TextView textView = new TextView(mContext, null, R.attr.MarkdownListStyle);
		textView.setMovementMethod(LinkMovementMethod.getInstance());

		if (pMarkDownList.isOrdered()) {
			SpannableString string = new SpannableString(pText);
			string.setSpan(new NumberSpan(mListMargin, pIndex + 1), 0, string.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			textView.setText(string);
		} else {
			final Drawable drawable = mContext.getResources().getDrawable(getDrawable(pMarkDownList.getNestedLevel()));
			SpannableString string = new SpannableString(pText);
			string.setSpan(new BulletSpan(mListMargin, drawable), 0, string.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			textView.setText(string);
		}

		return textView;
	}

	/**
	 * Span that adds a number in front of the first line of a list item
	 */
	private class NumberSpan implements LeadingMarginSpan.LeadingMarginSpan2 {

		/** Width of the margin */
		private final int mGapWidth;

		/** Number of the list item */
		private final int mIndex;

		public NumberSpan(int pGapWidth, int pIndex) {
			mGapWidth = pGapWidth;
			mIndex = pIndex;
		}

		@Override
		public void drawLeadingMargin(Canvas pCanvas, Paint pPaint, int pX, int pDir, int pTop, int pBaseline, int pBottom,
				CharSequence pText, int pStart, int pEnd, boolean pFirst, Layout pLayout) {
			if (pFirst) {
				final String number = mIndex + ". ";
				final float v = pPaint.measureText(number, 0, number.length());
				pCanvas.drawText(number, mGapWidth - v, pBaseline, pPaint);
			}
		}

		@Override
		public int getLeadingMargin(final boolean first) {
			return mGapWidth;
		}

		@Override
		public int getLeadingMarginLineCount() {
			// Only add to first line
			return 1;
		}
	}

	/**
	 * Span that adds a bullet in front of the first line of a list item
	 */
	private class BulletSpan implements LeadingMarginSpan.LeadingMarginSpan2 {

		/** Width of the margin */
		private final int mGapWidth;

		/** Drawable that will be drawn in front of the first line */
		private final Drawable mDrawable;

		public BulletSpan(int pGapWidth, final Drawable pDrawable) {
			mGapWidth = pGapWidth;
			mDrawable = pDrawable;
		}

		@Override
		public int getLeadingMargin(final boolean first) {
			return mGapWidth;
		}

		@Override
		public void drawLeadingMargin(Canvas pCanvas, Paint pPaint, int pX, int pDir, int pTop, int pBaseline, int pBottom,
				CharSequence pText, int pStart, int pEnd, boolean pFirst, Layout pLayout) {
			if (!pFirst) {
				return;
			}
			int drawableHeight = mDrawable.getMinimumHeight();
			int drawableWidth = mDrawable.getMinimumWidth();
			int y = (pBaseline / 2);
			int x = (mGapWidth / 2);

			mDrawable.setBounds(x, y, x + drawableWidth, y + drawableHeight);
			mDrawable.draw(pCanvas);
		}

		@Override
		public int getLeadingMarginLineCount() {
			// Only add to first line
			return 1;
		}
	}
}