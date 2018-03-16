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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.not;

/**
 * Created by vibar on 16/03/2018.
 */

public class TeamDetailActivityTest {
    /**
     * Test user for this test set
     */
    private static User testUser;
    private static Intent intent = new Intent();

    @Rule
    public IntentsTestRule<TeamDetailActivity> mActivityRule =
            new IntentsTestRule(TeamDetailActivity.class, false, false);

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

    @Test
    public void testProperFieldsExist() throws Exception {
        //Try to get the fields and button.
        onView(withId(R.id.editTeamButton));
        onView(withId(R.id.teamName));
        onView(withId(R.id.teamMotto));
    }
    @Test
    public void testNotEditable() throws Exception{
        onView(withId(R.id.teamName)).check(matches(not(isEnabled())));
        onView(withId(R.id.teamMotto)).check(matches(not(isEnabled())));

    }
    @Test
    public void testisEditable() throws Exception{
        onView(withId(R.id.editTeamButton)).perform(click());
        onView(withId(R.id.teamName)).check(matches(isEnabled()));
        onView(withId(R.id.teamMotto)).check(matches(isEnabled()));
    }
    @Test
    public void testUpdatedInfo() throws Exception{
        onView(withId(R.id.editTeamButton)).perform(click());
        onView(withId(R.id.teamName)).perform(typeText("UpdatedTeamName"));
        onView(withId(R.id.teamMotto)).perform(typeText("UpdatedTeamMotto"));
        onView(withId(R.id.updateTeamButton)).perform(click());
        onView(withId(R.id.teamName)).check(matches(withText("UpdatedTeamName")));
        onView(withId(R.id.teamMotto)).check(matches(withText("UpdatedTeamMotto")));
    }
}
