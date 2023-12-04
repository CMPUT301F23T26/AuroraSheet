package com.example.aurorasheetapp;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

import com.google.android.gms.common.internal.Objects;

/**
 * This test verifies the functionality of Tag class
 */
public class TagUnitTest {

    private Tag tag;
    private Item item;

    @Before
    public void setUp() {
        this.tag = new Tag("testTag");
        this.item = new Item("testItem", new ItemDate(0,0,0), "description", "make", "serialNo", "model", 10d, "comment");
    }

    @Test
    public void testSetName() {
        this.tag.setName("newName");
        assertTrue(Objects.equal(tag.getName(), "newName"));
    }

    @Test
    public void testTagItem() {
        Item newItem = new Item("Item 1", new ItemDate(2023, 12, 1), "Description", "Make", "Serial", "Model", 50.0, "Comment");
        tag.tagItem(item);
        assertEquals(1, tag.getTagged_items().size());
        tag.tagItem(this.item);
        assertEquals(2, tag.getTagged_items().size());
        tag.untagItem(newItem);
        assertEquals(1, tag.getTagged_items().size());
        assertTrue(Objects.equal(this.item, tag.getTagged_items().get(0)));
    }
}
