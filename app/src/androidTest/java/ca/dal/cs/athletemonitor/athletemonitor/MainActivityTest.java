package ca.dal.cs.athletemonitor.athletemonitor;


import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;
import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Espresso Test for the Main Activity.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    /**
     * Test user for this test set
     */
    private static User testUser;
    private static Intent intent = new Intent();

    @ClassRule
    public static IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule(MainActivity.class, false, false);

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
     * Tests the featured button on the page (in this case it will start recording a workout).
     * @throws Exception
     */
    @Test
    public void testFeaturedActivityButton() throws Exception{
        onView(withId(R.id.featureBtn))
                .perform(click());
        intended(hasComponent(RecordActivity.class.getName()));
        onView(withId(R.id.pause_button))
                .perform(click());
    }

    /**
     * Test that the button to transfer to the exercise activity works.
     * @throws Exception
     */
    @Test
    public void testGoToExerciseButton() throws Exception {
        openNavDrawer();
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.goToExerciseActivity));
        intended(hasComponent(ExerciseActivity.class.getName()));
    }

    /**
     * Assert that the User Info button takes you to User Info.
     */
    @Test
    public void testGoToUserInfoButton() {
        openNavDrawer();
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.goToUserInfoActivity));
        intended(hasComponent(UserInformationActivity.class.getName()));
    }

    /**
     * Assert that the Record button takes you to Record.
     */
    @Test
    public void testGoToRecordButton() throws Exception{
        openNavDrawer();
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.goToRecordWorkoutActivity));
        intended(hasComponent(RecordActivity.class.getName()));
    }

    /**
     * Assert that the Goals button takes you to Goals
     * @throws Exception
     */
    @Test
    public void testGoalsButton() {
        openNavDrawer();
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.goToGoalsActivity));
        intended(hasComponent(GoalsActivity.class.getName()));
    }

    /**
     * Test that the button to create a new team transfers to the new team activity.
     * @throws Exception
     */
    @Test
    public void testTeamButton() throws Exception {
        openNavDrawer();
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.goToTeamActivity));
        intended(hasComponent(TeamActivity.class.getName()));
    }

    /**
     * Test that the online/offline toggle switch toggles offline status
     */
    @Test
    public void testOnlineToggleSwitch() throws Exception {
        sleep(1000);
        assertTrue(AccountManager.isOnline());
        onView(withId(R.id.onlineToggleSwitch)).perform(click());
        assertFalse(AccountManager.isOnline());
    }

    /**
     * Test that the sign out button works.
     * @throws Exception
     */
    @Test
    public void testSignOutButton() throws Exception {

        //Create a test user
        User testUser = TestingHelper.createTestUser();

        //Create an entry in the online_users node (in Firebase)
        AccountManager.setUserLoginState(testUser.getUsername(), false);

        openNavDrawer();
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.btnSignOut));

        //check that Firebase has been updated
        AccountManager.isLoggedIn(testUser, TestingHelper.assertFalseBooleanResult());

        // Need to dismiss dialog before finishing
        onView(withText(R.string.logout_while_offline_warning_quit)).perform(click());

        /* During testing, there is no login activity to return to,
         * so in its prior form, this test fails. To alleviate this,
         * we simply check that the activity "isFinishing". */
                assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void testSignOutButtonOffline() throws Exception {
        AccountManager.setOnline(false);

        openNavDrawer();
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.btnSignOut));

        onView(withText(R.string.logout_while_offline_warning)).check(matches(isDisplayed()));
        onView(withText(R.string.logout_while_offline_warning_save)).perform(click());

        assertTrue(AccountManager.isOnline());

        /* During testing, there is no login activity to return to,
         * so in its prior form, this test fails. To alleviate this,
         * we simply check that the activity "isFinishing". */
        assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @After
    public void release() {
        Intents.release();
    }

    /**
     * Helper for opening the navigation drawer
     */
    public void openNavDrawer(){
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
    }

}

