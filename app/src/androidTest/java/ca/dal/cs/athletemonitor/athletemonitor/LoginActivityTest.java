package ca.dal.cs.athletemonitor.athletemonitor;

import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;
import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.not;

/**
 * UI Test for Login Activity
 */

public class LoginActivityTest {
    /**
     * Test user for this test set
     */
    private static User testUser;

   @Rule
   public IntentsTestRule<LoginActivity> loginTestRule = new IntentsTestRule<>(LoginActivity.class, false, false);

    /**
     * Initializes and starts the activity before each test is run
     */
    @Before
    public void launchActivity() throws Exception {
        testUser = TestingHelper.createTestUser();
        loginTestRule.launchActivity(new Intent());
    }


    /**
     * Test username with empty string
     *
     * @throws Exception Generic exception
     */
    @Test
    public void usernameEmptyTest() throws Exception {
        onView(withId(R.id.txtUsername)).check(matches(withText("")));
        onView(withId(R.id.btnSignIn)).check(matches(not(isEnabled())));
    }

    /**
     * Test username text box with input
     *
     * @throws Exception Generic exception
     */
    @Test
    public void usernameInputTextTest() throws Exception {
        onView(withId(R.id.txtUsername)).perform(typeText(testUser.getUsername()), closeSoftKeyboard());
        onView(withId(R.id.txtUsername)).check(matches(withText(testUser.getUsername())));
    }

    /**
     * Test password with empty string
     *
     * @throws Exception Generic exception
     */
    @Test
    public void passwordEmptyTest() throws Exception {
        onView(withId(R.id.txtPassword)).check(matches(withText("")));
        onView(withId(R.id.btnSignIn)).check(matches(not(isEnabled())));
    }

    /**
     * Test password text box with input
     *
     * @throws Exception Generic exception
     */
    @Test
    public void passwordInputTextTest() throws Exception {
        onView(withId(R.id.txtPassword)).perform(typeText(testUser.getPassword()), closeSoftKeyboard());
        onView(withId(R.id.txtPassword)).check(matches(withText(testUser.getPassword())));
    }

    /**
     * Test successful login when sign in button is clicked
     *
     * @throws Exception Generic exception
     */
    @Test
    public void signInSuccessTest() throws Exception {
        AccountManager.createUser(testUser, null);
        sleep(250);

        //use the user information to populate the controls
        onView(withId(R.id.txtUsername)).perform(typeText(testUser.getUsername()), closeSoftKeyboard());
        onView(withId(R.id.txtPassword)).perform(typeText(testUser.getPassword()), closeSoftKeyboard());
        onView(withId(R.id.btnSignIn)).perform(click());
        sleep(250);

        intended(hasComponent(MainActivity.class.getName()));

        //clean up test user
        AccountManager.setUserLoginState(testUser.getUsername(), false);
        AccountManager.deleteUser(testUser, null);
    }

    /**
     * Test unsuccessful login due to non existing user
     *
     * @throws Exception Generic exception
     */
    @Test
    public void signInInvalidUserTest() throws Exception {
        //ensure the test account does not exist
        AccountManager.deleteUser(testUser, new BooleanResultListener() {
            @Override
            public void onResult(boolean result) {
            }
        });

        //use the user information to populate the controls
        onView(withId(R.id.txtUsername)).perform(typeText(testUser.getUsername()), closeSoftKeyboard());
        onView(withId(R.id.txtPassword)).perform(typeText(testUser.getPassword()), closeSoftKeyboard());
        onView(withId(R.id.btnSignIn)).perform(click());
        sleep(250);

        onView(withId(R.id.lblMessage)).check(matches(withText(R.string.loginFailure)));
    }

    /**
     * Tests Register button click creates account
     *
     * @throws Exception
     */
    @Test
    public void accountRegisterSuccessTest() throws Exception {
        //populate controls with login information
        onView(withId(R.id.txtUsername)).perform(typeText(testUser.getUsername()), closeSoftKeyboard());
        onView(withId(R.id.txtPassword)).perform(typeText(testUser.getPassword()), closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());
        sleep(250);

        onView(withId(R.id.lblMessage)).check(matches(withText(R.string.accountCreated)));

        AccountManager.deleteUser(testUser, TestingHelper.assertTrueBooleanResult());
    }

    /**
     * Tests Register button click creates account could not create account
     *
     * @throws Exception
     */
    @Test
    public void accountRegisterFailureTest() throws Exception {
        AccountManager.createUser(testUser, null);

        //populate controls with login information
        onView(withId(R.id.txtUsername)).perform(typeText(testUser.getUsername()), closeSoftKeyboard());
        onView(withId(R.id.txtPassword)).perform(typeText(testUser.getPassword()), closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());
        sleep(250);

        onView(withId(R.id.lblMessage)).check(matches(withText(R.string.accountNotCreated)));

        AccountManager.deleteUser(testUser, TestingHelper.assertTrueBooleanResult());
    }
}
