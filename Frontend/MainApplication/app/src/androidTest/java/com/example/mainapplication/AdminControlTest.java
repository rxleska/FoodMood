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
import static org.hamcrest.CoreMatchers.containsString;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.mainapplication.datainterface.MiddleMan;
import com.example.mainapplication.customTestFunctions.CustomEspressoWait;
import com.example.mainapplication.customTestFunctions.ChildClickRecyclerView;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminControlTest {

    private static String testLoginUserName;// = "TestUser" + (int)(Math.random() * 100000);
    private static String testLoginPassword;// = "TestUserPass" + (int)(Math.random() * 100000);
    private static String testReviewText;

    /**
     * The Activity scenario rule is used to test the MainActivity.
     * */
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

        //Sets Admin Account to true
        onView(withId(R.id.adminSwitch)).perform(click());
        //Click Register Button
        onView(withId(R.id.Accept_Button)).perform(click());

        //Confirm that the user is logged in
        CustomEspressoWait.waitForDialogWithText("You have been registered", 5000);
//        onView(withText(containsString("You have been registered"))).check(matches(isDisplayed()));
    }
    @Test
    public void TestB_NavigateToReviewPageToDelete() {
        //bring to review tab
        onView(withId(R.id.navigation_home)).perform(click());
        //enter restaurant page
        onView(withId(R.id.idRVRestaurants)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //enter first food
        onView(withId(R.id.idFoodRV)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //create Review
        onView(withId(R.id.fab)).perform(click());
        //Create Review Text
        testReviewText = "TestReview" + (int)(Math.random() * 100000);
        //adds Text
        onView(withId(R.id.editReview)).perform(typeText(testReviewText));
        //Close Keyboard
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        //adds stars
        onView(withId(R.id.ratingBar)).perform(click());
        //send review
        onView(withId(R.id.sendButton)).perform(click());
        //goes back a screen
        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack());
        //reenter
        //enter first food
        onView(withId(R.id.idFoodRV)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //scroll
        onView(withId(R.id.idRVCourses)).perform(RecyclerViewActions.scrollToLastPosition());
        //click delete Review
        onView(withId(R.id.idRVCourses)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText(containsString("" + testReviewText))), clickChildViewWithId(R.id.deleteReview)));
        //Click on yes button in popup dialogue box DialogInterface
        onView(withText(containsString("YES"))).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        //goes back a screen
        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack());
        //re enters food
        onView(withId(R.id.idFoodRV)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //Scroll
        onView(withId(R.id.idRVCourses)).perform(RecyclerViewActions.scrollToLastPosition());
        //Tests to find review
        onView(withText(containsString("" + testReviewText))).check(doesNotExist());

    }
    @Test
    public void TestC_NavigateToReviewPageToBan() {
        //bring to review tab
        onView(withId(R.id.navigation_home)).perform(click());
        //enter restaurant page
        onView(withId(R.id.idRVRestaurants)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //enter first food
        onView(withId(R.id.idFoodRV)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //create Review
        onView(withId(R.id.fab)).perform(click());
        //Create Review Text
        testReviewText = "TestReview" + (int)(Math.random() * 100000);
        //adds Text
        onView(withId(R.id.editReview)).perform(typeText(testReviewText));
        //Close Keyboard
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        //adds stars
        onView(withId(R.id.ratingBar)).perform(click());
        //send review
        onView(withId(R.id.sendButton)).perform(click());
        //goes back a screen
        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack());
        //reenter
        //enter first food
        onView(withId(R.id.idFoodRV)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //scroll
        onView(withId(R.id.idRVCourses)).perform(RecyclerViewActions.scrollToLastPosition());
        //click user ban
        onView(withId(R.id.idRVCourses)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText(containsString("" + testReviewText))), clickChildViewWithId(R.id.BanUser)));
        //Click on yes button in popup dialogue box DialogInterface
        onView(withText(containsString("YES"))).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        //Goes back a screen
        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack());
        //checks for add review being gone
        onView(withId(R.id.fab)).check(doesNotExist());
    }

}
