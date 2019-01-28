package com.m2mobi.markymark;

import com.m2mobi.markymark.rules.Rule;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MarkdownLinesTest {

    MarkdownLines sot;

    @Before
    public void setUp() {
        InputStream stream = getClass().getResourceAsStream("/README.md");
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader bReader = new BufferedReader(reader);
        String result = bReader.lines().collect(Collectors.joining("\n"));
        sot = new MarkdownLines(result);
    }

    @Test
    public void removeLinesForRule() {
        Rule rule = mock(Rule.class);
        when(rule.getLinesConsumed()).thenReturn(20);
        assertEquals(453, sot.getLines().size());
        sot.removeLinesForRule(rule);
        assertEquals(433, sot.getLines().size());
    }

    @Test
    public void isEmpty() {
        assertFalse(sot.isEmpty());
    }

    @Test
    public void getLines() {
        assertEquals(453, sot.getLines().size());
    }

    @Test
    public void getLinesSubset() {
        assertEquals(10, sot.getLines(10).size());
    }
}