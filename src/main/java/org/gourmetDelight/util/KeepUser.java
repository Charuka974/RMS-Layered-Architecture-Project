package org.gourmetDelight.util;

public class KeepUser {
    private static KeepUser instance;
    private String userID;

    private KeepUser() {}

    public static KeepUser getInstance() {
        if (instance == null) {
            instance = new KeepUser();
        }
        return instance;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
}
