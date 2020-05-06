package edu.apsu.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Set ID based on Saved City
        CityInfo cityInfo = new CityInfo(4613868, getApplicationContext());
        cityInfo.execute();

        // Wait for Ci.tyInfo to finish
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.i("INFO", cityInfo.name + ", " + cityInfo.country);
        TextView location = findViewById(R.id.location);
        location.setText(cityInfo.name + ", " + cityInfo.country);
        TextView current_temp = findViewById(R.id.current_temp);
        current_temp.setText(cityInfo.temp[0] + "° F");
        TextView weather_desc = findViewById(R.id.weather_desc);
        weather_desc.setText(String.valueOf(cityInfo.weather_desc[0]).toUpperCase());

        findViewById(R.id.location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocationDetails.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.location_management).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocationSearch.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.lookUpCities).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SavedCities.class);
                startActivity(intent);
            }
        });
    }
}
