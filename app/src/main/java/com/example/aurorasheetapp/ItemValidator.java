package com.example.aurorasheetapp;

/**
 * This class is used by the AddItemActivity and ViewItemActivity classes to make sure all the
 * user input is valid and in the correct formats for adding or editing items.
 */
public class ItemValidator {
    /**
     * This method validates the item name by ensuring it is not empty.
     * @param itemName
     * @return boolean
     */
    public static boolean validateItemName(String itemName) {
        return itemName.length() > 0;
    }

    /**
     * This method validates the item description by ensuring it is not empty.
     * @param itemDescription
     * @return boolean
     */
    public static boolean validateItemDescription(String itemDescription) {
        return itemDescription.length() > 0;
    }

    /**
     * This method validates the item value by ensuring it is not empty.
     * @param itemValue
     * @return
     */
    public static boolean validateItemValue(String itemValue) {
        return itemValue.length() > 0;
    }

    /**
     * This method validates the item serial number by ensuring it is not empty.
     * @param itemMake
     * @return boolean
     */
    public static boolean validateItemMake(String itemMake) {
        return itemMake.length() > 0;
    }

    /**
     * This method validates the item model by ensuring it is not empty.
     * @param itemModel
     * @return boolean
     */
    public static boolean validateItemModel(String itemModel) {
        return itemModel.length() > 0;
    }

    /**
     * This method validates the item comment by ensuring it is not empty.
     * @param itemComment
     * @return
     */
    public static boolean validateItemComment(String itemComment) {
        return itemComment.length() > 0;
    }
    /**
     * This method validates the item date by ensuring it is within acceptable range.
     * @param itemDate                   date of the item as ItemDate structure
     * @return
     */
    public static boolean validateDate(ItemDate itemDate) {
        int month = itemDate.getMonth();
        boolean checkMonth = (month >= 0) && (month <= 12);
        boolean checkDay = false;
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                checkDay = itemDate.getDay() >= 0 && itemDate.getDay() <= 31;}
        if (month == 2){
            checkDay = itemDate.getDay() >= 0 && itemDate.getDay() <= 28;
        }
        switch ( month){
            case 4:
            case 6:
            case 9:
            case 11:
                checkDay = itemDate.getDay() >= 0 && itemDate.getDay() <= 30;}
        boolean checkYear = itemDate.getYear() >= 1000 && itemDate.getYear() <= 3000;
        return (checkYear && checkMonth && checkDay);
        }
    /**
     * This method validates the item date by ensuring it is within acceptable range.
     * @param date                       date of the item in string structure
     * @return
     */
    public static boolean validateDate(String date) {
        String[] splice = date.split( "[\\s-/]+" );
        int year = Integer.parseInt(splice[2]);
        int month = Integer.parseInt(splice[1]);
        int day = Integer.parseInt(splice[0]);
        boolean checkMonth = (month >= 0) && (month <= 12);
        boolean checkDay = false;
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                checkDay = day >= 0 && day <= 31;}
        if (month == 2){
            checkDay = day >= 0 && day <= 28;
        }
        switch ( month){
            case 4:
            case 6:
            case 9:
            case 11:
                checkDay = day >= 0 && day <= 30;}
        boolean checkYear = year >= 1000 && year <= 3000;
        return (checkYear && checkMonth && checkDay);
    }
    }
