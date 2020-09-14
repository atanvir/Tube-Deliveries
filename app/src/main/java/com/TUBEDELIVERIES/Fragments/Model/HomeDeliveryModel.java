package com.TUBEDELIVERIES.Fragments.Model;

import androidx.annotation.NonNull;

public class HomeDeliveryModel {



    private String address;

    private String type;
    private String lat;
    private String longt;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongt() {
        return longt;
    }

    public void setLongt(String longt) {
        this.longt = longt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @NonNull
    @Override
    public String toString() {
        return "type:"+type+"\n" +
                "address"+address;
    }
}
