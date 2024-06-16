package com.myprograms.dapprapplication.model;

import java.util.List;

public class Order {

    String orderID;
    String userName;
    String userAddress;
    String date;
    int totalAmount;
    String paymentMethod;
    String[] items;
    List<Integer> prices;
    int quantity;

    public Order() {
    }

    public Order(String orderID,
                 String userName,
                 String userAddress,
                 String date,
                 int totalAmount,
                 String paymentMethod,
                 String[] items,
                 List<Integer> prices,
                 int quantity) {
        this.orderID = orderID;
        this.userName = userName;
        this.userAddress = userAddress;
        this.date = date;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.items = items;
        this.prices = prices;
        this.quantity = quantity;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }



    public String[] getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items.toArray(new String[0]);
    }

    public List<Integer> getPrices() {
        return prices;
    }

    public void setPrices(List<Integer> prices) {
        this.prices = prices;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
