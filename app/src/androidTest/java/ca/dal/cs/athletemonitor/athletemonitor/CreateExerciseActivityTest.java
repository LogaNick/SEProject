package ca.dal.cs.athletemonitor.athletemonitor;


import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Espresso Test for the CreateExercise Activity.
 */
@RunWith(AndroidJUnit4.class)
public class CreateExerciseActivityTest {
    @Rule
    public IntentsTestRule<CreateExerciseActivity> mActivityRule =
            new IntentsTestRule(CreateExerciseActivity.class);

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
    }
}
