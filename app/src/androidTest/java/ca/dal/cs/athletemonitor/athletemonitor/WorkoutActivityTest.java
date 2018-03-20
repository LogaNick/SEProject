package ca.dal.cs.athletemonitor.athletemonitor;


import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper.authTestUser;
import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * Espresso Test for the Workout Data Activity.
 */
@RunWith(AndroidJUnit4.class)
public class WorkoutActivityTest {
    /**
     * Test user for this test set
     */
    private static User testUser;
    private static Intent intent = new Intent();


    @Rule
    public IntentsTestRule<WorkoutActivity> mActivityRule =
            new IntentsTestRule(WorkoutActivity.class, false, false);

    /**
     * Set up test environment for this test set
     *
     * @throws Exception General exceptions
     */
    @BeforeClass
    public static void setupTestEnvironment() throws Exception {
        testUser = TestingHelper.createTestUser();

        Workout workout = new Workout("testWorkout");
        workout.addWorkoutExercise(new WorkoutExercise(TestingHelper.testExercise1));
        workout.addWorkoutExercise(new WorkoutExercise(TestingHelper.testExercise2));
        workout.addWorkoutExercise(new WorkoutExercise(TestingHelper.testExercise3));
        testUser.addUserWorkout(workout);

        TestingHelper.setupTestEnvironment(intent, testUser);
    }

    /**
     * Clean up test environment after this test set has run
     * @throws Exception
     */
    @AfterClass
    public static void cleanupEnvironment() throws Exception {
        AccountManager.setUserLoginState(testUser.getUsername(), false);
        AccountManager.deleteUser(testUser, null);
    }

    /**
     * Initializes and starts the activity before each test is run
     */
    @Before
    public void launchActivity() throws Exception {
        sleep(250);
        mActivityRule.launchActivity(intent);
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
        onView(withId(R.id.createWorkoutButton));
    }

    /**
     * Test that the button to transfer to the create workout activity works.
     * @throws Exception
     */
    @Test
    public void testGoToExerciseButton() throws Exception {
        //Try to click the button.
        onView(withId(R.id.createWorkoutButton)).perform(click());
        intended(hasComponent(CreateWorkoutActivity.class.getName()));
    }

    /**
     * Test that the user has a workout
     */
    @Test
    public void testViewWorkout() throws Exception {
        onView(withParent(withId(R.id.spinner))).check(matches(withText(containsString("testWorkout"))));
        onView(withTagValue(is((Object)"exerciseName0"))).check(matches(allOf(isDisplayed(), withText(TestingHelper.testExercise1.getName()))));
        onView(withTagValue(is((Object)"exerciseName1"))).check(matches(allOf(isDisplayed(), withText(TestingHelper.testExercise2.getName()))));
        onView(withTagValue(is((Object)"exerciseName2"))).check(matches(allOf(isDisplayed(), withText(TestingHelper.testExercise3.getName()))));
    }

    /**
     * Test that the user object is updated when workout data is submitted
     */
    @Test
    public void testWorkoutSubmission() {
        onView(withParent(withId(R.id.spinner))).check(matches(withText(containsString("testWorkout"))));
        assertFalse(testUser.getUserWorkouts().get(0).isCompleted());
        onView(withTagValue(is((Object)"workoutExercise0"))).perform(typeText("" + TestingHelper.testExercise1.getTime()), closeSoftKeyboard());
        onView(withTagValue(is((Object)"workoutExercise1"))).perform(typeText("" + TestingHelper.testExercise2.getTime()), closeSoftKeyboard());
        onView(withTagValue(is((Object)"workoutExercise2"))).perform(typeText("" + 0), closeSoftKeyboard());
        onView(withId(R.id.submitDataButton)).perform(click());
        assertTrue(WorkoutActivity.user.getUserWorkouts().get(0).isCompleted());
        assertEquals(0, WorkoutActivity.user.getUserWorkouts().get(0).getExercises().get(2).getData());
    }
}
