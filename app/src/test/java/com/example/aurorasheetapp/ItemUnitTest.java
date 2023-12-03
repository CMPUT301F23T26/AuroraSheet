package com.example.aurorasheetapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ItemUnitTest {

    private ItemDate itemDate;

    @Before
    public void setUp() {
        itemDate = new ItemDate(2023, 12, 1);
    }

    @Test
    public void testItemConstructor() {
        Item item = new Item("TestItem", itemDate, "Description", "Make", "12345", "Model", 100.0, "Comment");

        assertEquals("TestItem", item.getName());
        assertEquals(itemDate, item.getDateOfPurchase());
        assertEquals("Description", item.getBriefDescription());
        assertEquals("Make", item.getMake());
        assertEquals("12345", item.getSerialNumber());
        assertEquals("Model", item.getModel());
        assertEquals(100.0, item.getEstimatedValue(), 0.001);
        assertEquals("Comment", item.getComment());
        assertFalse(item.getSelection());
        assertNull(item.getDocumentId());
        assertNull(item.getPath());
        assertEquals(-1, item.getTopImageIndex());
        assertTrue(item.getImage().isEmpty());
    }

    @Test
    public void testItemSetters() {
        Item item = new Item();

        item.setName("UpdatedName");
        item.setDateOfPurchase(itemDate);
        item.setBriefDescription("UpdatedDescription");
        item.setMake("UpdatedMake");
        item.setSerialNumber("67890");
        item.setModel("UpdatedModel");
        item.setEstimatedValue(200.0);
        item.setComment("UpdatedComment");
        item.setDocumentId("UpdatedDocumentId");
        item.setPath("UpdatedPath");
        item.setTopImageIndex(0);

        assertEquals("UpdatedName", item.getName());
        assertEquals(itemDate, item.getDateOfPurchase());
        assertEquals("UpdatedDescription", item.getBriefDescription());
        assertEquals("UpdatedMake", item.getMake());
        assertEquals("67890", item.getSerialNumber());
        assertEquals("UpdatedModel", item.getModel());
        assertEquals(200.0, item.getEstimatedValue(), 0.001);
        assertEquals("UpdatedComment", item.getComment());
        assertEquals("UpdatedDocumentId", item.getDocumentId());
        assertEquals("UpdatedPath", item.getPath());
        assertEquals(0, item.getTopImageIndex());
    }

    @Test
    public void testItemSelectionMethods() {
        Item item = new Item();

        assertFalse(Boolean.TRUE.equals(item.getSelection()));

        item.select();
        assertTrue(Boolean.TRUE.equals(item.getSelection()));

        item.unselect();
        assertFalse(Boolean.TRUE.equals(item.getSelection()));

        item.toggleSelect();
        assertTrue(Boolean.TRUE.equals(item.getSelection()));

        item.toggleSelect();
        assertFalse(Boolean.TRUE.equals(item.getSelection()));
    }

}
