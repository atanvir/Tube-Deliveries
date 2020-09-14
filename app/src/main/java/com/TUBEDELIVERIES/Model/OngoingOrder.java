package com.TUBEDELIVERIES.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OngoingOrder {

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
    private long price;
    @SerializedName("discount")
    @Expose
    private long discount;
    @SerializedName("totalPrice")
    @Expose
    private long totalPrice;
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
    @SerializedName("eat_option")
    @Expose
    private long eatOption;
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
    @SerializedName("orderMenu")
    @Expose
    private List<OrderMenu> orderMenu = null;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public OngoingOrder withOrderId(long orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OngoingOrder withOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public OngoingOrder withRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
        return this;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public OngoingOrder withRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
        return this;
    }

    public String getRestaurantImage() {
        return restaurantImage;
    }

    public void setRestaurantImage(String restaurantImage) {
        this.restaurantImage = restaurantImage;
    }

    public OngoingOrder withRestaurantImage(String restaurantImage) {
        this.restaurantImage = restaurantImage;
        return this;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public OngoingOrder withOrderDate(String orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public OngoingOrder withDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
        return this;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public OngoingOrder withPrice(long price) {
        this.price = price;
        return this;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public OngoingOrder withDiscount(long discount) {
        this.discount = discount;
        return this;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OngoingOrder withTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public Object getNote() {
        return note;
    }

    public void setNote(Object note) {
        this.note = note;
    }

    public OngoingOrder withNote(Object note) {
        this.note = note;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OngoingOrder withAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public OngoingOrder withPaymentType(String paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public String getOrderPlaceTime() {
        return orderPlaceTime;
    }

    public void setOrderPlaceTime(String orderPlaceTime) {
        this.orderPlaceTime = orderPlaceTime;
    }

    public OngoingOrder withOrderPlaceTime(String orderPlaceTime) {
        this.orderPlaceTime = orderPlaceTime;
        return this;
    }

    public String getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(String expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
    }

    public OngoingOrder withExpectedDeliveryTime(String expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
        return this;
    }

    public String getDeliveryPeroson() {
        return deliveryPeroson;
    }

    public void setDeliveryPeroson(String deliveryPeroson) {
        this.deliveryPeroson = deliveryPeroson;
    }

    public OngoingOrder withDeliveryPeroson(String deliveryPeroson) {
        this.deliveryPeroson = deliveryPeroson;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public OngoingOrder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public long getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(long cancelStatus) {
        this.cancelStatus = cancelStatus;
    }

    public OngoingOrder withCancelStatus(long cancelStatus) {
        this.cancelStatus = cancelStatus;
        return this;
    }

    public long getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(long cancelTime) {
        this.cancelTime = cancelTime;
    }

    public OngoingOrder withCancelTime(long cancelTime) {
        this.cancelTime = cancelTime;
        return this;
    }

    public long getEatOption() {
        return eatOption;
    }

    public void setEatOption(long eatOption) {
        this.eatOption = eatOption;
    }

    public OngoingOrder withEatOption(long eatOption) {
        this.eatOption = eatOption;
        return this;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public OngoingOrder withPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
        return this;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public OngoingOrder withPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
        return this;
    }

    public String getVistDate() {
        return vistDate;
    }

    public void setVistDate(String vistDate) {
        this.vistDate = vistDate;
    }

    public OngoingOrder withVistDate(String vistDate) {
        this.vistDate = vistDate;
        return this;
    }

    public String getVistTime() {
        return vistTime;
    }

    public void setVistTime(String vistTime) {
        this.vistTime = vistTime;
    }

    public OngoingOrder withVistTime(String vistTime) {
        this.vistTime = vistTime;
        return this;
    }

    public String getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(String noOfPeople) {
        this.noOfPeople = noOfPeople;
    }

    public OngoingOrder withNoOfPeople(String noOfPeople) {
        this.noOfPeople = noOfPeople;
        return this;
    }

    public String getCartAddress() {
        return cartAddress;
    }

    public void setCartAddress(String cartAddress) {
        this.cartAddress = cartAddress;
    }

    public OngoingOrder withCartAddress(String cartAddress) {
        this.cartAddress = cartAddress;
        return this;
    }

    public List<OrderMenu> getOrderMenu() {
        return orderMenu;
    }

    public void setOrderMenu(List<OrderMenu> orderMenu) {
        this.orderMenu = orderMenu;
    }

    public OngoingOrder withOrderMenu(List<OrderMenu> orderMenu) {
        this.orderMenu = orderMenu;
        return this;
    }

}


