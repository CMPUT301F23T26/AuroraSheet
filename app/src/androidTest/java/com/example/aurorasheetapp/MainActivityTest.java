package com.example.aurorasheetapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    // define rules
    @Rule
    public ActivityScenarioRule<Login> loginActivityRule =
            new ActivityScenarioRule<>(Login.class);

    @Test
    public void testAddItemAfterLogin() {
        // Login to app
        onView(withId(R.id.user_login)).perform(ViewActions.typeText("bonobo"));
        onView(withId(R.id.password_login)).perform(ViewActions.typeText("cmput301"), ViewActions.closeSoftKeyboard());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.loginButton)).perform(ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Click on Add Item button
        onView(withId(R.id.buttonAdd)).perform(click());

        String randomItemName = UUID.randomUUID().toString();
        // Enter in test data for the item and close the keyboard after each entry
        onView(withId(R.id.itemName)).perform(ViewActions.typeText(randomItemName), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemDescription)).perform(ViewActions.typeText("Test Description"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemDate)).perform(ViewActions.click());
        onView(withText("OK")).perform(ViewActions.click());
        onView(withId(R.id.itemValue)).perform(ViewActions.typeText("100"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemSerialNumber)).perform(ViewActions.typeText("100"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemMake)).perform(ViewActions.typeText("Test Make"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemModel)).perform(ViewActions.typeText("Test Model"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemComment)).perform(ViewActions.typeText("Test Comment"), ViewActions.closeSoftKeyboard());

        // Click on Confirm button
        onView(withId(R.id.addItemButton)).perform(click());

        // Check if the added item's details are displayed correctly
        onView(ViewMatchers.withId(R.id.recyclerView));
        onView(withText(randomItemName)).check(matches(isDisplayed()));
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