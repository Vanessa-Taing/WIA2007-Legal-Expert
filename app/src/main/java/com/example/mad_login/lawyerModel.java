package com.example.mad_login;

public class lawyerModel {
    private String name;
    private String profilePictureUrl;
    private float rating;
    private String specializedField;

    public lawyerModel() {
    }

    public lawyerModel(String name, String profilePictureUrl, float rating, String specializedField) {
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
        this.rating = rating;
        this.specializedField = specializedField;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getSpecializedField() {
        return specializedField;
    }

    public void setSpecializedField(String specializedField) {
        this.specializedField = specializedField;
    }
}