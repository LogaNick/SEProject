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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.util.Log;

import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Test class for Team Activity
 */
@RunWith(AndroidJUnit4.class)
public class TeamActivityTest {
    /**
     * Test user for this test set
     */
    private static User testUser;

    /**
     * Intent used to launch the activity
     */
    private static Intent intent = new Intent();

    @Rule
    public IntentsTestRule<TeamActivity> mActivityRule =
            new IntentsTestRule(TeamActivity.class, false, false);

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
     * Tests that all required components exist on the activity
     *
     * @throws Exception General exception
     */
    @Test
    public void testCorrectComponentsTest() throws Exception {
        //Try to get the button.
        onView(withId(R.id.createTeamButton));
        onView(withId(R.id.teamLayoutScrollView));
        onView(withId(R.id.teamLinearLayout));
    }

    /**
     * Tests that create team button transitions to create team activity
     *
     * @throws Exception General exception
     */
    @Test
    public void testGoCreateTeamButton() throws Exception {
        //Try to click the button.
        onView(withId(R.id.createTeamButton)).perform(click());
        intended(hasComponent(CreateTeamActivity.class.getName()));
    }

    /**
     * Tests whether creating a team results in team activity being updated with newly created
     * team information
     *
     * @throws Exception General exception
     */
    @Test
    public void testCreateAndViewTeam() throws Exception{
        // Click the button
        onView(withId(R.id.createTeamButton)).perform(click());

        // Enter team information
        onView(withId(R.id.newTeamName)).perform(typeText("TestTeam_name"), closeSoftKeyboard());
        onView(withId(R.id.newTeamMotto)).perform(typeText("TestTeam_motto"), closeSoftKeyboard());
        onView(withId(R.id.submitTeamButton)).perform(click());

        // Check that the new exercise is there
        onView(allOf(withParent(withId(R.id.teamLinearLayout)),withText("TestTeam_name"))).check(matches(withText("TestTeam_name")));

        // try to click on team
        onView(allOf(withParent(withId(R.id.teamLinearLayout)),withText("TestTeam_name"))).perform(click());
        onView(allOf(withText("TestTeam_name")));
    }

    /**
     * Tests team overview is visible with the correct owner and buttons
     * @throws Exception
     */
    @Test
    public void teamListOnClickTest() throws Exception {
        // first create a test team to click on
        onView(withId(R.id.createTeamButton)).perform(click());
        onView(withId(R.id.newTeamName)).perform(typeText("TestTeam_name"), closeSoftKeyboard());
        onView(withId(R.id.newTeamMotto)).perform(typeText("TestTeam_motto"), closeSoftKeyboard());
        onView(withId(R.id.submitTeamButton)).perform(click());

        // try to click on team
        onView(allOf(withParent(withId(R.id.teamLinearLayout)),withText("TestTeam_name") )).perform(click());
        onView(allOf(withText("TestTeam_name")));

    }
}
