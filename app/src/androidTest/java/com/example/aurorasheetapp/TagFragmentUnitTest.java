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
public class TagFragmentUnitTest {
    // define rules
    @Rule
    public ActivityScenarioRule<Login> loginActivityRule =
            new ActivityScenarioRule<>(Login.class);

    @Test
    public void A_testAddTagAfterLogin() {
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
            Thread.sleep(3000);
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
    }


    @Test
    public void B_testEditTag(){
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
        onView(withId(R.id.tag_View)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Enter in test data for the item
        onView(withId(R.id.tagName_input)).perform(ViewActions.clearText(), ViewActions.typeText("new Tag"));

        // Click on Confirm button
        onView(withText("Confirm")).perform(click());

        onView(withId(R.id.tag_View));
        //check if change is applied
        onView(withText("new Tag")).check(matches(isDisplayed()));
    }

    @Test
    public void C_testDeletion(){
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
        onView(withText("new Tag")).check(doesNotExist());
    }
}
