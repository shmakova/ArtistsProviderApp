package ru.shmakova.artistsapp.database;

import android.content.Context;

/**
 * Created by user on 31/07/2016.
 */
public class FakeContainer {

    private static DbProvider dbProviderInstance;
    public static DbProvider getProviderInstance(Context context) {
        context = context.getApplicationContext();
        if (dbProviderInstance == null) {
            dbProviderInstance = new DbProvider(context);
        }
        return dbProviderInstance;
    }

    private static DbNotificationManager dbNotificationInstance;
    public static DbNotificationManager getNotificationInstance(Context context) {
        context = context.getApplicationContext();
        if (dbNotificationInstance == null) {
            dbNotificationInstance = new DbNotificationManager();
        }
        return dbNotificationInstance;
    }
}
