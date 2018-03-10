package ca.dal.cs.athletemonitor.athletemonitor;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * The Exercise class contains data for a single exercise
 */
public class Exercise {
    private String name;
    private String description;
    private int time;
    private TimeUnit timeUnit;

    /**
     * No args constructor used primarily for database based initialization of exercise objects
     */
    public Exercise() { /* intentionally left blank */ }

    /**
     * Constructor to create an exercise with all necessary information. This validates the
     * parameters and throws an exception if any are invalid.
     *
     * @param name Name of exercise
     * @param description Description of exercise
     * @param time Time of exercise
     * @param timeUnit Time unit of the time parameter
     */
    public Exercise(String name, String description, int time, TimeUnit timeUnit) {
        if (!(Exercise.validateAll(name, description, time))) {
            throw new IllegalArgumentException("Invalid Exercise parameters");
        }

        this.name = name;
        this.description = description;
        this.time = time;
        this.timeUnit = timeUnit;
    }

    /**
     * Retrieve name of exercise
     *
     * @return name of exercise
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of exercise
     *
     * @param name of exercise
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve description of exercise
     *
     * @return description of exercise
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description of exercise
     *
     * @param description of exercise
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieve time of exercise
     *
     * @return time of exercise
     */
    public int getTime() {
        return time;
    }

    /**
     * Set time of exercise
     *
     * @param time of exercise
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Retrieve time unit of exercise
     *
     * @return time unit of exercise
     */
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    /**
     * Set time unit of exercise
     *
     * @param timeUnit of exercise
     */
    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    /**
     * Static validator methods to check each of the fields.
     * @param name Name of exercise to check
     * @param description Description of exercise
     * @param time Time length of exercise
     * @return True if the exercise is valid
     */
    public static boolean validateAll(String name, String description, int time){
        return validateName(name) && validateDescription(description) && validateTime(time);
    }

    /**
     * Validate the name of an exercise
     * @param name to be used for an exercise
     * @return whether name is valid
     */
    public static boolean validateName(String name) {
        return name.length() > 0 && name.length() <= 20;
    }

    /**
     * Validate the description of an exercise
     * @param description to be used for an exercise
     * @return whether description is valid
     */
    public static boolean validateDescription(String description){
        return description.length() > 0 && description.length() <= 500;
    }

    /**
     * Validate the time of an exercise
     * @param time to be used for an exercise
     * @return whether time is valid
     */
    public static boolean validateTime(int time){
        return time > 0;
    }

    public boolean equals(Exercise e){
        return name.equals(e.getName()) && description.equals(e.getDescription())
                && time == e.getTime() && timeUnit.equals(e.getTimeUnit());
    }
}