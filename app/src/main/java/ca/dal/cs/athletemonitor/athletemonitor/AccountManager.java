package ca.dal.cs.athletemonitor.athletemonitor;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;

/**
 * This class performs management of a user account
 */
public class AccountManager {
    /**
     * Listener interface for checking if a user exists.
     *
     * Callers of userExists must implement this listener interface.
     */
    public interface UserExistsListener {
        void onUserExists(boolean result);
    }

    /**
     * Listener interface for checking if a user exists.
     *
     * Callers of userExists must implement this listener interface.
     */
    public interface CreateUserListener {
        void onCreateUserResult(User user);
    }

    public static void authenticate(final User user, final BooleanResultListener listener) {
        //retrieve a reference to the users node
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("users/" + user.getUsername());

        //attach a listener for data changes of the users reference.  this will occur when
        //the reference is populated
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if the reference exists, convert it to a user instance
                if (dataSnapshot.exists()) {
                    User userLoggingIn = dataSnapshot.getValue(User.class);

                    //compare passwords, if they match add the user to the logged in users list
                    //and return a successful login attempt, otherwise, report a failed login
                    if (userLoggingIn.getPassword().equals(user.getPassword())) {
                        AccountManager.setUserLoginState(user.getUsername(), true);

                        listener.onResult(true);
                    } else {
                        listener.onResult(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Sets whether a user is marked as being online or not.  Users are tagged as online will
     * have a node in the online_users branch of the database
     *
     * @param username Name of user to tag online/offline
     * @param online Online state of user.  True if online, False if offline
     */
    public static void setUserLoginState(String username, boolean online) {
        //retrieve a reference to the online users node
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("online_users/");

        //if the user is to be tagged as online, write an entry in the online users branch
        //otherwise, delete any existing entry
        if (online) {
            usersReference.child(username).setValue(true);
        } else {
            usersReference.child(username).removeValue();
        }
    }

    /**
     * Determines whether or not the specified user exists in the database
     *
     * @param username Name of the user to look up
     *
     */
    public static void userExists(final String username, final UserExistsListener listener) {
        //retrieve a reference to the users node
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("users/");

        //attach a listener for data changes of the users reference.  this will occur when
        //the reference is populated
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if the username is a child of the users node then fire the user exists event
                //indicating true, otherwise, fire event with false outcome
                if (dataSnapshot.hasChild(username)) {
                    if (listener != null) listener.onUserExists(true);
                }
                else {
                    if (listener != null) listener.onUserExists(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Creates a new user account
     *
     * @param newUser User details
     */
    public static void createUser(final User newUser) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("users");
        usersReference.child(newUser.getUsername()).setValue(newUser, null);
    }

    /**
     * Creates a new user account and called the specific listener when complete.
     *
     * Note: This method causes a destructive write.  Callers should ensure that the user does
     * not exist prior to calling this method.
     *
     * @param newUser User details
     * @param createUserListener Callback for completion
     */
    public static void createUser(final User newUser, final CreateUserListener createUserListener) {
        //get a reference to the users node
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("users");

        //attempt to write the data
        usersReference.child(newUser.getUsername()).setValue(newUser, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                //if we have a listener, report the result
                if (createUserListener != null) {
                    //if no database errors occurred, assume successful operation
                    if (databaseError == null) {
                        createUserListener.onCreateUserResult(newUser);
                    } else {
                        createUserListener.onCreateUserResult(null);
                    }
                }
            }
        });
    }

    /**
     * Delete a user account from the database
     *
     * @param user User to be removed
     *
     * @param booleanResultListener Callback to be called on completion
     */
    public static void deleteUser(User user, final BooleanResultListener booleanResultListener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("users/" + user.getUsername());

        usersReference.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (booleanResultListener != null) {
                    if (databaseError == null) {
                        booleanResultListener.onResult(true);
                    } else {
                        booleanResultListener.onResult(false);
                    }
                }
            }
        });
    }
}
