package com.saloj.prosaloj.todo.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetroServer {
   // public static final String baseURL ="http://192.168.43.68/Todo/";
    public static final String baseURL ="http://todo.orgfree.com/Todo/";

    public static Retrofit retro;

    public static Retrofit connectRetrofit(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retro == null){

            retro = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create()) //important
                    .addConverterFactory(GsonConverterFactory.create(gson))
                   // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retro;

      /*  HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        String BASE_URL = "baseUrl";
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();*/
    }

}
