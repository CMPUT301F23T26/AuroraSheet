package com.example.aurorasheetapp;

public class ItemValidator {
public static boolean validateItemName(String itemName) {
        return itemName.length() > 0;
    }

    public static boolean validateItemDescription(String itemDescription) {
        return itemDescription.length() > 0;
    }

    public static boolean validateItemValue(String itemValue) {
        return itemValue.length() > 0;
    }

    public static boolean validateItemMake(String itemMake) {
        return itemMake.length() > 0;
    }

    public static boolean validateItemModel(String itemModel) {
        return itemModel.length() > 0;
    }

    public static boolean validateItemComment(String itemComment) {
        return itemComment.length() > 0;
    }
}
