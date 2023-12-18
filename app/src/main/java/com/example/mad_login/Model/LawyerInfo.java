package com.example.mad_login.Model;
public class LawyerInfo {
    private String barNumber;
    private String expYear;
    private String lawFirm;
    private String specialization;
    private String qualification;

    // Required default constructor for Firebase
    public LawyerInfo() {
    }

    public LawyerInfo(String barNumber, String expYear, String lawFirm, String specialization, String qualification) {
        this.barNumber = barNumber;
        this.expYear = expYear;
        this.lawFirm = lawFirm;
        this.specialization = specialization;
        this.qualification = qualification;
    }

    // Getter and setter methods for each field
    public String getBarNumber() {
        return barNumber;
    }

    public void setBarNumber(String barNumber) {
        this.barNumber = barNumber;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    public String getLawFirm() {
        return lawFirm;
    }

    public void setLawFirm(String lawFirm) {
        this.lawFirm = lawFirm;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
}
