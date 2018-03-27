package ca.dal.cs.athletemonitor.athletemonitor;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * This class describes a user associated with a location for display
 * in the MapsActivity.
 */
public class UserLocation implements Parcelable {

    private String username;
    private long time;
    private int imageId;
    private LatLng loc;

    /**
     * @param username the username to be set
     * @param time the time to be set
     * @param imageId the imageId to be set
     * @param loc the location to be set
     */
    public UserLocation(String username, long time, int imageId, LatLng loc) {
        this.username = username;
        this.time = time;
        this.imageId = imageId;
        this.loc = loc;
    }

    private UserLocation(Parcel in) {
        username = in.readString();
        time = in.readLong();
        imageId = in.readInt();
        loc = in.readParcelable(LatLng.class.getClassLoader());
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
        out.writeParcelable(loc, 0);
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
     * @param username the username to be set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @param time the time to be set
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * @return the image ID
     */
    public int getImageId() {
        return imageId;
    }

    /**
     * @param imageId the imageId to be set
     */
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    /**
     * @return the location
     */
    public LatLng getLoc() {
        return loc;
    }

    /**
     * @param loc the location to be set
     */
    public void setLoc(LatLng loc) {
        this.loc = loc;
    }

}
