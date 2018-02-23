package ca.dal.cs.athletemonitor.athletemonitor;


import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

/**
 * Espresso Test for the CreateExercise Activity.
 */
@RunWith(AndroidJUnit4.class)
public class CreateExerciseActivityTest {
    @Rule
    public IntentsTestRule<CreateExerciseActivity> mActivityRule =
            new IntentsTestRule(CreateExerciseActivity.class);

    @Before
    public void setupUser(){
        new UserManager().login("user", "pass");
    }

    /**
     * Test that the proper button and fields exist.
     * @throws Exception
     */
    @Test
    public void testProperFieldsExist() throws Exception {
        //Try to get the fields and button.
        onView(withId(R.id.newExerciseName));
        onView(withId(R.id.newExerciseDescription));
        onView(withId(R.id.newExerciseTime));
        onView(withId(R.id.newExerciseSubmitButton));
        onView(withId(R.id.newExerciseTimeUnits));
    }

    /**
     * Test that the submit new exercise button is disabled if the fields are invalid
     */
    @Test
    public void testSubmitButtonDisabled(){
        onView(withId(R.id.newExerciseSubmitButton)).check(matches(not(isEnabled())));
    }

    /**
     * Test that the submit new exercise button is enabled if the fields are valid
     */
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
}
