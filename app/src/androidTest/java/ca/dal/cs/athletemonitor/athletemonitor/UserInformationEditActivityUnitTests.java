package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.app.Activity.RESULT_OK;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.dal.cs.athletemonitor.athletemonitor.UserInformationActivity.USER_ID;
import static ca.dal.cs.athletemonitor.athletemonitor.UserInformationActivity.USER_INFORMATION;
import static ca.dal.cs.athletemonitor.athletemonitor.UserInformationActivityUnitTests.TEST_ID;
import static org.junit.Assert.*;

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
	public IntentsTestRule<UserInformationEditActivity> uiEditIntentRule =
			new IntentsTestRule<>(UserInformationEditActivity.class, false, false);

	@Before
	public void prepIntentAndLaunch() {
		Intent intent = new Intent();
		intent.putExtra(USER_ID, TEST_ID);
		intent.putExtra(USER_INFORMATION, TEST_INFO);
        uiEditIntentRule.launchActivity(intent);
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

	@Test
    public void checkSaveButton() {
        onView(withId(R.id.saveInfo)).perform(click());
        Intent intent = uiEditIntentRule.getActivityResult().getResultData();
        assertTrue(intent.hasExtra(USER_INFORMATION));
        assertEquals(uiEditIntentRule.getActivityResult().getResultCode(), RESULT_OK);
        assertTrue(uiEditIntentRule.getActivity().isFinishing());
    }

    @Test
    public void editName() {
        onView(withId(R.id.nameEditText))
                .perform(
                        clearText(),
                        typeText(TEST_FIRST_NAME + " " + TEST_LAST_NAME),
                        closeSoftKeyboard()
                );

        onView(withId(R.id.saveInfo)).perform(click());
        Intent intent = uiEditIntentRule.getActivityResult().getResultData();
        UserInformation info = (UserInformation) intent.getExtras().get(USER_INFORMATION);
        assertEquals(info.getFirstName(), TEST_FIRST_NAME);
        assertEquals(info.getLastName(), TEST_LAST_NAME);
    }

    @Test
    public void editAge() {
        onView(withId(R.id.ageEditText))
                .perform(
                        clearText(),
                        typeText(Integer.toString(TEST_AGE)),
                        closeSoftKeyboard()
                );

        onView(withId(R.id.saveInfo)).perform(click());
        Intent intent = uiEditIntentRule.getActivityResult().getResultData();
        UserInformation info = (UserInformation) intent.getExtras().get(USER_INFORMATION);
        assertEquals(info.getAge(), TEST_AGE);
    }

    @Test
    public void editHeight() {
        onView(withId(R.id.heightEditText))
                .perform(
                        clearText(),
                        typeText(Integer.toString(TEST_HEIGHT)),
                        closeSoftKeyboard()
                );

        onView(withId(R.id.saveInfo)).perform(click());
        Intent intent = uiEditIntentRule.getActivityResult().getResultData();
        UserInformation info = (UserInformation) intent.getExtras().get(USER_INFORMATION);
        assertEquals(info.getHeight(), TEST_HEIGHT);
    }

    @Test
    public void editWeight() {
        onView(withId(R.id.weightEditText))
                .perform(
                        clearText(),
                        typeText(Integer.toString(TEST_WEIGHT)),
                        closeSoftKeyboard()
                );

        onView(withId(R.id.saveInfo)).perform(click());
        Intent intent = uiEditIntentRule.getActivityResult().getResultData();
        UserInformation info = (UserInformation) intent.getExtras().get(USER_INFORMATION);
        assertEquals(info.getWeight(), TEST_WEIGHT);
    }

    @Test
    public void editAthleteType() {
        onView(withId(R.id.athleteTypeEditText))
                .perform(
                        clearText(),
                        typeText(TEST_ATHLETE_TYPE),
                        closeSoftKeyboard()
                );

        onView(withId(R.id.saveInfo)).perform(click());
        Intent intent = uiEditIntentRule.getActivityResult().getResultData();
        UserInformation info = (UserInformation) intent.getExtras().get(USER_INFORMATION);
        assertEquals(info.getAthleteType(), TEST_ATHLETE_TYPE);
    }

    @Test
    public void editPersonalStatement() {
        onView(withId(R.id.statementEditText))
                .perform(
                        clearText(),
                        typeText(TEST_STATEMENT),
                        closeSoftKeyboard()
                );

        onView(withId(R.id.saveInfo)).perform(click());
        Intent intent = uiEditIntentRule.getActivityResult().getResultData();
        UserInformation info = (UserInformation) intent.getExtras().get(USER_INFORMATION);
        assertEquals(info.getPersonalStatement(), TEST_STATEMENT);
    }

}
