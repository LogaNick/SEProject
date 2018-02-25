package ca.dal.cs.athletemonitor.athletemonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the account information of a user, including all data associated with
 * functionality of the app.
 */
public class User {
    private String username; //login username and unique identifier
    private String password; //login password

    private List<Exercise> userExercises;

    /**
     * No args constructor used primarily for database based initialization of user objects
     */
    public User() { /* intentionally left blank */ }

    /**
     * Constructs a user instance with the given username and password
     *
     * @param username Username of the user
     * @param password Password of the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;

        // Initialize exercises list
        userExercises = new ArrayList<Exercise>();
    }

    public List<Exercise> getUserExercises() {
        return userExercises;
    }

    public void addUserExercise(Exercise exercise){
        userExercises.add(exercise);
    }

    /**
     * Retrieves the username of the user
     *
     * @return User's current username
     */
    public String getUsername() { return this.username; }

    /**
     * Retrieves the password of the user
     *
     * @return User's current password
     */
    public String getPassword() { return this.password; }

    /**
     * Sets the password of the user
     *
     * @param password Desired password
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Sets the user's username
     *
     * @param username Desired username
     */
    public void setUsername(String username) { this.username = username; }
}
