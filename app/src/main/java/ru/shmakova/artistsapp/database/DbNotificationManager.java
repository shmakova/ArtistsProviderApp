package ru.shmakova.artistsapp.database;

import android.os.Handler;
import android.os.Looper;

import java.util.HashSet;

public class DbNotificationManager {

    private HashSet<Listener> listeners = new HashSet<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable notifyRunnable = () -> notifyOnUiThread();

    public interface Listener {
        void onDataUpdated();
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    void notifyListeners() {
        handler.removeCallbacks(notifyRunnable);
        handler.post(notifyRunnable);
    }

    private void notifyOnUiThread() {
        for (Listener l : listeners) l.onDataUpdated();
    }
}
