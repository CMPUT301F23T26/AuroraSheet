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
}
