package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.cs.athletemonitor.athletemonitor.AccountManager;
import ca.dal.cs.athletemonitor.athletemonitor.R;
import ca.dal.cs.athletemonitor.athletemonitor.TeamActivity;
import ca.dal.cs.athletemonitor.athletemonitor.User;
import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.allOf;

public class TeamDetailActivityTestset2 {
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
        testUser = (User) mActivityRule.getActivity().getIntent().getSerializableExtra("user");
        onView(withParent(withId(R.id.teamLinearLayout))).perform(click());
        onView(allOf(withText("More"))).perform(click());
    }

    /**
     * Tests that transfer ownership UI works
     *
     * @throws Exception
     */
    @Test
    public void transferOwnerShipTestSuccess() throws Exception {
        User testUser = TestingHelper.createTestUser();
        AccountManager.createUser(testUser);

        sleep(300);
        onView(withId(R.id.editTeamButton)).perform(click());
        onView(withId(R.id.teamOwner)).perform(clearText(), typeText(testUser.getUsername()), closeSoftKeyboard());
        onView(withId(R.id.transferOwnerButton)).perform(click());
        onView(withId(R.id.lblMessage)).check(matches(withText(R.string.ownershipTransferred)));

        AccountManager.deleteUser(testUser, TestingHelper.assertTrueBooleanResult());
    }
}