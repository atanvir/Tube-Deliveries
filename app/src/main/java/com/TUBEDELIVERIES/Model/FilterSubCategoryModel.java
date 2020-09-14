package com.TUBEDELIVERIES.Model;

public class FilterSubCategoryModel {
    private String id;
    private String name;
    private boolean isCheck;
    private boolean isRating;
    private boolean isDistance;
    private float rating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isRating() {
        return isRating;
    }

    public void setRating(boolean rating) {
        isRating = rating;
    }

    public boolean isDistance() {
        return isDistance;
    }

    public void setDistance(boolean distance) {
        isDistance = distance;
    }

    public FilterSubCategoryModel() {
    }

    public FilterSubCategoryModel(String name, boolean isCheck) {
        this.name = name;
        this.isCheck = isCheck;
    }

    public FilterSubCategoryModel(String id,String name, boolean isCheck) {
        this.id=id;
        this.name = name;
        this.isCheck = isCheck;
    }

    public FilterSubCategoryModel(String name, boolean isCheck,boolean isRating) {
        this.name = name;
        this.isCheck = isCheck;
        this.isRating=isRating;
    }

    public FilterSubCategoryModel(String name, boolean isCheck,boolean isRating,boolean isDistance) {
        this.name = name;
        this.isCheck = isCheck;
        this.isRating=isRating;
        this.isDistance=isDistance;
    }



}
