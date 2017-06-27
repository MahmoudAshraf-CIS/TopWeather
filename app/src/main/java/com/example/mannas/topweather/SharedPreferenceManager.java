package com.example.mannas.topweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Mannas on 6/24/2017.
 */

public class SharedPreferenceManager {

    public static final String Location_lat_key = "Location_lat_key";
    public static final String Location_lng_key = "Location_lng_key";
    public static final String Location_Address_key = "address_key";
    public static final String Location_Id_key= "id_key";
    public static final String Location_Local_key= "local_key";
    public static final String Location_Name_key= "name_key";


    public static LatLng getLatLan(Context context){
        Double lat=0.0,lng=0.0;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        lat = Double.longBitsToDouble(settings.getLong(Location_lat_key, 0));
        lng = Double.longBitsToDouble(settings.getLong(Location_lng_key, 0));
        return new LatLng(lat,lng);
    }
    public static String getLocation_Address(Context context ){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Location_Address_key,null);
    }
    public static String getCity(Context context){
        String add = getLocation_Address(context);
        String[] parts = add.split(",");
        if(parts.length>=3)
            return parts[2];
        if(parts.length>=2)
            return parts[1];
        return add;
    }
    public static String getLocation_Id(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Location_Id_key,null);
    }
    public static String getLocation_Local(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Location_Local_key,null);
    }
    public static String getLocation_Name(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Location_Name_key,null);
    }
    //-------------------------

    public static void putLatLan(Context context, LatLng latLng){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(Location_lat_key, Double.doubleToRawLongBits(latLng.latitude));
        editor.putLong(Location_lng_key, Double.doubleToRawLongBits(latLng.longitude));
        editor.apply();
    }

    public static void putPlace(Context context, Place place){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Location_Address_key,place.getAddress().toString());
        editor.putString(Location_Id_key,place.getId());
        if(place.getLocale()!=null)
        editor.putString(Location_Local_key,place.getLocale().toString());
        editor.putString(Location_Name_key,place.getName().toString());
        editor.putLong(Location_lat_key, Double.doubleToRawLongBits(place.getLatLng().latitude));
        editor.putLong(Location_lng_key, Double.doubleToRawLongBits(place.getLatLng().longitude));
        editor.apply();
    }


}
