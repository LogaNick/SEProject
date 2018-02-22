package ca.dal.cs.athletemonitor.athletemonitor;

import android.support.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * UI Test for Login Activity
 */

public class AccountManagerTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    /**
     * Tests creation of a new user
     *
     * Will pass if new account is added to the users branch of database
     *
     * @throws Exception Generic exception
     */
    @Test
    public void createUserTest() throws Exception {
       final User testUser = createTestUser();

       AccountManager.createUser(testUser, new AccountManager.CreateUserListener() {
           @Override
           public void onCreateUserResult(User user) {
               assertNotNull(user);

               //if the user was created successfully, remove before exiting
               AccountManager.deleteUser(testUser, assertTrueBooleanResult());
           }
       });
    }

    /**
     * Test helper method to generate the standard testing user account
     *
     * @return Pre-determined user object with known information for testing purposes
     */
    private User createTestUser() {
        return new User("test_user", "test_password");
    }

    /**
     * Test for true positive on existing user
     *
     * Will pass in a username that does exist in the user accounts to the validate method and
     * assert that the return value of validate is true
     *
     * @throws Exception Generic exception
     */
    @Test
    public void userExistsTest() throws Exception {
        final User testUser = createTestUser();
        AccountManager.createUser(createTestUser());

        AccountManager.userExists(testUser.getUsername(), new AccountManager.UserExistsListener() {
            @Override
            public void onUserExists(boolean result) {
                assertTrue(result);

                //delete the test user before exiting test
                AccountManager.deleteUser(testUser, null);
            }
        });
    }

    /**
     * Test for false positive on existing user
     *
     * Will pass if a username that does not exist in the user accounts is passed to the userExists
     * method.  Asserts that the return value of is false
     *
     * @throws Exception Generic exception
     */
    @Test
    public void userDoesNotExistTest() throws Exception {
        User testUser = new User("i don't exist", "testPassword");

        AccountManager.userExists(testUser.getUsername(), new AccountManager.UserExistsListener() {
            @Override
            public void onUserExists(boolean result) {
                assertFalse("User should not exist, but user is found.", result);
            }
        });
    }

    /**
     * Test for deleting a user account.
     *
     * First checks if the test user exists in the database.  If it does not exist, it is created
     * and then a deleteUser call is made.  If the delete operation is completed without errors
     * the test is assumed to be passed.
     *
     * @throws Exception Generic exception
     */
    @Test
    public void deleteUserTest() throws Exception {
        //get the test account
        final User testUser = createTestUser();

        AccountManager.userExists(testUser.getUsername(), new AccountManager.UserExistsListener() {
            @Override
            public void onUserExists(boolean result) {
                //if the user doesn't exist, create it first
                if (!result) {
                    AccountManager.createUser(testUser);
                }

                //delete the user
                AccountManager.deleteUser(testUser, assertTrueBooleanResult());
            }
        });
    }

    /**
     * Creates a BooleanResultListener with default behaviour of asserting a true result as true
     *
     * @return Listener with assertTrue behaviour
     */
    private BooleanResultListener assertTrueBooleanResult() {
        return new BooleanResultListener() {
            @Override
            public void onResult(boolean result) {
                assertTrue(result);
            }
        };
    }
}
