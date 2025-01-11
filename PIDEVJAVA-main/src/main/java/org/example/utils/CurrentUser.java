package org.example.utils;

import org.example.models.User;

public class CurrentUser {
    private static User currentUser;
    private static String secretCode ;
    public static String getUserSecretKey(){
        return CurrentUser.secretCode ;
    }

    public static void setUserSecretKey(String secretCode){
        CurrentUser.secretCode=secretCode ;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void clearCurrentUser() {
        currentUser = null;
    }
}
