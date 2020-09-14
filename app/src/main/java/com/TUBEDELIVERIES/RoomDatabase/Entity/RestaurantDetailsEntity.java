package com.TUBEDELIVERIES.RoomDatabase.Entity;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

import com.TUBEDELIVERIES.RoomDatabase.DataTypeConverter;

import java.io.Serializable;
import java.util.List;

public class RestaurantDetailsEntity implements Serializable {


    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;


    @ColumnInfo(name = "name")
    private String name;


    @ColumnInfo(name = "description")
    private String description;


    @ColumnInfo(name = "address")
    private String address;


    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "rating")
    private String rating;


    @ColumnInfo(name = "review")
    private int review;

    @ColumnInfo(name = "cart_value")
    private String cart_value;


    @ColumnInfo(name = "is_favorite")
    private String is_favorite;

    @ColumnInfo(name = "min_order_value")
    private String min_order_value;


    @ColumnInfo(name = "delivery_time")
    private String delivery_time;


    @ColumnInfo(name = "cuisines")
    @TypeConverters(DataTypeConverter.class)
    private List<MenuDetailsEntity>cuisines;


    @ColumnInfo(name = "category")
    @TypeConverters(DataTypeConverter.class)
    private List<MenuDetailsEntity>category;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public String getCart_value() {
        return cart_value;
    }

    public void setCart_value(String cart_value) {
        this.cart_value = cart_value;
    }

    public String getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(String is_favorite) {
        this.is_favorite = is_favorite;
    }

    public String getMin_order_value() {
        return min_order_value;
    }

    public void setMin_order_value(String min_order_value) {
        this.min_order_value = min_order_value;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public List<MenuDetailsEntity> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<MenuDetailsEntity> cuisines) {
        this.cuisines = cuisines;
    }

    public List<MenuDetailsEntity> getCategory() {
        return category;
    }

    public void setCategory(List<MenuDetailsEntity> category) {
        this.category = category;
    }
}
