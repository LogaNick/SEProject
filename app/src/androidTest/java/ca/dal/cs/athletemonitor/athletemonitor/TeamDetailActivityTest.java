package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.cs.athletemonitor.athletemonitor.AccountManager;
import ca.dal.cs.athletemonitor.athletemonitor.CreateTeamActivity;
import ca.dal.cs.athletemonitor.athletemonitor.R;
import ca.dal.cs.athletemonitor.athletemonitor.User;
import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Tests TeamDetailActivity functionality
 */

public class TeamDetailActivityTest {
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
     *
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
        onView(withParent(withId(R.id.teamLinearLayout))).perform(click());
        onView(allOf(withText("More"))).perform(click());
    }

    /**
     * Tests that the correct views are on the activity
     *
     * @throws Exception
     */
    @Test
    public void testProperFieldsExist() throws Exception {
        //Try to get the fields and button.
        onView(withId(R.id.editTeamButton));
        onView(withId(R.id.teamName));
        onView(withId(R.id.teamMotto));
        onView(withId(R.id.transferOwnerButton));
    }

    /**
     * Tests that the team information cannot be changed if not in edit mode
     *
     * @throws Exception Generic exception
     */
    @Test
    public void testNotEditable() throws Exception {
        onView(withId(R.id.editTeamButton)).check(matches(withText(R.string.editTeam)));
        onView(withId(R.id.teamName)).check(matches(not(isEnabled())));
        onView(withId(R.id.teamMotto)).check(matches(not(isEnabled())));
    }

    /**
     * Tests that when the Edit button is clicked, the fields become enabled
     *
     * @throws Exception Generic exception
     */
    @Test
    public void testisEditable() throws Exception {
        onView(withId(R.id.editTeamButton)).perform(click());
        onView(withId(R.id.editTeamButton)).check(matches(withText(R.string.submitChanges)));
        onView(withId(R.id.teamName)).check(matches(isEnabled()));
        onView(withId(R.id.teamMotto)).check(matches(isEnabled()));
    }

    /**
     * Tests that when Submit Changes button is clicked, the team information is editable and
     * that edits are reflected in the database
     *
     * @throws Exception Generic exception
     */
    @Test
    public void testUpdatedInfo() throws Exception {
        onView(withId(R.id.editTeamButton)).perform(click());
        onView(withId(R.id.teamName)).perform(clearText(), typeText("UpdatedTeamName"), closeSoftKeyboard());
        onView(withId(R.id.teamMotto)).perform(clearText(), typeText("UpdatedTeamMotto"), closeSoftKeyboard());
        onView(withId(R.id.editTeamButton)).perform(click());
        onView(allOf(withText("UpdatedTeamName")));
    }

    /**
     * Tests that transfer ownership succeeds when user exists
     *
     * @throws Exception
     */
    @Test
    public void transferOwnerShipTestSuccess() throws Exception {
        assertTrue(false);
    }

    /**
     * Tests that transferring ownership fails when user does not exist
     *
     * @throws Exception
     */
    @Test
    public void transferOwnershipTestFailure() throws Exception {
        assertTrue(false);
    }
}
