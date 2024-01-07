package com.example.mad_login;

public class client_CaseModel {


    private String clientName;
    private String clientState;
    private String clientGender;
    private String clientPhone;
    private String caseDescription;
    private String caseName;
    private String caseType;

    private String clientID;
    private String caseID;

    private String status;
    private String rejectionReason;

    public client_CaseModel() {
    }

    public client_CaseModel(String clientName, String clientState, String clientGender, String clientPhone, String caseDescription, String caseName, String caseType, String clientID, String caseID, String status, String rejectionReason) {
        this.clientName = clientName;
        this.clientState = clientState;
        this.clientGender = clientGender;
        this.clientPhone = clientPhone;
        this.caseDescription = caseDescription;
        this.caseName = caseName;
        this.caseType = caseType;
        this.clientID = clientID;
        this.caseID = caseID;
        this.status = status;
        this.rejectionReason = rejectionReason;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientState() {
        return clientState;
    }

    public void setClientState(String clientState) {
        this.clientState = clientState;
    }

    public String getClientGender() {
        return clientGender;
    }

    public void setClientGender(String clientGender) {
        this.clientGender = clientGender;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getCaseDescription() {
        return caseDescription;
    }

    public void setCaseDescription(String caseDescription) {
        this.caseDescription = caseDescription;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
