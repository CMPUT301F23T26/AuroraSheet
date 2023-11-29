package com.example.aurorasheetapp;

import org.junit.Test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    //for testing the validateDate function
    @Test
    public void testValidate(){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        assertTrue(ItemValidator.validateDate(currentDate));
        assertFalse(ItemValidator.validateDate("12-12-2023"));
        assertFalse(ItemValidator.validateDate("31-11-2023"));
        assertTrue(ItemValidator.validateDate("28-11-2023"));
        assertFalse(ItemValidator.validateDate("12-12-2024"));
        assertTrue(ItemValidator.validateDate("12-12-2022"));
    }
}