package com.example.mad_login;
public class Case {

    private String caseName;
    private String caseDescription,documentUrl;
    private String caseType;

    // Required default constructor for Firebase
    public Case() {
        // Default constructor required for calls to DataSnapshot.getValue(Case.class)
    }

    public Case(String caseName, String caseDescription, String caseType,String documentUrl) {
        this.caseName = caseName;
        this.caseDescription = caseDescription;
        this.caseType = caseType;
        this.documentUrl =documentUrl;
    }

    public String documentUrl() {
        return documentUrl;
    }

    public void documentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseDescription() {
        return caseDescription;
    }

    public void setCaseDescription(String caseDescription) {
        this.caseDescription = caseDescription;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }
}
