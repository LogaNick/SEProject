package ca.dal.cs.athletemonitor.athletemonitor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertTrue;

/**
 * Test the User class's control of the exercises
 */
@RunWith(JUnit4.class)
public class UserExerciseTest {

    @Test
    public void testUserExerciseList(){
        // Test that creating the user class creates an empty exercise list
        User user = new User("user", "pass");
        assertTrue(user.getUserExercises().size() == 0);

        // Test that adding an exercise works
        user.addUserExercise(new Exercise("Run", "Running outside", 5, TimeUnit.MINUTES));
        assertTrue(user.getUserExercises().size() == 1);
        assertTrue(user.getUserExercises().get(0).getName().equals("Run"));
    }
}
