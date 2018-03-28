package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.os.RemoteException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * This class contains tests which assert the correct functionality of the
 * Maps Activity.
 */
public class MapsActivityUnitTests {

    /** The default image ID is R.drawable.ic_map_run */
    private static final int DEFAULT_IMAGE_VALUE = 9;
    private static final String TEST_USERNAME = "testauston";

    @Rule
    public ActivityTestRule<MapsActivity> mapsActivityTestRule =
            new ActivityTestRule<MapsActivity>(MapsActivity.class, false, false);

    @BeforeClass
    public static void initUserLoc() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef
                = db.getReference("user_locations");

        UserLocation zLoc = new UserLocation("testzachary", System.currentTimeMillis(), 0, 0.0, 0.0);
        myRef.child("testzachary").setValue(zLoc);
    }

    @Before
    public void init() {
        Intent intent = new Intent();
        User user = new User(TEST_USERNAME, "leafs");
        intent.putExtra("user", user);
        mapsActivityTestRule.launchActivity(intent);
    }

    /**
     * Assert that clicking the record button starts recording.
     */
    @Test
    public void clickRecordStart() {
        onView(withId(R.id.record_button)).perform(click());

        assertTrue(mapsActivityTestRule.getActivity().getIsRecording());
    }

    /**
     * Assert that clicking the record button after it has started recording
     * stops recording.
     */
    @Test
    public void clickRecordStop() {
        onView(withId(R.id.record_button)).perform(click());
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        onView(withId(R.id.record_button)).perform(click());

        assertFalse(mapsActivityTestRule.getActivity().getIsRecording());
    }

    /**
     * Assert that clicking the pause button pauses recording.
     */
    @Test
    public void clickPause() {
        onView(withId(R.id.record_button)).perform(click());
        onView(withId(R.id.pause_button)).perform(click());

        assertTrue(mapsActivityTestRule.getActivity().getIsRecording());
        assertTrue(mapsActivityTestRule.getActivity().getIsPaused());
    }

    /**
     * Assert that clicking the pause button after it has been paused
     * resumes recording.
     */
    @Test
    public void clickResume() {
        onView(withId(R.id.record_button)).perform(click());
        onView(withId(R.id.pause_button)).perform(click());
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        onView(withId(R.id.pause_button)).perform(click());

        assertTrue(mapsActivityTestRule.getActivity().getIsRecording());
        assertFalse(mapsActivityTestRule.getActivity().getIsPaused());
    }

    /**
     * Assert that starting and then stopping a recording brings up a save
     * dialog box.
     */
    @Test
    public void saveDialogAppears() {
        onView(withId(R.id.record_button)).perform(click());
        onView(withId(R.id.record_button)).perform(click());

        onView(withText(R.string.activity_maps_save_dialog)).check(matches(isDisplayed()));
    }

    /**
     * Assert that recording continues when the device is locked.
     */
    @Test
    public void recordWhileScreenIsLocked() {
        onView(withId(R.id.record_button)).perform(click());

        UiDevice dev = UiDevice.getInstance(getInstrumentation());
        try {
            dev.sleep();
            Thread.sleep(2000);
            dev.wakeUp();
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertTrue(mapsActivityTestRule.getActivity().getIsRecording());
    }

    @Test
    public void displayUsers() {
         assertTrue(!mapsActivityTestRule.getActivity().getMarkerList().isEmpty());
    }

    @Test
    public void checkMarkerImage() {
        ArrayList<UserLocation> friendLocationList =
                mapsActivityTestRule.getActivity().getFriendLocationList();

        if (!mapsActivityTestRule.getActivity().getFriendLocationList().isEmpty()) {
            // choose a test user
            for (UserLocation f : friendLocationList) {
                if (f.getUsername().equals(TEST_USERNAME)) {
                    assertTrue(f.getImageId() != DEFAULT_IMAGE_VALUE);
                }
            }
        }
    }

    @AfterClass
    public static void teardownTestLoc() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef
                = db.getReference("user_locations");

        myRef.child("testzachary").setValue(null);
    }

}
