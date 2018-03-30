/**
 * {@link MarkDownString} used for storing inline code
 */

package com.m2mobi.markymarkcontentful.rules.inline;

import com.m2mobi.markymark.item.inline.MarkdownString;
import com.m2mobi.markymark.rules.InlineRule;
import com.m2mobi.markymarkcommon.markdownitems.inline.ImageString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link InlineRule} that matches inline images
 */
public class InlineImageRule implements InlineRule {

    private static final Pattern IMAGE_PATTERN = Pattern.compile("(!\\[(.*)\\]\\((.*)\\))");

    private static final int GROUP_ALT_TEXT = 2;

    private static final int GROUP_IMAGE_URL = 3;

    @Override
    public Pattern getRegex() {
        return IMAGE_PATTERN;
    }

    @Override
    public MarkdownString toMarkdownString(String pContent) {
        final Matcher matcher = getRegex().matcher(pContent);
        if (matcher.find()) {
            return new ImageString(pContent, matcher.group(GROUP_ALT_TEXT), matcher.group(GROUP_IMAGE_URL), false);
        }
        return new ImageString("", "", "", false);
    }
}
