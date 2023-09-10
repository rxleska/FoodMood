package com.example.mainapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.mainapplication.datainterface.MiddleMan;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapTests {
    /**
     * The Activity scenario rule is used to test the MainActivity.
     * */
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Tests map appearing
     * */
    @Test
    public void Test_mapView() {

        //brings to center tab
        onView(withId(R.id.navigation_dashboard)).perform(click());

        //launches map screen
        onView(withId(R.id.mapLauncher)).perform(click());

        //click center pin
        onView(withId(R.id.mapView)).perform(click());
        //Checks that map appeared
        onView(withId(R.id.mapView)).check(matches(isDisplayed()));

    }

}
