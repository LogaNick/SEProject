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

import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper.authTestUser;
import static org.hamcrest.CoreMatchers.not;

/**
 * Espresso Test for the Workout Data Activity.
 */
@RunWith(AndroidJUnit4.class)
public class WorkoutActivityTest {
    @Rule
    public IntentsTestRule<WorkoutActivity> mActivityRule =
            new IntentsTestRule(WorkoutActivity.class, false, false);

    @BeforeClass
    public static void setupEnvironment(){
        authTestUser();
    }

    @Before
    public void setupUser(){
        TestingHelper.resetTestUserWorkouts();
        Intent i = new Intent();
        i.putExtra("username", "testuser");
        mActivityRule.launchActivity(i);
    }

    /**
     * Test that the proper button and fields exist.
     * @throws Exception
     */
    @Test
    public void testProperFieldsExist() throws Exception {
        //Try to get the fields and button.
        onView(withId(R.id.submitDataButton));
        onView(withId(R.id.spinner));
    }

    /**
     * Test that the submit new exercise button is disabled if the fields are invalid
     */
    /*
    @Test
    public void testSubmitButtonDisabled(){
        onView(withId(R.id.submitDataButton)).check(matches(not(isEnabled())));
    }
    */

    /**
     * Test that the submit new exercise button is enabled if the fields are valid
     */
    /*
    @Test
    public void testSubmitButtonEnabled() throws Exception {
        onView(withId(R.id.newExerciseName)).perform(typeText("Run"));
        onView(withId(R.id.newExerciseDescription)).perform(typeText("Run outside"));
        onView(withId(R.id.newExerciseTime)).perform(typeText("5"), closeSoftKeyboard());

        onView(withId(R.id.newExerciseSubmitButton)).check(matches(isEnabled()));

        //Try to click the button.
        onView(withId(R.id.newExerciseSubmitButton)).perform(click());
        intended(hasComponent(ExerciseActivity.class.getName()));
    }
    */
}
