package com.example.legalexpert_casestatus2;

public class CaseModel {
    private String caseDescription;
    private String caseName;
    private String caseType;

    private String receiverId;
    private String status;

    private String caseID ;

    private String rejectionReason ;
    private boolean isSentRequest;

    private String currentUserId ;
    private String terminateReason ;

    public CaseModel() {

    }

    public CaseModel(String caseDescription, String caseName, String caseType, String receiverId, String status, String caseID, String rejectionReason, boolean isSentRequest,String currentUserId,String terminateReason) {
        this.caseDescription = caseDescription;
        this.caseName = caseName;
        this.caseType = caseType;
        this.receiverId = receiverId;
        this.status = status;
        this.caseID = caseID;
        this.rejectionReason = rejectionReason;
        this.isSentRequest = isSentRequest;
        this.currentUserId = currentUserId ;
        this.terminateReason = terminateReason ;
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

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    public boolean isSentRequest() {
        return isSentRequest;
    }

    public void setSentRequest(boolean sentRequest) {
        isSentRequest = sentRequest;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getTerminateReason() {
        return terminateReason;
    }

    public void setTerminateReason(String terminateReason) {
        this.terminateReason = terminateReason;
    }
}