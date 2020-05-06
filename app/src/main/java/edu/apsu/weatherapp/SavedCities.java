package edu.apsu.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class SavedCities extends Activity implements View.OnClickListener {
    ArrayList<CityInfo> cities;
    RecyclerView rv;
    CityAdapter adapter;
    Button deleteButton;
    Button defaultButton;
    CityInfo defaultCity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCities(getApplicationContext());
        defaultCity = setDefaultCity(getApplicationContext());
        setContentView(R.layout.saved_cities);
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);
        defaultButton = findViewById(R.id.defaultButton);
        defaultButton.setOnClickListener(this);
        rv = findViewById(R.id.savedRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        adapter = new CityAdapter();
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.deleteButton) {
            deleteCity();
        }
        else{
            setDefault();
        }
    }

    interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    public class CityAdapter extends RecyclerView.Adapter<CityViewHolder>
            implements RecyclerViewClickListener, LocationSearch.RecyclerViewClickListener {

        @NonNull
        @Override
        public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view, parent, false);
            CityViewHolder viewHolder = new CityViewHolder(textView, this);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
            holder.view.setText(cities.get(position).name+"   "+cities.get(position).temp[0]+"°");
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }

        @Override
        public void onClick(View view, int position) {
            Intent intent = new Intent(getApplicationContext(), LocationDetails.class);
            intent.putExtra("CITY_ID", cities.get(position).city_id);
            startActivity(intent);
        }
    }

    public class CityViewHolder extends RecyclerView.ViewHolder{

        public TextView view;
        public LocationSearch.RecyclerViewClickListener listener;

        public CityViewHolder(final TextView view, final CityAdapter listener) {
            super(view);
            this.view = view;
            this.listener = listener;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(view, getAdapterPosition());
                }
            });
        }
    }

    private void setCities(Context context)  {
        cities = new ArrayList<>();
        FileInputStream fi;
        Scanner scan = null;
        String approved = "";
        String buffer = "";
        try {
            fi = context.openFileInput("cities.txt");
            scan = new Scanner(fi);
            while(scan.hasNextLine()){
                buffer = (scan.nextLine());
                for(int i = 0; i < buffer.length(); i++){
                    if(Character.isDigit(buffer.charAt(i))){
                        approved += buffer.charAt(i);
                    }
                }
                CityInfo info = new CityInfo(Integer.valueOf(approved), getApplicationContext());
                info.execute();
                cities.add(info);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                approved="";
                buffer = null;
            }
            scan.close();
            fi.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CityInfo setDefaultCity(Context context)  {
        Scanner scan = null;
        int defaultCity = -1;
        String approved = "";
        String buffer;
        FileInputStream fi;
        try {
            fi = context.openFileInput("defaultcity.txt");
            scan = new Scanner(fi);
            buffer = (scan.nextLine());
            for(int i = 0; i < buffer.length(); i++){
                if(Character.isDigit(buffer.charAt(i))){
                    approved += buffer.charAt(i);
                }
            }
            scan.close();
            fi.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        defaultCity = Integer.valueOf(approved);
        return new CityInfo(defaultCity, getApplicationContext());
    }

    private void deleteCity(){
        if(cities.size() == 1){
            Toast.makeText(getApplicationContext(), "Cannot delete last saved city", Toast.LENGTH_SHORT).show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Remove City");

            String[] listOfCites = new String[cities.size()];
            for (int i = 0; i < cities.size(); i++) {
                listOfCites[i] = cities.get(i).name;
            }
            final int choice = -1;
            builder.setSingleChoiceItems(listOfCites, choice, null);

            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ListView listView = ((AlertDialog) dialogInterface).getListView();
                    cities.remove(listView.getCheckedItemPosition());
                    adapter.notifyItemChanged(listView.getCheckedItemPosition());
                    adapter.notifyItemRangeChanged(listView.getCheckedItemPosition(), cities.size());
                    // rewrite file with new items
                    try {
                        FileOutputStream fo = getApplicationContext().openFileOutput("cities.txt", Context.MODE_PRIVATE);
                        PrintStream ps = new PrintStream(fo);

                        for(int j = 0; j < cities.size(); j++){
                            ps.println((cities.get(j).city_id));
                        }
                        ps.close();
                        fo.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    boolean defaultDeleted = true;
                    for(int j = 0; j < cities.size(); j++){
                        if(cities.get(j) == defaultCity){
                            defaultDeleted = false;
                            break;
                        }
                    }
                    if(defaultDeleted){
                        defaultCity = cities.get(0);
                        try {
                            FileOutputStream fo = getApplicationContext().openFileOutput("defaultcity.txt", Context.MODE_PRIVATE);
                            PrintStream ps = new PrintStream(fo);
                            ps.println((defaultCity.city_id));

                            ps.close();
                            fo.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            builder.setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void setDefault(){
        if(cities.size() == 1){
            Toast.makeText(getApplicationContext(), "Cannot change widget city", Toast.LENGTH_SHORT).show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Set Widget City");

            String[] listOfCites = new String[cities.size()];
            for (int i = 0; i < cities.size(); i++) {
                listOfCites[i] = cities.get(i).name;
            }
            final int choice = -1;
            builder.setSingleChoiceItems(listOfCites, choice, null);

            builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ListView listView = ((AlertDialog) dialogInterface).getListView();
                    defaultCity = cities.get(listView.getCheckedItemPosition());
                    try {
                        FileOutputStream fo = getApplicationContext().openFileOutput("defaultcity.txt", Context.MODE_PRIVATE);
                        PrintStream ps = new PrintStream(fo);
                        ps.println((defaultCity.city_id));

                        ps.close();
                        fo.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            builder.setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public int getDefault(Context context) {
        Scanner scan = null;
        int defaultCity = -1;
        String approved = "";
        String buffer;
        FileInputStream fi;
        try {
            fi = context.openFileInput("defaultcity.txt");
            scan = new Scanner(fi);
            buffer = (scan.nextLine());
            for(int i = 0; i < buffer.length(); i++){
                if(Character.isDigit(buffer.charAt(i))){
                    approved += buffer.charAt(i);
                }
            }
            scan.close();
            fi.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        defaultCity = Integer.valueOf(approved);
        return defaultCity;
    }
}
