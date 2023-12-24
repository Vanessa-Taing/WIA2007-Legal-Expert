package com.example.mad_login;

import java.io.Serializable;

public class Case implements Serializable {
    private static final long serialVersionUID = 1L;
    public String caseName,caseDescription,documentUrl, caseType,fileName;

    // Required default constructor for Firebase
    public Case() {
        // Default constructor required for calls to DataSnapshot.getValue(Case.class)
    }

    public Case(String fileName,String documentUrl) {
        this.documentUrl = documentUrl;
        this.fileName = fileName;
    }

    public Case(String caseName, String caseDescription, String caseType, String documentUrl) {
        this.caseName = caseName;
        this.caseDescription = caseDescription;
        this.caseType = caseType;
        this.documentUrl =documentUrl;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }
}
