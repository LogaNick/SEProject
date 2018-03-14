package ca.dal.cs.athletemonitor.athletemonitor;


import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


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
    @Rule
    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule(MainActivity.class, false, false);

    @Before
    public void loginToTestUser() {
        // Authenticate user and set login state to false so Firebase does not keep testuser online
        AccountManager.authenticate(new User("testuser", "abc"), null);
        AccountManager.setUserLoginState("testuser", false);
        Intent i = new Intent();
        i.putExtra("username", "testuser");
        mActivityRule.launchActivity(i);
    }

    /**
     * Test that the button to transfer to the exercise activity works.
     * @throws Exception
     */
    @Test
    public void testGoToExerciseButton() throws Exception {
        //Try to click the button.
        onView(withId(R.id.goToExerciseActivityButton)).perform(click());
        intended(hasComponent(ExerciseActivity.class.getName()));
    }

    @Test
    public void testGoToUserInfoButton() {
        onView(withId(R.id.goToUserInfo)).perform(click());
        intended(hasComponent(UserInformationActivity.class.getName()));
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

        onView(withId(R.id.btnSignOut)).perform(click());

        //check that Firebase has been updated
        AccountManager.isLoggedIn(testUser, TestingHelper.assertFalseBooleanResult());

        //check that Login Activity has been started
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void testSignOutButtonOffline() throws Exception {
        AccountManager.setOnline(false);

        onView(withId(R.id.btnSignOut)).perform(click());

        onView(withText(R.string.logout_while_offline_warning)).check(matches(isDisplayed()));
        onView(withText(R.string.logout_while_offline_warning_save)).perform(click());

        assertTrue(AccountManager.isOnline());

        //check that Login Activity has been started
        intended(hasComponent(LoginActivity.class.getName()));
    }

    /**
     * Test that the button to create a new team transfers to the new team activity.
     * @throws Exception
     */
    @Test
    public void testCreateNewTeamButton() throws Exception {
        // Try to click the button.
        onView(withId(R.id.createTeamButton)).perform(click());
        intended(hasComponent(CreateTeamActivity.class.getName()));
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
}
