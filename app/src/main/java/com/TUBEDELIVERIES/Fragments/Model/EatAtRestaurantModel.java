package com.TUBEDELIVERIES.Fragments.Model;

import androidx.annotation.NonNull;

public class EatAtRestaurantModel {

    private String visitingDate;
    private String visitingTime;
    private String noPeople;

    public String getVisitingDate() {
        return visitingDate;
    }

    public void setVisitingDate(String visitingDate) {
        this.visitingDate = visitingDate;
    }

    public String getVisitingTime() {
        return visitingTime;
    }

    public void setVisitingTime(String visitingTime) {
        this.visitingTime = visitingTime;
    }

    public String getNoPeople() {
        return noPeople;
    }

    public void setNoPeople(String noPeople) {
        this.noPeople = noPeople;
    }


    @NonNull
    @Override
    public String toString() {
        return "visitingDate:"+visitingDate+"\n" +
                "visitingTime"+visitingTime+"\n"
               + "noPeople:"+noPeople;
    }
}
