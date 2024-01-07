package com.example.mad_login;

public class CaseModel4 {

    String caseDescription, caseName , caseType , documentUrl ;

    CaseModel4(){

    }

    public CaseModel4(String caseDescription, String caseName, String caseType, String documentUrl){
        this.caseDescription = caseDescription;
        this.caseName = caseName ;
        this.caseType = caseType ;
       this.documentUrl = documentUrl ;


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

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }
}
