/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymarkandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

import com.m2mobi.markymark.DisplayItem;
import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymarkcommon.markdownitems.ListItem;
import com.m2mobi.markymarkcommon.markdownitems.MarkDownList;

import java.util.List;
import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Converts an {@link MarkDownList} to a {@link View}.
 */
public class ListDisplayItem implements DisplayItem<View, MarkDownList, Spanned> {

	private Context mContext;

	private final int mIndentationSpacingPixels, mIndicatorSpacingPixels;

	@DrawableRes
	private final int[] mBulletDrawables;

	public ListDisplayItem(final Context pContext, @DimenRes final int pIndentationSpacing,
			@DimenRes final int pBulletSpacing, @DrawableRes final int[] pBulletDrawables) {
		mContext = pContext;
		mIndicatorSpacingPixels = mContext.getResources().getDimensionPixelSize(pBulletSpacing);
		mIndentationSpacingPixels = mContext.getResources().getDimensionPixelSize(pIndentationSpacing);
		mBulletDrawables = pBulletDrawables;
	}

	@Override
	public View create(final MarkDownList pMarkDownItem, final InlineConverter<Spanned> pInlineConverter) {
		return createLayout(pMarkDownItem, pInlineConverter);
	}

	private LinearLayout createLayout(final MarkDownList pMarkDownItem, final InlineConverter<Spanned> pInlineConverter) {
		LinearLayout layout = new LinearLayout(mContext);
		layout.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
		layout.setOrientation(LinearLayout.VERTICAL);

		final List<ListItem> items = pMarkDownItem.getListItems();
		for (int i = 0; i < items.size(); i++) {
			final ListItem item = items.get(i);
			final View view = getTextView(pMarkDownItem, pInlineConverter.convert(item.getText()), i);
			layout.addView(view);

			if (item.hasChild()) {
				for (MarkDownList list : item.getChild()) {
					final LinearLayout nestedLayout = createLayout(list, pInlineConverter);
					LayoutParams params = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT
					);
					params.setMargins(mIndentationSpacingPixels, 0, 0, 0);
					nestedLayout.setLayoutParams(params);
					layout.addView(nestedLayout);
				}
			}
		}
		return layout;
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
	private TextView getTextView(final MarkDownList pMarkDownList, final CharSequence pText, final int pIndex) {
		final TextView textView = new TextView(mContext, null, R.attr.MarkDownListStyle);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		if (pMarkDownList.isOrdered()) {
			textView.setText(String.format(Locale.getDefault(), "%d. %s", pIndex + 1, pText));
		} else {
			Drawable bullet = mContext.getResources().getDrawable(getDrawable(pMarkDownList.getNestedLevel()));
			TopCompoundDrawable gravityDrawable = new TopCompoundDrawable(bullet, textView.getLineHeight());
			bullet.setBounds(0, 0, bullet.getIntrinsicWidth(), bullet.getIntrinsicHeight());
			gravityDrawable.setBounds(0, 0, bullet.getIntrinsicWidth(), bullet.getIntrinsicHeight());
			textView.setCompoundDrawables(gravityDrawable, null, null, null);
			textView.setCompoundDrawablePadding(mIndicatorSpacingPixels);
			textView.setText(pText);
		}
		return textView;
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
	 * Drawable that aligns vertically on the first line of the TextView
	 */
	public class TopCompoundDrawable extends Drawable {

		private final Drawable mDrawable;

		private final int mTextLineHeight;

		public TopCompoundDrawable(Drawable drawable, int textLineHeight) {
			mDrawable = drawable;
			mTextLineHeight = textLineHeight;
		}

		@Override
		public int getIntrinsicWidth() {
			return mDrawable.getIntrinsicWidth();
		}

		@Override
		public int getIntrinsicHeight() {
			return mDrawable.getIntrinsicHeight();
		}

		@Override
		public void draw(@NonNull Canvas canvas) {
			int halfCanvas = canvas.getHeight() / 2;
			int halfLineHeight = mTextLineHeight / 2;

			// align to top
			canvas.save();
			canvas.translate(0, -halfCanvas + halfLineHeight);
			mDrawable.draw(canvas);
			canvas.restore();
		}

		@Override
		public void setAlpha(final int alpha) {
			mDrawable.setAlpha(alpha);
		}

		@Override
		public void setColorFilter(@Nullable final ColorFilter colorFilter) {
			mDrawable.setColorFilter(colorFilter);
		}

		@Override
		public int getOpacity() {
			return mDrawable.getOpacity();
		}
	}
}
