package ru.shmakova.artistsapp;

import android.support.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import ru.shmakova.artistsapp.network.models.Artist;
import ru.shmakova.artistsapp.ui.activities.MainActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ApplicationTest {
    private static final String ARTIST_NAME = "Сплин";
    private static final String ARTIST_GENRES = "rusrock";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void ApplicationTest() {
        // find Artist in Artists list with ARTIST_NAME name and ARTIST_GENRES genres and tap on it
        onData(allOf(withArtistName(ARTIST_NAME), withGenres(ARTIST_GENRES))).perform(click());

        // check visibility all items in the opened activity
        onView(withId(R.id.biography)).check(matches(isDisplayed()));
        onView(withId(R.id.genres)).check(matches(withText(ARTIST_GENRES)));
        onView(withId(R.id.info)).check(matches(isDisplayed()));
        onView(withId(R.id.description)).check(matches(isDisplayed()));
        onView(withId(R.id.cover_big)).check(matches(isDisplayed()));
    }

    public static Matcher<Object> withArtistName(final String artistName) {
        return new BoundedMatcher<Object, Artist>(Artist.class) {
            @Override
            protected boolean matchesSafely(Artist artist) {
                return artistName.equals(artist.getName());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with id: " + artistName);
            }
        };
    }

    public static Matcher<Object> withGenres(final String genres) {
        return new BoundedMatcher<Object, Artist>(Artist.class) {
            @Override
            protected boolean matchesSafely(Artist artist) {
                return genres.equals(artist.getGenresString());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with id: " + genres);
            }
        };
    }
}