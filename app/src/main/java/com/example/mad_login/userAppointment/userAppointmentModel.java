package com.example.mad_login.userAppointment;

public class userAppointmentModel {


    private String appointmentId;
    private String caseId;
    private String date;
    private String lawyerId ;
    private String status;
    private String time;
    private String userId;

    private String userName;
    private String caseName;
    public userAppointmentModel() {
    }

    public userAppointmentModel(String appointmentId, String caseId, String date, String lawyerId, String status, String time, String userId, String userName, String caseName) {
        this.appointmentId = appointmentId;
        this.caseId = caseId;
        this.date = date;
        this.lawyerId = lawyerId;
        this.status = status;
        this.time = time;
        this.userId = userId;
        this.userName = userName;
        this.caseName =caseName;

    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(String lawyerId) {
        this.lawyerId = lawyerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }
}
