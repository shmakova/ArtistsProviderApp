package ru.shmakova.artistsapp.database;

/**
 * Created by user on 31/07/2016.
 */
interface DbContract {
    String DB_NAME = "main.sqlite";

    String ARTISTS = "artists";
    interface Artists {
        String ID = "rowid";
        String NAME = "name";
        String TRACKS = "tracks";
        String ALBUMS = "albums";
        String DESCRIPTION = "description";
        String LINK = "link";
        String COVER_SMALL = "cover_small";
        String COVER_BIG = "cover_big";
    }

    String ARTISTS_GENRES = "artists_genres";
    interface ArtistsGenres {
        String ID = "rowid";
        String ARTIST_ID = "artist_id";
        String GENRE_ID = "genre_id";
    }

    String GENRES = "genres";
    interface Genres {
        String ID = "rowid";;
        String NAME = "name";
    }
}
