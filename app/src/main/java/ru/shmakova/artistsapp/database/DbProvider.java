package ru.shmakova.artistsapp.database;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.VisibleForTesting;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ru.shmakova.artistsapp.network.models.Artist;

public class DbProvider {

    private final DbBackend dbBackend;
    private final DbNotificationManager dbNotificationManager;
    private final CustomExecutor executor;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public interface ResultCallback<T> {
        void onFinished(T result);
    }

    public DbProvider(Context context) {
        dbBackend = new DbBackend(context);
        dbNotificationManager = FakeContainer.getNotificationInstance(context);
        executor = new CustomExecutor();
    }

    @VisibleForTesting
    DbProvider(DbBackend dbBackend,
               DbNotificationManager dbNotificationManager,
               CustomExecutor executor) {
        this.dbBackend = dbBackend;
        this.dbNotificationManager = dbNotificationManager;
        this.executor = executor;
    }

    public void getArtistsList(final ResultCallback<Cursor> callback) {
        executor.execute(() -> {
            final Cursor c = dbBackend.getArtistsList();
            handler.post(() -> callback.onFinished(c));
        });
    }

    public void getGenresByArtist(Long artistId, final ResultCallback<Cursor> callback) {
        executor.execute(() -> {
            final Cursor c = dbBackend.getGenresByArtist(artistId);
            handler.post(() -> callback.onFinished(c));
        });
    }

    public void insertArtistsList(final List<Artist> artistList) {
        executor.execute(() -> {
            dbBackend.insertArtistsList(artistList);
            dbNotificationManager.notifyListeners();
        });
    }


    class CustomExecutor extends ThreadPoolExecutor {
        CustomExecutor() {
            super(
                    Runtime.getRuntime().availableProcessors() * 2,
                    Runtime.getRuntime().availableProcessors() * 2,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());
        }
    }
}
