package com.example.aurorasheetapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FilterCriteria {
    private ItemDate startDate;
    private ItemDate endDate;

    public FilterCriteria(ItemDate startDate, ItemDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // ... (Getters and setters for startDate and endDate)

    public List<Item> applyFilters(List<Item> originalItems) {
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : originalItems) {
            if (meetsCriteria(item)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    private boolean meetsCriteria(Item item) {
        ItemDate itemDate = item.getDateOfPurchase();
        if (itemDate != null) {
            boolean isBefore = isBefore(itemDate, startDate);
            boolean isAfter = isAfter(itemDate, endDate);
            Log.d("FilterCriteria", "Item Date: " + itemDate.toString() +
                    ", Start Date: " + startDate.toString() +
                    ", End Date: " + endDate.toString() +
                    ", isBefore: " + isBefore +
                    ", isAfter: " + isAfter);
            return !isBefore(itemDate, startDate) && !isAfter(itemDate, endDate);
        }
        return false;
    }

    private boolean isBefore(ItemDate itemDate, ItemDate comparisonDate) {
        // Compare years, then months, then days
        return (itemDate.getYear() < comparisonDate.getYear()) ||
                (itemDate.getYear() == comparisonDate.getYear() && itemDate.getMonth() < comparisonDate.getMonth()) ||
                (itemDate.getYear() == comparisonDate.getYear() && itemDate.getMonth() == comparisonDate.getMonth() && itemDate.getDay() < comparisonDate.getDay());
    }

    private boolean isAfter(ItemDate itemDate, ItemDate comparisonDate) {
        // Compare years, then months, then days
        return (itemDate.getYear() > comparisonDate.getYear()) ||
                (itemDate.getYear() == comparisonDate.getYear() && itemDate.getMonth() > comparisonDate.getMonth()) ||
                (itemDate.getYear() == comparisonDate.getYear() && itemDate.getMonth() == comparisonDate.getMonth() && itemDate.getDay() > comparisonDate.getDay());
    }
}
