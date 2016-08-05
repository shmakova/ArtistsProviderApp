package ru.shmakova.artistsapp.ui.views;

import android.support.annotation.NonNull;

import ru.shmakova.artistsapp.performance.AnyThread;

public interface DeveloperSettingsView {

    @AnyThread
    void changeBuildVersionCode(@NonNull String versionCode);

    @AnyThread
    void changeBuildVersionName(@NonNull String versionName);

    @AnyThread
    void changeStethoState(boolean enabled);

    @AnyThread
    void changeLeakCanaryState(boolean enabled);

    @AnyThread
    void changeTinyDancerState(boolean enabled);

    @AnyThread
    void showMessage(@NonNull String message);

    @AnyThread
    void showAppNeedsToBeRestarted();
}
