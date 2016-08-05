package ru.shmakova.artistsapp.ui.activities;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.shmakova.artistsapp.App;
import ru.shmakova.artistsapp.R;
import ru.shmakova.artistsapp.database.DbProvider;
import ru.shmakova.artistsapp.database.FakeContainer;
import ru.shmakova.artistsapp.managers.DataManager;
import ru.shmakova.artistsapp.network.models.Artist;
import timber.log.Timber;

public class MainActivity extends BaseActivity {
    private DataManager dataManager;
    private DbProvider dbProvider;
    private DbProvider.ResultCallback<Cursor> updateCallback = null;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.get(this).applicationComponent().inject(this);
        dataManager = DataManager.getInstance(this);
        dbProvider = FakeContainer.getProviderInstance(this);
        setContentView(getLayoutInflater().inflate(R.layout.activity_main, null));

        if (savedInstanceState == null) {
            loadArtists();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void loadArtists() {
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
