package ru.shmakova.artistsapp.network;

import android.content.Context;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.shmakova.artistsapp.network.interceptors.CacheInterceptor;
import ru.shmakova.artistsapp.network.interceptors.OfflineCacheInterceptor;
import ru.shmakova.artistsapp.utils.AppConfig;

/**
 * Created by shmakova on 16.07.16.
 */

public class ServiceGenerator {
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass, Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        File httpCacheDirectory = new File(context.getCacheDir(), "http-cache");
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024); // 10mb

        httpClient.addInterceptor(logging)
                .addInterceptor(new OfflineCacheInterceptor(context))
                .addNetworkInterceptor(new CacheInterceptor())
                .cache(cache);

        Retrofit retrofit = builder
                .client(httpClient.build())
                .build();

        return retrofit.create(serviceClass);
    }
}