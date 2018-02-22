package ca.dal.cs.athletemonitor.athletemonitor;

/**
 * Created by Lee on 2018-02-16.
 *
 * Description: The User class allows an object consisting of an ordered pair (username,password)
 * to be instantiated
 */

public class User {
    private String username;
    private String password;

    public User() { /* intentionally left blank */ }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }

    public void setPassword(String password) { this.password = password; }
}
