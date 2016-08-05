package ru.shmakova.artistsapp.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.shmakova.artistsapp.network.models.Artist;

/**
 * Created by shmakova on 13.04.16.
 */
public interface YandexService {
    @GET("mobilization-2016/artists.json")
    Call<List<Artist>> getArtistsList();
}
