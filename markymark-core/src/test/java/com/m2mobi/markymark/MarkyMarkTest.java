package com.m2mobi.markymark;

import com.m2mobi.markymark.item.MarkdownItem;
import com.m2mobi.markymark.rules.Rule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MarkyMarkTest {

    /**
     * System under test.
     */
    MarkyMark markyMark;

    /**
     * MarkyMark parts.
     */
    private Converter converter;
    private Rule rule;
    private Rule defaultRule;
    private Flavor flavor;

    /**
     * File to parse.
     */
    private String file;

    @Before
    public void setUp() {
        MarkyMark.Builder builder = new MarkyMark.Builder();
        flavor = mock(Flavor.class);
        rule = mock(Rule.class);
        defaultRule = mock(Rule.class);
        converter = mock(Converter.class);
        InlineConverter inlineConverter = mock(InlineConverter.class);
        markyMark = builder.addFlavor(flavor)
                           .addRule(rule)
                           .setConverter(converter)
                           .setInlineConverter(inlineConverter)
                           .setDefaultRule(defaultRule)
                           .build();

        InputStream stream = getClass().getResourceAsStream("/README-light.md");
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader bReader = new BufferedReader(reader);
        file = bReader.lines().collect(Collectors.joining("\n"));
    }

    @Test
    public void testParseDefaultRules() {
        when(rule.conforms(ArgumentMatchers.<String>anyList())).thenReturn(false);
        when(defaultRule.getLinesConsumed()).thenReturn(2);
        when(defaultRule.toMarkdownItem(ArgumentMatchers.<String>anyList())).thenReturn(mock(MarkdownItem.class));
        List items = markyMark.parseMarkdown(file);
        verify(converter).convert(ArgumentMatchers.<MarkdownItem>anyList(), isA(InlineConverter.class));
        assertTrue(items.isEmpty());
    }

    @Test
    public void testParseCustomRules() {
        when(rule.conforms(ArgumentMatchers.<String>anyList())).thenReturn(true);
        when(rule.getLinesConsumed()).thenReturn(2);
        when(rule.toMarkdownItem(ArgumentMatchers.<String>anyList())).thenReturn(mock(MarkdownItem.class));
        List items = markyMark.parseMarkdown(file);
        verify(converter).convert(ArgumentMatchers.<MarkdownItem>anyList(), isA(InlineConverter.class));
        assertTrue(items.isEmpty());
    }
}