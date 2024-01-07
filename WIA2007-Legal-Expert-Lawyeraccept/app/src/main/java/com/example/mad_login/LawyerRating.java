package com.example.mad_login;

import com.example.mad_login.Model.User;

public class LawyerRating {
    private String OverallEXP;
    private String LawyerEXP;
    private float Rating;

    private String UserID;

    // Required default constructor for Firebase
    public LawyerRating() {
        // Default constructor required for calls to DataSnapshot.getValue(Case.class)
    }

    public LawyerRating(String OverallEXP, String LawyerEXP, float Rating, String UserID) {
        this.OverallEXP = OverallEXP;
        this.LawyerEXP = LawyerEXP;
        this.Rating = Rating;
        this.UserID = UserID;
    }

    public String getOverallEXP() {
        return OverallEXP;
    }

    public void setOverallEXP(String overallEXP) {
        this.OverallEXP = overallEXP;
    }

    public String getLawyerEXP() {
        return LawyerEXP;
    }

    public void setLawyerEXP(String lawyerEXP) {
        this.LawyerEXP = lawyerEXP;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        this.Rating = rating;
    }
    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getUserID(){return this.UserID;}
}


