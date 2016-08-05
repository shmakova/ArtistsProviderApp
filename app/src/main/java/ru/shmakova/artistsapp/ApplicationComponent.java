package ru.shmakova.artistsapp;

import android.os.Handler;
import android.support.annotation.NonNull;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import ru.shmakova.artistsapp.ui.activities.MainActivity;

@Singleton
@Component(modules = {
        ApplicationModule.class
})
public interface ApplicationComponent {

    @NonNull @Named(ApplicationModule.MAIN_THREAD_HANDLER)
    Handler mainThreadHandler();

    void inject(@NonNull MainActivity mainActivity);
}
