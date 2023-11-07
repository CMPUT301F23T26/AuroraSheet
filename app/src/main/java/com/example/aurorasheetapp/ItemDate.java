package com.example.aurorasheetapp;
/**
 * A data structure that holds three integers as the date for the item
 * Added parsing support for string dates
 */
public class ItemDate {
    int year, month, day;
    /**
     * Constructs ItemDate with the specified attributes.
     *
     * @param day             the date of the item in integer
     * @param month           the month of the item in integer
     * @param year            the year of the item in integer
     *
     */
    public ItemDate(int day, int month, int year){
        this.year = year;
        this.month = month;
        this.day = day;
    }
    /**
     * Constructs ItemDate with the specified attributes, automatically parse formatted string
     * supported format: "dd/mm/yyyy"  -   "dd-mm-yyyy"   - "dd mm yyyy"
     * @param time             the date of the item in string form
     *
     */
    public ItemDate(String time){
        String[] splice = time.split( "[\\s-/]+" );
        this.year = Integer.parseInt(splice[2]);
        this.month = Integer.parseInt(splice[1]);
        this.day = Integer.parseInt(splice[0]);
    }
    /**
     * Set the date of the item and parse the input string
     * supported format: "dd/mm/yyyy"  -   "dd-mm-yyyy"   - "dd mm yyyy"
     * @param time          the date of the item in string form
     */
    public void setDate(String time){
        String[] splice = time.split( "[\\s-/]+" );
        this.year = Integer.parseInt(splice[2]);
        this.month = Integer.parseInt(splice[1]);
        this.day = Integer.parseInt(splice[0]);
    }

    /**
     * Set the year of the date
     * @param year          the year of the item in int form
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Set the month of the date
     * @param month          the month of the item in int form
     */

    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Set the day of the date
     * @param day          the day of the item in int form
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Get the year of the item date
     *
     * @return The year of purchase.
     */
    public int getYear() {
        return year;
    }
    /**
     * Get the month of the item date
     *
     * @return The month of purchase.
     */
    public int getMonth() {
        return month;
    }
    /**
     * Get the day of the item date
     *
     * @return The day of purchase.
     */
    public int getDay() {
        return day;
    }
    /**
     * Output as string form when prompted the string representation of this object
     *
     * @return the date of item in a string with format "dd-mm-yyyy".
     */
    public String toString(){

        return day + "-" + month + "-" + year;
    }
}