package ca.dal.cs.athletemonitor.athletemonitor;

/**
 * Created by Lee on 2018-02-16.
 */

public class UserManager {
    public User login(String username, String password) {
        if (username.equals("nulluser")) return null;
        return new User(username, password);
    }
}
