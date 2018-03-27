package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.os.RemoteException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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

    @Rule
    public ActivityTestRule<MapsActivity> recordActivityTestRule =
            new ActivityTestRule<MapsActivity>(MapsActivity.class, false, false);

    @Before
    public void init() {
        Intent intent = new Intent();
        User user = new User("testauston", "leafs");
        intent.putExtra("user", user);
        recordActivityTestRule.launchActivity(intent);
    }

    /**
     * Assert that clicking the record button starts recording.
     */
    @Test
    public void clickRecordStart() {
        onView(withId(R.id.record_button)).perform(click());

        assertTrue(recordActivityTestRule.getActivity().getIsRecording());
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

        assertFalse(recordActivityTestRule.getActivity().getIsRecording());
    }

    /**
     * Assert that clicking the pause button pauses recording.
     */
    @Test
    public void clickPause() {
        onView(withId(R.id.record_button)).perform(click());
        onView(withId(R.id.pause_button)).perform(click());

        assertTrue(recordActivityTestRule.getActivity().getIsRecording());
        assertTrue(recordActivityTestRule.getActivity().getIsPaused());
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

        assertTrue(recordActivityTestRule.getActivity().getIsRecording());
        assertFalse(recordActivityTestRule.getActivity().getIsPaused());
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

        assertTrue(recordActivityTestRule.getActivity().getIsRecording());
    }

    @Test
    public void displayUsers() {
        assertTrue(false);
//         assertTrue(!markerList.isEmpty());
    }

    @Test
    public void checkMarkerImage() {
        assertTrue(false);
//        if (!friendLocationList.isEmpty()) {
//            // choose a test user
//            for (Friend f : friendLocationList) {
//                //TODO change to proper test user
//                if (f.username.equals("test_zachary")) {
//                    assertTrue(f.imageId != defaultvalue);
//                }
//            }
//        }
    }

}
