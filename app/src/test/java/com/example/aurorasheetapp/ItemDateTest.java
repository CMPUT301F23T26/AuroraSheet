package com.example.aurorasheetapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This test provides verification for itemDate's functions
 */
public class ItemDateTest {
    private ItemDate itemDate;
    @Test
    public void testStringRepresentation(){
        String dateString = "11-12-2021";
        itemDate = new ItemDate(dateString);
        assertEquals(itemDate.toString(), dateString);
    }

    @Test
    public void testParseDate(){
        String dateString = "11-12-2021";
        int[] dateInts = ItemDate.parseDate(dateString);
        assertEquals(dateInts[0], 11);
        assertEquals(dateInts[1], 12);
        assertEquals(dateInts[2], 2021);
    }
    @Test
    public void testConstructor(){
        String dateString = "11-12-2021";
        itemDate = new ItemDate(dateString);
        assertEquals(11, itemDate.getDay());
        assertEquals(12,  itemDate.getMonth());
        assertEquals(2021, itemDate.getYear());

        int[] dateInts = {8, 12, 2021};
        itemDate = new ItemDate(dateInts[0], dateInts[1], dateInts[2]);
        assertEquals(8, itemDate.getDay());
        assertEquals(12,  itemDate.getMonth());
        assertEquals(2021, itemDate.getYear());
    }
}
