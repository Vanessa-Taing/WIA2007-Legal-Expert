package com.example.mad_login;

public class Report {

    private String Report;
    private String LawyerID;

    private String UserID;

    // Required default constructor for Firebase
    public Report() {
        // Default constructor required for calls to DataSnapshot.getValue(Case.class)
    }

    public Report(String Report, String LawyerID, String UserID) {
        this.Report = Report;
        this.LawyerID = LawyerID;
        this.UserID = UserID;
    }
}
