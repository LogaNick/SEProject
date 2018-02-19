package ca.dal.cs.athletemonitor.athletemonitor;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for AccountManager class
 */

public class AccountManagerTest {
     /**
     * Test for true positive on existing user
     *
     * Will pass in a username that does exist in the user accounts to the validate method and
     * assert that the return value of validate is true
     *
     * @throws Exception
     */
    @Test
    public void userExistsTest() throws Exception {
        User testUser = new User("testAccount3", "testPassword");
        AccountManager.createUser(testUser);
        assertTrue(AccountManager.userExists(testUser.getUsername()));

        // TODO: Add code to delete test user
    }

    /**
     * Test for false positive on existing user
     *
     * Will pass in a username that does not exist in the user accounts to the validate method
     * and assert that the return value of validate is false
     *
     * @throws Exception
     */
    @Test
    public void userDoesNotExistTest() throws Exception {

    }

}
