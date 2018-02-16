package ca.dal.cs.athletemonitor.athletemonitor;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserLoginTest {
    @Test
    public void loginTest() throws Exception {
        String username = "testusername";
        String password = "testpassword";

        User user;
        UserManager userManager = new UserManager;

        user = userManager.login(username, password);

        assertNotNull(user); //invalid credentials returns null
        assertTrue(user.getUsername().equals(username)); //authenticated user has same name as user returned
    }
}