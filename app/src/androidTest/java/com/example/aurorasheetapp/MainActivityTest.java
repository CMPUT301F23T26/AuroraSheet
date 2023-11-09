package com.example.aurorasheetapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    // define rules
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<MainActivity>(MainActivity.class);
    // define tests
    @Test
    public void testAddItem(){
        // Click on Add Item button
        onView(withId(R.id.buttonAdd)).perform(click());
        // Enter in test data for the item
        onView(withId(R.id.itemName)).perform(ViewActions.typeText("Test Item"));
        onView(withId(R.id.itemDescription)).perform(ViewActions.typeText("Test Description"));
        onView(withId(R.id.itemValue)).perform(ViewActions.typeText("100"));
        onView(withId(R.id.itemSerialNumber)).perform(ViewActions.typeText("100"));
        onView(withId(R.id.itemMake)).perform(ViewActions.typeText("Test Make"));
        onView(withId(R.id.itemModel)).perform(ViewActions.typeText("Test Model"));
        onView(withId(R.id.itemComment)).perform(ViewActions.typeText("Test Comment"));
        // Click on Confirm button
        onView(withId(R.id.addItemButton)).perform(click());

        //todo
        // Check that the item was added to the list and possibly firebase?
    }

//    @Test
//    public void testEditItem(){
//
//        // Click on Add Item button
//        onView(withId(R.id.buttonAdd)).perform(click());
//        // Enter in test data for the item
//        onView(withId(R.id.itemName)).perform(ViewActions.typeText("Test Item"));
//        onView(withId(R.id.itemDescription)).perform(ViewActions.typeText("Test Description"));
//        onView(withId(R.id.itemValue)).perform(ViewActions.typeText("100"));
//        onView(withId(R.id.itemSerialNumber)).perform(ViewActions.typeText("100"));
//        onView(withId(R.id.itemMake)).perform(ViewActions.typeText("Test Make"));
//        onView(withId(R.id.itemModel)).perform(ViewActions.typeText("Test Model"));
//        onView(withId(R.id.itemComment)).perform(ViewActions.typeText("Test Comment"));
//        // Click on Confirm button
//        onView(withId(R.id.addItemButton)).perform(click());
//
//        onView(withId(R.id.recyclerView)).check(matches(withText("Test Item")));
//        //not sure how to do a item click with expresso and recyclerView
//
//        onView(withId(R.id.buttonEdit)).perform(click());
//        // Enter in test data for the item
//        onView(withId(R.id.itemName_edit)).perform(ViewActions.typeText("Test Item"));
//        onView(withId(R.id.itemDescription_edit)).perform(ViewActions.typeText("Test Description"));
//        onView(withId(R.id.itemValue_edit)).perform(ViewActions.typeText("100"));
//        onView(withId(R.id.date_edit)).perform(ViewActions.typeText("01-01-1400"));
//        onView(withId(R.id.itemSerialNumber_edit)).perform(ViewActions.typeText("100"));
//        onView(withId(R.id.itemMake_edit)).perform(ViewActions.typeText("Test Make"));
//        onView(withId(R.id.itemModel_edit)).perform(ViewActions.typeText("Test Model"));
//        onView(withId(R.id.itemComment_edit)).perform(ViewActions.typeText("Test Comment"));
//        // Click on Confirm button
//        onView(withId(R.id.confirmButton_edit)).perform(click());
//
//
//    }
}