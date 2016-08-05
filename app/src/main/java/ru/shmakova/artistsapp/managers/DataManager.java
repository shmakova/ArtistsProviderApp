package ru.shmakova.artistsapp.managers;

import android.content.Context;

import java.util.List;

import retrofit2.Call;
import ru.shmakova.artistsapp.database.DbNotificationManager;
import ru.shmakova.artistsapp.database.DbProvider;
import ru.shmakova.artistsapp.database.FakeContainer;
import ru.shmakova.artistsapp.network.ServiceGenerator;
import ru.shmakova.artistsapp.network.YandexService;
import ru.shmakova.artistsapp.network.models.Artist;

/**
 * Created by shmakova on 16.07.16.
 */

public class DataManager {
    private static DataManager INSTANCE = null;
    private YandexService yandexService;
    private DbProvider dbProvider;
    private DbNotificationManager dbNotificationManager;

    private DataManager(Context context) {
        this.yandexService = ServiceGenerator.createService(YandexService.class, context);
        dbProvider = FakeContainer.getProviderInstance(context.getApplicationContext());
        dbNotificationManager = FakeContainer.getNotificationInstance(context.getApplicationContext());
    }

    public static DataManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DataManager(context.getApplicationContext());
        }

        return INSTANCE;
    }

    public Call<List<Artist>> getArtistsList() {
        return yandexService.getArtistsList();
    }


}
