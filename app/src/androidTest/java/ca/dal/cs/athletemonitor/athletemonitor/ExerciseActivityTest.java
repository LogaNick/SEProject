package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Espresso Test for the exercise Activity.
 */
@RunWith(AndroidJUnit4.class)
public class ExerciseActivityTest {
    @Rule
    public ActivityTestRule<ExerciseActivity> mActivityRule =
            new ActivityTestRule(ExerciseActivity.class);

    /**
     * Test that the button to create an exercise exist.
     * @throws Exception
     */
    @Test
    public void testHasCreateButton() throws Exception {
        //Try to get the button.
        onView(withId(R.id.createExerciseButton));
    }
}
