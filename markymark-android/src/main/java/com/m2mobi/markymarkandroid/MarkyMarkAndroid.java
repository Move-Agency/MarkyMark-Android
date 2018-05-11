/*
 * Copyright (C) M2mobi BV - All Rights Reserved
 */

package com.m2mobi.markymarkandroid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spanned;
import android.view.View;

import com.m2mobi.markymark.Converter;
import com.m2mobi.markymark.DisplayItem;
import com.m2mobi.markymark.Flavor;
import com.m2mobi.markymark.InlineConverter;
import com.m2mobi.markymark.InlineDisplayItem;
import com.m2mobi.markymark.MarkyMark;
import com.m2mobi.markymarkandroid.inline.BoldInlineDisplayItem;
import com.m2mobi.markymarkandroid.inline.CodeInlineDisplayItem;
import com.m2mobi.markymarkandroid.inline.ItalicInlineDisplayItem;
import com.m2mobi.markymarkandroid.inline.LinkInlineDisplayItem;
import com.m2mobi.markymarkandroid.inline.StrikeInlineDisplayItem;
import com.m2mobi.markymarkandroid.inline.StringInlineDisplayItem;
import com.m2mobi.markymarkandroid.inline.TextDisplayItem;

import java.util.List;

/**
 * Utility class used to get a {@link MarkyMark} instance
 */
public class MarkyMarkAndroid {

    /**
     * Creates a MarkyMark instance that converts the rules provided by the flavor into MarkyMarkAndroid Views
     *
     * @param pContext    Context used to create Views
     * @param pFlavor     Flavor that provides the markdown parsing rules
     * @param imageLoader The ImageLoader that should be used to load images
     * @return MarkMark instance that renders MarkyMarkAndroid Views from Markdown
     */
    public static MarkyMark<View> getMarkyMark(final Context pContext, final Flavor pFlavor, @NonNull final ImageLoader imageLoader) {
        return getMarkyMark(pContext, pFlavor, null, null, imageLoader);
    }

    /**
     * Creates a MarkyMark instance that converts the rules provided by the flavor into MarkyMarkAndroid Views
     *
     * @param pContext            Context used to create Views
     * @param pFlavor             Flavor that provides the markdown parsing rules
     * @param pDisplayItems       Extra DisplayItems (replaces DisplayItems that render the same MarkdownItem}
     * @param pInlineDisplayItems Extra InlineDisplayItems (replaces InlineDisplayItems that render the same MarkdownItem}
     * @param imageLoader         The ImageLoader that should be used to load images
     * @return MarkMark instance that renders MarkyMarkAndroid Views from Markdown
     */
    public static MarkyMark<View> getMarkyMark(final Context pContext, final Flavor pFlavor,
                                               @Nullable final List<DisplayItem> pDisplayItems, @Nullable final List<InlineDisplayItem> pInlineDisplayItems,
                                               @NonNull ImageLoader imageLoader) {

        Converter<View> viewConverter = new Converter<>();
        InlineConverter<Spanned> inlineConverter = new InlineConverter<>();
        inlineConverter.addMapping(new BoldInlineDisplayItem());
        inlineConverter.addMapping(new StringInlineDisplayItem());
        inlineConverter.addMapping(new TextDisplayItem());
        inlineConverter.addMapping(new StrikeInlineDisplayItem());
        inlineConverter.addMapping(new ItalicInlineDisplayItem());
        inlineConverter.addMapping(new LinkInlineDisplayItem());
        inlineConverter.addMapping(new CodeInlineDisplayItem());

        final ThemedContext context = new ThemedContext(pContext);

        viewConverter.addMapping(new HeaderDisplayItem(context));
        viewConverter.addMapping(new ParagraphDisplayItem(context));
        viewConverter.addMapping(new HorizontalRuleDisplayItem(context));
        viewConverter.addMapping(new ListDisplayItem(
                context,
                R.dimen.list_indentation_spacing,
                R.dimen.list_indicator_spacing,
                new int[]{
                        R.drawable.bullet_filled,
                        R.drawable.bullet,
                        R.drawable.dash,
                        R.drawable.square
                })
        );
        viewConverter.addMapping(new QuoteDisplayItem(context));
        viewConverter.addMapping(new CodeBlockDisplayItem(context));
        viewConverter.addMapping(new ImageDisplayItem(context, imageLoader));

        // Add or replace extra inline display items
        if (pInlineDisplayItems != null) {
            for (InlineDisplayItem item : pInlineDisplayItems) {
                inlineConverter.addMapping(item);
            }
        }

        // Add or replace extra display items
        if (pDisplayItems != null) {
            for (DisplayItem item : pDisplayItems) {
                viewConverter.addMapping(item);
            }
        }

        return new MarkyMark.Builder<View>()
                .addFlavor(pFlavor)
                .setConverter(viewConverter)
                .setInlineConverter(inlineConverter)
                .build();
    }
}