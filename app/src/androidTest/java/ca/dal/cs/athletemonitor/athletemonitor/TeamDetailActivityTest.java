package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.Assert;
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
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.v4.content.res.TypedArrayUtils.getString;
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
        onView(withId(R.id.inviteUserButton));
        onView(withId(R.id.inviteUsername));
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
        onView(withId(R.id.teamOwner)).perform(clearText(), typeText(testUser.getUsername()));
        onView(withId(R.id.transferOwnerButton)).perform(click());
        onView(withId(R.id.lblMessage)).check(matches(withText(R.string.ownershipTransferred)));

        AccountManager.deleteUser(testUser, TestingHelper.assertTrueBooleanResult());
    }

    /**
     * Tests that a team owner can invite another user to a team
     *
     * @throws Exception
     */
    @Test
    public void inviteTeamMemberTest() throws Exception {


        onView(withId(R.id.editTeamButton)).perform(click(), closeSoftKeyboard());
        onView(withId(R.id.inviteUserButton)).perform(click(), closeSoftKeyboard());
        onView(withId(R.id.inviteUsername)).perform(clearText(), typeText(testUser.getUsername()), closeSoftKeyboard());
        onView(withId(R.id.inviteUserButton)).perform(click());
        onView(withId(R.id.lblMessage)).check(matches(withText("Invitation sent!")));

    }

    /**
     * Tests that a member is added to a team when the user accepts an invitation
     *
     * @throws Exception
     */
    @Test
    public void teamInviteAcceptTest() throws Exception {
        onView(withId(R.id.editTeamButton)).perform(click(), closeSoftKeyboard());
        onView(withId(R.id.inviteUserButton)).perform(click(), closeSoftKeyboard());
        onView(withId(R.id.inviteUsername)).perform(clearText(), typeText(testUser.getUsername()), closeSoftKeyboard());
        onView(withId(R.id.inviteUserButton)).perform(click());
        onView(withId(R.id.lblMessage)).check(matches(withText("Invitation sent!")));
        pressBack();

        onView(allOf(withText("You have an invitation!")));
        onView(allOf(withText("Accept"))).perform(click());
        onView(withId(R.id.teamLinearLayout)).check(matches(Matchers.withListSize(2)));
    }

    /**
     * Tests that a member is not added to a team when the user declines an invitation
     *
     * @throws Exception
     */
    @Test
    public void teamInviteDeclineTest() throws Exception {
        onView(withId(R.id.editTeamButton)).perform(click(), closeSoftKeyboard());
        onView(withId(R.id.inviteUserButton)).perform(click(), closeSoftKeyboard());
        onView(withId(R.id.inviteUsername)).perform(clearText(), typeText(testUser.getUsername()), closeSoftKeyboard());
        onView(withId(R.id.inviteUserButton)).perform(click());
        onView(withId(R.id.lblMessage)).check(matches(withText("Invitation sent!")));
        pressBack();

        onView(allOf(withText("You have an invitation!")));
        onView(allOf(withText("Decline"))).perform(click());
        onView(withId(R.id.teamLinearLayout)).check(matches(Matchers.withListSize(1)));
    }


}

/**
 * Custom adapted from Cory Roy @ https://stackoverflow.com/a/30361345/3169479
 *
 */
class Matchers {
    public static Matcher<View> withListSize (final int size) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                return ((LinearLayout) view).getChildCount() == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("Expected " + size + " items");
            }
        };
    }
}