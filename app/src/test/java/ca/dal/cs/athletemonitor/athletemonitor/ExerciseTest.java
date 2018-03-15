package ca.dal.cs.athletemonitor.athletemonitor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the Exercise class
 */
@RunWith(JUnit4.class)
public class ExerciseTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testValidExercise(){
        String name = "Run 5K";
        String description = "Running 5 kilometers";
        int time = 20;

        // Assert the fields are valid
        assertTrue(Exercise.validateAll(name,description,time));
        //Create exercise an exercise expect no exception
        new Exercise(name,description,time, TimeUnit.MINUTES);
    }

    @Test
    public void testInvalidExercise(){
        String name = "";
        String description = "";
        int time = 0;

        // Test individual fields validators
        assertFalse(Exercise.validateName(name));
        assertFalse(Exercise.validateDescription(description));
        assertFalse(Exercise.validateTime(time));
        assertFalse(Exercise.validateAll(name, description, time));

        // Expect an exception from the constructor
        exception.expect(IllegalArgumentException.class);
        new Exercise(name, description, time, TimeUnit.MINUTES);
    }

}
