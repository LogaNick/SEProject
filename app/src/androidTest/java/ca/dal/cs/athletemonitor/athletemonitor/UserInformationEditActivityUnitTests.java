package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.dal.cs.athletemonitor.athletemonitor.UserInformationActivity.USER_ID;
import static ca.dal.cs.athletemonitor.athletemonitor.UserInformationActivity.USER_INFORMATION;
import static ca.dal.cs.athletemonitor.athletemonitor.UserInformationActivityUnitTests.TEST_ID;

@RunWith(AndroidJUnit4.class)
public class UserInformationEditActivityUnitTests {

	//TODO Look into using Mockito to mock context and access string resources
	private static final String TEST_FIRST_NAME = "Auston";
	private static final String TEST_LAST_NAME = "Matthews";
	private static final int TEST_AGE = 20;
	private static final int TEST_HEIGHT = 191;
	private static final int TEST_WEIGHT = 98;
	private static final String TEST_ATHLETE_TYPE = "Hockey Player";
	private static final String TEST_STATEMENT = "I want to win the Stanley Cup";
	private static final UserInformation TEST_INFO
			= new UserInformation.UserInformationBuilder(TEST_FIRST_NAME, TEST_LAST_NAME)
				.age(TEST_AGE)
				.height(TEST_HEIGHT)
				.weight(TEST_WEIGHT)
				.athleteType(TEST_ATHLETE_TYPE)
				.personalStatement(TEST_STATEMENT)
				.build();

	@Rule
	public ActivityTestRule<UserInformationEditActivity> uiEditActivityRule =
			new ActivityTestRule<>(UserInformationEditActivity.class, false, false);

	@Before
	public void prepIntentAndLaunch() {
		Intent intent = new Intent();
		intent.putExtra(USER_ID, TEST_ID);
		intent.putExtra(USER_INFORMATION, TEST_INFO);
		uiEditActivityRule.launchActivity(intent);
	}

	@Test
	public void displayEditInfo() {
		onView(withId(R.id.nameEditText))
				.check(matches(withText(TEST_FIRST_NAME + " " + TEST_LAST_NAME)));
		onView(withId(R.id.ageEditText)).check(matches(withText(Integer.toString(TEST_AGE))));
		onView(withId(R.id.heightEditText)).check(matches(withText(TEST_HEIGHT + " cm")));
		onView(withId(R.id.weightEditText)).check(matches(withText(TEST_WEIGHT + " kg")));
		onView(withId(R.id.athleteTypeEditText)).check(matches(withText(TEST_ATHLETE_TYPE)));
		onView(withId(R.id.statementEditText)).check(matches(withText(TEST_STATEMENT)));
	}

}
