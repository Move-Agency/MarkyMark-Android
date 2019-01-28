package com.m2mobi.markymark;

import com.m2mobi.markymark.rules.Rule;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for the markymark builder.
 */
public class MarkyMarkBuildTest {

    @Test
    public void buildInstance() {
        MarkyMark.Builder builder = new MarkyMark.Builder();
        Flavor flavor = mock(Flavor.class);
        Rule rule = mock(Rule.class);
        Converter converter = mock(Converter.class);
        InlineConverter inlineConverter = mock(InlineConverter.class);
        MarkyMark markyMark = builder.addFlavor(flavor)
                                     .addRule(rule)
                                     .setConverter(converter)
                                     .setInlineConverter(inlineConverter)
                                     .setDefaultRule(rule)
                                     .build();
        assertNotNull(markyMark);
    }

    @Test
    public void buildInstanceFailsOnEmptyRules() {
        MarkyMark.Builder builder = new MarkyMark.Builder();
        Flavor flavor = mock(Flavor.class);
        Rule rule = mock(Rule.class);
        Converter converter = mock(Converter.class);
        InlineConverter inlineConverter = mock(InlineConverter.class);
        try {
            builder.addFlavor(flavor)
                    .setConverter(converter)
                    .setInlineConverter(inlineConverter)
                    .setDefaultRule(rule)
                    .build();
            fail("No exception");
        } catch (IllegalArgumentException e) {
            assertEquals("No rules set, use Builder.addRule() or addFlavor() to add rules", e.getMessage());
        }
    }

    @Test
    public void buildInstanceFailsOnEmptyDefaultRule() {
        MarkyMark.Builder builder = new MarkyMark.Builder();
        Flavor flavor = mock(Flavor.class);
        Rule rule = mock(Rule.class);
        Converter converter = mock(Converter.class);
        InlineConverter inlineConverter = mock(InlineConverter.class);
        try {
            builder.addFlavor(flavor)
                    .addRule(rule)
                    .setConverter(converter)
                    .setInlineConverter(inlineConverter)
                    .build();
            fail("No exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Default rule not set, use Builder.setDefaultRule() to set the default rule.", e.getMessage());
        }
    }

    @Test
    public void buildInstanceFailsOnEmptyConverter() {
        MarkyMark.Builder builder = new MarkyMark.Builder();
        Flavor flavor = mock(Flavor.class);
        Rule rule = mock(Rule.class);
        InlineConverter inlineConverter = mock(InlineConverter.class);
        try {
            builder.addFlavor(flavor)
                    .addRule(rule)
                    .setDefaultRule(rule)
                    .setInlineConverter(inlineConverter)
                    .build();
            fail("No exception");
        } catch (IllegalArgumentException e) {
            assertEquals("No Converter set, use Builder.setConverter() to set the converter.", e.getMessage());
        }
    }

    @Test
    public void buildInstanceFailsOnEmptyInlineConverter() {
        MarkyMark.Builder builder = new MarkyMark.Builder();
        Flavor flavor = mock(Flavor.class);
        Rule rule = mock(Rule.class);
        Converter converter = mock(Converter.class);
        try {
            builder.addFlavor(flavor)
                    .addRule(rule)
                    .setDefaultRule(rule)
                    .setConverter(converter)
                    .build();
            fail("No exception");
        } catch (IllegalArgumentException e) {
            assertEquals("No InlineConverter set, use Builder.setInlineConverter() to set the converter.", e.getMessage());
        }
    }
}