package ru.shmakova.artistsapp.database;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.VisibleForTesting;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ru.shmakova.artistsapp.network.models.Artist;

public class DbProvider {

    private final DbBackend dbBackend;
    private final DbNotificationManager dbNotificationManager;
    private final CustomExecutor executor;

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

    public Cursor getArtistsList() throws ExecutionException, InterruptedException {
        Future<Cursor> result = executor.submit(() -> {
            final Cursor c = dbBackend.getArtistsList();
            return c;
        });

        return result.get();
    }

    public void insertArtistsList(final List<Artist> artistList) {
        executor.execute(() -> {
            dbBackend.insertArtistsList(artistList);
            dbNotificationManager.notifyListeners();
        });
    }


    private class CustomExecutor extends ThreadPoolExecutor {
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
