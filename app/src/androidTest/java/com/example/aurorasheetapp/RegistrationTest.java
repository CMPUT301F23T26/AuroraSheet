package com.example.aurorasheetapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.action.ViewActions;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RegistrationTest {

    @Rule
    public ActivityScenarioRule<Registration> registrationActivityRule =
            new ActivityScenarioRule<>(Registration.class);


    @Test
    public void testFailedRegistrationDueToUserCollision() {
        onView(withId(R.id.emailId)).perform(ViewActions.typeText("mehreenpa@gmail.com"),  ViewActions.closeSoftKeyboard());
        onView(withId(R.id.reg_pass)).perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.userid)).perform(ViewActions.typeText("mehreen"), ViewActions.closeSoftKeyboard());

        // Click the sign-up button
        onView(withId(R.id.waitButton)).perform(ViewActions.click());


        // Wait for the error message to show
        try {
            Thread.sleep(2000); // Adjust this based on your app's response time
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.signupText)).check(matches(withText("Sign Up")));
    }
    @Test
    public void testEmptyEmail() {
        onView(withId(R.id.emailId)).perform(ViewActions.typeText(""),  ViewActions.closeSoftKeyboard());
        onView(withId(R.id.reg_pass)).perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.userid)).perform(ViewActions.typeText("mehreen"), ViewActions.closeSoftKeyboard());

        // Click the sign-up button
        onView(withId(R.id.waitButton)).perform(ViewActions.click());

        // Add a delay to wait for the response
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.signupText)).check(matches(withText("Sign Up")));

    }
}
