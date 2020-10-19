package com.TUBEDELIVERIES.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplyCoupon implements Parcelable {

    @SerializedName("discount_amount")
    @Expose
    private Double discount_amount;
    @SerializedName("final_amount")
    @Expose
    private Double final_amount;
    @SerializedName("coupon_id")
    @Expose
    private long coupon_id;
    @SerializedName("coupon_code")
    @Expose
    private String coupon_code;

    protected ApplyCoupon(Parcel in) {
        if (in.readByte() == 0) {
            discount_amount = null;
        } else {
            discount_amount = in.readDouble();
        }
        if (in.readByte() == 0) {
            final_amount = null;
        } else {
            final_amount = in.readDouble();
        }
        coupon_id = in.readLong();
        coupon_code = in.readString();
    }

    public static final Creator<ApplyCoupon> CREATOR = new Creator<ApplyCoupon>() {
        @Override
        public ApplyCoupon createFromParcel(Parcel in) {
            return new ApplyCoupon(in);
        }

        @Override
        public ApplyCoupon[] newArray(int size) {
            return new ApplyCoupon[size];
        }
    };

    public Double getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(Double discount_amount) {
        this.discount_amount = discount_amount;
    }

    public Double getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(Double final_amount) {
        this.final_amount = final_amount;
    }

    public long getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(long coupon_id) {
        this.coupon_id = coupon_id;
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
        if (discount_amount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(discount_amount);
        }
        if (final_amount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(final_amount);
        }
        dest.writeLong(coupon_id);
        dest.writeString(coupon_code);
    }
}
