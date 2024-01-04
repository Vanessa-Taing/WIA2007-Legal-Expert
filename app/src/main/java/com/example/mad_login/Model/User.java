package com.example.mad_login.Model;

public class User {
    private String uid,email,name, doB, mobile,gender,state, imageURL;

    // Default constructor (required for Firebase)
    public User() {
    }
    // Constructor to initialize the user with values
    public User(String uid, String email, String name, String doB, String mobile, String gender, String state, String imageURL) {
        this.uid = uid;
        this.email =email;
        this.name = name;
        this.doB = doB;
        this.mobile = mobile;
        this.gender = gender;
        this.state = state;
        this.imageURL = imageURL;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email =email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoB() {
        return doB;
    }

    public void setDoB(String doB) {
        this.doB = doB;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}