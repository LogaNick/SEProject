package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.dal.cs.athletemonitor.athletemonitor.UserInformationActivity.USER_ID;
import static ca.dal.cs.athletemonitor.athletemonitor.UserInformationActivity.USER_INFORMATION;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UserInformationActivityUnitTests {

	//TODO Look into using Mockito to mock context and access string resources
	protected static final String TEST_ID = "testauston";
	private static final String TEST_FIRST_NAME = "Auston";
	private static final String TEST_LAST_NAME = "Matthews";
	private static final int TEST_AGE = 20;
	private static final int TEST_HEIGHT = 191;
	private static final int TEST_WEIGHT = 98;
	private static final String TEST_ATHLETE_TYPE = "Hockey Player";
	private static final String TEST_STATEMENT = "I want to win the Stanley Cup";

	@Rule
	public IntentsTestRule<UserInformationActivity> uiIntentRule =
			new IntentsTestRule<>(UserInformationActivity.class, false, false);

	@Before
	public void prepIntentAndLaunch() {
		Intent intent = new Intent();
		intent.putExtra(USER_ID, TEST_ID);
        uiIntentRule.launchActivity(intent);
		try {
			// Wait for database retrieval
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
	}

	@Test
	public void displayUserInfo() {
		onView(withId(R.id.nameTextView))
				.check(matches(withText(TEST_FIRST_NAME + " " + TEST_LAST_NAME)));
		onView(withId(R.id.ageDisplayView)).check(matches(withText(Integer.toString(TEST_AGE))));
		onView(withId(R.id.heightDisplayView)).check(matches(withText(TEST_HEIGHT + " cm")));
		onView(withId(R.id.weightDisplayView)).check(matches(withText(TEST_WEIGHT + " kg")));
		onView(withId(R.id.athleteTypeDisplayView)).check(matches(withText(TEST_ATHLETE_TYPE)));
		onView(withId(R.id.statementTextView)).check(matches(withText(TEST_STATEMENT)));
	}

	@Test
	public void checkEditButton() {
		onView(withId(R.id.editInfo)).perform(click());
		intended(hasComponent(UserInformationEditActivity.class.getName()));
	}

}
