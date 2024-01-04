package com.example.mad_login;
public class ReadWriteUserDetails {
    public String email,doB,gender,mobile,state,name,language,barNumber,expYear,lawFirm,specialization,qualification;
    public ReadWriteUserDetails(){}
    public ReadWriteUserDetails(String email, String doB, String gender, String mobile, String state, String name, String language, String barNumber, String expYear, String lawFirm, String specialization, String qualification) {
        this.email = email;
        this.doB = doB;
        this.gender = gender;
        this.mobile = mobile;
        this.state = state;
        this.name = name;
        this.language = language;
        this.barNumber = barNumber;
        this.expYear = expYear;
        this.lawFirm = lawFirm;
        this.specialization = specialization;
        this.qualification = qualification;
    }
    // Constructor to initialize user details
    public ReadWriteUserDetails(String txtGender, String txtState, String txtPhoneNum, String txtName, String txtBirthday) {
        this.gender = txtGender;
        this.state = txtState;
        this.mobile = txtPhoneNum;
        this.name = txtName;
        this.doB = txtBirthday;
    }
    public ReadWriteUserDetails(String txtGender, String txtState, String txtPhoneNum, String txtName, String txtBirthday,String txtLanguage) {
        this.gender = txtGender;
        this.state = txtState;
        this.mobile = txtPhoneNum;
        this.name = txtName;
        this.doB = txtBirthday;
        this.language = txtLanguage;
    }
    public ReadWriteUserDetails(String doB,String gender,String mobile){
        this.doB =doB;
        this.gender =gender;
        this.mobile =mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDoB() {
        return doB;
    }

    public void setDoB(String doB) {
        this.doB = doB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

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
