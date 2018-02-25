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

    public Exercise(String name, String description, int time, TimeUnit timeUnit) {
        if (!(Exercise.validateAll(name, description, time))) {
            throw new IllegalArgumentException("Invalid Exercise parameters");
        }
        this.name = name;
        this.description = description;
        this.time = time;
        this.timeUnit = timeUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

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
        return (validateName(name) && validateDescription(description) && validateTime(time));
    }

    public static boolean validateName(String name) {
        return (name.length()>0 && name.length()< 20);
    }
    public static boolean validateDescription(String description){
        return (description.length()>0 && description.length()<500);
    }
    public static boolean validateTime(int time){
        return (time>0);
    }
}
