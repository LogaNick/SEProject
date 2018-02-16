package ca.dal.cs.athletemonitor.athletemonitor;

/**
 * Created by Lee on 2018-02-16.
 */

public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public String getUsername() { return this.username; }

    public void setPassword(String password) { this.password = password; }
}
