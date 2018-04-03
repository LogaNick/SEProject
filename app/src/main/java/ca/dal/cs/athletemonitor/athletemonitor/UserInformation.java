package ca.dal.cs.athletemonitor.athletemonitor;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class contains personal information that will be displayed
 * on a user profile page. To instantiate an object of this class, a
 * UserInformationBuilder must be used.
 */
public class UserInformation implements Parcelable {

    public String firstName = "";
    public String lastName = "";
    public int age;
    /** Height in centimetres */
    public int height;
    /** Weight in kilograms */
    public int weight;
    /** The type of athlete (e.g.: Runner, Golfer, Hockey Player) */
    public String athleteType = "";
    /** A statement or goal of the user, to be displayed on their page */
    public String personalStatement = "";
    /** Pictorial representing user on map, default to runner (9) */
    public int imageId = 9;

    private UserInformation(UserInformationBuilder builder) {
        firstName = builder.firstName;
        lastName = builder.lastName;
        age = builder.age;
        height = builder.height;
        weight = builder.weight;
        athleteType = builder.athleteType;
        personalStatement = builder.personalStatement;
        imageId = builder.imageId;
    }

    private UserInformation(Parcel in) {
        String[] strings = new String[4];
        in.readStringArray(strings);
        firstName = strings[0];
        lastName = strings[1];
        athleteType = strings[2];
        personalStatement = strings[3];

        int[] ints = new int[3];
        in.readIntArray(ints);
        age = ints[0];
        height = ints[1];
        weight = ints[2];
        imageId = ints[3];
    }

    /** Default constructor, for parcelization */
    public UserInformation() {
    }

    /** For parcelization */
    @Override
    public int describeContents() {
        return 0;
    }

    /** For parcelization */
    public void writeToParcel(Parcel out, int flags) {
        out.writeStringArray(new String[]{firstName, lastName, athleteType, personalStatement});
        out.writeIntArray(new int[]{age, height, weight, imageId});
    }

    /** For parcelization */
    public static final Parcelable.Creator<UserInformation> CREATOR
            = new Parcelable.Creator<UserInformation>() {

        public UserInformation createFromParcel(Parcel in) {
            return new UserInformation(in);
        }

        public UserInformation[] newArray(int size) {
            return new UserInformation[size];
        }
    };

    // These getters are here for Firebase
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public String getAthleteType() {
        return athleteType;
    }

    public String getPersonalStatement() {
        return personalStatement;
    }

    public int getImageId() {
        return imageId;
    }

    /**
     * This class is used to instantiate a UserInformation object. It uses
     * the builder design pattern.
     */
    static class UserInformationBuilder {
        private String firstName;
        private String lastName;
        private int age;
        /**
         * Height in centimetres
         */
        private int height;
        /**
         * Weight in kilograms
         */
        private int weight;
        /** The type of athlete (e.g.: Runner, Golfer, Hockey Player) */
        private String athleteType = "";
        /** A statement or goal of the user, to be displayed on their page */
        private String personalStatement = "";
        /** Pictorial representing user on map, default to runner (9) */
        private int imageId = 9;

        /**
         * Instantiates a Builder for UserInformation
         * @param firstName the first name of the user
         * @param lastName the last name of the user
         * @throws RuntimeException if firstName or lastName are null
         */
        public UserInformationBuilder(String firstName, String lastName) throws RuntimeException {
            if (firstName == null || lastName == null)
                throw new RuntimeException("Name fields must not be null!");

            this.firstName = firstName;
            this.lastName = lastName;
        }

        /**
         * Add the specified age to the UserInformation
         * @param age the age of the user
         * @return this builder, for method chaining
         */
        public UserInformationBuilder age(int age) {
            this.age = age;
            return this;
        }

        /**
         * Add the specified height to the UserInformation
         * @param height the height of the user
         * @return this builder, for method chaining
         */
        public UserInformationBuilder height(int height) {
            this.height = height;
            return this;
        }

        /**
         * Add the specified weight to the UserInformation
         * @param weight the weight of the user
         * @return this builder, for method chaining
         */
        public UserInformationBuilder weight(int weight) {
            this.weight = weight;
            return this;
        }

        /**
         * Add the specified athleteType to the UserInformation
         * @param athleteType the athleteType of the user
         * @return this builder, for method chaining
         */
        public UserInformationBuilder athleteType(String athleteType) {
            if (athleteType != null)
                this.athleteType = athleteType;
            return this;
        }

        /**
         * Add the specified personalStatement to the UserInformation
         * @param personalStatement the personalStatement of the user
         * @return this builder, for method chaining
         */
        public UserInformationBuilder personalStatement(String personalStatement) {
            if (personalStatement != null)
                this.personalStatement = personalStatement;
            return this;
        }

        /**
         * Add the specified imageId to the UserInformation
         * @param imageId the imageId of the user
         * @return this builder, for method chaining
         */
        public UserInformationBuilder imageId(int imageId) {
            this.imageId = imageId;
            return this;
        }

        /**
         * Build a UserInformation object as presently described by this builder.
         * @return the UserInformation object described by this builder
         */
        public UserInformation build() {
            return new UserInformation(this);
        }
    }
}
