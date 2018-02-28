package ca.dal.cs.athletemonitor.athletemonitor.utilities;

import org.junit.Before;
import org.junit.Test;

import ca.dal.cs.athletemonitor.athletemonitor.utilities.StringJoiner;

import static org.junit.Assert.*;

/**
 * These tests ensure {@link StringJoiner} functions as desired.
 */
public class StringJoinerUnitTests {

	private StringJoiner sj;

	private static final String DELIMITER = "-";
	private static final String TOKEN1 = "token1";
	private static final String TOKEN2 = "token2";
	private static final String TOKEN3 = "token3";

	@Before
	public void initStringJoiner() {
		sj = new StringJoiner(DELIMITER);
	}

	@Test
	public void testNoTokens() {
		assertEquals(sj.toString(), "");
	}

	@Test
	public void testOneToken() {
		sj.add(TOKEN1);
		assertEquals(sj.toString(), TOKEN1);
	}

	@Test
	public void testTwoTokens() {
		sj.add(TOKEN1);
		sj.add(TOKEN2);
		assertEquals(sj.toString(), TOKEN1 + DELIMITER + TOKEN2);
	}

	@Test
	public void testThreeTokens() {
		sj.add(TOKEN1);
		sj.add(TOKEN2);
		sj.add(TOKEN3);
		assertEquals(sj.toString(), TOKEN1 + DELIMITER + TOKEN2 + DELIMITER + TOKEN3);
	}

}
