package ca.dal.cs.athletemonitor.athletemonitor;

import android.app.ActivityManager;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;


import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

//import for intended() and hasComponent()
import static android.support.test.espresso.intent.Intents.intended;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * UI Test for Login Activity
 */

public class LoginActivityUITest {
    private String usernameInputTestText = "username";
    private String passwordInputTestText = "password";


    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<LoginActivity>(
            LoginActivity.class);

    /**
     * Test username with empty string
     *
     * @throws Exception
     */
    @Test
    public void usernameEmptyTest() throws Exception {
        onView(withId(R.id.txtUsername)).check(matches(withText("")));
    }

    /**
     * Test username textbox with input
     *
     * @throws Exception
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
     * @throws Exception
     */
    @Test
    public void passwordEmptyTest() throws Exception {
        onView(withId(R.id.txtPassword)).check(matches(withText("")));
    }

    /**
     * Test username textbox with input
     *
     * @throws Exception
     */
    @Test
    public void passwordInputTextTest() throws Exception {
        onView(withId(R.id.txtPassword)).perform(typeText(passwordInputTestText));
        closeSoftKeyboard();
        onView(withId(R.id.txtPassword)).check(matches(withText(passwordInputTestText)));
    }

    /**
     * Test for true positive on existing user
     *
     * Will pass in a username that does exist in the user accounts to the validate method and
     * assert that the return value of validate is true
     *
     * @throws Exception
     */
    @Test
    public void userExistsTest() throws Exception {
        User testUser = new User("testAccount5", "testPassword");
        AccountManager.createUser(testUser);

        AccountManager.userExists(testUser.getUsername(), new AccountManager.UserExistsListener() {
            @Override
            public void onUserExists(boolean result) {
                assertTrue(result);
            }

        });

        // TODO: Add code to delete test user
    }

    /**
     * Test for false positive on existing user
     *
     * Will pass in a username that does not exist in the user accounts to the validate method
     * and assert that the return value of validate is false
     *
     * @throws Exception
     */
    @Test
    public void userDoesNotExistTest() throws Exception {
        User testUser = new User("idon'texist", "testPassword");

        AccountManager.userExists(testUser.getUsername(), new AccountManager.UserExistsListener() {
            @Override
            public void onUserExists(boolean result) {
                assertFalse("User should not exist, but user is found.", result);
            }
        });
    }
}
