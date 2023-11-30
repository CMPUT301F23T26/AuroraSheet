package com.example.aurorasheetapp;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    private List<Item> listItems;
    private List<Item> tagged_Items;

    public ItemManager() {
        listItems = new ArrayList<>();
        tagged_Items = new ArrayList<>();
    }

    public List<Item> getItems() {
        if (tagged_Items.size() == 0)
            return listItems;
        else
            return tagged_Items;
    }

    public Item getItem(int index) {
        return listItems.get(index);
    }

    public void setTagged_Items(List<Item> tagged_Items){
        this.tagged_Items = tagged_Items;
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

    public int size() {
        return listItems.size();
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
