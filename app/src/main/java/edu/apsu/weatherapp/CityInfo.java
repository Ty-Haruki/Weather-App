package edu.apsu.weatherapp;

public class CityInfo {
    private String city;
    private int degrees;
    private final String file = "cities.txt";
    private String condition;

    public CityInfo(String city) {
        this.city = city;
        this.degrees = searchDegrees();
        this.condition = searchCondition();
    }

    public String getCity() {
        return city;
    }

    public int getDegrees() {
        return degrees;
    }

    public String getCondition(){
        return this.condition;
    }

    public int searchDegrees(){
        // uses city name and searches for the degrees currently for that city.
        return 50;
    }

    public String searchCondition(){
        // uses city name and searches for the condition of that city.
        return "Sunny";
    }
}
