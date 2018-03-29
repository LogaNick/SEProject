package ca.dal.cs.athletemonitor.athletemonitor.testhelpers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Map;

import static android.support.test.internal.util.Checks.checkNotNull;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Custom adapted from Cory Roy @ https://stackoverflow.com/a/30361345/3169479
 *
 */
public class Matchers {
    public static Matcher<View> withListSize (final int size) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                return ((LinearLayout) view).getChildCount() == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("Expected " + size + " items");
            }
        };
    }

    /**
     * AdapterView matcher
     * https://developer.android.com/training/testing/espresso/recipes.html
     *
     * @param dataMatcher
     * @return
     */
    public static Matcher<View> withAdaptedData(final Matcher<Object> dataMatcher) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("with class name: ");
                dataMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof AdapterView)) {
                    return false;
                }

                @SuppressWarnings("rawtypes")
                Adapter adapter = ((AdapterView) view).getAdapter();
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (dataMatcher.matches(adapter.getItem(i))) {
                        return true;
                    }
                }

                return false;
            }
        };
    }

    /**
     * https://github.com/vgrec/EspressoExamples/blob/master/app/src/androidTest/java/com/vgrec/espressoexamples/matchers/CustomMatchers.java
     * @param expectedText
     * @return
     */
    public static Matcher<Object> withItemContent(String expectedText) {
        checkNotNull(expectedText);
        return withItemContent(equalTo(expectedText));
    }

    /**
     * https://github.com/vgrec/EspressoExamples/blob/master/app/src/androidTest/java/com/vgrec/espressoexamples/matchers/CustomMatchers.java
     * @param itemTextMatcher
     * @return
     */
    public static BoundedMatcher<Object, Map> withItemContent(final Matcher<String> itemTextMatcher) {
        return new BoundedMatcher<Object, Map>(Map.class) {
            @Override
            public boolean matchesSafely(Map map) {
                return hasEntry(equalTo("STR"), itemTextMatcher).matches(map);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with item content: ");
                itemTextMatcher.describeTo(description);
            }
        };
    }
}
