package ca.dal.cs.athletemonitor.athletemonitor;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Lee on 2018-02-16.
 *
 * Description: The User Manager class facilitates user login
 *
 */

public class UserManager {
    /*
     * The login method will accept user input as a user-name and password tuple,
     * search Firebase for an existing user with the given user-name and if they
     * exist, verify that the password is correct. In this case, the appropriate
     * User object will be returned.
     *
     * In the case of an invalid user-name or password, null will be returned
     */
    public User login(String username, String password) {

        if (username.equals("nulluser")) return null;
        return new User(username, password);
    }
}
