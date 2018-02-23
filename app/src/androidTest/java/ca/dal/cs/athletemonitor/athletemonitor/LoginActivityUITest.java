package ca.dal.cs.athletemonitor.athletemonitor;

import ca.dal.cs.athletemonitor.athletemonitor.AccountManagerTest;
import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;
import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

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

//    @Rule
//    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
//            LoginActivity.class);

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
     * TODO: THIS TEST USES LOCKS TO WAIT FOR ASYNC OPERATIONS TO COMPLETE, IT SEEMS VERY HACKERY...BETTER APPROACHES???
     *
     * @throws Exception Generic exception
     */
    @Test
    public void signInSuccessTest() throws Exception {
        //generate a test user and add it to the user accounts list
        final User testUser = TestingHelper.createTestUser();
        final Object synchronizer = new Object();

        //use the user information to populate the controls
        AccountManager.createUser(testUser, new AccountManager.UserObjectListener() {
            @Override
            public void onUserPopulated(User user) {
                synchronized (synchronizer) {
                    synchronizer.notify();
                }
            }
        });

        synchronized(synchronizer) {
            Log.d("signInSuccessTestDebug", "Waiting for test account creation to finish...");
            synchronizer.wait();
        }

        Log.d("signInSuccessTestDebug", "Test account created...populating controls");

        //use the user information to populate the controls
        onView(withId(R.id.txtUsername)).perform(typeText(testUser.getUsername()));
        closeSoftKeyboard();
        onView(withId(R.id.txtPassword)).perform(typeText(testUser.getPassword()));
        closeSoftKeyboard();

        Log.d("signInSuccessTestDebug", "Controls populated, performing click...");
        onView(withId(R.id.btnSignIn)).perform(click());


        //clean up test user
        AccountManager.deleteUser(testUser, new BooleanResultListener() {
            @Override
            public void onResult(boolean result) {
                synchronized (synchronizer) {
                    synchronizer.notify();
                }
            }
        });

        synchronized(synchronizer) {
            Log.d("signInSuccessTestDebug", "Waiting for test account deletion to finish...");
            synchronizer.wait();
        }

        intended(hasComponent(MainActivity.class.getName()));
    }
}
