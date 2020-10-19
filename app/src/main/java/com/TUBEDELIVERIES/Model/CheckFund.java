package com.TUBEDELIVERIES.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckFund implements Parcelable {

    @SerializedName("wallet")
    @Expose
    private Wallet wallet;
    @SerializedName("subscription")
    @Expose
    private Subscription subscription;
    @SerializedName("credit")
    @Expose
    private Credit credit;

    protected CheckFund(Parcel in) {
    }

    public static final Creator<CheckFund> CREATOR = new Creator<CheckFund>() {
        @Override
        public CheckFund createFromParcel(Parcel in) {
            return new CheckFund(in);
        }

        @Override
        public CheckFund[] newArray(int size) {
            return new CheckFund[size];
        }
    };

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public class Wallet {

        @SerializedName("balance")
        @Expose
        private Integer balance;
        @SerializedName("status")
        @Expose
        private Integer status;

        public Integer getBalance() {
            return balance;
        }

        public void setBalance(Integer balance) {
            this.balance = balance;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

    }



    public class Subscription {

        @SerializedName("balance")
        @Expose
        private Double balance;
        @SerializedName("status")
        @Expose
        private Integer status;

        public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
            this.balance = balance;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

    }
    public class Credit {

        @SerializedName("balance")
        @Expose
        private Integer balance;
        @SerializedName("status")
        @Expose
        private Integer status;

        public Integer getBalance() {
            return balance;
        }

        public void setBalance(Integer balance) {
            this.balance = balance;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

    }

}


