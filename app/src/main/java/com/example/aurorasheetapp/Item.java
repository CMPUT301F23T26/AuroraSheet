package com.example.aurorasheetapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an item with a date of purchase, description, make, serial number, model, estimated value, comment
 */
// I changed the date to integer for testing so feel free to change its type
public class Item {
    private String name;
    private ItemDate dateOfPurchase;
    private String briefDescription;
    private String make;
    private double serialNumber;
    private String model;
    private double estimatedValue;
    private String comment;
    private ArrayList<String> image;
    private String documentID;

    private Boolean isSelected;

    private Boolean isHidden;

    public List<Tag> tags;

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
                double serialNumber, String model, double estimatedValue, String comment) {
        this.name = name;
        this.dateOfPurchase = dateOfPurchase;
        this.briefDescription = briefDescription;
        this.make = make;
        this.model = model;
        this.serialNumber = serialNumber;
        this.estimatedValue = estimatedValue;
        this.comment = comment;

        this.isSelected = false;
        this.isHidden = false;

        this.tags = new ArrayList<Tag>();

        image = new ArrayList<String>();
    }

    public Item(String name, ItemDate dateOfPurchase, String briefDescription, String make,
                double serialNumber, String model, double estimatedValue, String comment, String documentID) {
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


        image = new ArrayList<String>();
    }


    public Item() {
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
    public double getSerialNumber() {
        return serialNumber;
    }
    /**
     * Set serial number of the item.
     * @param serialNumber Serial Number to set.
     */
    public void setSerialNumber(double serialNumber) {
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    // -----------------------------------------------------------------------------------------
    // selection and hiddenness

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

    /**
     * For use in sorting and filtering.
     * @return Whether this item is hidden
     */
    public Boolean getHiddenness() {
        return this.isHidden;
    }
    /**
     * Set the item's isHidden to true. for use in filtering
     *
     */
    public void hide() {
        this.isHidden = true;
    }
    /**
     * Set the item's isHidden to false. for use in filtering
     *
     */
    public void unhide() {
        this.isHidden = false;
    }
    /**
     * Toggle the item's hiddenness. for use in filtering
     *
     */
    public void toggleHidden() {
        if (this.isSelected) { this.isHidden = false; } else { this.isHidden = true; }
    }


    public String getDocumentId() {
        return documentID;
    }

    public void setDocumentId(String documentId) {
        this.documentID = documentId;
    }

}


