package ru.shmakova.artistsapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DbOpenHelper extends SQLiteOpenHelper implements DbContract {

    private static final int DB_VERSION = 1;

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ARTISTS + "(" +
                Artists.NAME + " TEXT UNIQUE NOT NULL, " +
                Artists.TRACKS + " INTEGER NOT NULL, " +
                Artists.ALBUMS + " INTEGER NOT NULL, " +
                Artists.DESCRIPTION + " TEXT, " +
                Artists.LINK + " TEXT, " +
                Artists.COVER_SMALL + " TEXT, " +
                Artists.COVER_BIG + " TEXT" +
                ")");
        db.execSQL(
                "CREATE TABLE " + GENRES + "(" +
                        Genres.NAME + " TEXT NOT NULL UNIQUE" +
                        ")");
        db.execSQL(
                "CREATE TABLE " + ARTISTS_GENRES + "(" +
                        ArtistsGenres.ARTIST_ID + " INTEGER NOT NULL, " +
                        ArtistsGenres.GENRE_ID + " INTEGER NOT NULL" +
                        ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Updating for dev versions!
        db.execSQL("DROP TABLE " + ARTISTS);
        db.execSQL("DROP TABLE " + GENRES);
        db.execSQL("DROP TABLE " + ARTISTS_GENRES);
        onCreate(db);
    }
}
