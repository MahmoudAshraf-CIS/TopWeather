package com.example.mannas.topweather.content;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Created by Mannas on 6/24/2017.
 */

public class WeatherCurent {
    public Coord coord;
    public Sys sys;
    public List<Weather> weather;
    public String base;
    public Main main;
    public String visibility;
    public Wind wind;
    public String dt;
    public String id;
    public String name;
    public String cod;

    public class Coord{
        public Double lon;
        public Double lat;
    }
    public class Sys {
        public int type;
        public int id;
        public String message;
        public String country;
        public String sunrise;
        public String sunset;
    }
    public class Weather{
        public String id;
        public String main;
        public String description;
        public String icon;
    }
    public class Main{
        public String temp;
        public String humidity;
        public String pressure;
        public String temp_min;
        public String temp_max;
    }
    public class Wind{
        public Double speed;
        public Double deg;
    }

}
