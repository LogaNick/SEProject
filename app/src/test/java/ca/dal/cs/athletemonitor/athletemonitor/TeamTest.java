package ca.dal.cs.athletemonitor.athletemonitor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Unit tests for the Team class
 */
public class TeamTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Tests that valid team information accepted
     */
    @Test
    public void testValidTeam(){
        String name = "Warriors";
        String motto = "We have swords!";
        String owner = "team_owner";

        // Assert all fields are valid
        assertTrue(Team.validateAll(name, motto));
        // Create new team and expect no exception
        new Team(name, motto, owner);
    }

    /**
     * Tests that invalid team information is detected
     */
    @Test
    public void testInvalidTeam(){
        String name = "We love really long team names that don't make sense";
        String motto = "";
        String owner = "";

        assertFalse(Team.validateName(name));
        assertFalse(Team.validateMotto(motto));
        assertFalse(Team.validateOwner(owner));
        assertFalse(Team.validateAll(name, motto, owner));

        // Expect an exception when creating the new Team
        exception.expect(IllegalArgumentException.class);
        new Team(name, motto, owner);
    }
}
