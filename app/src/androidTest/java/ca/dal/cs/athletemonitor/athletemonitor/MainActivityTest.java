package ca.dal.cs.athletemonitor.athletemonitor;


import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Espresso Test for the Main Activity.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule(MainActivity.class, false, false);

    @Before
    public void loginToTestUser() {
        // Authenticate user and set login state to false so Firebase does not keep testuser online
        AccountManager.authenticate(new User("testuser", "abc"), null);
        AccountManager.setUserLoginState("testuser", false);
        Intent i = new Intent();
        i.putExtra("username", "testuser");
        mActivityRule.launchActivity(i);
    }

    /**
     * Test that the button to transfer to the exercise activity works.
     * @throws Exception
     */
    @Test
    public void testGoToExerciseButton() throws Exception {
        //Try to click the button.
        onView(withId(R.id.goToExerciseActivityButton)).perform(click());
        intended(hasComponent(ExerciseActivity.class.getName()));
    }
}
