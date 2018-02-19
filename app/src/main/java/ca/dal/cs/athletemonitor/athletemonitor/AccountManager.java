package ca.dal.cs.athletemonitor.athletemonitor;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This class performs authentication
 */

public class AccountManager {
    public static boolean onUserExists(boolean result) { return result; }



    /**
     * Determines whether or not the specified user exists in the database
     *
     * @param username Name of the user to look up
     *
     * @return True if user exists, otherwise false
     */
    public static boolean userExists(String username) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("users/" + username);

        final User user = new User("", "");

        //usersReference.getKey().equals(username);

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("username").getValue().equals("testAccount")) {

                    FirebaseDatabase.getInstance().getReference("users/").child("blahblah").setValue("bbbbbbb");

                }
                //user.setPassword( dataSnapshot.getValue(User.class).getPassword());
                //AccountManager.onUserExists(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return true;
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
}
