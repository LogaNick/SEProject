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
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.allOf;
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
    //@Ignore
    @Test
    public void z_testUpdatedInfo() throws Exception {
        onView(withId(R.id.editTeamButton)).perform(click());
        onView(withId(R.id.teamName)).perform(clearText(), typeText("UpdatedTeamName"), closeSoftKeyboard());
        onView(withId(R.id.teamMotto)).perform(clearText(), typeText("UpdatedTeamMotto"), closeSoftKeyboard());
        onView(withId(R.id.editTeamButton)).perform(click());
        onView(allOf(withText("UpdatedTeamName")));
        sleep(500);
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