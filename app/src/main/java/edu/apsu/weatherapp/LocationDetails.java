package edu.apsu.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocationDetails extends AppCompatActivity {

    public static CityInfo cityInfo;
    public int id;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_day_layout);
        context = getApplicationContext();

        id = getIntent().getIntExtra("CITY_ID", 0);
        cityInfo = (CityInfo) new CityInfo(id, getApplicationContext()).execute();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Set City name and Country
        TextView name = findViewById(R.id.location_name);
        name.setText(cityInfo.name + ", " + cityInfo.country);

        // Set Temperature
        TextView temp = findViewById(R.id.location_temp);
        temp.setText(cityInfo.temp[0] +"° F");

        // Set Weather Description
        TextView desc = findViewById(R.id.location_weather);
        desc.setText(String.valueOf(cityInfo.weather_desc[0]).toUpperCase());

        // Set Date Time
        TextView date_time = findViewById(R.id.location_date_time);
        String date = new SimpleDateFormat("E, dd MMM HH:mm z", Locale.getDefault()).format(new Date());
        date_time.setText(date);

        // Set Weather Img
        ImageView location_img = findViewById(R.id.location_weather_img);
        location_img.setImageResource(cityInfo.setPic(0, cityInfo));

        findViewById(R.id.add_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutputStreamWriter outputStreamWriter;
                try {
                    outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("cities.txt", Context.MODE_APPEND));
                    outputStreamWriter.append(id+"\n");
                    outputStreamWriter.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), SavedCities.class);
                startActivity(intent);
            }
        });
    }

}
