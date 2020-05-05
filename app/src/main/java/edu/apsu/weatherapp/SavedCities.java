package edu.apsu.weatherapp;

import android.app.Activity;
import android.content.DialogInterface;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class SavedCities extends Activity implements View.OnClickListener {
    ArrayList<Integer> cities;
    RecyclerView rv;
    CityAdapter adapter;
    Button deleteButton;
    Button defaultButton;
    int defaultCity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCities();
        setDefaultCity();
        setContentView(R.layout.saved_cities);
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);
        defaultButton = findViewById(R.id.defaultButton);
        defaultButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        rv = findViewById(R.id.savedRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        adapter = new CityAdapter();
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.deleteButton) {
            deleteMeals();
        }
        else{
            setDefault();
        }
    }

    public class CityAdapter extends RecyclerView.Adapter<CityViewHolder>{

        @NonNull
        @Override
        public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view, parent, false);
            CityViewHolder viewHolder = new CityViewHolder(textView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
            TextView view = holder.getView();
            view.setText(cities.get(position));
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }
    }

    public class CityViewHolder extends RecyclerView.ViewHolder{

        private TextView view;

        public CityViewHolder(@NonNull TextView itemView) {
            super(itemView);
            this.view = itemView;
        }

        public TextView getView(){
            return view;
        }
    }

    private void setCities()  {
        Scanner scan = null;
        try {
            scan = new Scanner(new File("raw/cities.txt"));
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

    private void setDefaultCity()  {
        Scanner scan = null;
        try {
            scan = new Scanner(new File("raw/defaultcity.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(!scan.hasNextLine()){
            defaultCity = (4859268);
        }
        else{
            defaultCity = (Integer.getInteger(scan.nextLine()));
        }
    }

    private void deleteMeals(){
        if(cities.size() == 1){
            Toast.makeText(getApplicationContext(), "Cannot delete last saved city", Toast.LENGTH_SHORT).show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Remove City");

            String[] listOfCites = new String[cities.size()];
            for (int i = 0; i < cities.size(); i++) {
                listOfCites[i] = cities.get(i).toString();
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
                    PrintWriter pw;
                    try {
                        pw = new PrintWriter(new File("raw/cities.txt"));
                        pw.write("");
                        for(int j = 0; j < cities.size(); j++){
                            pw.append(cities.get(j).toString());
                        }
                        pw.close();
                    } catch (FileNotFoundException e) {
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
                            pw = new PrintWriter(new File("raw/defaultcity.txt"));
                            pw.write(defaultCity);
                            pw.close();
                        } catch (FileNotFoundException e) {
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
                listOfCites[i] = cities.get(i).toString();
            }
            final int choice = -1;
            builder.setSingleChoiceItems(listOfCites, choice, null);

            builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ListView listView = ((AlertDialog) dialogInterface).getListView();
                    defaultCity = cities.get(listView.getCheckedItemPosition());
                    PrintWriter pw;
                    try {
                        pw = new PrintWriter(new File("raw/defaultcity.txt"));
                        pw.write(defaultCity);
                        pw.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            builder.setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
