package ru.shmakova.artistsapp.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import ru.shmakova.artistsapp.BuildConfig;
import ru.shmakova.artistsapp.network.models.Artist;
import ru.shmakova.artistsapp.network.models.Cover;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DbBackendTest implements DbContract {

    @Test
    public void testInsertArtistsList() {
        DbOpenHelper helper = new DbOpenHelper(RuntimeEnvironment.application);
        SQLiteDatabase db = helper.getWritableDatabase();

        DbBackend dbBackend = new DbBackend(helper);

        Artist artist1 = getArtist1();
        Artist artist2 = getArtist2();
        List<Artist> artistList = new ArrayList<>();
        artistList.add(artist1);
        artistList.add(artist2);

        dbBackend.insertArtistsList(artistList);
        Assert.assertEquals(2, getCount(db, ARTISTS));
    }

    @Test
    public void testGetArtistList() {
        DbOpenHelper helper = new DbOpenHelper(RuntimeEnvironment.application);
        DbBackend dbBackend = new DbBackend(helper);

        Artist artist1 = getArtist1();
        Artist artist2 = getArtist2();
        List<Artist> artistList = new ArrayList<>();
        artistList.add(artist1);
        artistList.add(artist2);

        dbBackend.insertArtistsList(artistList);
        Cursor cursor = dbBackend.getArtistsList();
        int i = 0;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Assert.assertEquals(artistList.get(i).getName(),
                        cursor.getString(cursor.getColumnIndex(Artists.NAME)));
                Assert.assertEquals(artistList.get(i).getTracks(),
                        cursor.getInt(cursor.getColumnIndex(Artists.TRACKS)));
                Assert.assertEquals(artistList.get(i).getAlbums(),
                        cursor.getInt(cursor.getColumnIndex(Artists.ALBUMS)));
                Assert.assertEquals(artistList.get(i).getDescription(),
                        cursor.getString(cursor.getColumnIndex(Artists.DESCRIPTION)));
                Assert.assertEquals(artistList.get(i).getLink(),
                        cursor.getString(cursor.getColumnIndex(Artists.LINK)));
                Assert.assertEquals(artistList.get(i).getCover().getSmall(),
                        cursor.getString(cursor.getColumnIndex(Artists.COVER_SMALL)));
                Assert.assertEquals(artistList.get(i).getCover().getBig(),
                        cursor.getString(cursor.getColumnIndex(Artists.COVER_BIG)));
                i++;
            } while (cursor.moveToNext());
        }
    }

    @Test
    public void testGetGenresByArtist() {
        DbOpenHelper helper = new DbOpenHelper(RuntimeEnvironment.application);
        SQLiteDatabase db = helper.getWritableDatabase();

        DbBackend dbBackend = new DbBackend(helper);

        Artist artist = getArtist2();
        List<Artist> artistList = new ArrayList<>();
        artistList.add(artist);

        dbBackend.insertArtistsList(artistList);
        Cursor cursor = dbBackend.getArtistsList();
        cursor.moveToFirst();
        Long artistId = cursor.getLong(cursor.getColumnIndex(Artists.ID));
        Cursor genreCursor = dbBackend.getGenresByArtist(artistId);
        Assert.assertEquals(artist.getGenres().size(), getCount(db, GENRES));

        if (genreCursor != null && genreCursor.moveToFirst()) {
            do {
                Assert.assertTrue(artist.getGenres().contains(
                        genreCursor.getString(genreCursor.getColumnIndex(Genres.NAME))));
            } while (genreCursor.moveToNext());
        }
    }

    private int getCount(SQLiteDatabase db, String table) {
        return DbUtils.getResultLongAndClose(
                db.rawQuery("select count(*) from " + table, null)).intValue();
    }


    private Artist getArtist1() {
        List<String> genres = new ArrayList<>();
        genres.add("rusrock");

        Cover cover = new Cover(
                "http://avatars.yandex.net/get-music-content/4b3e1806.p.161243/300x300",
                "http://avatars.yandex.net/get-music-content/4b3e1806.p.161243/1000x1000"
        );

        return new Artist.Builder()
                .setName("Сплин")
                .setTracks(178)
                .setAlbums(17)
                .setGenres(genres)
                .setCover(cover)
                .setDescription("российская рок-группа из Санкт-Петербурга. Бессменный лидер - Александр Васильев.")
                .setLink("http://splean.ru/")
                .build();
    }

    private Artist getArtist2() {
        List<String> genres = new ArrayList<>();
        genres.add("pop");
        genres.add("rnb");
        genres.add("rap");

        Cover cover = new Cover(
                "http://avatars.yandex.net/get-music-content/40598113.p.1150/300x300",
                "http://avatars.yandex.net/get-music-content/40598113.p.1150/1000x1000"
        );

        return new Artist.Builder()
                .setName("Keri Hilson")
                .setTracks(99)
                .setAlbums(64)
                .setGenres(genres)
                .setCover(cover)
                .setDescription("американская певица и автор песен в стиле современного R&B")
                .build();
    }
}
