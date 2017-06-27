package com.example.mannas.topweather.content;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.mannas.topweather.BuildConfig;
import com.example.mannas.topweather.SharedPreferenceManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Mannas on 6/24/2017.
 */

public class WeatherCurent_loader extends AsyncTaskLoader<WeatherCurent> {
    private LatLng latLng;
    OkHttpClient client;

    public WeatherCurent_loader(Context context) {
        super(context);
        latLng = SharedPreferenceManager.getLatLan(getContext());
    }
    private String BuildUrl(){
    //http://api.openweathermap.org/data/2.5/weather?lat=0.0&lon=0.0&APPID=8caabb2ab9fd65e0fdffd0971f85cafe
//http://api.openweathermap.org/data/2.5/weather?lat=30.132387499999997&lon=31.256722656249977&APPID=8caabb2ab9fd65e0fdffd0971f85cafe&units=metric

        return  "http://api.openweathermap.org/data/2.5/weather?" +
                "lat=" + latLng.latitude+
                "&lon=" + latLng.longitude+
                "&APPID="+ BuildConfig.APPID+"&units=metric";
    }
    @Override
    public WeatherCurent loadInBackground() {
        client = new OkHttpClient();
         String respond = null;
        try {
            respond = download(BuildUrl());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(),"error Downloading :: check internet connection");
            return null;
        }

        try {
            return pars(respond);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(),"error Parsing :: chack the Parsing method ()");
            return null;
        }
    }
    private String download(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        Log.i("loader ","finish");
        return response.body().string();
        //return "{\"coord\":{\"lon\":31.26,\"lat\":30.13},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"base\":\"stations\",\"main\":{\"temp\":26,\"pressure\":1010,\"humidity\":78,\"temp_min\":26,\"temp_max\":26},\"visibility\":6000,\"wind\":{\"speed\":3.1,\"deg\":350},\"clouds\":{\"all\":0},\"dt\":1498456800,\"sys\":{\"type\":1,\"id\":6392,\"message\":0.0036,\"country\":\"EG\",\"sunrise\":1498445729,\"sunset\":1498496416},\"id\":7916170,\"name\":\"Masākin al Akhshāb wa ash Shāy\",\"cod\":200}";

    }

    private WeatherCurent pars(String json) throws JSONException {
        if(json!=null && !json.equals("")){
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(json);
            WeatherCurent weatherCurent = gson.fromJson(jsonObject.toString(),WeatherCurent.class);
            String s = weatherCurent.id;
            return weatherCurent;
        }
        return null;

    }
}
