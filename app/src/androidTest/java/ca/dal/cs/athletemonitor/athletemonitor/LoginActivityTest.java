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

/**
 * UI Test for Login Activity
 */
public class LoginActivityTest {
    @Rule
    public IntentsTestRule<LoginActivity> intentsTestRule = new IntentsTestRule<>(LoginActivity.class);

    /**
     * Test successful login when signin button is clicked
     *
     * @throws Exception
     */
    @Test
    public void signInSuccessTest() throws Exception {
        //perform signin button click
        onView(withId(R.id.btnSignIn)).perform(click());

        //test if main activity has been launched
        intended(hasComponent(MainActivity.class.getName()));
    }
}