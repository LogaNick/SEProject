package ca.dal.cs.athletemonitor.athletemonitor;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by vibar on 07/03/2018.
 */

public class TeamActivityTest {
    @Rule
    public IntentsTestRule<TeamActivity> mActivityRule =
            new IntentsTestRule(TeamActivity.class);
    @Test
    public void testHasCreateButton() throws Exception {
        //Try to get the button.
        onView(withId(R.id.createTeamButton));
        onView(withId(R.id.teamLayoutScrollView));
        onView(withId(R.id.teamLinearLayout));
    }

}
