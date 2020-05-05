package edu.apsu.weatherapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class WeatherWidgetProvider extends AppWidgetProvider {

    private ArrayList<Integer> cities;


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;
        setCities();

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            int city;
            try{
                city = cities.get(i);
            }
            catch (Exception e){
                city = cities.get(0);
            }
            //CityInfo info = new CityInfo(city);
            //int degrees = info.getDegrees();

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            //remoteViews.setTextViewText(R.id.degreesTV, Integer.toString(degrees));// change to city get degrees
            //remoteViews.setTextViewText(R.id.cityName, "Clarksville, TN, US");// change to city get cityName

            Intent intent = new Intent(context, WeatherWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private void setCities()  {
        Scanner scan = null;
        try {
            scan = new Scanner(new File("cities.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        cities = new ArrayList<>();
        if(!scan.hasNextLine()){
            cities.add(4859268);
        }
        while(scan.hasNextLine()){
            cities.add(Integer.getInteger(scan.nextLine()));
        }
    }

}
