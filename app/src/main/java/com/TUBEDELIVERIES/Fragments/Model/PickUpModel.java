package com.TUBEDELIVERIES.Fragments.Model;

import androidx.annotation.NonNull;

public class PickUpModel {
   private String pickUpDate;
   private String pickUpTime;


    public String getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(String pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }


    @NonNull
    @Override
    public String toString() {
        return "pickUpDate:"+pickUpDate+"\n" +
                "pickUpTime"+pickUpTime;
    }
}
