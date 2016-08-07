package ru.shmakova.artistsapp.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.shmakova.artistsapp.managers.DataManager;
import ru.shmakova.artistsapp.network.models.Artist;
import timber.log.Timber;

/**
 * Created by shmakova on 06.08.16.
 */

public class ArtistsContentProvider extends ContentProvider {
    private static final String AUTHORITY = "ru.shmakova.artistsapp";
    private static final int ARTISTS_URI_CODE = 1;
    private static final UriMatcher uriMatcher;
    private DataManager dataManager;
    private DbProvider dbProvider;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "artists", ARTISTS_URI_CODE);
    }

    public Cursor query(
            @NonNull Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case ARTISTS_URI_CODE:
                Cursor cursor = null;

                try {
                    cursor = dbProvider.getArtistsList();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                return cursor;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }


    @Override
    public boolean onCreate() {
        dataManager = DataManager.getInstance(getContext());
        dbProvider = FakeContainer.getProviderInstance(getContext());
        saveArtistsToDb();
        return false;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ARTISTS_URI_CODE:
                return "vnd.android.cursor.dir/vnd.ru.shmakova.artistsapp.provider.artists";
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private void saveArtistsToDb() {
        Call<List<Artist>> call = dataManager.getArtistsList();

        call.enqueue(new Callback<List<Artist>>() {
            @Override
            public void onResponse(Call<List<Artist>> call, Response<List<Artist>> response) {
                try {
                    dbProvider.insertArtistsList(response.body());
                } catch (NullPointerException e) {
                    Timber.e(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Artist>> call, Throwable t) {

            }
        });
    }
}
