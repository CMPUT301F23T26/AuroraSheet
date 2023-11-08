package com.example.aurorasheetapp;

import java.util.ArrayList;

public class Tag {
    private String name;
    private ArrayList<Item> tagged_items;
    private boolean selected;

    public Tag(String name){
        this.name = name;
        this.tagged_items = new ArrayList<>();
        this.selected = false;
    }

    public Tag(String name, ArrayList<Item> tagged_items){
        this.name = name;
        this.tagged_items = tagged_items;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public boolean tagItem(Item newItem){
        try {
            tagged_items.add(newItem);
            return true; // returns true indicating item was added
        } catch (Exception e) {
            System.out.println(e.toString());
            return false; // returns false if unable to add the item
        }

    }

    public void untagItem(Item newItem){
        tagged_items.remove(newItem);
    }

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
