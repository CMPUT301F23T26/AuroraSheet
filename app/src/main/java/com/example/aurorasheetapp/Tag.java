package com.example.aurorasheetapp;

import java.util.ArrayList;

/**
 * This class represents a tag with a name and a list of tagged items.
 */
public class Tag {
    private String name;
    private ArrayList<Item> tagged_items;
    private boolean main_selected;
    private boolean select_tagItem;

    private String documentID;

    public Tag(String name){
        this.name = name;
        this.tagged_items = new ArrayList<>();
        this.main_selected = false;
        this.select_tagItem = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    /**
     * Add an item to the list of tagged items
     * @param newItem
     * @return boolean indicating if the item was added
     */
    public void tagItem(Item newItem){
        tagged_items.add(newItem);
    }

    public void untagItem(Item newItem){
        tagged_items.remove(newItem);
    }

    public void select_tag(){
        main_selected = true;
    }

    public void unselect_tag(){
        main_selected = false;
    }

    public boolean getStatus(){
        return main_selected;
    }

    public void setStatus(boolean status) {main_selected = status;}

    public void select_tagItem() {select_tagItem = true;}

    public void unselect_tagItem() {select_tagItem = false;}

    public boolean getSelect_tagItem() {return select_tagItem;}

    public ArrayList<Item> getTagged_items(){return tagged_items;}

    public void setDocumentID(String ID){
        this.documentID = ID;
    }

    public String getDocumentID(){
        return documentID;
    }

}
