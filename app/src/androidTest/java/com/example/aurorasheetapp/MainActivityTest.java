package com.example.aurorasheetapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
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


    @Test
    public void testEditItem(){
        //login to user
        onView(withId(R.id.user_login)).perform(ViewActions.typeText("jey"));
        onView(withId(R.id.password_login)).perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());

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
        //add item before testing edit
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

        onView(withId(R.id.addItemButton)).perform(click());
        //click on the item on recycler view
        onView(ViewMatchers.withText(randomItemName)).perform(ViewActions.click());
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.buttonEdit)).perform(click());
        // Enter in test data for the item
        onView(withId(R.id.itemName_edit)).perform(ViewActions.typeText("TestObject"));
        onView(withId(R.id.itemDescription_edit)).perform(ViewActions.typeText("Test Description edited"));
        onView(withId(R.id.itemValue_edit)).perform(ViewActions.typeText("121.00"));
        onView(withId(R.id.date_edit)).perform(ViewActions.typeText("01-01-1400"));
        onView(withId(R.id.itemSerialNumber_edit)).perform(ViewActions.typeText("100"));

        // Click on Confirm button
        onView(withId(R.id.confirmButton_edit)).perform(click());

        onView(ViewMatchers.withId(R.id.recyclerView));
        //check if change is applied
        onView(withText("TestObject")).check(matches(isDisplayed()));
    }

    @Test
    public void testDeletion(){
        onView(withId(R.id.user_login)).perform(ViewActions.typeText("jey"));
        onView(withId(R.id.password_login)).perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());

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
        //add item before deleting
        onView(withId(R.id.buttonAdd)).perform(click());
        String randomItemName = UUID.randomUUID().toString();
        // Enter in test data for the item and close the keyboard after each entry
        onView(withId(R.id.itemName)).perform(ViewActions.typeText(randomItemName), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemDescription)).perform(ViewActions.typeText("Test Description"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemDate)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.itemValue)).perform(ViewActions.typeText("100"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemSerialNumber)).perform(ViewActions.typeText("100"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemMake)).perform(ViewActions.typeText("Test Make"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemModel)).perform(ViewActions.typeText("Test Model"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemComment)).perform(ViewActions.typeText("Test Comment"), ViewActions.closeSoftKeyboard());

        // Click on Confirm button
        onView(withId(R.id.addItemButton)).perform(click());

        // Check if the added item's details are displayed correctly
        onView(ViewMatchers.withId(R.id.recyclerView));
        onView(withId(R.id.buttonDelete)).perform(click());
        //check if deletion worked
        onView(withText(randomItemName)).check(doesNotExist());
    }
}