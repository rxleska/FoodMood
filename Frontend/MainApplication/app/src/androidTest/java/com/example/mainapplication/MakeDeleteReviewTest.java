package com.example.mainapplication;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.mainapplication.customTestFunctions.ChildClickRecyclerView.clickChildViewWithId;
import static org.hamcrest.Matchers.containsString;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.mainapplication.customTestFunctions.CustomEspressoWait;
import com.example.mainapplication.datainterface.MiddleMan;

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
public class MakeDeleteReviewTest {

    private static String testLoginUserName;// = "TestUser" + (int)(Math.random() * 100000);
    private static String testLoginPassword;// = "TestUserPass" + (int)(Math.random() * 100000);

    private static int testItemID = 1;


    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void TestA_RegisterNewUserToMakeReview() {
        //Clear the cache //TODO: This is a temporary fix, need to find a better way to clear the cache
        MiddleMan.clearCache(true); //

        //click nav bar
        onView(withId(R.id.navigation_notifications)).perform(click());

        //close keyboard just in case
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        //Go to Registration Page
        onView(withId(R.id.Change_To_Reg_Mode)).perform(click());

        //Create a Unique User
        testLoginUserName = "TestUser" + (int)(Math.random() * 100000);
        testLoginPassword = "TestUserPass" + (int)(Math.random() * 100000);

        // Enter User Info in to Registaration Page
        onView(withId(R.id.Login_User_Name)).perform(typeText(testLoginUserName));
        onView(withId(R.id.Login_User_Pass)).perform(typeText(testLoginPassword));
        onView(withId(R.id.REG_USER_PASS_CONF)).perform(typeText(testLoginPassword));

        //Close Keyboard
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        //Click Register Button
        onView(withId(R.id.Accept_Button)).perform(click());

        //Confirm that the user is logged in
        CustomEspressoWait.waitForDialogWithText("You have been registered", 5000);
//        onView(withText(containsString("You have been registered"))).check(matches(isDisplayed()));
    }

    @Test
    public  void TestB_CreateReivewForFirstItem(){
        //click nav bar
        onView(withId(R.id.navigation_home)).perform(click());

        //Click on the first item
        onView(withId(R.id.idRVRestaurants)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Click on the first food item
        onView(withId(R.id.idFoodRV)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Click on the review button
        onView(withId(R.id.fab)).perform(click());

        // Create Random number to add to review to make it unique
        testItemID = (int)(Math.random() * 100000);

        //Enter a review
        onView(withId(R.id.editReview)).perform(typeText("This is a test review" + testItemID));

        //Close Keyboard
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        //click on rating bar
        onView(withId(R.id.ratingBar)).perform(click());

        //Click on the submit button
        onView(withId(R.id.sendButton)).perform(click());



        //Go back a page
        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack());

//        //Go back a page
//        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack());

        //Click on the first food item
        onView(withId(R.id.idFoodRV)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //scroll to bottom of reyclerview
        onView(withId(R.id.idRVCourses)).perform(RecyclerViewActions.scrollToLastPosition());

        // check if there is a reiview with the text "This is a test review"
        CustomEspressoWait.waitForDialogWithText("" + testItemID, 5000);
//        onView(withText(containsString("" + randomNum))).check(matches(isDisplayed()));
    }


    @Test
    public void TestC_DeleteReview(){
        //click nav bar to go to home page
        onView(withId(R.id.navigation_home)).perform(click());

        //Click on the first item
        onView(withId(R.id.idRVRestaurants)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Click on the first food item
        onView(withId(R.id.idFoodRV)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Scroll to bottom of reyclerview
        onView(withId(R.id.idRVCourses)).perform(RecyclerViewActions.scrollToLastPosition());

        //click on delete button in review containing the text testItemID
        onView(withId(R.id.idRVCourses)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText(containsString("" + testItemID))), clickChildViewWithId(R.id.deleteReview)));

        //Click on yes button in popup dialogue box DialogInterface
        onView(withText(containsString("YES"))).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());

//        Dialog button = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).findObject(new UiSelector().text("YES"));
//        onView(withText(containsString("YES"))).perform(click());

        //Go back a page
        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack());

//        //Go back a page
//        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack());

        //Click on the first food item
        onView(withId(R.id.idFoodRV)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //scroll to bottom of reyclerview
        onView(withId(R.id.idRVCourses)).perform(RecyclerViewActions.scrollToLastPosition());

        // check if there is a reiview with the text "This is a test review"
        onView(withText(containsString("" + testItemID))).check(doesNotExist());
    }



}
