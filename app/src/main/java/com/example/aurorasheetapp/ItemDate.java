package com.example.aurorasheetapp;
/**
 * A data structure that holds three integers as the date for the item
 * Added parsing support for string dates
 */
public class ItemDate implements Comparable<ItemDate> {
    int year, month, day;

    /**
     * Constructs ItemDate with the specified attributes.
     *
     * @param day   the date of the item in integer
     * @param month the month of the item in integer
     * @param year  the year of the item in integer
     */
    public ItemDate(int day, int month, int year) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Constructs ItemDate with the specified attributes, automatically parse formatted string
     * supported format: "dd/mm/yyyy"  -   "dd-mm-yyyy"   - "dd mm yyyy"
     *
     * @param time the date of the item in string form
     */
    public ItemDate(String time) {
        int[] dates = ItemDate.parseDate(time);
        this.year = dates[2];
        this.month = dates[1];
        this.day = dates[0];
    }

    /**
     * Set the date of the item and parse the input string
     * supported format: "dd/mm/yyyy"  -   "dd-mm-yyyy"   - "dd mm yyyy"
     *
     * @param time the date of the item in string form
     */
    public void setDate(String time) {
        int[] dates = ItemDate.parseDate(time);
        this.year = dates[2];
        this.month = dates[1];
        this.day = dates[0];
    }

    /**
     * Set the year of the date
     *
     * @param year the year of the item in int form
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Set the month of the date
     *
     * @param month the month of the item in int form
     */

    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Set the day of the date
     *
     * @param day the day of the item in int form
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
    public String toString() {

        return day + "-" + month + "-" + year;
    }

    /**
     * Takes in a formatted string containing date and returns an int array of length 3
     * supported format: "dd/mm/yyyy"  -   "dd-mm-yyyy"   - "dd mm yyyy"
     * returned int array has day, month, year at the 1st, 2nd and 3rd index
     *
     * @param time the date of the item in string form
     * @return dates int array that has day, month, year at 1st, 2nd and 3rd index
     */
    public static int[] parseDate(String time) {
        int[] dates = {0, 0, 0};
        String[] date = time.split("[\\s-/]+");
        dates[2] = Integer.parseInt(date[2]);
        dates[1] = Integer.parseInt(date[1]);
        dates[0] = Integer.parseInt(date[0]);
        return dates;
    }

    /**
    * Makes dates comparable
    *
    * @param anotherItemDate another instance of this class to compare to
    * @return int, positive if this instance is later, negative if this instance is later
    */
    public int compareTo(ItemDate anotherItemDate) {
        int thisnumber = this.year                 * 10000 + this.month                 * 100 + this.day ;
        int thatnumber = anotherItemDate.getYear() * 10000 + anotherItemDate.getMonth() * 100 + anotherItemDate.getDay();
        return thisnumber - thatnumber;
    }
}

