package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper.authTestUser;
import static java.lang.Thread.sleep;


/**
 * Created by nicholasbarreyre on 2018-03-10.
 *
 * Esspresso Test for Goals Activity
 */

@RunWith(AndroidJUnit4.class)
public class GoalsActivityTest {

    /**
     * Test user for this test set
     */
    private static User testUser;
    private static Intent intent = new Intent();

    @Rule
    public IntentsTestRule<GoalsActivity> mActivityRule =
            new IntentsTestRule(GoalsActivity.class, false, false);

    @BeforeClass
    public static void setupTestEnvironment() throws Exception{
        testUser = TestingHelper.createTestUser();

        Goal goal = new Goal ("testGoal");
        testUser.addUserGoal(goal);
        TestingHelper.setupTestEnvironment(intent, testUser);
    }

    /**
     * Clean up test environment after this test set has run
     *
     * @throws Exception
     */
    @AfterClass
    public static void cleanupEnvironment() throws Exception {
        AccountManager.setUserLoginState(testUser.getUsername(), false);
        AccountManager.deleteUser(testUser, null);
    }
/*
    @Before
    public void setupActivity(){
        Intent i = new Intent();
        i.putExtra("username", "testuser");
        mActivityRule.launchActivity(i);
        TestingHelper.resetTestUserExercises();
    }*/

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
        onView(withId(R.id.createGoalButton));
    }

    /**
     * Test that layout scroll view exist.
     * @throws Exception
     */
    @Test
    public void testHasLayoutScroll() throws Exception {
        //Try to get the button.
        onView(withId(R.id.layoutScrollView));
    }

    /**
     * Test that a linear layout exist.
     * @throws Exception
     */
    @Test
    public void testHasLinearLayout() throws Exception {
        //Try to get the button.
        onView(withId(R.id.goalLinearLayout));
    }

    /**
     * Test that the button to transfer to the create goal activity works.
     *
     * @throws Exception
     */
    @Test
    public void testGoToGoalsButton() throws Exception {
        //Try to click the button.
        onView(withId(R.id.createGoalButton)).perform(click());
        intended(hasComponent(CreateGoalActivity.class.getName()));
    }
}