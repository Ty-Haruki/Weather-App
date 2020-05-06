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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
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
        File defaultfile = new File(context.getFilesDir(), "defaultcity.txt");
        File cityfile = new File(context.getFilesDir(), "cities.txt");
        if (!defaultfile.exists()) {
            Log.i("new file", "new file");
            try {
                FileOutputStream fo = context.openFileOutput("defaultcity.txt", Context.MODE_PRIVATE);
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
                FileOutputStream fo = context.openFileOutput("cities.txt", Context.MODE_PRIVATE);
                PrintStream ps = new PrintStream(fo);
                ps.println(4613868);
                ps.close();
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileInputStream fi;
        int defaultCity = -1;
        String approved = "";
        String buffer;
        try {
            fi = context.openFileInput("cities.txt");
            Scanner isr =  new Scanner(fi);
            buffer = (isr.nextLine());
            for(int i = 0; i < buffer.length(); i++){
                if(Character.isDigit(buffer.charAt(i))){
                    approved += buffer.charAt(i);
                }
            }
            isr.close();
            fi.close();
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
