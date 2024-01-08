package com.example.mad_login.Model;

public class FeedbackApp {
    private int id;
    private float rating;
    private String comments;
    private String Uid;
    private long timestamp;


    public FeedbackApp(float rating, String comments, String Uid, long timestamp) {
        this.rating = rating;
        this.comments = comments;
        this.Uid = Uid;
        this.timestamp = timestamp;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
