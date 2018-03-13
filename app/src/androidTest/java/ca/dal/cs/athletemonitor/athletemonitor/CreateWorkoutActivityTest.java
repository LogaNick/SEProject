package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper.authTestUser;
import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

/**
 * Espresso Test for the Create Workout Activity.
 */
@RunWith(AndroidJUnit4.class)
public class CreateWorkoutActivityTest {
    @Rule
    public IntentsTestRule<CreateWorkoutActivity> mActivityRule =
            new IntentsTestRule<>(CreateWorkoutActivity.class, false, false);

    @BeforeClass
    public static void setupEnvironment(){
        authTestUser();
    }

    @Before
    public void setupUser(){
        TestingHelper.addTestUserExercises();
        mActivityRule.launchActivity(new Intent().putExtra("username", "testuser"));
    }

    @After
    public void cleanupTestUser() throws Exception {
        // Sleep to wait for any firebase callbacks
        sleep(3000);
        TestingHelper.resetTestUserWorkouts();
        TestingHelper.resetTestUserExercises();
        sleep(1000);
    }

    @Test
    public void testExerciseCheckboxes() throws Exception {
        sleep(2000);
        onView(withTagValue(is((Object) "exercise0"))).check(matches(withText(containsString("exercise 1"))));
        onView(withTagValue(is((Object) "exercise1"))).check(matches(withText(containsString("exercise 2"))));
        onView(withTagValue(is((Object) "exercise2"))).check(matches(withText(containsString("exercise 3"))));
        onView(withId(R.id.createWorkoutLinearLayout)).check(matches(hasChildCount(3)));
        onView(withId(R.id.submitNewWorkoutButton));
    }

    /**
     * Test creating a new workout by selecting some exercises, and submitting the workout.
     */
    @Test
    public void testCreateNewWorkout() throws Exception {
        onView(withId(R.id.newWorkoutName)).perform(clearText(), typeText("Test Workout"), closeSoftKeyboard());
        onView(withTagValue(is((Object) "exercise0"))).perform(click());
        onView(withTagValue(is((Object) "exercise2"))).perform(click());
        onView(withId(R.id.submitNewWorkoutButton)).perform(click());
        sleep(3000);

        AccountManager.getUser("testuser", new AccountManager.UserObjectListener() {
            @Override
            public void onUserPopulated(User user) {
                assertEquals(1, user.getUserWorkouts().size());
                assertEquals(user.getUserWorkouts().get(0).getName(), "Test Workout");
                assertFalse(user.getUserWorkouts().get(0).isCompleted());
                assertEquals(user.getUserWorkouts().get(0).getExercises().size(), 2);

                assertTrue(user.getUserWorkouts().get(0).getExercises().get(0).equals(TestingHelper.testExercise1));
                assertEquals(user.getUserWorkouts().get(0).getExercises().get(0).getData(), 0);

                assertTrue(user.getUserWorkouts().get(0).getExercises().get(1).equals(TestingHelper.testExercise3));
                assertEquals(user.getUserWorkouts().get(0).getExercises().get(1).getData(), 0);
            }
        });
    }
}