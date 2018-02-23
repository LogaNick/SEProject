package ca.dal.cs.athletemonitor.athletemonitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Lee on 2018-02-16.
 *
 * Description: The User class allows an object consisting of an ordered pair (username,password)
 * to be instantiated
 */

public class User {
    private String username;
    private String password;

    private List<Exercise> userExercises;

    public User() { /* intentionally left blank */ }

    public User(String username, String password) {
        this.username = username;
        this.password = password;

        // Initialize exercises list
        userExercises = new ArrayList<Exercise>();
    }

    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }

    public List<Exercise> getUserExercises() {
        return userExercises;
    }

    public void addUserExercise(Exercise exercise){
        userExercises.add(exercise);
    }

    public void setPassword(String password) { this.password = password; }
}