package ca.dal.cs.athletemonitor.athletemonitor;


import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
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
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper.authTestUser;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.not;

/**
 * Espresso Test for the CreateTeam Activity.
 */
@RunWith(AndroidJUnit4.class)
public class CreateTeamActivityTest {
    /**
     * Test user for this test set
     */
    private static User testUser;

    @Rule
    public IntentsTestRule<CreateTeamActivity> mActivityRule =
            new IntentsTestRule(CreateTeamActivity.class, false, false);

    /**
     * Set up test environment for this test set
     *
     * @throws Exception General exceptions
     */
    @BeforeClass
    public static void setupTestEnvironment() throws Exception {
        // create a test user in the database and authenticate
        testUser = TestingHelper.createTestUser();
        AccountManager.createUser(testUser);
        AccountManager.authenticate(testUser, null);
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
    public void launchActivity(){
        // load the test user username as an extra before runing tests
        Intent i = new Intent();
        i.putExtra("username", testUser.getUsername());
        mActivityRule.launchActivity(i);
    }

    /**
     * Test that the proper button and fields exist.
     * @throws Exception
     */
    @Test
    public void testProperFieldsExist() throws Exception {
        //Try to get the fields and button.
        onView(withId(R.id.newTeamName));
        onView(withId(R.id.newTeamMotto));
        onView(withId(R.id.submitTeamButton));
    }

    /**
     * Test that the submit new team button is disabled if the fields are invalid
     */
    @Test
    public void testSubmitButtonDisabled(){
        onView(withId(R.id.submitTeamButton)).check(matches(not(isEnabled())));
    }

    /**
     * Test that the submit new team button is enabled if the fields are valid
     */
    @Test
    public void testSubmitButtonEnabled() throws Exception {
        onView(withId(R.id.newTeamName)).perform(typeText("testteam"));
        onView(withId(R.id.newTeamMotto)).perform(typeText("testmotto"), closeSoftKeyboard());

        onView(withId(R.id.submitTeamButton)).check(matches(isEnabled()));
    }

    /**
     * Tests that creating a team when clicking create team button
     *
     * @throws Exception General exception
     */
    @Test
    public void testOnCreateTeamButtonClick() throws Exception {
        onView(withId(R.id.newTeamName)).perform(typeText("testteam"));
        onView(withId(R.id.newTeamMotto)).perform(typeText("testmotto"), closeSoftKeyboard());

        //Try to click the button.
        onView(withId(R.id.submitTeamButton)).perform(click());
        intended(hasComponent(TeamActivity.class.getName()));

        //test that the team was created in the database
        AccountManager.getUser(testUser.getUsername(), new AccountManager.UserObjectListener() {
            @Override
            public void onUserPopulated(User user) {
                assertNotNull(user);

                assertTrue(user.getUserTeams().contains(
                        new Team("testteam", "testmotto", user.getUsername())));
            }
        });
    }
}
