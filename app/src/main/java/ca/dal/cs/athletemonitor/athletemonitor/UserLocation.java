package ca.dal.cs.athletemonitor.athletemonitor;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * This class describes a user associated with a location for display
 * in the MapsActivity.
 */
public class UserLocation implements Parcelable {

    public static final SparseIntArray IMAGE_ID_MAP = new SparseIntArray();

    static {
        IMAGE_ID_MAP.put(0, R.drawable.ic_map_bicycle);
        IMAGE_ID_MAP.put(1, R.drawable.ic_map_face);
        IMAGE_ID_MAP.put(2, R.drawable.ic_map_film);
        IMAGE_ID_MAP.put(3, R.drawable.ic_map_key);
        IMAGE_ID_MAP.put(4, R.drawable.ic_map_monkey);
        IMAGE_ID_MAP.put(5, R.drawable.ic_map_music);
        IMAGE_ID_MAP.put(6, R.drawable.ic_map_paw);
        IMAGE_ID_MAP.put(7, R.drawable.ic_map_pizza);
        IMAGE_ID_MAP.put(8, R.drawable.ic_map_row);
        IMAGE_ID_MAP.put(9, R.drawable.ic_map_run);
        IMAGE_ID_MAP.put(10, R.drawable.ic_map_smiley);
        IMAGE_ID_MAP.put(11, R.drawable.ic_map_swim);
        IMAGE_ID_MAP.put(12, R.drawable.ic_map_walk);
        IMAGE_ID_MAP.put(13, R.drawable.ic_map_weight);
        IMAGE_ID_MAP.put(14, R.drawable.ic_map_wheelchair);
    }

    private String username;
    private long time;
    private int imageId;
    private double lat;
    private double lon;

    /**
     * @param username the username to be set
     * @param time the time to be set
     * @param imageId the imageId to be set
     * @param lat the latitude to be set
     * @param lon the longitude to be set
     */
    public UserLocation(String username, long time, int imageId, double lat, double lon) {
        this.username = username;
        this.time = time;
        this.imageId = imageId;
        this.lat = lat;
        this.lon = lon;
    }

    private UserLocation(Parcel in) {
        username = in.readString();
        time = in.readLong();
        imageId = in.readInt();
        lat = in.readDouble();
        lon = in.readDouble();
    }

    /** Default constructor, for parcelization */
    public UserLocation() {
    }

    /** For parcelization */
    @Override
    public int describeContents() {
        return 0;
    }

    /** For parcelization */
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(username);
        out.writeLong(time);
        out.writeInt(imageId);
        out.writeDouble(lat);
        out.writeDouble(lon);
    }

    /** For parcelization */
    public static final Parcelable.Creator<UserLocation> CREATOR
            = new Parcelable.Creator<UserLocation>() {

        public UserLocation createFromParcel(Parcel in) {
            return new UserLocation(in);
        }

        public UserLocation[] newArray(int size) {
            return new UserLocation[size];
        }
    };

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @return the image ID
     */
    public int getImageId() {
        return imageId;
    }

    /**
     * @return the user latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * @return the user longitude
     */
    public double getLon() {
        return lon;
    }

}
