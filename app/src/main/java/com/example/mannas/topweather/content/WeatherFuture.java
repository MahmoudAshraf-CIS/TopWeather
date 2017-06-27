package com.example.mannas.topweather.content;

import java.util.List;

/**
 * Created by Mannas on 6/24/2017.
 */

public class WeatherFuture {
    public City city;
    public String cod;
    public String message;
    public String cnt;
    public List<Item> list;

    public class Item{
        public String dt;
        public Temp temp;
        public String pressure;
        public String humidity;
        public List<Weather> weather;
        public String speed;
        public String deg;
        public String rain;

        public class Temp{
            public String day;
            public String min;
            public String max;
            public String night;
            public String eve;
            public String morn;
        }
        public class Weather{
            public String id;
            public String main;
            public String description;
            public String icon;
        }
    }
    public class City{
        public String id;
        public String name;
        public WeatherCurent.Coord coord;
        public String country;
        public String population;
    }
}
