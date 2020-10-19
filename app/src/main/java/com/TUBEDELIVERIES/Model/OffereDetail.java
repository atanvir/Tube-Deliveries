package com.TUBEDELIVERIES.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OffereDetail implements Parcelable {


    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("restorent_id")
    @Expose
    private long restorentId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("valid_date")
    @Expose
    private String validDate;
    @SerializedName("discount")
    @Expose
    private Object discount;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("status")
    @Expose
    private long status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("en_name")
    @Expose
    private String en_name;

    @SerializedName("coupon_code")
    @Expose
    private String coupon_code;


    protected OffereDetail(Parcel in) {
        id = in.readLong();
        restorentId = in.readLong();
        type = in.readString();
        img = in.readString();
        thumbnail = in.readString();
        validDate = in.readString();
        note = in.readString();
        status = in.readLong();
        createdAt = in.readString();
        updatedAt = in.readString();
        en_name= in.readString();
        coupon_code=in.readString();
    }

    public static final Creator<OffereDetail> CREATOR = new Creator<OffereDetail>() {
        @Override
        public OffereDetail createFromParcel(Parcel in) {
            return new OffereDetail(in);
        }

        @Override
        public OffereDetail[] newArray(int size) {
            return new OffereDetail[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRestorentId() {
        return restorentId;
    }

    public void setRestorentId(long restorentId) {
        this.restorentId = restorentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public Object getDiscount() {
        return discount;
    }

    public void setDiscount(Object discount) {
        this.discount = discount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(restorentId);
        dest.writeString(type);
        dest.writeString(img);
        dest.writeString(thumbnail);
        dest.writeString(validDate);
        dest.writeString(note);
        dest.writeLong(status);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(en_name);
        dest.writeString(coupon_code);
    }
}
