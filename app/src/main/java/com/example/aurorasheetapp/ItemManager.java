package com.example.aurorasheetapp;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the list of items of a user.
 */
public class ItemManager {
    private List<Item> listItems;

    public ItemManager() {
        listItems = new ArrayList<>();
    }

    public List<Item> getItems() {
        return listItems;
    }

    public Item getItem(int index) {
        return listItems.get(index);
    }

    public void add(Item item) {
        listItems.add(item);
    }

    public Item remove(int index) {
        return listItems.remove(index);
    }

    public boolean isEmpty() {
        return listItems.isEmpty();
    }

    /**
     * Computes the total value of all the items in the list.
     * @return A string representation of the total value of all the items.
     */
    public String computeTotal() {
        double total = 0;
        for (Item item : listItems) {
            total += item.getEstimatedValue();
        }
        return String.format("%.2f", total);
    }
}
