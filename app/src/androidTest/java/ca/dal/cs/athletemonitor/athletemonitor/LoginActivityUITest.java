package ca.dal.cs.athletemonitor.athletemonitor;


import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * UI Test for Login Activity
 */

public class LoginActivityUITest {
    private String usernameInputTestText = "username";
    private String passwordInputTestText = "password";

    /*
        23 Feb 2018 -- deactivated in favour of rule that allows intents to be created

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);
    */
    @Rule
    public IntentsTestRule<LoginActivity> intentsTestRule = new IntentsTestRule<>(LoginActivity.class);

    /**
     * Test username with empty string
     *
     * @throws Exception Generic exception
     */
    @Test
    public void usernameEmptyTest() throws Exception {
        onView(withId(R.id.txtUsername)).check(matches(withText("")));
    }

    /**
     * Test username text box with input
     *
     * @throws Exception Generic exception
     */
    @Test
    public void usernameInputTextTest() throws Exception {
        onView(withId(R.id.txtUsername)).perform(typeText(usernameInputTestText));
        closeSoftKeyboard();
        onView(withId(R.id.txtUsername)).check(matches(withText(usernameInputTestText)));
    }

    /**
     * Test password with empty string
     *
     * @throws Exception Generic exception
     */
    @Test
    public void passwordEmptyTest() throws Exception {
        onView(withId(R.id.txtPassword)).check(matches(withText("")));
    }

    /**
     * Test password text box with input
     *
     * @throws Exception Generic exception
     */
    @Test
    public void passwordInputTextTest() throws Exception {
        onView(withId(R.id.txtPassword)).perform(typeText(passwordInputTestText));
        closeSoftKeyboard();
        onView(withId(R.id.txtPassword)).check(matches(withText(passwordInputTestText)));
    }

    /**
     * Test successful login when sign in button is clicked
     *
     * @throws Exception Generic exception
     */
    @Test
    public void signInSuccessTest() throws Exception {
        //perform sign in button click
        onView(withId(R.id.btnSignIn)).perform(click());

        //test if main activity has been launched
        intended(hasComponent(MainActivity.class.getName()));
    }

}
