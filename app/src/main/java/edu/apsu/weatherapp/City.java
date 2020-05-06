package edu.apsu.weatherapp;

public class City {
    int city_id;
    String city_location_name;

    public City(int city_id, String city_location_name) {
        this.city_id = city_id;
        this.city_location_name = city_location_name;
    }

    public int getCity_id() {
        return city_id;
    }

    public String getCity_location_name() {
        return city_location_name;
    }
}
