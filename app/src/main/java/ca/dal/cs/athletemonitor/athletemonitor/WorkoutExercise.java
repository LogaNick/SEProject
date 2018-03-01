package ca.dal.cs.athletemonitor.athletemonitor;

import java.util.concurrent.TimeUnit;

/**
 * The Workout Exercise is an extension of exercise, to store the achieved
 * results of the exercise.
 */

public class WorkoutExercise extends Exercise{

    private int data;

    public WorkoutExercise ()
    {
        this.data = 0;
    }

    public WorkoutExercise(Exercise e) {
        this.setName(e.getName());
        this.setDescription(e.getDescription());
        this.setTime(e.getTime());
        this.setTimeUnit(e.getTimeUnit());
        this.setData(0);
    }

    public int getData ()
    {
        return this.data;
    }

    public void setData (int d)
    {
        this.data = d;
    }

}
