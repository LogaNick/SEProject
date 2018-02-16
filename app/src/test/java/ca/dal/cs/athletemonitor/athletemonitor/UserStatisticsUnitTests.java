package ca.dal.cs.athletemonitor.athletemonitor;

import org.junit.Test;
import ca.dal.cs.athletemonitor.athletemonitor.UserStatistics.UserStatisticsBuilder;

import static org.junit.Assert.*;

/**
 * These tests check that the {@link ca.dal.cs.athletemonitor.athletemonitor.UserStatistics} class
 * can be properly instantiated using the
 * {@link ca.dal.cs.athletemonitor.athletemonitor.UserStatistics.UserStatisticsBuilder}
 * builder class.
 */
public class UserStatisticsUnitTests {

	private static String TEST_FIRST_NAME = "Auston";
	private static String TEST_LAST_NAME = "Matthews";
	private static int TEST_AGE = 20;
	private static int TEST_HEIGHT = 191;
	private static int TEST_WEIGHT = 98;
	private static String TEST_ATHLETE_TYPE = "Hockey Player";
	private static String TEST_STATEMENT = "I want to win the Stanley Cup";
	private static String TEST_EMPTY_STRING = "";

	@Test(expected = RuntimeException.class)
	public void createStatsWithNullNames() {
		UserStatistics stats = new UserStatisticsBuilder(null, null).build();
	}

	@Test
	public void createStatsWithNullStatementType() {
		UserStatistics stats = new UserStatisticsBuilder(TEST_FIRST_NAME, TEST_LAST_NAME)
				.personalStatement(null)
				.athleteType(null)
				.build();

		assertEquals(TEST_EMPTY_STRING, stats.personalStatement);
		assertEquals(TEST_EMPTY_STRING, stats.athleteType);
	}

	@Test
	public void createStatsWithEmptyStrings() {
		UserStatistics stats = new UserStatisticsBuilder(TEST_FIRST_NAME, TEST_LAST_NAME).build();

		assertEquals(TEST_EMPTY_STRING, stats.athleteType);
		assertEquals(TEST_EMPTY_STRING, stats.personalStatement);
	}

	@Test
	public void createStatsWithZeroedAttributes() {
		UserStatistics stats = new UserStatisticsBuilder(TEST_FIRST_NAME, TEST_LAST_NAME).build();

		assertEquals(0, stats.age);
		assertEquals(0, stats.weight);
		assertEquals(0, stats.height);
	}

	@Test
	public void createStatsWithAttributes() {
		UserStatistics stats =
				new UserStatisticsBuilder(TEST_FIRST_NAME, TEST_LAST_NAME)
						.age(TEST_AGE)
						.height(TEST_HEIGHT)
						.weight(TEST_WEIGHT)
						.athleteType(TEST_ATHLETE_TYPE)
						.personalStatement(TEST_STATEMENT)
						.build();

		assertEquals(TEST_FIRST_NAME, stats.firstName);
		assertEquals(TEST_LAST_NAME, stats.lastName);
		assertEquals(TEST_AGE, stats.age);
		assertEquals(TEST_HEIGHT, stats.height);
		assertEquals(TEST_WEIGHT, stats.weight);
		assertEquals(TEST_ATHLETE_TYPE, stats.athleteType);
		assertEquals(TEST_STATEMENT, stats.personalStatement);
	}

}
