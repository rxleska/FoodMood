package com.example.mainapplication.customTestFunctions;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.containsString;

import android.view.View;

import org.hamcrest.Matcher;

public class CustomEspressoWait {
    public static void waitForDialogWithText(String text, long timeout) {
        waitForDialog(withText(containsString(text)), timeout);
    }

    private static void waitForDialog(Matcher<View> viewMatcher, long timeout) {
        final long endTime = System.currentTimeMillis() + timeout;
        Exception exception = null;
        while (System.currentTimeMillis() < endTime) {
            try {
                // wait for x second // Thread.sleep(x mills)
                onView(viewMatcher).check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                exception = e;
            }
        }
        if (exception != null) {
            throw new RuntimeException(exception.getMessage());
        }
    }
}

