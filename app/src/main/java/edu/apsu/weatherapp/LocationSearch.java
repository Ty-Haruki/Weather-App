package edu.apsu.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static edu.apsu.weatherapp.FindCity.cities;

public class LocationSearch extends AppCompatActivity {
    // Class for handling searching for and adding locations to user list

    public static Context context;
    public static RecyclerView recyclerView;
    public static FindCity findCity;
    public static WeatherAdapter weatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_search);
        context = getApplicationContext();
        weatherAdapter = new WeatherAdapter();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        findViewById(R.id.search_location).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {

                        EditText editText = findViewById(R.id.search_location);
                        String search_term = editText.getText().toString();
                        findCity = (FindCity) new FindCity(search_term).execute();
                    }
                }
                return false;
            }


        });

        findViewById(R.id.saved_locations).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SavedCities.class);
                startActivity(intent);
            }
        });
    }

    interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder>
            implements RecyclerViewClickListener {

        @NonNull
        @Override
        public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view, parent, false);
            WeatherViewHolder viewHolder = new WeatherViewHolder(textView, this);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
            holder.view.setText(cities.get(position).getCity_location_name());
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }

        @Override
        public void onClick(View view, int position) {
            Intent intent = new Intent(getApplicationContext(), LocationDetails.class);
            intent.putExtra("CITY_ID", cities.get(position).getCity_id());
            startActivity(intent);
        }
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        public TextView view;
        public RecyclerViewClickListener listener;

        public WeatherViewHolder(final TextView view, final WeatherAdapter listener) {
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

}
