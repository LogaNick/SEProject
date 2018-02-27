package ca.dal.cs.athletemonitor.athletemonitor;

import android.os.Parcel;

import org.junit.Test;
import ca.dal.cs.athletemonitor.athletemonitor.UserInformation.UserInformationBuilder;

import static org.junit.Assert.*;

/**
 * These tests check that the {@link UserInformation} class
 * can be properly instantiated using the
 * {@link UserInformationBuilder}
 * builder class.
 */
public class UserInformationUnitTests {

	private static final String TEST_FIRST_NAME = "Auston";
	private static final String TEST_LAST_NAME = "Matthews";
	private static final int TEST_AGE = 20;
	private static final int TEST_HEIGHT = 191;
	private static final int TEST_WEIGHT = 98;
	private static final String TEST_ATHLETE_TYPE = "Hockey Player";
	private static final String TEST_STATEMENT = "I want to win the Stanley Cup";
	private static final String TEST_EMPTY_STRING = "";

	@Test(expected = RuntimeException.class)
	public void createInfoWithNullNames() {
		UserInformation info = new UserInformation.UserInformationBuilder(null, null).build();
	}

	@Test
	public void createInfoWithNullStatementType() {
		UserInformation info = new UserInformationBuilder(TEST_FIRST_NAME, TEST_LAST_NAME)
				.personalStatement(null)
				.athleteType(null)
				.build();

		assertEquals(TEST_EMPTY_STRING, info.personalStatement);
		assertEquals(TEST_EMPTY_STRING, info.athleteType);
	}

	@Test
	public void createInfoWithEmptyStrings() {
		UserInformation info = new UserInformationBuilder(TEST_FIRST_NAME, TEST_LAST_NAME).build();

		assertEquals(TEST_EMPTY_STRING, info.athleteType);
		assertEquals(TEST_EMPTY_STRING, info.personalStatement);
	}

	@Test
	public void createInfoWithZeroedAttributes() {
		UserInformation info = new UserInformation.UserInformationBuilder(TEST_FIRST_NAME, TEST_LAST_NAME).build();

		assertEquals(0, info.age);
		assertEquals(0, info.weight);
		assertEquals(0, info.height);
	}

	@Test
	public void createInfoWithAttributes() {
		UserInformation info =
				new UserInformationBuilder(TEST_FIRST_NAME, TEST_LAST_NAME)
						.age(TEST_AGE)
						.height(TEST_HEIGHT)
						.weight(TEST_WEIGHT)
						.athleteType(TEST_ATHLETE_TYPE)
						.personalStatement(TEST_STATEMENT)
						.build();

		assertEquals(TEST_FIRST_NAME, info.firstName);
		assertEquals(TEST_LAST_NAME, info.lastName);
		assertEquals(TEST_AGE, info.age);
		assertEquals(TEST_HEIGHT, info.height);
		assertEquals(TEST_WEIGHT, info.weight);
		assertEquals(TEST_ATHLETE_TYPE, info.athleteType);
		assertEquals(TEST_STATEMENT, info.personalStatement);
	}

	@Test
	public void checkGetters() {
        UserInformation info =
                new UserInformationBuilder(TEST_FIRST_NAME, TEST_LAST_NAME)
                        .age(TEST_AGE)
                        .height(TEST_HEIGHT)
                        .weight(TEST_WEIGHT)
                        .athleteType(TEST_ATHLETE_TYPE)
                        .personalStatement(TEST_STATEMENT)
                        .build();

        assertEquals(TEST_FIRST_NAME, info.getFirstName());
        assertEquals(TEST_LAST_NAME, info.getLastName());
        assertEquals(TEST_AGE, info.getAge());
        assertEquals(TEST_HEIGHT, info.getHeight());
        assertEquals(TEST_WEIGHT, info.getWeight());
        assertEquals(TEST_ATHLETE_TYPE, info.getAthleteType());
        assertEquals(TEST_STATEMENT, info.getPersonalStatement());
    }
}
