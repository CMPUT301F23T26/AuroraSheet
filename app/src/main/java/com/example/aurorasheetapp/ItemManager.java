package com.example.aurorasheetapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ItemManager {
    private List<Item> listItems;

    public ArrayList<Integer> sortingStatus;

    public List<Item> shownItems;


    public ItemManager() {
        listItems = new ArrayList<>();
        shownItems = new ArrayList<>();
        sortingStatus = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0));
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
     *
     * @return A string representation of the total value of all the items.
     */
    public String computeTotal() {
        double total = 0;
        for (Item item : listItems) {
            total += item.getEstimatedValue();
        }
        return String.format("%.2f", total);
    }


    /**
     * With a correct sortingStatus list in place, sort the list of items as specified
     *
     *
     */
    public void doSorting() {


        ArrayList<Comparator> comparators = new ArrayList<>();
        ArrayList<Comparator> activeComparators = new ArrayList<>();

        // to make iteration easier, we will iterate through all these comparators
        comparators.add(Comparator.comparing(Item::getName));
        comparators.add(Comparator.comparing(Item::getDateOfPurchase));
        comparators.add(Comparator.comparing(Item::getMake));
        comparators.add(Comparator.comparing(Item::getSerialNumber));
        comparators.add(Comparator.comparing(Item::getModel));
        comparators.add(Comparator.comparing(Item::getEstimatedValue));
        comparators.add(Comparator.comparing(Item::getComment));
        comparators.add(Comparator.comparing(Item::getBriefDescription));

        activeComparators.add(Comparator.comparing(Item::getHiddenness));



        for (int i = 0; i < 8; i++) {
            if (sortingStatus.get(i) == 2) {
                activeComparators.add(comparators.get(i).reversed());
            } else if (sortingStatus.get(i) == 1) {
                activeComparators.add(comparators.get(i));
            }
        }

        // Thank you to Radiodef on StackExchange (2014 Jan 23): "Efficient way to sort a list by several comparators in Java"
        // https://stackoverflow.com/questions/21319128/efficient-way-to-sort-a-list-by-several-comparators-in-java

        final Comparator<Item> combined = (e1, e2) -> {
            for(Comparator<Item> thiscomparator:activeComparators) {

                int result = thiscomparator.compare(e1, e2);

                if(result != 0)
                    return result;
            }
            return 0;
        };

        Collections.sort(listItems,combined);

    }

    // ADD FILTER FUNCTIONS HERE
    /**
     * Any item with a date before the provided one will become hidden
     *
     */
    public void FilterDatesBefore(ItemDate providedDate) {
        for(Item item:listItems) {
            if (item.getDateOfPurchase().compareTo(providedDate) < 0) {
                item.hide();
            }
        }
    }

    /**
     * shows all items
     */
    public void clearFilters() {
        for(Item item:listItems) {
            item.unhide();
        }
    }
}
