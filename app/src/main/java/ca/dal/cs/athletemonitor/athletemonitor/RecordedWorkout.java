package ca.dal.cs.athletemonitor.athletemonitor;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordedWorkout implements Parcelable {

    private List<Location> gpsCoordinates;
    /** Time in milliseconds */
    private long time;

    public RecordedWorkout(List<Location> gpsCoordinates, long time) {
        this.gpsCoordinates = gpsCoordinates;
        this.time = time;
    }

    public RecordedWorkout(Parcel in) {
        Location[] locs = (Location[]) in.readParcelableArray(Location.class.getClassLoader());
        gpsCoordinates = new ArrayList<Location>(Arrays.asList(locs));
        time = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelableArray(gpsCoordinates.toArray(new Parcelable[gpsCoordinates.size()]), 0);
        out.writeLong(time);
    }

    public static final Parcelable.Creator<RecordedWorkout> CREATOR =
            new Parcelable.Creator<RecordedWorkout>() {
                public RecordedWorkout createFromParcel(Parcel in) {
                    return new RecordedWorkout(in);
                }

                public RecordedWorkout[] newArray(int size) {
                    return new RecordedWorkout[size];
                }
            };

    public List<Location> getGpsCoordinates() {
        return gpsCoordinates;
    }

    public long getTime() {
        return time;
    }

}
