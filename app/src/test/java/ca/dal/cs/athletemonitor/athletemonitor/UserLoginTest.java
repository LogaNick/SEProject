package ca.dal.cs.athletemonitor.athletemonitor;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserLoginTest {

    /*
     * Test that the User object returned by userManager's login method returns a User with
     * the appropriate username (matches the argument given)
     */
    @Test
    public void validLoginTest() throws Exception {
        String username = "testusername";
        String password = "testpassword";
        User user;
        UserManager userManager = new UserManager();

        user = userManager.login(username, password);
        assertTrue(user.getUsername().equals(username)); //authenticated user has same name as user returned
    }

    /*
     * Test that the a null User is returned by the userManager's login method when a username
     * and password tuple which does not exist in Firebase are given as arguments
     */
    @Test
    public void invalidLoginTest() throws Exception {
        String username = "nulluser";
        String password = "testpass";
        UserManager userManager = new UserManager();

        User user = userManager.login(username, password);
        assertNull(user);
    }
}