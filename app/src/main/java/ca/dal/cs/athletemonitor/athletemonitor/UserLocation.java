package ca.dal.cs.athletemonitor.athletemonitor;

/**
 * Created by vibar on 28/03/2018.
 */

public class UserLocation {

    private long timeStamp;
    private String username;
    private int imageId;
    private double lat;
    private double lon;

    public UserLocation (long timeStamp, String username, int imageId, double lat, double lon)
    {
        this.timeStamp = timeStamp;
        this.username = username;
        this.imageId = imageId;
        this.lat = lat;
        this.lon = lon;
    }

    public UserLocation () { /* intentionally left blank */ }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long time) {
        this.timeStamp = time;
    }

    public String getUserLocationName() {
        return username;
    }

    public void setUserLocationName(String username) {
        this.username = username;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
