package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper.authTestUser;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by nicholasbarreyre on 2018-03-10.
 *
 * Esspresso Test for Goals Activity
 */

public class GoalsActivityTest {
    @Rule
    public IntentsTestRule<GoalsActivity> mActivityRule =
            new IntentsTestRule(GoalsActivity.class, false, false);

    @BeforeClass
    public static void setupEnvironment(){
        authTestUser();
    }

    @Before
    public void setupActivity(){
        Intent i = new Intent();
        i.putExtra("username", "testuser");
        mActivityRule.launchActivity(i);
        TestingHelper.resetTestUserExercises();
    }

    /**
     * Test that the button to create an exercise exist.
     * @throws Exception
     */
    @Test
    public void testHasCreateButton() throws Exception {
        //Try to get the button.
        onView(withId(R.id.createGoalButton));
    }

    /**
     * Test that layout scroll view exist.
     * @throws Exception
     */
    @Test
    public void testHasLayoutScroll() throws Exception {
        //Try to get the button.
        onView(withId(R.id.layoutScrollView));
    }

    /**
     * Test that a linear layout exist.
     * @throws Exception
     */
    @Test
    public void testHasLinearLayout() throws Exception {
        //Try to get the button.
        onView(withId(R.id.goalLinearLayout));
    }

    /**
     * Test that the button to transfer to the create goal activity works.
     *
     * @throws Exception
     */
    @Test
    public void testGoToGoalsButton() throws Exception {
        //Try to click the button.
        onView(withId(R.id.createGoalButton)).perform(click());
        intended(hasComponent(CreateGoalActivity.class.getName()));
    }
}