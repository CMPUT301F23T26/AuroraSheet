package com.example.aurorasheetapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
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

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * This test will test for normal user flow in sequence by adding, editing and deleting an item
 * It verifies the functionality of add, edit and deletion functionalities
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TagItemsFragmentUnitTest {
    // define rules
    @Rule
    public ActivityScenarioRule<Login> loginActivityRule =
            new ActivityScenarioRule<>(Login.class);

    @Test
    public void A_testSelectTagAfterLogin() {
        // Login to app
        onView(withId(R.id.user_login)).perform(ViewActions.typeText("harrison"));
        onView(withId(R.id.password_login)).perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.loginButton)).perform(click());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Click on Add Item button
        onView(withId(R.id.addTagButton)).perform(click());

        String tagName = "tag 1";
        // Enter in test data for the item and close the keyboard after each entry
        onView(withId(R.id.tagName_input)).perform(ViewActions.typeText(tagName), ViewActions.closeSoftKeyboard());

        // Click on Confirm button
        onView(withText("Confirm")).perform(click());

        // Check if the added item's details are displayed correctly
        onView(withId(R.id.tag_View));
        onView(withText(tagName)).check(matches(isDisplayed()));

        onView(withId(R.id.buttonAdd)).perform(click());

        String ItemName = "item 1";
        // Enter in test data for the item and close the keyboard after each entry
        onView(withId(R.id.itemName)).perform(ViewActions.typeText(ItemName), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemDescription)).perform(ViewActions.typeText("Test Description"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemDate)).perform(ViewActions.click());
        onView(withText("OK")).perform(ViewActions.click());
        onView(withId(R.id.itemValue)).perform(ViewActions.typeText("100"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemSerialNumber)).perform(ViewActions.typeText("100"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemMake)).perform(ViewActions.typeText("Test Make"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemModel)).perform(ViewActions.typeText("Test Model"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.itemComment)).perform(ViewActions.typeText("Test Comment"), ViewActions.closeSoftKeyboard());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Click on Confirm button
        onView(withId(R.id.addItemButton)).perform(click());

        // Check if the added item's details are displayed correctly
        onView(ViewMatchers.withId(R.id.recyclerView));
        onView(withText(ItemName)).check(matches(isDisplayed()));

        onView(withId(R.id.tag_View)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(ViewMatchers.withId(R.id.recyclerView));
        onView(withText(ItemName)).check(doesNotExist());
    }


    @Test
    public void B_testTagItem(){
        String ItemName = "item 1";

        //login to user
        onView(withId(R.id.user_login)).perform(ViewActions.typeText("harrison"));
        onView(withId(R.id.password_login)).perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.loginButton)).perform(click());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //click on the item on recycler view
        onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(ItemName)),
                        click()
                )
        );

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.buttonTagItem)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withId(R.id.tag_Item_View)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Click on Confirm button
        onView(withText("Confirm")).perform(click());

        onView(ViewMatchers.withId(R.id.recyclerView));
        onView(withText(ItemName)).check(matches(isDisplayed()));

        onView(withId(R.id.tag_View)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText(ItemName)).check(matches(isDisplayed()));
    }

    @Test
    public void C_testDeletion(){
        String ItemName = "item 1";
        onView(withId(R.id.user_login)).perform(ViewActions.typeText("harrison"));
        onView(withId(R.id.password_login)).perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.loginButton)).perform(click());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //click on the item on recycler view
        onView(withId(R.id.tag_View)) .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withText("Delete")).perform(click());
        //check if deletion worked
        onView(withText("tag 1")).check(doesNotExist());

        //click on the item on recycler view
        onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(ItemName)),
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
        onView(withText(ItemName)).check(doesNotExist());
    }
}
