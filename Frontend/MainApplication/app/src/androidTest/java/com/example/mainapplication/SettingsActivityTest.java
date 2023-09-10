package com.example.mainapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.mainapplication.ui.notifications.SettingsActivity;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * Instrumented test, which will execute on an Android device.
 * @implNote Test device must have all animations disabled, otherwise the test will fail, as per Espresso requirements
 *
 * @author Ryan Leska
 * */
@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SettingsActivityTest {

    @Rule
    public ActivityScenarioRule<SettingsActivity> activityRule =
            new ActivityScenarioRule<>(SettingsActivity.class);

    @Test
    public void TestA_TestBinding(){
        onView(withText(containsString("Settings"))).check(matches(isDisplayed()));
    }
}
