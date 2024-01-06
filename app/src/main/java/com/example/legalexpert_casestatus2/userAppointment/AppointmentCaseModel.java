package com.example.legalexpert_casestatus2.userAppointment;

public class AppointmentCaseModel {
    private String lawyerId;
    private String userId;
    private String caseId;
    private String status;

    private String date;
    private String time;
    private String caseName ;

    public AppointmentCaseModel() {
    }

    public AppointmentCaseModel(String lawyerId, String userId, String caseId, String status, String date, String time,String caseName) {
        this.lawyerId = lawyerId;
        this.userId = userId;
        this.caseId = caseId;
        this.status = status;
        this.date = date;
        this.time = time;
        this.caseName = caseName;
    }

    public String getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(String lawyerId) {
        this.lawyerId = lawyerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }
}
