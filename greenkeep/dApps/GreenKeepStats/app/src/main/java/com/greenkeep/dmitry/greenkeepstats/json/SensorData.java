package com.greenkeep.dmitry.greenkeepstats.json;

/**
 * Created by Dmitry on 8/5/2018.
 */

public class SensorData {
    Integer id;
    Integer distance_traveled_per_day;
    Integer weight;
    Integer age;
    Integer batchdate;
    String location;

    public SensorData() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDistance_traveled_per_day() {
        return distance_traveled_per_day;
    }

    public void setDistance_traveled_per_day(Integer distance_traveled_per_day) {
        this.distance_traveled_per_day = distance_traveled_per_day;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getBatchdate() {
        return batchdate;
    }

    public void setBatchdate(Integer batchdate) {
        this.batchdate = batchdate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "DT: " + distance_traveled_per_day + " Weight: " + weight + " Age: " + age + " BatchDate: " +batchdate + " Location:" + location;
    }
}