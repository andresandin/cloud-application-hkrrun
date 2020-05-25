package com.example.myapplication.server;

public class RequestSaveWorkout {

    private float distance;
    private int steps;
    private String time;
    private int userId;

    public RequestSaveWorkout(){this (0, "");};

    public RequestSaveWorkout(int steps, String time) {
        this.distance = 0;
        this.steps = steps;
        this.time = time;
        this.userId = 0;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
