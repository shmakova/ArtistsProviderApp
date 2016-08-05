package ru.shmakova.artistsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import ru.shmakova.artistsapp.network.models.Artist;

class DbBackend implements DbContract {

    private final DbOpenHelper mDbOpenHelper;

    DbBackend(Context context) {
        mDbOpenHelper = new DbOpenHelper(context);
    }

    @VisibleForTesting
    DbBackend(DbOpenHelper dbOpenHelper) {
        mDbOpenHelper = dbOpenHelper;
    }

    public Cursor getArtistsList() {
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        String tables = ARTISTS;

        String[] columns = new String[]{
                ARTISTS + "." + Artists.ID,
                ARTISTS + "." + Artists.NAME,
                ARTISTS + "." + Artists.TRACKS,
                ARTISTS + "." + Artists.ALBUMS,
                ARTISTS + "." + Artists.DESCRIPTION,
                ARTISTS + "." + Artists.LINK,
                ARTISTS + "." + Artists.COVER_SMALL,
                ARTISTS + "." + Artists.COVER_BIG
        };

        Cursor c = db.query(tables, columns, null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

    public Cursor getGenresByArtist(Long artistId) {
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        String tables = GENRES + " INNER JOIN " + ARTISTS_GENRES +
                " ON " + GENRES + "." + Genres.ID + "=" + ARTISTS_GENRES + "." + ArtistsGenres.ARTIST_ID;

        String[] columns = new String[]{
                GENRES + "." + Genres.ID,
                GENRES + "." + Genres.NAME
        };

        String where = ArtistsGenres.ARTIST_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(artistId)};
        String orderBy = Genres.NAME + " ASC";
        Cursor c = db.query(tables, columns, where, whereArgs, null, null, orderBy);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

    public void insertArtistsList(List<Artist> artistList) {
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            for (Artist artist : artistList) {
                Long artistId = queryArtistByName(db, artist.getName());

                if (artistId == null) {
                    artistId = insertArtist(db, artist);
                    insertGenres(db, artistId, artist.getGenres());
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private Long queryArtistByName(SQLiteDatabase db, String artistName) {
        Cursor c = db.query(
                ARTISTS, new String[]{Artists.ID},  // => SELECT rowid FROM artists
                Artists.NAME + "=?", new String[]{artistName},  // => WHERE artists.name='artistName'
                null, null, null);
        return DbUtils.getResultLongAndClose(c);
    }

    private long insertArtist(SQLiteDatabase db, Artist artist) {
        ContentValues values = new ContentValues();
        values.put(Artists.NAME, artist.getName());
        values.put(Artists.ALBUMS, artist.getAlbums());
        values.put(Artists.TRACKS, artist.getTracks());
        values.put(Artists.DESCRIPTION, artist.getDescription());
        values.put(Artists.LINK, artist.getLink());
        values.put(Artists.COVER_SMALL, artist.getCover().getSmall());
        values.put(Artists.COVER_BIG, artist.getCover().getBig());
        return db.insert(ARTISTS, null, values);
    }

    private void insertGenres(SQLiteDatabase db, Long artistId, List<String> genres) {
        for (String genre : genres) {
            Long genreId = queryGenreByName(db, genre);

            if (genreId == null) {
                genreId = insertGenre(db, genre);
            }

            Long artistGenreId = queryArtistGenreByIds(db, artistId, genreId);

            if (artistGenreId == null) {
                insertArtistGenre(db, artistId, genreId);
            }

        }
    }

    private long insertGenre(SQLiteDatabase db, String genre) {
        ContentValues values = new ContentValues();
        values.put(Genres.NAME, genre);
        return db.insert(GENRES, null, values);
    }

    private Long insertArtistGenre(SQLiteDatabase db, Long artistId, Long genreId) {
        ContentValues values = new ContentValues();
        values.put(ArtistsGenres.ARTIST_ID, artistId);
        values.put(ArtistsGenres.GENRE_ID, genreId);
        return db.insert(ARTISTS_GENRES, null, values);
    }

    private Long queryArtistGenreByIds(SQLiteDatabase db, Long artistId, Long genreId) {
        Cursor c = db.query(
                ARTISTS_GENRES, new String[]{ArtistsGenres.ID},  // => SELECT rowid FROM artists_genres
                ArtistsGenres.ARTIST_ID + "=? and " + ArtistsGenres.GENRE_ID + "=?", // => WHERE artists_genres.artist_id='artistId' and
                new String[]{String.valueOf(artistId), String.valueOf(genreId)}, // =>  artists_genres.genre_id='genreId'
                null, null, null);
        return DbUtils.getResultLongAndClose(c);
    }

    private Long queryGenreByName(SQLiteDatabase db, String genre) {
        Cursor c = db.query(
                GENRES, new String[]{Genres.ID},  // => SELECT rowid FROM genres
                Genres.NAME + "=?", new String[]{genre},  // => WHERE genres.name='genre'
                null, null, null);
        return DbUtils.getResultLongAndClose(c);
    }
}
