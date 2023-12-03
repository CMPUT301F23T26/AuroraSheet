package com.example.aurorasheetapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.UUID;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityTest {
    // define rules
    @Rule
    public ActivityScenarioRule<Login> loginActivityRule =
            new ActivityScenarioRule<>(Login.class);
    String randomItemName;

    @Test
    public void A_testAddItemAfterLogin() {
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

        String randomItemName = "TestString";
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
    public void B_testEditItem(){
        //login to user
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

        //click on the item on recycler view
        onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItem(
                        hasDescendant(withText("TestString")),
                        click()
                )
        );
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.buttonEdit)).perform(click());
        // Enter in test data for the item
        onView(withId(R.id.itemName_edit)).perform(ViewActions.clearText(), ViewActions.typeText("TestObjects"));

        // Click on Confirm button
        onView(withId(R.id.confirmButton_edit)).perform(click());

        onView(ViewMatchers.withId(R.id.recyclerView));
        //check if change is applied
        onView(withText("TestObjects")).check(matches(isDisplayed()));
    }

    @Test
    public void C_testDeletion(){
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

        //click on the item on recycler view
        onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItem(
                        hasDescendant(withText("TestObjects")),
                        click()
                )
        );
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Check if the added item's details are displayed correctly
        onView(ViewMatchers.withId(R.id.recyclerView));
        onView(withId(R.id.buttonDelete)).perform(click());
        //check if deletion worked
        onView(withText("TestObjects")).check(doesNotExist());
    }
}
