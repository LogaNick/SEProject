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
import static android.support.test.espresso.intent.matcher.IntentMatchers.ha



/**
 * UI Test for Login Activity
 */

public class LoginActivityTest {
    private String usernameInputTestText = "username";
    private String passwordInputTestText = "password";


    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<LoginActivity>(
            LoginActivity.class);
    @Rule
    public IntentsTestRule<LoginActivity> intentsTestRule = new IntentsTestRule<>(LoginActivity.class);


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
     * Test successful login when signin button is clicked
     *
     * @throws Exception
     */
    @Test
    public void signInSuccessTest() throws Exception {
        //perform signin button click
        onView(withId(R.id.btnSignin)).perform(click());

        //test if main activity has been launched
        intended(hasComponent(MainActivity.class.getName()));

        //test correct response on successful login
        //onView(withId(R.id.lblLoginResult)).check(matches(withText(R.string.loginSuccess)));
        //onView(withId(R.id.btnSignin)).check(matches(withText(R.string.loginSuccess)));


    }




}

//
//    // invalid password
//    onView(withId(R.id.txtPassword)).perform(typeText(oneRulePassedInput));
//    closeSoftKeyboard();
//    onView(withId(R.id.btnValidate)).perform(click());
//    onView(withId(R.id.txtviewPasswordStrength)).check(matches(withText(oneRulePassedResponse)));