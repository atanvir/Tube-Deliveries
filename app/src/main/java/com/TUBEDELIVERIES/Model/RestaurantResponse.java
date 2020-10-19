package com.TUBEDELIVERIES.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.TUBEDELIVERIES.RoomDatabase.DataTypeConverter;
import com.TUBEDELIVERIES.RoomDatabase.Entity.CustomDataConveter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class RestaurantResponse  implements  Parcelable {


    private String cuisineString;
    private boolean isCheck;
    private boolean isHeartAnimCheck;
    private String AppendCuisines;


    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private String id;



    @SerializedName("restorent_time")
    private String restorent_time;


    @SerializedName("cart_value")
    private String cart_value;

    @SerializedName("catname")
    private String catname;

    protected RestaurantResponse(Parcel in) {
        cuisineString = in.readString();
        isCheck = in.readByte() != 0;
        isHeartAnimCheck = in.readByte() != 0;
        AppendCuisines = in.readString();
        id = in.readString();
        restorent_time = in.readString();
        cart_value = in.readString();
        catname = in.readString();
        addon = in.readString();
        rating = in.readString();
        latitude = in.readString();
        min_order_value = in.readString();
        delivery_time = in.readString();
        review = in.readString();
        image = in.readString();
        user_name = in.readString();
        en_name = in.readString();
        name = in.readString();
        valid_date = in.readString();
        note = in.readString();
        quantity = in.readInt();
        discount = in.readString();
        customizes = in.createTypedArrayList(CustomizationModel.CREATOR);
        item_type = in.readString();
        price = in.readString();
        restorent_id = in.readString();
        RestaurantId = in.readString();
        type = in.readString();
        menuCount = in.readString();
        cuisines = in.createTypedArrayList(ResponseBean.CREATOR);
        is_favorite = in.readString();
        res_name = in.readString();
        img = in.readString();
        thumbnail = in.readString();
        address = in.readString();
        description = in.readString();
        created_at = in.readString();
        longitude = in.readString();
        store_type = in.readInt();
    }

    public static final Creator<RestaurantResponse> CREATOR = new Creator<RestaurantResponse>() {
        @Override
        public RestaurantResponse createFromParcel(Parcel in) {
            return new RestaurantResponse(in);
        }

        @Override
        public RestaurantResponse[] newArray(int size) {
            return new RestaurantResponse[size];
        }
    };

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    @SerializedName("addon")
    private String addon;

    @ColumnInfo(name = "rating")
    @SerializedName("rating")
    private String rating;

    @SerializedName("latitude")
    private String latitude;



    @ColumnInfo(name = "min_order_value")
    @SerializedName("min_order_value")
    private String min_order_value;


    @ColumnInfo(name = "delivery_time")
    @SerializedName("delivery_time")
    private String delivery_time;


    @ColumnInfo(name = "review")
    @SerializedName("review")
    private String review;



    @ColumnInfo(name = "image")
    @SerializedName("image")
    private String image;

    @SerializedName("user_name")
    private String user_name;

    @SerializedName("en_name")
    private String en_name ;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;


    @SerializedName("valid_date")
    private String valid_date;

    @SerializedName("note")
    private String note;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("discount")
    private String discount;

    @ColumnInfo(name = "customizes")
    @TypeConverters(CustomDataConveter.class)
    @SerializedName("customizes")
    private List<CustomizationModel>customizes;



    @SerializedName("item_type")
    private String item_type;

    @SerializedName("price")
    private String price;

    @SerializedName("restorent_id")
    private String restorent_id;

    @SerializedName("RestaurantId")
    private String RestaurantId;

    public String getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        RestaurantId = restaurantId;
    }

    @SerializedName("type")
    private String type;

    @SerializedName("menuCount")
    private String menuCount;


    @ColumnInfo(name = "cuisines")
    @TypeConverters(DataTypeConverter.class)
    @SerializedName("cuisines")
    private List<ResponseBean> cuisines;



    @ColumnInfo(name = "is_favorite")
    @SerializedName("is_favorite")
    private String is_favorite;

    @SerializedName("res_name")
    private String res_name;
    @SerializedName("img")
    private String img;
    @SerializedName("thumbnail")
    private String thumbnail;

    @ColumnInfo(name = "address")
    @SerializedName("address")
    private String address;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String description;


    @SerializedName("created_at")
    private String created_at;


    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    private String longitude;

    @SerializedName("store_type")
    @Expose
    private int store_type;

    public RestaurantResponse(){

    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<CustomizationModel> getCustomizes() {
        return customizes;
    }

    public void setCustomizes(List<CustomizationModel> customizes) {
        this.customizes = customizes;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isHeartAnimCheck() {
        return isHeartAnimCheck;
    }

    public void setHeartAnimCheck(boolean heartAnimCheck) {
        isHeartAnimCheck = heartAnimCheck;
    }

    public String getCuisineString() {
        return cuisineString;
    }

    public void setCuisineString(String cuisineString) {
        this.cuisineString = cuisineString;
    }

    public String getAppendCuisines() {
        return AppendCuisines;
    }

    public void setAppendCuisines(String appendCuisines) {
        AppendCuisines = appendCuisines;
    }

    public String getAddon() {
        return addon;
    }

    public void setAddon(String addon) {
        this.addon = addon;
    }

    public String getCart_value() {
        return cart_value;
    }

    public void setCart_value(String cart_value) {
        this.cart_value = cart_value;
    }

    public String getRestorent_time() {
        return restorent_time;
    }

    public void setRestorent_time(String restorent_time) {
        this.restorent_time = restorent_time;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRes_name() {
        return res_name;
    }

    public void setRes_name(String res_name) {
        this.res_name = res_name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getValid_date() {
        return valid_date;
    }

    public void setValid_date(String valid_date) {
        this.valid_date = valid_date;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }



    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRestorent_id() {
        return restorent_id;
    }

    public void setRestorent_id(String restorent_id) {
        this.restorent_id = restorent_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMenuCount() {
        return menuCount;
    }

    public void setMenuCount(String menuCount) {
        this.menuCount = menuCount;
    }

    public List<ResponseBean> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<ResponseBean> cuisines) {
        this.cuisines = cuisines;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(String is_favorite) {
        this.is_favorite = is_favorite;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStore_type() {
        return store_type;
    }

    public void setStore_type(int store_type) {
        this.store_type = store_type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cuisineString);
        dest.writeByte((byte) (isCheck ? 1 : 0));
        dest.writeByte((byte) (isHeartAnimCheck ? 1 : 0));
        dest.writeString(AppendCuisines);
        dest.writeString(id);
        dest.writeString(restorent_time);
        dest.writeString(cart_value);
        dest.writeString(catname);
        dest.writeString(addon);
        dest.writeString(rating);
        dest.writeString(latitude);
        dest.writeString(min_order_value);
        dest.writeString(delivery_time);
        dest.writeString(review);
        dest.writeString(image);
        dest.writeString(user_name);
        dest.writeString(en_name);
        dest.writeString(name);
        dest.writeString(valid_date);
        dest.writeString(note);
        dest.writeInt(quantity);
        dest.writeString(discount);
        dest.writeTypedList(customizes);
        dest.writeString(item_type);
        dest.writeString(price);
        dest.writeString(restorent_id);
        dest.writeString(RestaurantId);
        dest.writeString(type);
        dest.writeString(menuCount);
        dest.writeTypedList(cuisines);
        dest.writeString(is_favorite);
        dest.writeString(res_name);
        dest.writeString(img);
        dest.writeString(thumbnail);
        dest.writeString(address);
        dest.writeString(description);
        dest.writeString(created_at);
        dest.writeString(longitude);
        dest.writeInt(store_type);
    }
}
