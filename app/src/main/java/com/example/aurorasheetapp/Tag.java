package com.example.aurorasheetapp;


import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * This class represents a tag with a name and a list of tagged items.
 */
public class Tag{
    private String name;
    private ArrayList<Item> tagged_items;
    private boolean main_selected;
    private boolean select_tagItem;
    private boolean tmp_status;

    private String documentID;

    /**
     * Constructor of Tag objects
     * @param name name of the tag in string form
     */
    public Tag(String name){
        this.name = name;
        this.tagged_items = new ArrayList<>();
        this.main_selected = false;
        this.select_tagItem = false;
    }

    /**
     * sets the name of the tag
     * @param name name of the tag in string form
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns the name of the tag
     * @return the name of the tag in string form
     */
    public String getName(){
        return name;
    }

    /**
     * Add an item to the list of tagged items
     * @param newItem new Item to be tagged.
     *
     */
    public void tagItem(Item newItem){
        tagged_items.add(newItem);
    }

    /**
     * deletes an item from the list of tagged items
     * @param newItem item to be deleted
     */
    public void untagItem(Item newItem){
        tagged_items.remove(newItem);
    }

    /**
     * toggle tag selection status
     */
    public void select_tag(){
        main_selected = true;
    }

    /**
     * toggle off tag selection status
     */
    public void unselect_tag(){
        main_selected = false;
    }

    /**
     * get the selection status
     * @return the selection status as boolean
     */
    public boolean getStatus(){
        return main_selected;
    }

    /**
     * set the selection status
     * @param status the boolean to set the selection status as
     */
    public void setStatus(boolean status) {main_selected = status;}

    /**
     * toggle tagged item selection
     */
    public void select_tagItem() {select_tagItem = true;}

    /**
     * toggle off tag item selection
     */
    public void unselect_tagItem() {select_tagItem = false;}

    /**
     * returns the selected tag item status
     * @return the selected tag item status as boolean
     */
    public boolean getSelect_tagItem() {return select_tagItem;}

    /**
     * gets the all the items with the tag
     * @return Arraylist of items with the tag
     */
    public ArrayList<Item> getTagged_items(){return tagged_items;}

    /**
     * set the document ID of the tag
     * @param ID firebase id of the tag to be set
     */
    public void setDocumentID(String ID){
        this.documentID = ID;
    }

    /**
     * get the document ID of the tag
     * @return firebase id of the tag in string form
     */
    public String getDocumentID(){
        return documentID;
    }

    /**
     * set temporary status
     * @param status boolean of the status to be set
     */
    public void setTmp_status(boolean status){tmp_status = status;}

    /**
     * get the temporary status
     * @return temporary status in boolean form
     */
    public boolean getTmp_status(){return tmp_status;}
}
