package ca.dal.cs.athletemonitor.athletemonitor;


import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * UI Test for Login Activity
 */

public class LoginActivityUITest {
    private String usernameInputTestText = "username";
    private String passwordInputTestText = "password";

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

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



}
