package com.TUBEDELIVERIES.Model;

public class CardDetailSaveModel {

    private String user_id;
    private String eat_option;
    private String pickup_date;
    private String pickup_time;
    private String vist_date;
    private String vist_time;
    private String address;
    private String no_of_people;

    public CardDetailSaveModel(String user_id, String eat_option, String pickup_date, String pickup_time, String vist_date, String vist_time, String address, String no_of_people) {
        this.user_id = user_id;
        this.eat_option = eat_option;
        this.pickup_date = pickup_date;
        this.pickup_time = pickup_time;
        this.vist_date = vist_date;
        this.vist_time = vist_time;
        this.address = address;
        this.no_of_people = no_of_people;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEat_option() {
        return eat_option;
    }

    public void setEat_option(String eat_option) {
        this.eat_option = eat_option;
    }

    public String getPickup_date() {
        return pickup_date;
    }

    public void setPickup_date(String pickup_date) {
        this.pickup_date = pickup_date;
    }

    public String getPickup_time() {
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public String getVist_date() {
        return vist_date;
    }

    public void setVist_date(String vist_date) {
        this.vist_date = vist_date;
    }

    public String getVist_time() {
        return vist_time;
    }

    public void setVist_time(String vist_time) {
        this.vist_time = vist_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNo_of_people() {
        return no_of_people;
    }

    public void setNo_of_people(String no_of_people) {
        this.no_of_people = no_of_people;
    }
}
