package com.example.aurorasheetapp;

import org.junit.Test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Unit test for verifying the correctness of Validator for item fields
 */
public class ValidatorUnitTest {
    @Test
    public void testValidateItemName() {
        assertFalse(ItemValidator.validateItemName(""));
        assertTrue(ItemValidator.validateItemName("TotallyLegalName"));
    }

    @Test
    public void testValidateItemDescription() {
        assertFalse(ItemValidator.validateItemDescription(""));
        assertTrue(ItemValidator.validateItemDescription("PerfectlyGoodDescription"));
    }
    @Test
    public void testValidateItemSerialNumber() {
        assertFalse(ItemValidator.validateSerialNumber(""));
        assertTrue(ItemValidator.validateSerialNumber("BI123342DSS"));
    }

    @Test
    public void testValidateItemValue() {
        assertFalse(ItemValidator.validateItemValue(""));
        assertTrue(ItemValidator.validateItemValue("13.00"));
    }

    @Test
    public void testValidateItemMake() {
        assertFalse(ItemValidator.validateItemMake(""));
        assertTrue(ItemValidator.validateItemMake("mmm"));
    }

    @Test
    public void testValidateItemModel() {
        assertFalse(ItemValidator.validateItemModel(""));
        assertTrue(ItemValidator.validateItemModel("NACHTREIHER/46E"));
    }

    @Test
    public void testValidateItemComment() {
        assertFalse(ItemValidator.validateItemComment(""));
        assertTrue(ItemValidator.validateItemComment("This is my one and only possession help"));
    }

    @Test
    public void testValidate(){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        assertTrue(ItemValidator.validateDate(currentDate));     // check if current date will be seen as valid
        assertFalse(ItemValidator.validateDate("12-12-2026"));   // year is in the future
        assertFalse(ItemValidator.validateDate("12-13-2021"));   // non-existant month & month is in the future
        assertFalse(ItemValidator.validateDate("31-11-2022"));   // non-existant date
        assertFalse(ItemValidator.validateDate("31-09-2022"));   // non-existant date
        assertFalse(ItemValidator.validateDate("29-02-2022"));   // illegal date for February
        assertTrue(ItemValidator.validateDate("28-02-2022"));   // legal date for February
        assertTrue(ItemValidator.validateDate("28-11-2023"));    // a little before the present day
        assertFalse(ItemValidator.validateDate("31-12-2023"));   // date is in the future
        assertTrue(ItemValidator.validateDate("22-12-2022"));    // date and month in the future but year is not

        ItemDate dateClass = new ItemDate(currentDate);
        assertTrue(ItemValidator.validateDate(dateClass));
        dateClass.setDate("12-12-2026");
        assertFalse(ItemValidator.validateDate(dateClass));
        dateClass.setDate("12-13-2021");
        assertFalse(ItemValidator.validateDate(dateClass));
        dateClass.setDate("31-11-2022");
        assertFalse(ItemValidator.validateDate(dateClass));
        dateClass.setDate("31-09-2022");
        assertFalse(ItemValidator.validateDate(dateClass));
        dateClass.setDate("29-02-2022");
        assertFalse(ItemValidator.validateDate(dateClass));
        dateClass.setDate("28-02-2022");
        assertTrue(ItemValidator.validateDate(dateClass));
        dateClass.setDate("28-11-2023");
        assertTrue(ItemValidator.validateDate(dateClass));
        dateClass.setDate("31-12-2023");
        assertFalse(ItemValidator.validateDate(dateClass));
        dateClass.setDate("22-12-2022");
        assertTrue(ItemValidator.validateDate(dateClass));
    }
}
