package com.myprograms.dapprapplication.model;

public class User {

    String userName;
    String userAddress;
    String userEmail;
    String userPhone;
    String userID;

    public User() {
    }

    public User(String userName, String userAddress, String userEmail, String userPhone, String userID) {
        this.userName = userName;
        this.userAddress = userAddress;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userID = userID;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
