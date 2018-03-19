package ca.dal.cs.athletemonitor.athletemonitor;

import java.io.Serializable;

/**
 * The Team class contains data for a team
 */

public class Team implements Serializable {
    /**
     * Team name
     */
    private String name;

    /**
     * Team motto
     */
    private String motto;

    /**
     * Owner of team
     */
    private String owner;

    /**
     * No args constructor used primarily for database based initialization of exercise objects
     */
    public Team() { /* intentionally left blank */ }

    /**
     * Constructor to create an exercise with all necessary information. This validates the
     * parameters and throws an exception if any are invalid.
     *
     * @param name Name of team
     * @param motto Team motto
     */
    public Team(String name, String motto, String owner){
        if (!(Team.validateAll(name, motto))) {
            throw new IllegalArgumentException("Invalid Exercise parameters");
        }

        this.name = name;
        this.motto = motto;
        this.owner = owner;
    }

    /**
     * Retrieve name of team
     * @return team name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the team name
     * @param name of team
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve team motto
     * @return team motto
     */
    public String getMotto() {
        return motto;
    }

    /**
     * Set the team motto
     * @param motto of team
     */
    public void setMotto(String motto) {
        this.motto = motto;
    }

    /**
     * Sets the team owner
     *
     * @param owner Owner of the team
     */
    public void setOwner(String owner) { this.owner = owner; }

    /**
     * Retrieves the team owner
     *
     * @return Username of the team that owns the team
     */
    public String getOwner() { return this.owner; }

    /**
     * Validate a team name
     * @param name Name of team
     * @return Whether name is valid
     */
    public static boolean validateName(String name){
        return name.length() > 0 && name.length() <= 20;
    }

    /**
     * Validate a team motto
     * @param motto Motto of team
     * @return Whether motto is valid
     */
    public static boolean validateMotto(String motto){
        return motto.length() > 0 && motto.length() <= 50;
    }

    /**
     * Validate a team owner
     * @param owner Owner of team
     * @return Whether owner is valid
     */
    public static boolean validateOwner(String owner){
        return owner.length() > 0 && owner.length() <= 50;
    }

    /**
     * Validate all team parameters
     *
     * @param name Name of team
     * @param motto Motto of team
     *
     * @return Whether all are valid
     */
    public static boolean validateAll(String name, String motto){
        return validateName(name) && validateMotto(motto);
    }

    /**
     * Validate all team parameters
     *
     * @param name Name of team
     * @param motto Motto of team
     *
     * @return Whether all are valid
     */
    public static boolean validateAll(String name, String motto, String owner){
        return validateName(name) && validateMotto(motto) && validateOwner(owner);
    }

    /**
     * Determines equality of this team versus the team specified
     *
     * Equality is decided first by object reference value and second by team name and owner
     *
     * @param object Team to compare this team to
     *
     * @return True if the teams are the same object instance or have the same name and owner,
     * otherwise false
     */
    @Override
    public boolean equals(Object object) {
        if (object == null ) return false;
        if (!(object instanceof Team)) return false;
        if (object == this) return true;

        Team teamObject = (Team) object;

        if (this.name.equals(teamObject.getName()) &&
                this.owner.equals(teamObject.getOwner())) { return true; }

        // if we are here, the object is not equal to this one
        return false;
    }
}
