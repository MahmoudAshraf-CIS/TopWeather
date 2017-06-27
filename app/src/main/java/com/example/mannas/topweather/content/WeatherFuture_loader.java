package com.example.mannas.topweather.content;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.mannas.topweather.BuildConfig;
import com.example.mannas.topweather.SharedPreferenceManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Mannas on 6/24/2017.
 */

public class WeatherFuture_loader  extends AsyncTaskLoader<WeatherFuture> {
    private LatLng latLng;
    OkHttpClient client;

    public WeatherFuture_loader(Context context) {
        super(context);
        latLng = SharedPreferenceManager.getLatLan(getContext());
    }
    private String BuildUrl(){
        return  "http://api.openweathermap.org/data/2.5/forecast/daily?" +
                "lat=" + latLng.latitude+
                "&lon=" + latLng.longitude+
                "&APPID="+ BuildConfig.APPID+"&units=metric&cnt=14";
    }
    @Override
    public WeatherFuture loadInBackground() {
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
        //return "{\"city\":{\"id\":2925533,\"name\":\"Frankfurt am Main\",\"coord\":{\"lon\":8.6833,\"lat\":50.1167},\"country\":\"DE\",\"population\":650000},\"cod\":\"200\",\"message\":0.9601421,\"cnt\":14,\"list\":[{\"dt\":1498474800,\"temp\":{\"day\":23.83,\"min\":13.9,\"max\":24.17,\"night\":13.9,\"eve\":22.58,\"morn\":23.87},\"pressure\":993.85,\"humidity\":72,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"speed\":3,\"deg\":318,\"clouds\":36},{\"dt\":1498561200,\"temp\":{\"day\":21.52,\"min\":15.27,\"max\":21.52,\"night\":15.27,\"eve\":19.02,\"morn\":15.65},\"pressure\":990.74,\"humidity\":71,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"speed\":2.46,\"deg\":92,\"clouds\":80,\"rain\":2.92},{\"dt\":1498647600,\"temp\":{\"day\":21.42,\"min\":15.92,\"max\":24.01,\"night\":15.92,\"eve\":23.18,\"morn\":17.55},\"pressure\":980.81,\"humidity\":89,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"speed\":4.17,\"deg\":214,\"clouds\":24,\"rain\":8.51},{\"dt\":1498734000,\"temp\":{\"day\":19.27,\"min\":14.99,\"max\":19.27,\"night\":14.99,\"eve\":16.95,\"morn\":18.72},\"pressure\":976.1,\"humidity\":0,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"speed\":5.16,\"deg\":234,\"clouds\":62,\"rain\":9.7},{\"dt\":1498820400,\"temp\":{\"day\":17.63,\"min\":14.96,\"max\":18.14,\"night\":15.51,\"eve\":18.14,\"morn\":14.96},\"pressure\":981.52,\"humidity\":0,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"speed\":5.74,\"deg\":236,\"clouds\":42,\"rain\":1.65},{\"dt\":1498906800,\"temp\":{\"day\":21,\"min\":14.26,\"max\":21,\"night\":14.26,\"eve\":19.32,\"morn\":15.64},\"pressure\":982.72,\"humidity\":0,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"speed\":3.48,\"deg\":219,\"clouds\":22,\"rain\":4.42},{\"dt\":1498993200,\"temp\":{\"day\":17.65,\"min\":14.14,\"max\":17.65,\"night\":14.14,\"eve\":17.56,\"morn\":14.82},\"pressure\":982.17,\"humidity\":0,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"speed\":4.59,\"deg\":200,\"clouds\":49,\"rain\":8.35},{\"dt\":1499079600,\"temp\":{\"day\":18.56,\"min\":14.32,\"max\":18.56,\"night\":14.32,\"eve\":16.82,\"morn\":14.97},\"pressure\":983.64,\"humidity\":0,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"speed\":4.38,\"deg\":208,\"clouds\":34,\"rain\":8.57},{\"dt\":1499166000,\"temp\":{\"day\":18.98,\"min\":12.97,\"max\":19.19,\"night\":12.97,\"eve\":19.19,\"morn\":15.04},\"pressure\":991.55,\"humidity\":0,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"speed\":3.19,\"deg\":245,\"clouds\":11,\"rain\":3.51},{\"dt\":1499252400,\"temp\":{\"day\":20.19,\"min\":14.46,\"max\":20.19,\"night\":14.46,\"eve\":20.01,\"morn\":16.37},\"pressure\":992.06,\"humidity\":0,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"speed\":2.42,\"deg\":206,\"clouds\":88,\"rain\":2.43},{\"dt\":1499338800,\"temp\":{\"day\":19.58,\"min\":15.73,\"max\":19.58,\"night\":15.73,\"eve\":18.86,\"morn\":17.74},\"pressure\":986.08,\"humidity\":0,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"speed\":3.99,\"deg\":186,\"clouds\":96,\"rain\":7.84},{\"dt\":1499425200,\"temp\":{\"day\":16.9,\"min\":14.79,\"max\":16.9,\"night\":14.79,\"eve\":16.45,\"morn\":15.81},\"pressure\":983.61,\"humidity\":0,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"speed\":4.32,\"deg\":283,\"clouds\":95,\"rain\":9.81},{\"dt\":1499511600,\"temp\":{\"day\":17.22,\"min\":14.62,\"max\":18.38,\"night\":14.62,\"eve\":18.38,\"morn\":14.93},\"pressure\":992.32,\"humidity\":0,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"speed\":4.86,\"deg\":264,\"clouds\":70,\"rain\":4.05},{\"dt\":1499598000,\"temp\":{\"day\":22.88,\"min\":13.43,\"max\":23.41,\"night\":13.43,\"eve\":23.41,\"morn\":16.06},\"pressure\":998.8,\"humidity\":0,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"speed\":2.9,\"deg\":281,\"clouds\":2}]}";

    }

    private WeatherFuture pars(String json) throws JSONException {
        if(json!=null && !json.equals("")){
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(json);
            WeatherFuture weatherFuture = gson.fromJson(jsonObject.toString(),WeatherFuture.class);
            String s = weatherFuture.cnt;
            return weatherFuture;
        }
        return null;

    }
}
