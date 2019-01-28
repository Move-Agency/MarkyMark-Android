package com.m2mobi.markymark;

import com.m2mobi.markymark.item.MarkdownItem;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Every.everyItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConverterTest {

    private Converter<Object> sut;

    @Before
    public void setUp() {
        sut = new Converter<>();
    }

    @After
    public void testMapping() {
        assertThat(sut.getMapping(MockDisplayItem.class), Matchers.<DisplayItem>instanceOf(MockDisplayItem.class));
    }

    @Test
    public void addMapping() {
        MockDisplayItem displayItem = new MockDisplayItem();
        sut.addMapping(displayItem);
    }

    @Test(expected = IllegalStateException.class)
    public void convertFailsOnMismatchedObject() {
        MockDisplayItem displayItem = new MockDisplayItem();
        sut.addMapping(displayItem);
        List<MarkdownItem> mockList = new ArrayList<>();
        MarkdownItem mockItem = mock(MarkdownItem.class);
        mockList.add(mockItem);
        InlineConverter mockConverter = mock(InlineConverter.class);
        sut.convert(mockList, mockConverter);
    }

    @Test
    public void convertSucceeds() {
        LinkedList linkedList = mock(LinkedList.class);
        MockDisplayItem displayItem = new MockDisplayItem(linkedList);
        List<MarkdownItem> mockList = new ArrayList<>();
        mockList.add(displayItem);
        InlineConverter mockConverter = mock(InlineConverter.class);
        when(linkedList.getFirst()).thenReturn(new Object());

        sut.addMapping(displayItem);
        assertThat(sut.getMapping(MockDisplayItem.class), instanceOf(MockDisplayItem.class));
        List<Object> converted = sut.convert(mockList, mockConverter);
        assertThat(converted, everyItem(instanceOf(Object.class)));
        assertThat(converted.size(), equalTo(1));
        assertThat(converted, Matchers.containsInAnyOrder(instanceOf(Object.class)));
    }
}
class MockDisplayItem implements DisplayItem<Object, MockDisplayItem, InlineConverter>, MarkdownItem {
    private LinkedList list;

    MockDisplayItem() {
        this(new LinkedList());
        list.add(null);
    }

    MockDisplayItem(LinkedList list) {
        this.list = list;
    }

    @Override
    public Object create(MockDisplayItem pMarkdownItem, InlineConverter pInlineConverter) {
        return list.getFirst();
    }
}