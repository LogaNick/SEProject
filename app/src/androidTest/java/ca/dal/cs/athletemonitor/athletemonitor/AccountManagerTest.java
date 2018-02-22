package ca.dal.cs.athletemonitor.athletemonitor;

import android.support.test.rule.ActivityTestRule;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
     * Tests user authentication
     *
     * Will pass test if username exists in the database and supplied password matches
     * password as stored in database
     */
    @Test
    public void signInSuccessTest() {
        //construct a test user and add them to the accounts list
        User testUser = createTestUser();
        AccountManager.createUser(createTestUser());

        //attempt to sign in
        AccountManager.authenticate(testUser, assertTrueBooleanResult());

        //delete test user from database
        AccountManager.deleteUser(testUser, assertTrueBooleanResult());
    }

    /**
     * Tests user authentication
     *
     * Will pass test if username exists in the database and supplied password matches
     * password as stored in database
     */
    @Test
    public void signInFailureTest() {
        //construct a test user and add them to the accounts list
        User testUser = createTestUser();
        AccountManager.createUser(createTestUser());

        //change test user's local password
        testUser.setPassword("newpassword");

        //attempt to sign in using invalid credentials
        AccountManager.authenticate(testUser, assertFalseBooleanResult());

        //delete test user from database
        AccountManager.deleteUser(testUser, assertTrueBooleanResult());
    }

    /**
     * Tests that a user that has been authenticated is tagged as logged in
     *
     * Will pass if there exists a child node in the online_users reference equal to the users
     * username.
     *
     * @throws Exception
     */
    @Test
    public void setUserLoggedInTrueTest() throws Exception {
       //create a test user and set them as online
       User testUser = createTestUser();

       AccountManager.setUserLoginState(testUser.getUsername(), true);

        //attempt to retrieve a reference to the logged in user
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("online_users/" + testUser.getUsername());

        //attach a listener for data changes of the users reference.  this will occur when
        //the reference is populated
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if the reference exists, then the user is tagged as logged in
                if (dataSnapshot.exists()) {
                    assertTrue(true);
                } else {
                    assertTrue(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Tests that a user that has been authenticated is tagged as logged in
     *
     * Will pass if there exists a child node in the online_users reference equal to the users
     * username.
     *
     * @throws Exception
     */
    @Test
    public void setUserLoggedInFalseTest() throws Exception {
        //create a test user and set them as online
        User testUser = createTestUser();

        //set the user as online then set them as offline
        AccountManager.setUserLoginState(testUser.getUsername(), false);

        //attempt to retrieve a reference to the logged in user
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("online_users/" + testUser.getUsername());

        //attach a listener for data changes of the users reference.  this will occur when
        //the reference is populated
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if the reference exists, then the user is tagged as logged in and we should fail
                //the test, otherwise it should succeed
                if (dataSnapshot.exists()) {
                    assertFalse(true);
                } else {
                    assertFalse(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



//    @Test
//    public void signOutTest() {
//        //construct a test user, add them to the accounts list and sign the user in
//        User testUser = createTestUser();
//        AccountManager.createUser(createTestUser());
//        AccountManager.authenticate(testUser, assertTrueBooleanResult());
//
//        //attempt to sign out
//        AccountManager.signOut(testUser, assertTrueBooleanResult());
//
//        //delete test user from database
//        AccountManager.deleteUser(testUser, assertTrueBooleanResult());
//    }

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

       AccountManager.createUser(testUser, new AccountManager.UserObjectListener() {
           @Override
           public void onUserPopulated(User user) {
               assertNotNull(user);

               //if the user was created successfully, remove before exiting
               AccountManager.deleteUser(testUser, assertTrueBooleanResult());
           }
       });
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

    /**
     * Creates a BooleanResultListener with default behaviour of asserting a false result
     *
     * @return Listener with assertTrue behaviour
     */
    private BooleanResultListener assertFalseBooleanResult() {
        return new BooleanResultListener() {
            @Override
            public void onResult(boolean result) {
                assertFalse(result);
            }
        };
    }

    /**
     * Test helper method to generate the standard testing user account
     *
     * @return Pre-determined user object with known information for testing purposes
     */
    private User createTestUser() {
        return new User("test_user", "test_password");
    }
}
