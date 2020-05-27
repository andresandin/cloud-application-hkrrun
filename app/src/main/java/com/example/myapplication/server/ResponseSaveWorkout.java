package com.example.myapplication.server;

public class ResponseSaveWorkout {

    private String status;
    private String message;
    private float distance;

    public ResponseSaveWorkout(){this("", "", 0);}

    public ResponseSaveWorkout(String status, String message, float distance) {
        this.status = status;
        this.message = message;
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
