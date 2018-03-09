package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper.authTestUser;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

/**
 * Test class for Team Activity
 */
public class TeamActivityTest {
    /**
     * Test user for this set of tests
     */
    private static User testUser;

    @Rule
    public IntentsTestRule<TeamActivity> mActivityRule =
            new IntentsTestRule(TeamActivity.class, false, false);

    /**
     * Sets up test environment for all tests
     *
     * @throws Exception General exception
     */
    @BeforeClass
    public static void setupEnvironment() throws Exception {
        testUser = authTestUser();
    }

    /**
     * Sets up test environment before each test is run
     *
     * @throws Exception General exception
     */
    @Before
    public void setupActivity() throws Exception {
        Intent i = new Intent();
        i.putExtra("username", testUser.getUsername());
        mActivityRule.launchActivity(i);
        TestingHelper.resetTestUserExercises();
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
    public void testGoToExerciseButton() throws Exception {
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
        onView(withId(R.id.newTeamName)).perform(typeText("TestTeam_name"));
        onView(withId(R.id.newTeamMotto)).perform((typeText("TestTeam_motto")));
        onView(withId(R.id.submitTeamButton)).perform(click());

        // Check that the new exercise is there
        onView(withParent(withId(R.id.teamLinearLayout))).check(matches(withText("TestTeam_name")));

        // Check that the dialog box works
        onView(withParent(withId(R.id.teamLinearLayout))).perform(click());
        onView(withText("TestTeam_name"));
    }
}
