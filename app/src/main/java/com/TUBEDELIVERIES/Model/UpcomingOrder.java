package com.TUBEDELIVERIES.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpcomingOrder {

    @SerializedName("order_id")
    @Expose
    private long orderId;
    @SerializedName("order_number")
    @Expose
    private String orderNumber;
    @SerializedName("restaurant_id")
    @Expose
    private long restaurantId;
    @SerializedName("restaurant_name")
    @Expose
    private String restaurantName;
    @SerializedName("restaurant_image")
    @Expose
    private String restaurantImage;
    @SerializedName("orderDate")
    @Expose
    private String orderDate;
    @SerializedName("deliveryStatus")
    @Expose
    private String deliveryStatus;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("totalPrice")
    @Expose
    private Double totalPrice;
    @SerializedName("note")
    @Expose
    private Object note;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("order_place_time")
    @Expose
    private String orderPlaceTime;
    @SerializedName("expected_delivery_time")
    @Expose
    private String expectedDeliveryTime;
    @SerializedName("delivery_peroson")
    @Expose
    private String deliveryPeroson;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("cancel_status")
    @Expose
    private long cancelStatus;
    @SerializedName("cancel_time")
    @Expose
    private long cancelTime;
    @SerializedName("orderMenu")
    @Expose
    private List<OrderMenu> orderMenu = null;


    @SerializedName("eat_option")
    @Expose
    private int eatOption;
    @SerializedName("pickup_date")
    @Expose
    private String pickupDate;
    @SerializedName("pickup_time")
    @Expose
    private String pickupTime;
    @SerializedName("vist_date")
    @Expose
    private String vistDate;
    @SerializedName("vist_time")
    @Expose
    private String vistTime;
    @SerializedName("no_of_people")
    @Expose
    private String noOfPeople;
    @SerializedName("cart_address")
    @Expose
    private String cartAddress;

    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("currency")
    @Expose
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getEatOption() {
        return eatOption;
    }

    public void setEatOption(int eatOption) {
        this.eatOption = eatOption;
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

    public String getVistDate() {
        return vistDate;
    }

    public void setVistDate(String vistDate) {
        this.vistDate = vistDate;
    }

    public String getVistTime() {
        return vistTime;
    }

    public void setVistTime(String vistTime) {
        this.vistTime = vistTime;
    }

    public String getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(String noOfPeople) {
        this.noOfPeople = noOfPeople;
    }

    public String getCartAddress() {
        return cartAddress;
    }

    public void setCartAddress(String cartAddress) {
        this.cartAddress = cartAddress;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantImage() {
        return restaurantImage;
    }

    public void setRestaurantImage(String restaurantImage) {
        this.restaurantImage = restaurantImage;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Object getNote() {
        return note;
    }

    public void setNote(Object note) {
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getOrderPlaceTime() {
        return orderPlaceTime;
    }

    public void setOrderPlaceTime(String orderPlaceTime) {
        this.orderPlaceTime = orderPlaceTime;
    }

    public String getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(String expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
    }

    public String getDeliveryPeroson() {
        return deliveryPeroson;
    }

    public void setDeliveryPeroson(String deliveryPeroson) {
        this.deliveryPeroson = deliveryPeroson;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(long cancelStatus) {
        this.cancelStatus = cancelStatus;
    }

    public long getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(long cancelTime) {
        this.cancelTime = cancelTime;
    }

    public List<OrderMenu> getOrderMenu() {
        return orderMenu;
    }

    public void setOrderMenu(List<OrderMenu> orderMenu) {
        this.orderMenu = orderMenu;
    }
}
