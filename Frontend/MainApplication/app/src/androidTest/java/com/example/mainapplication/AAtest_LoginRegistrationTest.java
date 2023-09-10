package com.example.mainapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.action.ViewActions;
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
public class AAtest_LoginRegistrationTest {

    private static String testLoginUserName;// = "TestUser" + (int)(Math.random() * 100000);
    private static String testLoginPassword;// = "TestUserPass" + (int)(Math.random() * 100000);

    /**
     * The Activity scenario rule is used to test the MainActivity.
     * */
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Tests if the user can register
     * */
    @Test
    public void TestA_canRegister() {
//        testLoginUserName = "TestUser" + (int)(Math.random() * 100000);
//        testLoginPassword = "TestUserPass" + (int)(Math.random() * 100000);


        //Clear the cache //TODO: This is a temporary fix, need to find a better way to clear the cache
        MiddleMan.clearCache(true); //

        //click nav bar
        onView(withId(R.id.navigation_notifications)).perform(click());

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
    public void TestB_canLogoutAfterRegister(){
//        //Confirm that the user is logged in
//        onView(withText(containsString("You have been registered"))).check(matches(isDisplayed()));
//
//        //Leave Login Page, then return
//        onView(withId(R.id.navigation_dashboard)).perform(click());
        onView(withId(R.id.navigation_notifications)).perform(click());

        //Check that the user is still logged in
        CustomEspressoWait.waitForDialogWithText("You are already logged in", 5000);
//        onView(withText("You are already logged in")).check(matches(isDisplayed()));

        //Log out
        onView(withId(R.id.Accept_Button)).perform(click());

        //Confirm that the user is logged out
        CustomEspressoWait.waitForDialogWithText("You have been logged out", 5000);
//        onView(withText("You have been logged out")).check(matches(isDisplayed()));
    }

    @Test
    public void TestC_canLoginAfterLogout(){
//        //Confirm that the user is logged out
//        onView(withText("You have been logged out")).check(matches(isDisplayed()));
//
//        //Leave Login Page, then return
//        onView(withId(R.id.navigation_dashboard)).perform(click());
        onView(withId(R.id.navigation_notifications)).perform(click());

        //Confirm that the page is ready to be logged in
        CustomEspressoWait.waitForDialogWithText("LOGIN:", 5000);
//        onView(withText("LOGIN:")).check(matches(isDisplayed()));

        //Login with the user
        onView(withId(R.id.Login_User_Name)).perform(typeText(testLoginUserName));
        onView(withId(R.id.Login_User_Pass)).perform(typeText(testLoginPassword));

        //Close Keyboard
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.Accept_Button)).perform(click());

        //Confirm that the user is logged in
        CustomEspressoWait.waitForDialogWithText("You are already logged in", 5000);
//        onView(withText("You are already logged in")).check(matches(isDisplayed()));
    }

    @Test
    public void TestD_canLogoutAfterLogin(){
        onView(withId(R.id.navigation_notifications)).perform(click());

        //Confirm that the user is logged in
        CustomEspressoWait.waitForDialogWithText("You are already logged in", 5000);
//        onView(withText("You are already logged in")).check(matches(isDisplayed()));

        //Logout Again
        onView(withId(R.id.Accept_Button)).perform(click());

        //Confirm that the user is logged out
        CustomEspressoWait.waitForDialogWithText("You have been logged out", 5000);
//        onView(withText("You have been logged out")).check(matches(isDisplayed()));
    }

}
