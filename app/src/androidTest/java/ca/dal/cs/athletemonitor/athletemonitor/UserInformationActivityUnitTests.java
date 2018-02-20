package ca.dal.cs.athletemonitor.athletemonitor;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class UserInformationActivityUnitTests {

	//TODO Look into using Mockito to mock context and access string resources
	private static final String TEST_FIRST_NAME = "Auston";
	private static final String TEST_LAST_NAME = "Matthews";
	private static final int TEST_AGE = 20;
	private static final int TEST_HEIGHT = 191;
	private static final int TEST_WEIGHT = 98;
	private static final String TEST_ATHLETE_TYPE = "Hockey Player";
	private static final String TEST_STATEMENT = "I want to win the Stanley Cup";

	@Rule
	public ActivityTestRule<UserInformationActivity> uiActivityRule =
			new ActivityTestRule<>(UserInformationActivity.class);

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

}
