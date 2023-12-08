package com.example.mad_login;


public class AppUtils {
    private static String userType;

    public static String getUserType(){
        if (userType.isEmpty()){
            return "Not Logged in";
        }
      return userType;
    };
    public static void isLawyer() {
        userType = "Lawyer";
    }
    public static void isUser() {
        userType = "User";
    }
}
