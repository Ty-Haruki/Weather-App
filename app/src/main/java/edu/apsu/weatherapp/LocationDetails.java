package edu.apsu.weatherapp;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LocationDetails extends AppCompatActivity {

    public static CityInfo cityInfo;

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_day_layout);

        context = getApplicationContext();

        cityInfo = (CityInfo) new CityInfo(4613868, getApplicationContext()).execute();

    }

}
