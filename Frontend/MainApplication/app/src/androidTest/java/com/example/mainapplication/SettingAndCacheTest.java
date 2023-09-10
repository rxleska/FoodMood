package com.example.mainapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

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
public class SettingAndCacheTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void TestA_ClearCache() {
        //click nav bar
        onView(withId(R.id.navigation_notifications)).perform(click());

        //click settings
        onView(withId(R.id.settingsFloatingActionButton)).perform(click());

        //Check if the cache does not say Empty
        onView(withText(containsString("Empty"))).check(doesNotExist());

        //click clear cache
        onView(withId(R.id.cache_clear_button)).perform(click());

        //Check if the cache says Empty
        onView(withId(R.id.cache_information_text)).check(matches(withText(containsString("Empty"))));

        //click back
        onView(withId(R.id.backButton)).perform(click());

        //go to home
        onView(withId(R.id.navigation_home)).perform(click());

        //click nav bar
        onView(withId(R.id.navigation_notifications)).perform(click());

        //click settings
        onView(withId(R.id.settingsFloatingActionButton)).perform(click());

        //Check if the cache doesn't say Empty
        onView(withText(containsString("Empty"))).check(doesNotExist());


    }
}
