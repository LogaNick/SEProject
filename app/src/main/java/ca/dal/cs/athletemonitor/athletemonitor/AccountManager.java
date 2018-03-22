package ca.dal.cs.athletemonitor.athletemonitor;

import android.accounts.Account;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;

/**
 * This class performs management of a user account
 */
public class AccountManager {

    /**
     * lastAuth holds the username of the last authenticated user
     */
    private static String lastAuth = "";

    /**
     * online holds the online status of the logged in user, if there is one.
     */
    private static boolean online = false;

    /**
     * User object for an offline user
     */
    private static User user;

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
    public interface UserObjectListener {
        void onUserPopulated(User user);
    }

    /**
     * Retrieves the user account associated with the specified username.  User will be available
     * to the provided callback.
     *
     * @param username Username of the user to be loaded
     * @param listener Callback to handle response
     */
    public static void getUser(String username, @NonNull final UserObjectListener listener) {
        Objects.requireNonNull(listener, "Null value for UserObjectListener is not valid.");
        Objects.requireNonNull(user, "Get user called in offline mode before logging in");

        if(!online) {
            if(username.equals(user.getUsername())){
                listener.onUserPopulated(user);
            }else{
                listener.onUserPopulated(null);
            }
            return;
        }

        //retrieve a reference to the users node
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("users/" + username);

        //attach a listener for data changes of the users reference.  this will occur when
        //the reference is populated
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if the reference exists, convert it to a user instance and pass to listener
                //otherwise return null
                if (dataSnapshot.exists()) {
                    listener.onUserPopulated(dataSnapshot.getValue(User.class));
                } else {
                    listener.onUserPopulated(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Determines whether the specified users username and password match those stored in the
     * database.
     *
     * @param user User to be authenticated
     * @param listener Callback on authentication result
     */
    public static void authenticate(final User user, final BooleanResultListener listener) {
        //retrieve a reference to the users node
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("users/" + user.getUsername());

        //attach a listener for data changes of the users reference.  this will occur when
        //the reference is populated
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean authResult = false; //always assume authentication fails until proven otherwise

                //if the reference exists, convert it to a user instance
                if (dataSnapshot.exists()) {
                    User userLoggingIn = dataSnapshot.getValue(User.class);

                    //compare passwords, if they match add the user to the logged in users list
                    //and return a successful login attempt, otherwise, report a failed login
                    if (userLoggingIn.getPassword().equals(user.getPassword())) {
                        AccountManager.setUserLoginState(user.getUsername(), true);
                        lastAuth = user.getUsername();
                        online = true;
                        AccountManager.user = userLoggingIn;
                        authResult = true;
                    }
                }

                //call the listener if there is one with the result of authentication
                if (listener != null) listener.onResult(authResult);
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
     */
    public static void userExists(final String username, @NonNull final UserExistsListener listener) {
        Objects.requireNonNull(listener, "Null value for UserExistsListener is not valid.");

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
                listener.onUserExists(dataSnapshot.hasChild(username));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Update a user account
     *
     * @param updatedUser User details
     */
    public static void updateUser(final User updatedUser){
        AccountManager.user = updatedUser;

        if(online) {
            // Ensure that the user is the currently authenticated user
            if (lastAuth.equals(updatedUser.getUsername())) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference usersReference = database.getReference("users/" + updatedUser.getUsername());
                usersReference.setValue(updatedUser, null);
            }
        }
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
     * @param userObjectListener Callback for completion
     */
    public static void createUser(final User newUser, final UserObjectListener userObjectListener) {
        Log.d("AC.createUser", "AccountManager.createUser entered...");

        AccountManager.userExists(newUser.getUsername(), new UserExistsListener() {
            @Override
            public void onUserExists(boolean result) {
                Log.d("AC.createUser", "userExists callback entered result: " + result);

                // if the user exists, do not create account and return a null user object
                // otherwise, create the account and return a populated user object
                if (result) {
                    if (userObjectListener != null) userObjectListener.onUserPopulated(null);
                } else {
                    //get a reference to the users node
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference usersReference = database.getReference("users");

                    //attempt to write the data
                    usersReference.child(newUser.getUsername()).setValue(newUser, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            //early exit condition, no one is listening for the result
                            if (userObjectListener == null) return;

                            //if no database errors occurred, assume successful operation
                            if (databaseError == null) {
                                Log.d("AC.createUser", "creating user...created: username: " + newUser.getUsername());
                                userObjectListener.onUserPopulated(newUser);
                            } else {
                                Log.d("AC.createUser", "creating user...database error");
                                userObjectListener.onUserPopulated(null);
                            }
                        }
                    });

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
                //early exit condition, no one is listening for the result
                if (booleanResultListener == null) return;

                //if there are no reported errors, delete succeeded
                if (databaseError == null) {
                    booleanResultListener.onResult(true);
                } else {
                    booleanResultListener.onResult(false);
                }
            }
        });
    }

    /**
     * Check if a user is logged in
     *
     * @param user User to be removed
     *
     * @param booleanResultListener Callback to be called on completion
     */
    public static void isLoggedIn(final User user, final BooleanResultListener booleanResultListener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("online_users/");

        //attach a listener for data changes of the users reference.  this will occur when
        //the reference is populated
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if the username is a child of the users node then fire the user exists event
                //indicating true, otherwise, fire event with false outcome
                booleanResultListener.onResult(dataSnapshot.hasChild(user.getUsername()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Get the online status of the user
     * @return online
     */
    public static boolean isOnline(){
        return online;
    }

    /**
     * Set the online status of the user
     * @param online
     */
    public static void setOnline(boolean online){
        AccountManager.online = online;
        AccountManager.updateUser(user);
    }

    /**
     * Transfers ownership of a team to a new user.
     *
     * If user exists and database operation succeeds then the method returns true to the callback.
     * Otherwise, false is returned.  NOTE:  Tranferring ownership will fail if user is marked as
     * being in offline mode.
     *
     * @param team Team to transfer ownership
     * @param newOwner Username of the new team owner
     * @param listener Callback for success/failure of transfer
     */
    public static void transferOwnership(final Team team, final String newOwner, final BooleanResultListener listener) {
        final String oldOwner = team.getOwner();

        AccountManager.getUser(newOwner, new UserObjectListener() {
            @Override
            public void onUserPopulated(User user) {
                if (user == null) {
                    listener.onResult(false);
                } else {
                    if (user.getUserTeams().indexOf(team) != -1) {
                       user.getUserTeams().get(user.getUserTeams().indexOf(team)).setOwner(user.getUsername());

                       //TODO: UPDATE TEAM OWNER FOR ALL OTHER MEMBERS OF THE TEAM WHEN USER INVITES IS IMPLEMENTED
                    } else { // only users on the team can get ownership
                        team.setOwner(user.getUsername());
                        user.addUserTeam(team);

                    }

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference usersReference = database.getReference("users/" + user.getUsername());
                    usersReference.setValue(user, null);

                    AccountManager.getUser(oldOwner, new UserObjectListener() {
                        @Override
                        public void onUserPopulated(User user) {
                            Team tempTeam = team;
                            tempTeam.setOwner(oldOwner);
                            if (user.getUserTeams().indexOf(team) != -1) {

                                user.getUserTeams().get(user.getUserTeams().indexOf(tempTeam)).setOwner(newOwner);

                                //TODO: UPDATE TEAM OWNER FOR ALL OTHER MEMBERS OF THE TEAM WHEN USER INVITES IS IMPLEMENTED
                            } else { // only users on the team can get ownership
                                team.setOwner(newOwner);
                                user.addUserTeam(team);

                            }

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference usersReference = database.getReference("users/" + user.getUsername());
                            usersReference.setValue(user, null);
                        }
                    });

                    listener.onResult(true);
                }
            }
        });
    }
}
