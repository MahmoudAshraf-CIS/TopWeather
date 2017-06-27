package com.example.mannas.topweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mannas.topweather.content.WeatherFuture;
import com.example.mannas.topweather.content.WeatherFuture_loader;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mannas on 6/24/2017.
 */

public class FutureFragment extends Fragment implements LoaderManager.LoaderCallbacks<WeatherFuture> ,SharedPreferences.OnSharedPreferenceChangeListener{
    WeatherFuture weatherFuture;
    int mWeatherFutureLoaderID =1;
    Integer i =9;
    boolean binded = false;
    @BindView(R.id.chart1) LineChart chart1;
    @BindView(R.id.city) TextView city;
    @BindView(R.id.weather_icon) ImageView weather_icon;
    @BindView(R.id.weather_main) TextView weather_main;
    @BindView(R.id.weather_description) TextView weather_description;
    @BindView(R.id.main_temp) TextView main_temp;
    @BindView(R.id.last_update) TextView last_update;
    @BindView(R.id.wind) TextView wind;
    @BindView(R.id.pressure) TextView pressure;
    @BindView(R.id.humidity) TextView humidity;
     MaterialDialog builder;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().restartLoader(mWeatherFutureLoaderID,null,this).forceLoad();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.future_fragment,container,false);
        binded = false;
        ButterKnife.bind(this,view);
        binded = true;
        city.setText(SharedPreferenceManager.getCity(getContext()));
        return view;
    }

    @Override
    public Loader<WeatherFuture> onCreateLoader(int id, Bundle args) {
        if(id == mWeatherFutureLoaderID){
            builder = new MaterialDialog.Builder( getContext())
                    .title("Downloading ")
                    .content("please wait").cancelable(false)
                    .progress(true, 100).show();

            return new WeatherFuture_loader(getContext());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<WeatherFuture> loader, WeatherFuture data) {
        weatherFuture = data;
        builder.dismiss();
        if(binded && data!=null ){
            chart1.setDrawGridBackground(false);
            chart1.getDescription().setEnabled(false);
            chart1.setDrawBorders(false);
            chart1.getAxisLeft().setEnabled(false);
            chart1.getAxisLeft().setDrawGridLines(false);
            chart1.getAxisRight().setEnabled(false);
            chart1.getAxisRight().setDrawGridLines(false);

            chart1.getXAxis().setDrawAxisLine(false);
            chart1.getXAxis().setDrawGridLines(false);
            // enable touch gestures
            chart1.setTouchEnabled(true);
            // enable scaling and dragging
            chart1.setDragEnabled(true);
            chart1.setScaleEnabled(false);
            // if disabled, scaling can be done on x- and y-axis separately
            chart1.setPinchZoom(false);
            chart1.enableScroll();
            chart1.animateX(3000);

            List<Entry> max = new ArrayList<Entry>();
            List<Entry> min = new ArrayList<Entry>();
            for(float i=0;i<weatherFuture.list.size();i++){
                WeatherFuture.Item.Temp t = weatherFuture.list.get(((int) i)).temp;
                Entry c1e1 = new Entry(i*2, Float.parseFloat(t.max)); // 0 == quarter 1
                max.add(c1e1);
                Entry c2e1 = new Entry(i*2, Float.parseFloat(t.min)); // 0 == quarter 1
                min.add(c2e1);
            }


            LineDataSet setComp1 = new LineDataSet(max, "Max");
            setComp1.setColor(R.color.max_line_color);
            setComp1.setCircleColor(R.color.max_circule_color);
            setComp1.setFillColor(R.color.max_line_color);
            setComp1.setDrawFilled(true);
            setComp1.setMode(LineDataSet.Mode.CUBIC_BEZIER);


            LineDataSet setComp2 = new LineDataSet(min, "Min");
            setComp2.setColor(R.color.min_line_color);
            setComp2.setCircleColor(R.color.min_circule_color);
            setComp2.setFillColor(R.color.min_line_color);
            setComp2.setDrawFilled(true);
            setComp2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(setComp1);
            dataSets.add(setComp2);
            LineData cdata = new LineData(dataSets);

            chart1.setData(cdata);
            chart1.setVisibleXRangeMaximum(10.0f);
            chart1.getXAxis().setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return getDate(Long.parseLong(weatherFuture.list.get(((int) value/2)).dt));
                }

            });
            chart1.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    Integer i = Math.round(h.getX());
                    i/=2;
                    city.setText(SharedPreferenceManager.getCity(getContext()));
                    Picasso.with(getContext()).load("http://openweathermap.org/img/w/" + weatherFuture.list.get(i).weather.get(0).icon + ".png").error(R.drawable.error).into(weather_icon);
                    weather_main.setText(weatherFuture.list.get(i).weather.get(0).main);
                    weather_description.setText(weatherFuture.list.get(i).weather.get(0).description);
                    main_temp.setText(weatherFuture.list.get(i).temp.eve+ "\u00b0" + "C");
                    last_update.setText(getLastUpdate(Long.parseLong(weatherFuture.list.get(i).dt)));
                    wind.setText(weatherFuture.list.get(i).speed + " Meter/H");
                    pressure.setText(getAirPressure_atm(weatherFuture.list.get(i).pressure));
                    humidity.setText(weatherFuture.list.get(i).humidity+" %");
                }

                @Override
                public void onNothingSelected() {

                }
            });
            chart1.invalidate(); // refresh

        }
    }

    @Override
    public void onLoaderReset(Loader<WeatherFuture> loader) {

    }

    public String getDate(Long UNIX_timeStamp){
        Date date = new Date(UNIX_timeStamp*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }

    public String getLastUpdate(Long UNIX_timeStamp){
        Date date = new Date(UNIX_timeStamp*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm a");
        sdf.setTimeZone(TimeZone.getDefault());
        return "Refresh Time "+sdf.format(date);
    }
    public String getAirPressure_atm(String airPressure_hpa){
        Double hpa = Double.parseDouble(airPressure_hpa);
        hpa*=0.00098692326671601;
        return hpa.toString().substring(0,3)+" atm";
    }
    public String getVisibility_mile(String meters){
        Double v = Double.parseDouble(meters);
        v*=0.000621371192;
        return v.toString().substring(0,3)+" Miles";

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals(SharedPreferenceManager.Location_lat_key)){
                getLoaderManager().restartLoader(mWeatherFutureLoaderID,null,this).forceLoad();
            }
    }
}
