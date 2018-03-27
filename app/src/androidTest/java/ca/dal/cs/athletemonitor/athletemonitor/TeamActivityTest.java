package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static java.lang.Thread.sleep;
import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.is;

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
     * Clean up test environment after each test
     * @throws Exception
     */
    @After
    public void cleanupEnvironment() throws Exception {
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
        sleep(1000);
        mActivityRule.launchActivity(intent);
    }

    /**
     * Tests that all required components exist on the activity
     *
     * @throws Exception General exception
     */
    @Test
    public void testCorrectComponentsTest() throws Exception {
        onView(withId(R.id.teamList));
        onView(withId(R.id.toolbar));
    }

    /**
     * Tests that create team button transitions to create team activity
     *
     * @throws Exception General exception
     */
    @Test
    public void testCreateTeamButton() throws Exception {
        //Try to click the button.
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.action_create_team)).perform(click());
        intended(hasComponent(CreateTeamActivity.class.getName()));
    }

    /**
     * Tests whether creating a team results in team activity being updated with newly created
     * team information
     *
     * @throws Exception General exception
     */
    @Test
    public void testViewTeam() throws Exception{
        Team testTeam = testUser.getUserTeams().get(0);
        onView(allOf(withParent(withId(R.id.teamLinearLayout)),withText(testTeam.getName()))).perform(click());
        onView(withText(testTeam.getName()));
    }

    /**
     * Tests the quit team button
     * @throws Exception
     */
    @Test
    public void quitTeamTest() throws Exception {
        Team testTeam = testUser.getUserTeams().get(0);
        onView(allOf(withParent(withId(R.id.teamLinearLayout)),withText(testTeam.getName()))).perform(click());
        onView(withText("Quit Team")).perform(click());

        try{
            onView(withText(testTeam.getName())).check(matches(isDisplayed()));
            fail();
        }catch(NoMatchingViewException e){
            // team not found, test is successful
        }
    }

    /**
     * Tests that the action bar has the join team option
     *
     * @throws Exception
     */
    @Test
    public void actionBarHasJoinTeam() throws Exception {
       openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

       sleep(5000);
       onView(allOf(withParent(withId(R.id.toolbar)), withText(R.string.action_join_team)));
    }
}
