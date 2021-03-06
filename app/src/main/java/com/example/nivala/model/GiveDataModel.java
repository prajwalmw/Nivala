package com.example.nivala.model;

public class GiveDataModel {
    private String expiry;
    private String foodItem;
    private String imageUri;
    private String phone;
    private String state;
    private String city;
    private String pickupAddress;
    private String pickupDate;
    private String pickupTime;
    private String policy;
    private String quantity;
    private String foodType;
// TODO: add food type
    public GiveDataModel() {
    }

    public GiveDataModel(String foodItem, String quantity, String expiry, String state, String city,
                         String pickupAddress, String pickupDate, String pickupTime,
                         String phone, String imageUri, String policy, String foodType) {

        this.foodItem = foodItem;
        this.quantity = quantity;
        this.expiry = expiry;
        this.state = state;
        this.city = city;
        this.pickupAddress = pickupAddress;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.phone = phone;
        this.imageUri = imageUri;
        this.policy = policy;
        this.foodType = foodType;
    }

    public String getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
}
