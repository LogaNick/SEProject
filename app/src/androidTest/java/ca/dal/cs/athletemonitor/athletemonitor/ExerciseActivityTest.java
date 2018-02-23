package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
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
import static org.junit.Assert.*;

/**
 * Espresso Test for the exercise Activity.
 */
@RunWith(AndroidJUnit4.class)
public class ExerciseActivityTest {
    @Rule
    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule(ExerciseActivity.class);

    /**
     * Test that the button to create an exercise exist.
     * @throws Exception
     */
    @Test
    public void testHasCreateButton() throws Exception {
        //Try to get the button.
        onView(withId(R.id.createExerciseButton));
    }

    /**
     * Test that the button to transfer to the create exercise activity works.
     * @throws Exception
     */
    @Test
    public void testGoToExerciseButton() throws Exception {
        //Try to click the button.
        onView(withId(R.id.createExerciseButton)).perform(click());
        intended(hasComponent(CreateExerciseActivity.class.getName()));
    }
}
