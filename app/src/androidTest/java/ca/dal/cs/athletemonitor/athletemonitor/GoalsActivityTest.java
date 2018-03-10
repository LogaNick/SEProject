package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by nicholasbarreyre on 2018-03-10.
 *
 * Esspresso Test for Goals Activity
 */

public class GoalsActivityTest {
    @Rule
    public IntentsTestRule<GoalsActivity> mActivityRule =
            new IntentsTestRule<>(GoalsActivity.class, false, false);

    @Before
    public void launchActivity() {
        // The CreateTeamActivity needs the extras because it returns to the MainActivity
        Intent i = new Intent();
        i.putExtra("username", "testuser");
        mActivityRule.launchActivity(i);
    }

    /**
     * Test that the proper button and field exist.
     *
     * @throws Exception
     */
    @Test
    public void testProperFieldsExist() throws Exception {
        //Try to get the fields and button.
        onView(withId(R.id.newGoal));
        onView(withId(R.id.submitGoalButton));
    }
}