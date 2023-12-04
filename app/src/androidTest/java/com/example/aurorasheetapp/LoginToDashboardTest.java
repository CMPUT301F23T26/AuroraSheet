package com.example.aurorasheetapp;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.action.ViewActions;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
public class LoginToDashboardTest {

    @Rule
    public ActivityScenarioRule<Login> loginActivityRule =
            new ActivityScenarioRule<>(Login.class);

    @Test
    public void A_testAddItemAfterLogin() {
        // Login to app
        onView(withId(R.id.user_login)).perform(ViewActions.typeText("bonobo"),ViewActions.closeSoftKeyboard());
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

        // Verifying that the TextView in MainActivity is displayed
        onView(withId(R.id.buttonAdd)).check(matches(isDisplayed()));
    }

        @Test
        public void testFailedLoginShowsErrorMessage() {
            // Input invalid credentials
            onView(withId(R.id.user_login)).perform(ViewActions.typeText("invalidUsername"),ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(ViewActions.typeText("wrongPassword"), ViewActions.closeSoftKeyboard());

            // Simulate a short delay for processing
            try {
                Thread.sleep(1000); // Adjust this based on your app's response time
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Click the login button
            onView(withId(R.id.loginButton)).perform(ViewActions.click());

            // Wait for the error message to show
            try {
                Thread.sleep(2000); // Adjust this based on your app's response time
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            onView(withText("Login")).check(matches(withId(R.id.loginText)));

}
    @Test
    public void testEmptyPasswordShowsErrorMessage() {
        // Input invalid credentials
        onView(withId(R.id.user_login)).perform(ViewActions.typeText("invalidUsername"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password_login)).perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard());

        // Simulate a short delay for processing
        try {
            Thread.sleep(1000); // Adjust this based on your app's response time
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Click the login button
        onView(withId(R.id.loginButton)).perform(ViewActions.click());

        // Wait for the error message to show
        try {
            Thread.sleep(2000); // Adjust this based on your app's response time
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withText("Login")).check(matches(withId(R.id.loginText)));

    }
     @Test
    public void testEmptyusernameShowsErrorMessage() {
        // Input invalid credentials
        onView(withId(R.id.user_login)).perform(ViewActions.typeText(""),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password_login)).perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());

        // Simulate a short delay for processing
        try {
            Thread.sleep(1000); // Adjust this based on your app's response time
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Click the login button
        onView(withId(R.id.loginButton)).perform(ViewActions.click());

        // Wait for the error message to show
        try {
            Thread.sleep(2000); // Adjust this based on your app's response time
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withText("Login")).check(matches(withId(R.id.loginText)));

    }
    }
