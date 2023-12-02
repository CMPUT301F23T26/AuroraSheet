package com.example.aurorasheetapp;

import java.util.ArrayList;
import java.util.List;

public class FilterCriteria {
    private ItemDate startDate;
    private ItemDate endDate;
    // Other filter criteria

    // Constructor, getters, and setters
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
        // Check date range
        if (startDate != null && endDate != null && itemDate != null) {
            if (isBefore(itemDate, startDate) || isAfter(itemDate, endDate)) {
                return false;
            }
        }
        // Add other criteria checks here
        return true;
    }

    private boolean isBefore(ItemDate itemDate, ItemDate comparisonDate) {
        // Implement comparison logic
        // Return true if itemDate is before comparisonDate
        return (itemDate.getYear() < comparisonDate.getYear()) ||
                (itemDate.getYear() == comparisonDate.getYear() && itemDate.getMonth() < comparisonDate.getMonth()) ||
                (itemDate.getYear() == comparisonDate.getYear() && itemDate.getMonth() == comparisonDate.getMonth() && itemDate.getDay() < comparisonDate.getDay());
    }

    private boolean isAfter(ItemDate itemDate, ItemDate comparisonDate) {
        // Implement comparison logic
        // Return true if itemDate is after comparisonDate
        return (itemDate.getYear() > comparisonDate.getYear()) ||
                (itemDate.getYear() == comparisonDate.getYear() && itemDate.getMonth() > comparisonDate.getMonth()) ||
                (itemDate.getYear() == comparisonDate.getYear() && itemDate.getMonth() == comparisonDate.getMonth() && itemDate.getDay() > comparisonDate.getDay());
    }
}
