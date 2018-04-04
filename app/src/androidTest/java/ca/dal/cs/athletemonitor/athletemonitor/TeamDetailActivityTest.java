package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Tests TeamDetailActivity functionality
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TeamDetailActivityTest {
    /**
     * Test user for this test set
     */
    private static User testUser;

    /**
     * Test team for this test set
     */
    private static Team testTeam;

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
        testUser = TestingHelper.createTestUser();
        testTeam = TestingHelper.createTestTeam(testUser.getUsername());

        TeamManager.newTeam(testTeam);
        TestingHelper.setupTestEnvironment(intent, testUser);
        sleep(250);
        mActivityRule.launchActivity(intent);

        onView(withParent(withId(R.id.teamList))).perform(click());
    }

    /**
     * Tests that the correct views are on the activity
     *
     * @throws Exception
     */
    @Test
    public void testProperFieldsExist() throws Exception {
        onView(withId(R.id.toolbar));
        onView(withId(R.id.teamName));
        onView(withId(R.id.teamMotto));
        onView(withId(R.id.memberList));
        onView(withId(R.id.separatorBar));
    }

    /**
     * Tests that the team information cannot be changed if not in edit mode
     *
     * @throws Exception Generic exception
     */
    @Test
    public void testNotEditable() throws Exception {
        onView(allOf(withParent(withId(R.id.mainLayout)), withId(R.id.teamName))).check(matches(not(isFocusable())));
        onView(withId(R.id.teamMotto)).check(matches(not(isFocusable())));
    }

    /**
     * Tests that when the Edit button is clicked, the fields become enabled
     *
     * @throws Exception Generic exception
     */
    @Test
    public void testisEditable() throws Exception {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.action_edit_team)).perform(click());
        onView(withId(R.id.teamName)).check(matches(isFocusable()));
        onView(withId(R.id.teamMotto)).check(matches(isFocusable()));
    }

    /**
     * Tests that when Submit Changes button is clicked, the team information is editable and
     * that edits are reflected in the database
     *
     * @throws Exception Generic exception
     */
    @Test
    public void testUpdatedInfo() throws Exception {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.action_edit_team)).perform(click());
        onView(withId(R.id.teamName)).perform(clearText(), typeText("UpdatedTeamName"), closeSoftKeyboard());
        onView(withId(R.id.teamMotto)).perform(clearText(), typeText("UpdatedTeamMotto"), closeSoftKeyboard());
        onView(withId(R.id.saveInfo)).perform(click());
        onView(allOf(withText("UpdatedTeamName")));
        onView(withId(R.id.teamName)).check(matches(not(isFocusable())));
        onView(withId(R.id.teamMotto)).check(matches(not(isFocusable())));
    }

    /**
     * Tests that a team owner can invite another user to a team
     *
     * @throws Exception
     */
    @Test
    public void inviteTeamMemberTest() throws Exception {
        User userToInvite = TestingHelper.createTestUser();

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.action_invite_user)).perform(click());
        onView(withResourceName("search_src_text")).perform(
                typeText(userToInvite.getUsername()), pressImeActionButton());

        onView(withText("No")).perform(click());
        onView(withResourceName("search_src_text")).check(matches(withText(userToInvite.getUsername())));

        onView(withResourceName("search_src_text")).perform(pressImeActionButton());

        onView(withText("Yes")).perform(click());
    }

    /**
     * Tests that a member is added to a team when the user accepts an invitation
     *
     * @throws Exception
     */
    @Test
    public void teamInviteAcceptTest() throws Exception {
        //mock up a test team and invitation
        Team teamToJoinOn = TestingHelper.createTestTeam(TestingHelper.createTestUser().getUsername());
        TeamManager.newTeam(teamToJoinOn);
        TeamManager.inviteUser(testUser.getUsername(), teamToJoinOn);
        sleep(250);
        pressBack();

        onView(allOf(withText("You have an invitation!")));
        onView(allOf(withText("Accept"))).perform(click());
        onView(allOf(withParent(withId(R.id.teamList)), withText(teamToJoinOn.getName())));
    }

    /**
     * Tests that a member is not added to a team when the user declines an invitation
     *
     * @throws Exception
     */
    @Test
    public void teamInviteDeclineTest() throws Exception {
        //mock up a test team and invitation
        Team teamToJoinOn = TestingHelper.createTestTeam(TestingHelper.createTestUser().getUsername());
        TeamManager.newTeam(teamToJoinOn);
        TeamManager.inviteUser(testUser.getUsername(), teamToJoinOn);
        sleep(250);
        pressBack();

        onView(allOf(withText("You have an invitation!")));
        onView(allOf(withText("Decline"))).perform(click());
        onView(not(allOf(withParent(withId(R.id.teamList)), withText(teamToJoinOn.getName()))));
    }

    /**
     * Tests that accepting a request from a user to join a team succeeds
     *
     * @throws Exception
     */
    @Test
    public void acceptRequestToJoinTeamTest() throws Exception {
        //mock up an invitation
        User requestingUser = TestingHelper.createTestUser();
        AccountManager.createUser(requestingUser);
        TeamManager.requestToJoinTeam(testTeam, requestingUser);
        sleep(250);
        pressBack();

        onView(allOf(withText("There is a request to join your team!")));
        onView(allOf(withText("Accept"))).perform(click());
        onView(withText(testTeam.getName())).perform(click());
        onView(withId(R.id.memberList)).check(matches(hasDescendant(withText(requestingUser.getUsername()))));
    }

    /**
     * Tests that declining a request to join a team succeeds
     *
     * @throws Exception
     */
    @Test
    public void declineRequestToJoinTeamTest() throws Exception {
        //mock up an invitation
        User requestingUser = TestingHelper.createTestUser();
        AccountManager.createUser(requestingUser);
        TeamManager.requestToJoinTeam(testTeam, requestingUser);
        sleep(250);
        pressBack();

        onView(allOf(withText("There is a request to join your team!")));
        onView(allOf(withText("Decline"))).perform(click());
        onView(withText(testTeam.getName())).perform(click());
        onView(withId(R.id.memberList)).check(matches(not(hasDescendant(withText(requestingUser.getUsername())))));
    }
}

