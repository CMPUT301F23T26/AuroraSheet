package com.example.aurorasheetapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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


    /* name;
    private ItemDate dateOfPurchase;
    private String briefDescription;
    private String make;
    private double serialNumber;
    private String model;
    private double estimatedValue;
    private String comment;
    private ArrayList<String> image;
    private int topImageIndex;
    private String documentID;
    private String path;

    private Boolean isSelected;

    private Boolean isHidden;*/


    // I am going to really overhaul this. there will be a getShownItems replacing most of the GetItems we see in mainactivity
    public void sortby(String query, Boolean reverse) {
        if (query.equals("name")) {
            listItems.sort(Comparator.comparing(Item::getHiddenness).thenComparing(Item::getName));
        }
        if (query.equals("dateOfPurchase")) {
            // NOT IMPLEMENTED
            // TODO
            // listItems.sort(Comparator.comparing(Item::getHiddenness).thenComparing(Item::getDateOfPurchase));
        }
        if (query.equals("briefDescription")) {
            listItems.sort(Comparator.comparing(Item::getHiddenness).thenComparing(Item::getBriefDescription));
        }
        if (query.equals("make")) {
            listItems.sort(Comparator.comparing(Item::getHiddenness).thenComparing(Item::getMake));
        }
        if (query.equals("serialNumber")) {
            listItems.sort(Comparator.comparing(Item::getHiddenness).thenComparing(Item::getSerialNumber));
        }
        if (query.equals("model")) {
            listItems.sort(Comparator.comparing(Item::getHiddenness).thenComparing(Item::getModel));
        }
        if (query.equals("estimatedValue")) {
            listItems.sort(Comparator.comparing(Item::getHiddenness).thenComparing(Item::getEstimatedValue));
        }
        if (query.equals("comment")) {
            listItems.sort(Comparator.comparing(Item::getHiddenness).thenComparing(Item::getComment));
        }
        if (reverse) {
            Collections.reverse(listItems);
            Comparator.comparing(Item::getHiddenness);
        }
    }

}
