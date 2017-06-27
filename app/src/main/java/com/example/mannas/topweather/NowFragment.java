package com.example.mannas.topweather;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mannas.topweather.content.WeatherCurent;
import com.example.mannas.topweather.content.WeatherCurent_loader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Mannas on 6/24/2017.
 */

public class NowFragment extends Fragment implements LoaderManager.LoaderCallbacks<WeatherCurent>,SharedPreferences.OnSharedPreferenceChangeListener {
    private final static int mWeatherLoaderID =0;
    boolean binded =false;

    @BindView(R.id.city) TextView city;
    @BindView(R.id.weather_icon) ImageView weather_icon;
    @BindView(R.id.weather_main) TextView weather_main;
    @BindView(R.id.weather_description) TextView weather_description;
    @BindView(R.id.main_temp) TextView main_temp;
    @BindView(R.id.main_temp_max) TextView main_temp_max;
    @BindView(R.id.main_temp_min) TextView main_temp_min;
    @BindView(R.id.last_update) TextView last_update;
    @BindView(R.id.wind) TextView wind;
    @BindView(R.id.pressure) TextView pressure;
    @BindView(R.id.visibility) TextView visibility;
    @BindView(R.id.humidity) TextView humidity;
    MaterialDialog builder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().restartLoader(mWeatherLoaderID,null,this).forceLoad();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        binded=false;
        View view = inflater.inflate(R.layout.now_fragment,container,false);
        ButterKnife.bind(this,view);
        binded = true;
        final int sdk = android.os.Build.VERSION.SDK_INT;

        return view;
    }



    @Override
    public Loader<WeatherCurent> onCreateLoader(int id, Bundle args) {
        if(id == mWeatherLoaderID){
            builder = new MaterialDialog.Builder( getContext())
                    .title("Downloading ")
                    .content("please wait").cancelable(false)
                    .progress(true, 100).show();

            return new WeatherCurent_loader(getContext());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<WeatherCurent> loader, WeatherCurent data) {
        builder.dismiss();
        if(binded && data !=null){

            city.setText(SharedPreferenceManager.getCity(getContext()));
            if(data.weather!=null &&data.weather.size()!=0){
                Picasso.with(getContext()).load("http://openweathermap.org/img/w/" + data.weather.get(0).icon  + ".png").error(R.drawable.error).into(weather_icon);
                weather_description.setText(data.weather.get(0).description);
                weather_main.setText(data.weather.get(0).main);
            }
            if(data.main!=null){
                main_temp.setText(data.main.temp + "\u00b0" + "C");
                main_temp_max.setText(data.main.temp_max);
                main_temp_min.setText(data.main.temp_min);
                humidity.setText(data.main.humidity+" %");
            }
            last_update.setText(getLastUpdate(Long.parseLong(data.dt)));
            wind.setText(data.wind.speed + " Meter/H");
            pressure.setText(getAirPressure_atm(data.main.pressure));
            visibility.setText(getVisibility_mile(data.visibility));

        }
    }

    @Override
    public void onLoaderReset(Loader<WeatherCurent> loader) {

    }





    public String getLastUpdate(Long UNIX_timeStamp){
        Date date = new Date(UNIX_timeStamp*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm a");
        sdf.setTimeZone(TimeZone.getDefault());
        return "Refresh Time "+sdf.format(date);

    }
    public String getAirPressure_atm(String airPressure_hpa){
        if(airPressure_hpa !=null){
            Double hpa = Double.parseDouble(airPressure_hpa);
            hpa*=0.00098692326671601;
            return hpa.toString().substring(0,3)+" atm";
        }
      return "";
    }
    public String getVisibility_mile(String meters){
        if(meters!=null){
            Double v = Double.parseDouble(meters);
            v*=0.000621371192;
            return v.toString().substring(0,3)+" Miles";
        }
       return "";
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(SharedPreferenceManager.Location_lat_key)){
            getLoaderManager().restartLoader(mWeatherLoaderID,null,this).forceLoad();
        }
    }
}
