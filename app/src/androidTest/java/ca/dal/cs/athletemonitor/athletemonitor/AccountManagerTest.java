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
import ca.dal.cs.athletemonitor.athletemonitor.testhelpers.TestingHelper;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * UI Test for Login Activity
 */

public class AccountManagerTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    /**
     * Tests the successful generation of a user object instance from user account information
     * in the database.
     *
     * Will pass if user object is populated with the user information from the database
     *
     * @throws Exception
     */
    @Test
    public void getUserSuccessTest() throws Exception {
        //construct a test user and add them to the accounts list for testing
        final User testUser = TestingHelper.createTestUser();
        AccountManager.createUser(TestingHelper.createTestUser());

        AccountManager.getUser(testUser.getUsername(), new AccountManager.UserObjectListener() {
            @Override
            public void onUserPopulated(User user) {
                assertNotNull("Expecting username " + testUser.getUsername() + ", but seen null",user);
                assertEquals("Expecting username " + testUser.getUsername() + ", but seen " + user.getUsername(),
                        user.getUsername(), testUser.getUsername());

                //delete test user from database
                AccountManager.deleteUser(testUser, TestingHelper.assertTrueBooleanResult());
            }
        });
    }

    /**
     * Tests that not user information is generated for users that do not exist in the database.
     *
     * Will pass if user object is null on reading account that doesn't exist
     *
     * @throws Exception
     */
    @Test
    public void getUserDoesNotExistTest() throws Exception {
        //construct a test user and ensure it is removed from the database
        final User testUser = TestingHelper.createTestUser();
        AccountManager.deleteUser(testUser, TestingHelper.assertTrueBooleanResult());

        //attempt to retrieve user information
        AccountManager.getUser(testUser.getUsername(), new AccountManager.UserObjectListener() {
            @Override
            public void onUserPopulated(User user) {
                assertNull("Expecting null user reference, but found user account " + testUser.getUsername(),user);
            }
        });
    }


    /**
     * Tests user authentication
     *
     * Will pass test if username exists in the database and supplied password matches
     * password as stored in database
     *
     * @throws Exception Generic exception
     */
    @Test
    public void signInSuccessTest() throws Exception {
        //construct a test user and add them to the accounts list
        User testUser = TestingHelper.createTestUser();
        AccountManager.createUser(TestingHelper.createTestUser());

        //attempt to sign in
        AccountManager.authenticate(testUser, TestingHelper.assertTrueBooleanResult());

        //delete test user from database
        AccountManager.deleteUser(testUser, TestingHelper.assertTrueBooleanResult());
    }

    /**
     * Tests user authentication
     *
     * Will pass test if username exists in the database and supplied password matches
     * password as stored in database
     *
     * @throws Exception Generic exception
     */
    @Test
    public void signInFailureTest() throws Exception {
        //construct a test user and add them to the accounts list
        User testUser = TestingHelper.createTestUser();
        AccountManager.createUser(TestingHelper.createTestUser());

        //change test user's local password
        testUser.setPassword("newpassword");

        //attempt to sign in using invalid credentials
        AccountManager.authenticate(testUser, TestingHelper.assertFalseBooleanResult());

        //delete test user from database
        AccountManager.deleteUser(testUser, TestingHelper.assertTrueBooleanResult());
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
       User testUser = TestingHelper.createTestUser();

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
                assertTrue("Expecting non-empty result from database, but no data returned...", dataSnapshot.exists());
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
        User testUser = TestingHelper.createTestUser();

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
                assertFalse("Expecting non-empty result from database, but no data returned...", dataSnapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Tests creation of a new user
     *
     * Will pass if new account is added to the users branch of database
     *
     * @throws Exception Generic exception
     */
    @Test
    public void createUserTest() throws Exception {
       final User testUser = TestingHelper.createTestUser();

       AccountManager.createUser(testUser, new AccountManager.UserObjectListener() {
           @Override
           public void onUserPopulated(User user) {
               assertNotNull("Expecting valid user object, but seen null...", user);

               //if the user was created successfully, remove before exiting
               AccountManager.deleteUser(testUser, TestingHelper.assertTrueBooleanResult());
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
        final User testUser = TestingHelper.createTestUser();
        AccountManager.createUser(TestingHelper.createTestUser());

        AccountManager.userExists(testUser.getUsername(), new AccountManager.UserExistsListener() {
            @Override
            public void onUserExists(boolean result) {
                assertTrue("Expecting account " + testUser.getUsername() +
                        " to be found, but it was not...", result);

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
        User testUser = new User("doesnotexist", "testPassword");

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
        final User testUser = TestingHelper.createTestUser();

        AccountManager.userExists(testUser.getUsername(), new AccountManager.UserExistsListener() {
            @Override
            public void onUserExists(boolean result) {
                //if the user doesn't exist, create it first
                if (!result) {
                    AccountManager.createUser(testUser);
                }

                //delete the user
                AccountManager.deleteUser(testUser, TestingHelper.assertTrueBooleanResult());
            }
        });
    }
}
