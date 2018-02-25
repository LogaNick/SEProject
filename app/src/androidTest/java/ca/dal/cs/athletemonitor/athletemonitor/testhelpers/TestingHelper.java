package ca.dal.cs.athletemonitor.athletemonitor.testhelpers;

import ca.dal.cs.athletemonitor.athletemonitor.User;
import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class encapsulates common methods, listeners, and object creation requirements used in
 * unit and instrumented tests.
 */
public class TestingHelper {
    /**
     * Creates a BooleanResultListener with default behaviour of asserting a true result as true
     *
     * @return Listener with assertTrue behaviour
     */
    public static BooleanResultListener assertTrueBooleanResult() {
        return new BooleanResultListener() {
            @Override
            public void onResult(boolean result) {
                assertTrue("True value expected, seen False...", result);
            }
        };
    }

    /**
     * Creates a BooleanResultListener with default behaviour of asserting a false result
     *
     * @return Listener with assertTrue behaviour
     */
    public static BooleanResultListener assertFalseBooleanResult() {
        return new BooleanResultListener() {
            @Override
            public void onResult(boolean result) {
                assertFalse("False value expected, seen True...", result);
            }
        };
    }

    /**
     * Test helper method to generate the standard testing user account
     *
     * @return Pre-determined user object with known information for testing purposes
     */
    public static User createTestUser() {
        return new User("test_user", "test_password");
    }
}
