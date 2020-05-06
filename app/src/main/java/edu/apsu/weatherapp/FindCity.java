package edu.apsu.weatherapp;

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

import static edu.apsu.weatherapp.LocationSearch.context;

public class FindCity extends AsyncTask<Void, Void, ArrayList<City>> {

    private URL url;
    private String api_key = context.getResources().getString(R.string.api_key);
    public static ArrayList<City> cities;

    public FindCity(String search_term) {
        Uri.Builder builder = Uri.parse("https://api.openweathermap.org/data/2.5/find?").buildUpon();
        builder.appendQueryParameter("q", search_term);
        builder.appendQueryParameter("cnt", "15");
        builder.appendQueryParameter("appid", api_key);

        try {
            url = new URL(builder.toString());
            Log.i("URL", String.valueOf(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ArrayList<City> doInBackground(Void... voids) {
        StringBuilder json = new StringBuilder();
        cities = new ArrayList<>();

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
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = list.getJSONObject(i);
                String name = item.getString("name");

                JSONObject sys = item.getJSONObject("sys");
                String country = sys.getString("country");
                int city_id = item.getInt("id");
                Log.i("NAME", name + ", " + country);
                City city = new City(city_id, name + ", " + country);
                cities.add(city);
            }

            connection.disconnect();

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return cities;
    }

    @Override
    protected void onPostExecute(ArrayList<City> s) {
        cities = s;
        LocationSearch.findCity = null;

        LocationSearch.recyclerView.setAdapter(LocationSearch.weatherAdapter);
    }
}
