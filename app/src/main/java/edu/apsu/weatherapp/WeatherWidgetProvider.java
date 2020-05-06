package edu.apsu.weatherapp;

import android.app.Application;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class WeatherWidgetProvider extends AppWidgetProvider {




    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;
        int defaultCity = setCities(context);
            for (int i = 0; i < count; i++) {
                int widgetId = appWidgetIds[i];

                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                        R.layout.widget_layout);
                CityInfo city = new CityInfo(defaultCity, context);
                city.execute();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                remoteViews.setTextViewText(R.id.degreesTV, (city.temp[0])+"°");// change to city get degrees
                remoteViews.setTextViewText(R.id.cityName, city.name + " ," + city.country);// change to city get cityName
                remoteViews.setImageViewResource(R.id.weatherImage, setPic(city));
                Log.i("Worked", String.valueOf(city.temp[0]));
                Intent intent = new Intent(context, WeatherWidgetProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.widlay, pendingIntent);
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
    }

    private int setCities(Context context)  {
        BufferedReader scan = null;
        int defaultCity = -1;
        String approved = "";
        String buffer;
        try {
            scan = new BufferedReader(new InputStreamReader((context.getAssets().open("defaultcity.txt"))));
            buffer = (scan.readLine());
            for(int i = 0; i < buffer.length(); i++){
                if(Character.isDigit(buffer.charAt(i))){
                    approved += buffer.charAt(i);
                }
            }
            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        defaultCity = Integer.valueOf(approved);
        return defaultCity;

    }

    private int setPic(CityInfo city){

        String[] condition;
        condition = city.weather_main;
        if(condition[0].equals("Thunderstorm")){
            return R.drawable.thunderstorm;
        }
        else if(condition[0].equals("Drizzle")){
            return R.drawable.cloudy_with_showers;
        }
        else if(condition[0].equals("Rain")){
            return R.drawable.cloudy_with_showers;
        }
        else if(condition[0].equals("Snow")){
            return R.drawable.snow;
        }
        else if(condition[0].equals("Clear")){
            return R.drawable.sunny;
        }
        else if(condition[0].equals("Clouds")){
            return R.drawable.cloudy;
        }

        return R.drawable.sunny;
    }

}
