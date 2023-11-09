package com.example.aurorasheetapp;

import java.util.ArrayList;


/**
 * Represents an tag with name and as a container of item objects that are tagged by this tag.
 */
public class Tag {
    private String name;
    private ArrayList<Item> tagged_items;
    private boolean selected;

    /**
     * One constructor method to create an Tag with only a specified name.
     *
     * @param name             The name of the tag
     */
    public Tag(String name){
        this.name = name;
        this.tagged_items = new ArrayList<>();
        this.selected = false;
    }

    /**
     * Another constructor method to create a Tag with both a name
     * and a list of Items.
     *
     * @param name             The name of the item
     * @param tagged_items     The list of Items tagged by this Tag
     */
    public Tag(String name, ArrayList<Item> tagged_items){
        this.name = name;
        this.tagged_items = tagged_items;
    }

    /**
     * This method allows the Tag to be renamed.
     *
     * @param name             The name of the Tag
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method retrieves the name of the Tag.
     *
     * @return name            The name of the Tag
     */
    public String getName(){
        return name;
    }

    /**
     * This method allows a single Item to be tagged by this tag.
     *
     * @param newItem          Item to be tagged
     * @return boolean         boolean value on if addition was successful
     */
    public boolean tagItem(Item newItem){
        try {
            tagged_items.add(newItem);
            return true; // returns true indicating item was added
        } catch (Exception e) {
            System.out.println(e.toString());
            return false; // returns false if unable to add the item
        }

    }

    /**
     * This method allows an Item to be untagged by this Tag.
     *
     * @param newItem           Item to be removed from the ArrayList of Items
     */
    public void untagItem(Item newItem){
        tagged_items.remove(newItem);
    }

    /**
     * 
     */
    public void select_tag(){
        selected = true;
    }

    public void unselect_tag(){
        selected = false;
    }

    public boolean getStatus(){
        return selected;
    }

    public ArrayList<Item> getTagged_items(){return tagged_items;}
}
