package com.myprograms.dapprapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Cart implements Parcelable {



    String uid;
    String itemName;
    int quantity;
    int price;
    String itemColor;
    String itemSize;
    String itemImage;
    String documentId;


    public Cart() {

    }

    public Cart(String documentId) {
        this.documentId = documentId;
    }

    public Cart(String uid, String itemName, int quantity, int price, String itemColor, String itemSize, String itemImage) {
        this.uid = uid;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.itemColor = itemColor;
        this.itemSize = itemSize;
        this.itemImage = itemImage;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    protected Cart(Parcel in) {
        uid = in.readString();
        itemName = in.readString();
        quantity = in.readInt();
        price = in.readInt();
        itemColor = in.readString();
        itemSize = in.readString();
        itemImage = in.readString();
        documentId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(itemName);
        dest.writeInt(quantity);
        dest.writeInt(price);
        dest.writeString(itemColor);
        dest.writeString(itemSize);
        dest.writeString(itemImage);
        dest.writeString(documentId);
    }

    // Requiredmethods for Parcelable interface
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

}
