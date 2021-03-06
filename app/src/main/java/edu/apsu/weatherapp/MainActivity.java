package edu.apsu.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public CityInfo cityInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocationDetails.class);
                intent.putExtra("CITY_ID", new SavedCities().getDefault(getApplicationContext()));
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

        File defaultfile = new File(getApplicationContext().getFilesDir(), "defaultcity.txt");
        File cityfile = new File(getApplicationContext().getFilesDir(), "cities.txt");
        if (!defaultfile.exists()) {
            Log.i("new file", "new file");
            try {
                FileOutputStream fo = getApplicationContext().openFileOutput("defaultcity.txt", Context.MODE_PRIVATE);
                PrintStream ps = new PrintStream(fo);
                ps.println("4613868");
                ps.close();
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!cityfile.exists()){
            Log.i("new file", "new file");
            try {
                FileOutputStream fo = getApplicationContext().openFileOutput("cities.txt", Context.MODE_PRIVATE);
                PrintStream ps = new PrintStream(fo);
                ps.println("4613868");
                ps.close();
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Set ID based on Saved City
        cityInfo = new CityInfo(new SavedCities().getDefault(getApplicationContext()), getApplicationContext());
        cityInfo.execute();

        // Wait for CityInfo to finish
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setData();

    }

    private void setData() {
        Log.i("INFO", cityInfo.name + ", " + cityInfo.country);
        TextView location = findViewById(R.id.location);
        location.setText(cityInfo.name + ", " + cityInfo.country);

        // Set Date Time
        TextView date_time = findViewById(R.id.date_time);
        String date = new SimpleDateFormat("E, dd MMM HH:mm z", Locale.getDefault()).format(new Date());
        date_time.setText(date);

        // Set Current Temp
        TextView current_temp = findViewById(R.id.current_temp);
        current_temp.setText(cityInfo.temp[0] + "° F");

        // Set Current Weather Desc
        TextView weather_desc = findViewById(R.id.weather_desc);
        weather_desc.setText(String.valueOf(cityInfo.weather_desc[0]).toUpperCase());

        // Set Current Weather Icon
        ImageView current_img = findViewById(R.id.current_img);
        current_img.setImageResource(cityInfo.setPic(0, cityInfo));

        setTemps();
        setHumidity();
        setImages();
    }

    private void setHumidity() {
        // Set Daily Humidity
        TextView[] day_humidity = new TextView[5];
        for (int i = 0; i < 5; i++) {
            int id = getResources().getIdentifier("day" + (i + 1) + "_precip", "id", getApplicationContext().getPackageName());
            day_humidity[i] = findViewById(id);
            day_humidity[i].setText(cityInfo.humidity[i] + "%");
        }
    }

    private void setImages() {
        // Set Daily Images
        ImageView[] day_imageViews = new ImageView[5];
        for (int i = 0; i < 5; i++) {
            int id = getResources().getIdentifier("day" + (i + 1) + "_img", "id", getApplicationContext().getPackageName());
            day_imageViews[i] = findViewById(id);
            int imageResource = cityInfo.setPic(i, cityInfo);
            day_imageViews[i].setImageResource(imageResource);
        }
    }

    private void setTemps() {
        // Set Daily Temps
        TextView[] day_temps = new TextView[5];
        for (int i = 0; i < 5; i++) {
            int id = getResources().getIdentifier("day" + (i + 1) + "_temp", "id", getApplicationContext().getPackageName());
            day_temps[i] = findViewById(id);
            day_temps[i].setText(cityInfo.temp[i] + "° F");
        }
    }

}
