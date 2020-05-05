package edu.apsu.weatherapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

// Class that handles Weather by individual IDs

public class CityInfo extends AsyncTask<Void, Void, ArrayList<String>> {
    private URL url;
    private String api_key;

    // Info
    public String name, country;

    // Main Weather
    public String[] date;
    public int[] temp, feels_like;
    public int[] humidity;

    // Weather Condition
    public String[] weather_desc;

    // Wind
    public double[] wind_speed;
    public int[] wind_direction;

    public CityInfo(int city_id, Context context) {
        api_key = context.getResources().getString(R.string.api_key);
        Uri.Builder builder = Uri.parse("https://api.openweathermap.org/data/2.5/forecast?").buildUpon();
        builder.appendQueryParameter("id", String.valueOf(city_id));
        builder.appendQueryParameter("units", "imperial");
        builder.appendQueryParameter("appid", api_key);

        date = new String[5];
        temp = new int[5];
        feels_like = new int[5];
        humidity = new int[5];
        weather_desc = new String[5];
        wind_speed = new double[5];
        wind_direction = new int[5];

        try {
            url = new URL(builder.toString());
            Log.i("URL", String.valueOf(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        StringBuilder json = new StringBuilder();

        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }

            JSONObject reader = new JSONObject(json.toString());
            JSONArray list = reader.getJSONArray("list");
            int count = 1;

            JSONObject info = reader.getJSONObject("city");
            name = info.getString("name");
            country = info.getString("country");

            for (int i = 0; i < list.length(); i++) {
                JSONObject item = list.getJSONObject(i);
                JSONObject main = item.getJSONObject("main");
                JSONArray weatherArray = item.getJSONArray("weather");
                JSONObject weather = weatherArray.getJSONObject(0);
                JSONObject wind = item.getJSONObject("wind");
                if (i == 0) {
                    date[0] = item.getString("dt_txt");
                    temp[0] = main.getInt("temp");
                    feels_like[0] = main.getInt("feels_like");
                    humidity[0] = main.getInt("humidity");
                    weather_desc[0] = weather.getString("description");
                    wind_speed[0] = wind.getDouble("speed");
                    wind_direction[0] = wind.getInt("deg");
                    Log.i("TEST", date[0] + ", " + temp[0] + ", " + feels_like[0] + ", " + humidity[0] + ", " + weather_desc[0] + ", " + wind_speed[0] + ", " + wind_direction[0]);
                }
                if (item.getString("dt_txt").contains("00:00:00") && count < 5) {
                    date[count] = item.getString("dt_txt");
                    temp[count] = main.getInt("temp");
                    feels_like[count] = main.getInt("feels_like");
                    humidity[count] = main.getInt("humidity");
                    weather_desc[count] = weather.getString("description");
                    wind_speed[count] = wind.getDouble("speed");
                    wind_direction[count] = wind.getInt("deg");
                    Log.i(name + " " + country, date[count] + ", " + temp[count] + ", " + feels_like[count] + ", " + humidity[count] + ", " + weather_desc[count] + ", " + wind_speed[count] + ", " + wind_direction[count]);
                    count++;
                }
            }

            connection.disconnect();

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        return;
    }
}
