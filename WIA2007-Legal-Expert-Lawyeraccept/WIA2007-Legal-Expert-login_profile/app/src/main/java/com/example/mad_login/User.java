package com.example.mad_login;
public class User {
    private String username,email,name,birthday,phoneNum,gender,state;

    // Default constructor (required for Firebase)
    public User() {
    }
    // Constructor to initialize the user with values
    public User(String username, String email,String name, String birthday, String phoneNum, String gender, String state) {
        this.username =username;
        this.email =email;
        this.name = name;
        this.birthday = birthday;
        this.phoneNum = phoneNum;
        this.gender = gender;
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email =email;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username =username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
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
}
