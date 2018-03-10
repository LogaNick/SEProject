package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

/**
 * Espresso Test for the Create Workout Activity.
 */
@RunWith(AndroidJUnit4.class)
public class CreateWorkoutActivityTest {
    @Rule
    public IntentsTestRule<WorkoutActivity> mActivityRule =
            new IntentsTestRule(WorkoutActivity.class, false, false);

    @Before
    public void setupUser(){
        TestingHelper.resetTestUserWorkouts();
        TestingHelper.addTestUserExercises();
        Intent i = new Intent();
        i.putExtra("username", "testuser");
        mActivityRule.launchActivity(i);
    }

    @After
    public void cleanupTestUser(){
        TestingHelper.resetTestUserExercises();
    }

    @Test
    public void testExerciseCheckboxes(){
        // Test that there are 3 exercises in the list
        onView(withTagValue(is((Object) "exercise0"))).check(matches(withText("exercise 1")));
        onView(withTagValue(is((Object) "exercise1"))).check(matches(withText("exercise 2")));
        onView(withTagValue(is((Object) "exercise2"))).check(matches(withText("exercise 3")));
        onView(withId(R.id.createWorkoutLinearLayout)).check(matches(hasChildCount(3)));

        onView(withId(R.id.submitNewWorkoutButton));
    }
}