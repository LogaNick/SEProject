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
        public void onUserExists(boolean result);
    }



    /**
     * Determines whether or not the specified user exists in the database
     *
     * @param username Name of the user to look up
     *
     * @return True if user exists, otherwise false
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
    public static void createUser(User newUser) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("users");
        usersReference.child(newUser.getUsername()).setValue(newUser);
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
