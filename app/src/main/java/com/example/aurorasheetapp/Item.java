package com.example.aurorasheetapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an item with a date of purchase, description, make, serial number, model, estimated value, comment
 */
// I changed the date to integer for testing so feel free to change its type
public class Item {
    private String name;
    private ItemDate dateOfPurchase;
    private String briefDescription;
    private String make;
    private String serialNumber;
    private String model;
    private double estimatedValue;
    private String comment;
    private ArrayList<String> image;
    private int topImageIndex;
    private String path;
    private String documentID;
    private String taggedDocumentID;

    private Boolean isSelected;
    /**
     * Constructs an Item with the specified attributes.
     * @param name             The name of the item
     * @param dateOfPurchase   The date of purchase or acquisition.
     * @param briefDescription A brief description of the item.
     * @param make             The make or manufacturer of the item.
     * @param model            The model of the item.
     * @param serialNumber     The serial number (if applicable) of the item.
     * @param estimatedValue   The estimated value of the item.
     * @param comment          Additional comments about the item.
     */
    public Item(String name, ItemDate dateOfPurchase, String briefDescription, String make,
                String serialNumber, String model, double estimatedValue, String comment) {
        this.name = name;
        this.dateOfPurchase = dateOfPurchase;
        this.briefDescription = briefDescription;
        this.make = make;
        this.model = model;
        this.serialNumber = serialNumber;
        this.estimatedValue = estimatedValue;
        this.comment = comment;
        this.isSelected = false;

        //image array initialization
        this.topImageIndex = -1;
        path = null;
        image = new ArrayList<String>();
    }

    public Item(String name, ItemDate dateOfPurchase, String briefDescription, String make,
                String serialNumber, String model, double estimatedValue, String comment, String documentID) {
        this.name = name;
        this.dateOfPurchase = dateOfPurchase;
        this.briefDescription = briefDescription;
        this.make = make;
        this.model = model;
        this.serialNumber = serialNumber;
        this.estimatedValue = estimatedValue;
        this.comment = comment;
        this.documentID = documentID;

        this.isSelected = false;

        //image array initialization
        this.topImageIndex = -1;
        path = null;
        image = new ArrayList<String>();
    }


    public Item() {
        }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    /**
     * Get the name of the item.
     *
     * @return The name of item.
     */
    public String getName() {
        return name;
    }
    /**
     * set the name of the item.
     *@param name The name of the item to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the date of purchase or acquisition.
     *
     * @return The date of purchase.
     */
    public ItemDate getDateOfPurchase() {
        return dateOfPurchase;
    }
    /**
     * Set the date of purchase or acquisition.
     * @param dateOfPurchase The date of purchase to set.
     */
    public void setDateOfPurchase(ItemDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }
    /**
     * Get a brief description of the item.
     * @return The brief description.
     */
    public String getBriefDescription() {
        return briefDescription;
    }
    /**
     * Set a brief description of the item.
     * @param briefDescription The brief description to set.
     */
    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }
    /**
     * Get make of the item.
     * @return make.
     */
    public String getMake() {
        return make;
    }
    /**
     * Set make of the item.
     * @param make The make to set.
     */
    public void setMake(String make) {
        this.make = make;
    }
    /**
     * Get serial number of the item.
     * @return Serial number.
     */
    public String getSerialNumber() {
        return serialNumber;
    }
    /**
     * Set serial number of the item.
     * @param serialNumber Serial Number to set.
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    /**
     * Get model of the item.
     * @return model.
     */
    public String getModel() {
        return model;
    }
    /**
     * Set model of the item.
     * @param model model to set.
     */
    public void setModel(String model) {
        this.model = model;
    }
    /**
     * Get estimated value of the item.
     * @return estimated value.
     */
    public double getEstimatedValue() {
        return estimatedValue;
    }
    /**
     * Set estimated value of the item.
     * @param estimatedValue estimated value to set.
     */
    public void setEstimatedValue(double estimatedValue) {
        this.estimatedValue = estimatedValue;
    }
    /**
     * Get additional comments about the item.
     * @return Additional comments.
     */
    public String getComment() {
        return comment;
    }
    /**
     * Set additional comments about the item.
     * @param comment Additional comments to set.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public int getTopImageIndex() {
        return topImageIndex;
    }

    public void setTopImageIndex(int topImageIndex) {
        this.topImageIndex = topImageIndex;
    }

    /**
     * For use in the multiselect.
     * @return Whether this item is selected
     */
    public Boolean getSelection() {
        return this.isSelected;
    }
    /**
     * Set the item's isSelected to true. for use in multiselect
     *
     */
    public void select() {
        this.isSelected = true;
    }
    /**
     * Set the item's isSelected to false. for use in multiselect
     *
     */
    public void unselect() {
        this.isSelected = false;
    }
    /**
     * Toggle the item's selection. for use in multiselect
     *
     */
    public void toggleSelect() {
        if (this.isSelected) { this.isSelected = false; } else { this.isSelected = true; }
    }

    public String getDocumentId() {
        return documentID;
    }

    public void setDocumentId(String documentId) {
        this.documentID = documentId;
    }

    public String getTaggedDocumentId() {return taggedDocumentID;}

    public void setTaggedDocumentId(String id) {taggedDocumentID = id;}

}


