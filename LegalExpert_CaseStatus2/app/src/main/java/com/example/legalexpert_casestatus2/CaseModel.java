package com.example.legalexpert_casestatus2;

public class CaseModel {

    String caseDescription, caseName , caseType , documentUrl ;

    CaseModel(){

    }

    public CaseModel(String caseDescription,String caseName,String caseType,String documentUrl){
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
