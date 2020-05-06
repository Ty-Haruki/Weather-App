package edu.apsu.weatherapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LocationDetails extends AppCompatActivity {

    public static CityInfo cityInfo;
    public int id;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_day_layout);
        id = getIntent().getIntExtra("CITY_ID", 0);
        context = getApplicationContext();

        cityInfo = (CityInfo) new CityInfo(id, getApplicationContext()).execute();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView name = findViewById(R.id.location_name);
        name.setText(cityInfo.name + ", " + cityInfo.country);
        TextView temp = (TextView) findViewById(R.id.location_temp);
        temp.setText(String.valueOf(cityInfo.temp[0]));
        TextView desc = findViewById(R.id.location_weather);
        desc.setText(String.valueOf(cityInfo.weather_desc[0]).toUpperCase());

        findViewById(R.id.add_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SavedCities().addCity(id);
            }
        });
    }

}
