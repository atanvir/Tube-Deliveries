package com.TUBEDELIVERIES.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationList implements Parcelable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("res_id")
    @Expose
    private long resId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;


    protected NotificationList(Parcel in) {
        title = in.readString();
        description = in.readString();
        type = in.readString();
        orderId = in.readString();
        resId = in.readLong();
        createdAt = in.readString();
    }

    public static final Creator<NotificationList> CREATOR = new Creator<NotificationList>() {
        @Override
        public NotificationList createFromParcel(Parcel in) {
            return new NotificationList(in);
        }

        @Override
        public NotificationList[] newArray(int size) {
            return new NotificationList[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getResId() {
        return resId;
    }

    public void setResId(long resId) {
        this.resId = resId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(type);
        dest.writeString(orderId);
        dest.writeLong(resId);
        dest.writeString(createdAt);
    }
}
