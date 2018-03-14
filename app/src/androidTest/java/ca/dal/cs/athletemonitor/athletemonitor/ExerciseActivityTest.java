package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.LinearLayout;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper.authTestUser;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Espresso Test for the exercise Activity.
 */
@RunWith(AndroidJUnit4.class)
public class ExerciseActivityTest {
    /**
     * Test user for this test set
     */
    private static User testUser;
    private static Intent intent = new Intent();

    @Rule
    public IntentsTestRule<ExerciseActivity> mActivityRule =
            new IntentsTestRule(ExerciseActivity.class, false, false);

    /**
     * Set up test environment for this test set
     *
     * @throws Exception General exceptions
     */
    @BeforeClass
    public static void setupTestEnvironment() throws Exception {
        testUser = TestingHelper.createTestUser();
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

    @Test
    public void testCreateAndViewExercise(){
        // Click the create exercise button
        onView(withId(R.id.createExerciseButton)).perform(click());

        // Enter exercise details
        onView(withId(R.id.newExerciseName)).perform(typeText("Run"));
        onView(withId(R.id.newExerciseDescription)).perform(typeText("Run outside"));
        onView(withId(R.id.newExerciseTime)).perform(typeText("5"), closeSoftKeyboard());
        onView(withId(R.id.newExerciseTimeUnits)).perform(click());
        onData(allOf(is(instanceOf(String.class)),is("Minutes")))
                .perform(click());

        // Click submit button
        onView(withId(R.id.newExerciseSubmitButton)).perform(click());

        // Check that the new exercise is there
        onView(withParent(withId(R.id.exerciseLinearLayout))).check(matches(withText("Run")));

        // Check that the dialog box works
        onView(withParent(withId(R.id.exerciseLinearLayout))).perform(click());
        onView(allOf(withText("Run outside"),withText("5 minutes")));
    }
}
