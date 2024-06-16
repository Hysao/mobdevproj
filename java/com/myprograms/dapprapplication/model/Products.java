package com.myprograms.dapprapplication.model;

import com.google.firebase.Timestamp;

public class Products {
    String productName;
    int productPrice;
    String productCategory;
    String productImg;
    int productStock;
    Timestamp timestamp;
    boolean productHighLight;
    boolean isWishListed;

    String documentId;

    public Products(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Products() {
    }

    public Products(boolean isWishListed) {
        this.isWishListed = isWishListed;
    }

    public Products(String productName,
                    int productPrice,
                    String productCategory,
                    String productImg,
                    int productStock,
                    Timestamp timestamp,
                    boolean productHighLight) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.productImg = productImg;
        this.productStock = productStock;
        this.timestamp = timestamp;
        this.productHighLight = productHighLight;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isProductHighLight() {
        return productHighLight;
    }

    public void setProductHighLight(boolean productHighLight) {
        this.productHighLight = productHighLight;
    }

    public boolean isWishListed() {
        return isWishListed;
    }

    public void setWishListed(boolean wishListed) {
        isWishListed = wishListed;
    }


}
