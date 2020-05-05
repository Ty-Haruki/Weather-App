package edu.apsu.weatherapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SavedCities extends Activity {
    ArrayList<Integer> cities;
    RecyclerView rv;
    CityAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCities();
        setContentView(R.layout.saved_cities);
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
