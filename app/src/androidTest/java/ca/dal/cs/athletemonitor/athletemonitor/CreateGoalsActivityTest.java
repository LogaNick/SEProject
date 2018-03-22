package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

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
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by nicholasbarreyre on 2018-03-21.
 */
@RunWith(AndroidJUnit4.class)
public class CreateGoalsActivityTest {
    @Rule
    public IntentsTestRule<CreateGoalActivity> mActivityRule =
            new IntentsTestRule(CreateGoalActivity.class, false, false);

    @BeforeClass
    public static void setupEnvironment(){
        authTestUser();
    }

    @Before
    public void setupUser(){
        TestingHelper.resetTestUserExercises();
        Intent i = new Intent();
        i.putExtra("username", "testuser");
        mActivityRule.launchActivity(i);
    }

    /**
     * Test that the proper button and fields exist.
     * @throws Exception
     */
    @Test
    public void testProperFieldsExist() throws Exception {
        //Try to get the fields and button.
        onView(withId(R.id.newGoalName));
        onView(withId(R.id.newGoalDescription));
        onView(withId(R.id.newGoalSubmitButton));
    }


    /**
     * Test that the submit new exercise button works
     */
    @Test
    public void testSubmitButtonEnabled() throws Exception {
        onView(withId(R.id.newGoalName)).perform(typeText("Run"));
        onView(withId(R.id.newGoalDescription)).perform(typeText("Run outside"),closeSoftKeyboard());

        //Try to click the button.
        onView(withId(R.id.newGoalSubmitButton)).perform(click());
        intended(hasComponent(GoalsActivity.class.getName()));
    }
}