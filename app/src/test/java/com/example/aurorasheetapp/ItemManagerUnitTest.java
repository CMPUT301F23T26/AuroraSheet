package com.example.aurorasheetapp;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ItemManagerUnitTest {

    private ItemManager itemManager;

    @Before
    public void setUp() {
        itemManager = new ItemManager();
    }

    @Test
    public void testGetItems_EmptyList() {
        List<Item> items = itemManager.getItems();
        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    @Test
    public void testAddItem() {
        Item item = new Item("Item 1", new ItemDate(2023, 12, 1), "Description", "Make", "Serial", "Model", 50.0, "Comment");
        itemManager.add(item);

        List<Item> items = itemManager.getItems();
        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertEquals(1, items.size());
        assertEquals(item, items.get(0));
    }

    @Test
    public void testRemoveItem() {
        Item item = new Item("Item 1", new ItemDate(2023, 12, 1), "Description", "Make", "Serial", "Model", 50.0, "Comment");
        itemManager.add(item);

        Item removedItem = itemManager.remove(0);
        assertNotNull(removedItem);
        assertEquals(item, removedItem);

        List<Item> items = itemManager.getItems();
        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(itemManager.isEmpty());

        Item item = new Item("Item 1", new ItemDate(2023, 12, 1), "Description", "Make", "Serial", "Model", 50.0, "Comment");
        itemManager.add(item);

        assertFalse(itemManager.isEmpty());
    }

    @Test
    public void testComputeTotal() {
        Item item1 = new Item("Item 1", new ItemDate(2023, 12, 1), "Description", "Make", "Serial", "Model", 50.0, "Comment");
        Item item2 = new Item("Item 2", new ItemDate(2023, 12, 1), "Description", "Make", "Serial", "Model", 30.0, "Comment");

        itemManager.add(item1);
        itemManager.add(item2);

        String total = itemManager.computeTotal();
        assertEquals("80.00", total);
    }
}
