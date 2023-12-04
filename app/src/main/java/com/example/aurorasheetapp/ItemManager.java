package com.example.aurorasheetapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * This class represents the list of items of a user.
 */
public class ItemManager {
    private List<Item> listItems;
    private List<Item> tagged_Items;


    public ArrayList<Integer> sortingStatus;

    public List<Item> shownItems;




    // Variables used for filtering
    public ItemDate filterBeforeDate;

    public ItemDate filterAfterDate;

    public String filterDescriptionSubstring;

    public String filterMake;

    private Boolean filterDescriptionSubstringExcludeMode;
    private Boolean filterMakeExcludeMode;

    public List<Tag> filterMustIncludeTags;
    public List<Tag> filterMustNotIncludeTags;

    public ItemManager() {
        listItems = new ArrayList<>();
        tagged_Items = new ArrayList<>();

        shownItems = new ArrayList<>();

        sortingStatus = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0));


        filterDescriptionSubstringExcludeMode = false;
        filterMakeExcludeMode = false;

        filterMustIncludeTags = new ArrayList<Tag>();
        filterMustNotIncludeTags = new ArrayList<Tag>();

    }

    public List<Item> getItems() {
        return listItems;
    }

    public List<Item> getItems(Boolean showTagItem){
        return tagged_Items;
    }

    public Item getItem(int index) {
        return listItems.get(index);
    }

    public void addTagged_Items(Item item) {this.tagged_Items.add(item);}

    public void delTagged_Items(Item item) {this.tagged_Items.remove(item);}

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



    // -----------------------------------------------------------------------------------------------------
    // functions about sorting



    // this is a default generated setter. If, when calling this, you don't know all 8 of these values,
    // you should create a new list that's 8 integers long, with unknown values set to 0
    public void setSortingStatus(ArrayList<Integer> sortingStatus) {
        this.sortingStatus = sortingStatus;
    }



    /**
     * With a correct sortingStatus list in place, sort the list of items as specified
     */
    public void doSorting() {




        ArrayList<Comparator> comparators = new ArrayList<>();
        ArrayList<Comparator> activeComparators = new ArrayList<>();

        // to make iteration easier, we will iterate through all these comparators
        comparators.add(Comparator.comparing(Item::getNameLower));
        comparators.add(Comparator.comparing(Item::getDateOfPurchase));
        comparators.add(Comparator.comparing(Item::getSerialNumber));
        comparators.add(Comparator.comparing(Item::getModelLower));
        comparators.add(Comparator.comparing(Item::getEstimatedValue));
        comparators.add(Comparator.comparing(Item::getMakeLower));
        comparators.add(Comparator.comparing(Item::getCommentLower));
        comparators.add(Comparator.comparing(Item::getDescriptionLower));

        activeComparators.add(Comparator.comparing(Item::getHiddenness));


        for (int i = 0; i < 8; i++) {

            if (sortingStatus.get(i) == 2) {
                activeComparators.add(comparators.get(i).reversed());
            } else if (sortingStatus.get(i) == 1) {
                activeComparators.add(comparators.get(i));
            }
            // if 0, leave it alone. there is nothing to do
        }

        // Thank you to Radiodef on StackExchange (2014 Jan 23): "Efficient way to sort a list by several comparators in Java"
        // https://stackoverflow.com/questions/21319128/efficient-way-to-sort-a-list-by-several-comparators-in-java

        final Comparator<Item> combined = (e1, e2) -> {
            for (Comparator<Item> thiscomparator : activeComparators) {

                int result = thiscomparator.compare(e1, e2);

                if (result != 0)
                    return result;
            }
            return 0;
        };

        Collections.sort(listItems, combined);

    }


    // ----------------------------------------------------------------------------------
    // functions for filtering
    // ADD FILTER FUNCTIONS HERE

    /**
     * Any item with a date before the provided one will become hidden
     *
     * @param providedDate an itemDate representing the earliest date that a shown item should have
     */
    public void filterDatesBefore(ItemDate providedDate) {
        for (Item item : listItems) {

            if (item.getDateOfPurchase().compareTo(providedDate) < 0) {
                item.hide();
            }
        }
    }

    /**
     * Any item with a date after the provided one will become hidden
     *
     * @param providedDate an itemDate representing the latest date that a shown item should have
     */
    public void filterDatesAfter(ItemDate providedDate) {
        for (Item item : listItems) {
            if (item.getDateOfPurchase().compareTo(providedDate) > 0) {
                item.hide();
            }
        }
    }

    /**
     * Hides items depending on if their description contains a given substring. case insensitive.
     *
     * @param substring Items with this substring found in the string of their description will be included
     * @param exclude   If this is set to false, finding the substring will result in an item's *exclusion* instead
     */
    public void filterItemsForDescriptionKeyword(String substring, boolean exclude) {
        for (Item item : listItems) {
            String fullstring = item.getBriefDescription().toLowerCase();
            substring = substring.toLowerCase();


            // ^ means XOR.
            // by default all items are shown
            // if the substring is contained and the mode is exclude, hide the item
            // if the substring is not contained and the mode is include, hide the item
            if (fullstring.contains(substring) ^ !exclude) {
                item.hide();
                //Log.d("desckeyword","Item Hidden!");
            }
        }
    }

    /**
     * Hides items depending on if their make matches a given string. case insensitive.
     *
     * @param givenMakeName Items with this make will be included
     * @param exclude       If this is set to true, finding a matching make will result in an item's *exclusion* instead
     */
    public void filterItemsForMake(String givenMakeName, boolean exclude) {

        for (Item item : listItems) {
            String itemMakeName = item.getMake().toLowerCase();
            givenMakeName = givenMakeName.toLowerCase();

            // ^ means XOR.
            // by default all items are shown
            // if the make matches and the mode is exclude, hide the item
            // if the make doesn't match and the mode is include, hide the item
            if (itemMakeName.equals(givenMakeName) ^ !exclude) {
                item.hide();
                //Log.d("makefilter","Item Hidden!");
            }
        }
    }

    /**
     *
     * @param list1
     * @param list2
     * @return a list containing all elements that are in both lists
     */
    private List<Tag> intersectionOfList(List<Tag> list1, List<Tag> list2) {
        List<Tag> intersection = list1;
        intersection.retainAll(list2);

        return intersection;

    }

    /**
     * Hides all items that do not share a tag with the active list of MustIncludeTags
     */
    public void filterForTags() {
        // unlike the other ones, this filter function takes its entries directly from the lists provided in the class
        for (Item item : listItems) {

            if (intersectionOfList(item.getTags(),filterMustIncludeTags).size() < 1) {
                item.hide();
            }
        }
    }


    /**
     * Hides all items that do share a tag with the active list of MustNotIncludeTags
     */
    public void filterAgainstTags() {
        // unlike the other ones, this filter function takes its entries directly from the lists provided in the class
        for (Item item : listItems) {

            if (intersectionOfList(item.getTags(),filterMustNotIncludeTags).size() > 0) {
                item.hide();
            }
        }
    }



    /**
     * shows all items
     */
    public void clearFilters() {
        for (Item item : listItems) {
            item.unhide();
        }
    }

    /**
     * Rests all selections and filters, then hides items according to active filters.
     * Will sort the filtered item list afterwards.
     *
     * Filters for Date (before / after), a substring (keyword) in the description, Make, and Tags (both their presence and their absence).
     */
    public void doFiltering() {
        // reset the list to having nothing filtered or selected
        deselectAllItems(); // safety, to prevent deletion of a hidden item
        clearFilters();

        printItemHiddenness();
        // apply every filter

        if (Objects.nonNull(filterBeforeDate)) {
            filterDatesBefore(filterBeforeDate);
        } printItemHiddenness();
        if (Objects.nonNull(filterAfterDate)) {
            filterDatesAfter(filterAfterDate);
        } printItemHiddenness();
        if (Objects.nonNull(filterDescriptionSubstring) && !"".equals(filterDescriptionSubstring) && Objects.nonNull(filterDescriptionSubstringExcludeMode)) {
            filterItemsForDescriptionKeyword(filterDescriptionSubstring,filterDescriptionSubstringExcludeMode);
        } printItemHiddenness();
        if (Objects.nonNull(filterMake) && !"".equals(filterMake) && Objects.nonNull(filterMakeExcludeMode)) {
            filterItemsForMake(filterMake,filterMakeExcludeMode);
        } printItemHiddenness();

        // filterForTags(); filterAgainstTags();

        // apply sorting, so that the hidden items go to the end of the active itemsList
        doSorting();
    }

    // I was going to make one function that updates everything, but I think it is wiser to just use lots of individual setters, in case the user only wants to edit one
    // here are those setters
    // In the implementation, if a filter is not active, set it to null

    public void setFilterBeforeDate(ItemDate filterBeforeDate) {
        this.filterBeforeDate = filterBeforeDate;
    }

    public void setFilterAfterDate(ItemDate filterAfterDate) {
        this.filterAfterDate = filterAfterDate;
    }

    public void setFilterDescriptionSubstring(String filterDescriptionSubstring) {
        this.filterDescriptionSubstring = filterDescriptionSubstring;
    }

    public void setFilterMake(String filterMake) {
        this.filterMake = filterMake;
    }

    public void setFilterDescriptionSubstringExcludeMode(Boolean filterDescriptionSubstringExcludeMode) {
        this.filterDescriptionSubstringExcludeMode = filterDescriptionSubstringExcludeMode;
    }

    public void setFilterMakeExcludeMode(Boolean filterMakeExcludeMode) {
        this.filterMakeExcludeMode = filterMakeExcludeMode;
    }

    public void setFilterMustIncludeTags(List<Tag> filterMustIncludeTags) {
        this.filterMustIncludeTags = filterMustIncludeTags;
    }

    public void setFilterMustNotIncludeTags(List<Tag> filterMustNotIncludeTags) {
        this.filterMustNotIncludeTags = filterMustNotIncludeTags;
    }

    // and here are some that do not involved completely replacing the old tag lists

    public void removeFromMustIncludeTags(Tag tag) {
        if (filterMustIncludeTags.contains(tag)) {
            filterMustIncludeTags.remove(tag);
        }
    }
    public void addToMustIncludeTags(Tag tag) {
        if (!filterMustIncludeTags.contains(tag)) {
            filterMustIncludeTags.add(tag);
        }
    }
    public void removeFromMustNotIncludeTags(Tag tag) {
        if (filterMustNotIncludeTags.contains(tag)) {
            filterMustNotIncludeTags.remove(tag);
        }
    }
    public void addToMustNotIncludeTags(Tag tag) {
        if (!filterMustNotIncludeTags.contains(tag)) {
            filterMustNotIncludeTags.add(tag);
        }
    }

    public void update_shown_items() {
        shownItems.clear();
        for (Item item:listItems) {
            // if the item is not hidden, don't add it to show

            if (!item.getHiddenness()) {

                shownItems.add(item);
            }
        }
    }







    // --------------------------------------------------------------------
    // methods about selection

    /**
     * Make all items unselected
     * <p>
     * this copies the code that was in MainActivity, but does not run updateSelection
     */
    public void deselectAllItems() {
        for (Item thisitem : listItems) {
            thisitem.unselect();

        }
    }

    /**
     * Get the index of the currently selected item.
     * If there are multiple selected items, it will return the first one
     *
     * @return The index of the item that is selected. If no item is selected, it returns -1
     */
    public int getTheOneSelectedItem() {
        int listsize = listItems.size();
        for (int index = 0; index < listsize; index++) {
            if (listItems.get(index).getSelection()) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Calculate how many items are currently selected
     *
     * @return The number of items that are currently selected
     */

    public int countSelectedItems() {
        int count = 0;
        for (Item thisitem : listItems) {
            if (thisitem.getSelection()) {
                count += 1;
            }
        }
        return count;
    }

    /**
     * Get a list containing the indices of all currently selected item.
     *
     * @return The index of the item that is selected. If no item is selected, it returns -1
     */
    public ArrayList<Integer> getListOfSelectedItems() {
        ArrayList<Integer> selected_items = new ArrayList<Integer>();
        int listsize = listItems.size();
        for (int index = 0; index < listsize; index++) {
            if (listItems.get(index).getSelection()) {
                selected_items.add(index);
            }
        }
        return selected_items;

    }

    private void printItemHiddenness() {
        for (Item item:listItems) {
            Log.d("hiddennessList", String.format("item %s is hidden: %b", item.getName(), item.getHiddenness()));
        }
    }
}

