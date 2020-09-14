package com.TUBEDELIVERIES.RoomDatabase.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.TUBEDELIVERIES.Model.CustomizationModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class MenuDetailsEntity implements Parcelable {


    @ColumnInfo(name = "name")
    private String name;


//    {"menuList":[{"id":2,"name":"Burger",
//            "img":"http:\/\/3.8.172.240\/just_bite\/public\/storage\/menu\/thumbnail\/1560319790_res2.jpeg",
//            "price":121,"item_type":1,"customize":[],"customizes":[],
//        "description":"","quantity":0,"catname":"Apetizers","addon":""}
//        ,{"id":15,"name":"Chicken Dehati",
//            "img":"http:\/\/3.8.172.240\/just_bite\/public\/storage\/menu\/thumbnail\/1561615956_1024x1024.png","price":12345,"item_type":1,"customize":[{"id":7,"name":"Quarter","price":159},{"id":8,"name":"Half","price":300},{"id":9,"name":"Full","price":500},{"id":10,"name":"Devine","price":999.99}],"customizes":[],"description":"","quantity":0,"catname":"Apetizers","addon":""},{"id":99,"name":"Cheese Blast Pizza","img":"http:\/\/3.8.172.240\/just_bite\/public\/storage\/menu\/thumbnail\/1563778853_recipe-pizza-pollo-arrosto.jpg","price":223,"item_type":1,"customize":[],"customizes":[{"itemHeading":"Size","type":1,"variation":1,"addons":[{"id":41,"en_name":"Regular","price":220},{"id":42,"en_name":"Medium","price":330},{"id":43,"en_name":"Large","price":550}]},{"itemHeading":"Extra","type":2,"variation":2,"addons":[{"id":40,"en_name":"Cheese","price":30}]},{"itemHeading":"Toppings","type":2,"variation":2,"addons":[{"id":44,"en_name":"Onion","price":20},{"id":45,"en_name":"Tomatoes","price":20},{"id":46,"en_name":"Sweet Corn","price":20}]}],"description":"","quantity":0,"catname":"Apetizers","addon":""},{"id":100,"name":"Matar Paneer","img":"http:\/\/3.8.172.240\/just_bite\/public\/storage\/menu\/thumbnail\/1563786489_matarpaneerr.jpeg","price":220,"item_type":1,"customize":[],"customizes":[{"itemHeading":"Quantity","type":1,"variation":1,"addons":[{"id":50,"en_name":"Half","price":130},{"id":51,"en_name":"Full","price":220}]}],"description":"","quantity":0,"catname":"Apetizers","addon":""}],"status":"true","message":"Restorent List"}
//


    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "img")
    private String img;

    @ColumnInfo(name = "price")
    private String price;

    @ColumnInfo(name = "RestaurantId")
    private String RestaurantId;

    public String getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        RestaurantId = restaurantId;
    }

    @ColumnInfo(name = "item_type")
    private String item_type;


    @ColumnInfo(name = "quantity")
    private int quantity;


    @ColumnInfo(name = "menuCount")
    private String menuCount;

    @ColumnInfo(name = "type")
    private String type;


    @ColumnInfo(name = "customizes")
    @TypeConverters(CustomDataConveter.class)
    @SerializedName("customizes")
    private List<CustomizationModel> customizes;

    public  MenuDetailsEntity(){}

    public MenuDetailsEntity(Parcel in) {
        name = in.readString();
        id = in.readString();
        img = in.readString();
        price = in.readString();
        item_type = in.readString();
        quantity = in.readInt();
        menuCount = in.readString();
        type = in.readString();
        customizes = in.createTypedArrayList(CustomizationModel.CREATOR);
        catname = in.readString();
    }

    public static final Creator<MenuDetailsEntity> CREATOR = new Creator<MenuDetailsEntity>() {
        @Override
        public MenuDetailsEntity createFromParcel(Parcel in) {
            return new MenuDetailsEntity(in);
        }

        @Override
        public MenuDetailsEntity[] newArray(int size) {
            return new MenuDetailsEntity[size];
        }
    };

    public List<CustomizationModel> getCustomizes() {
        return customizes;
    }

    public void setCustomizes(List<CustomizationModel> customizes) {
        this.customizes = customizes;
    }

    @ColumnInfo(name = "catname")
    private String catname;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMenuCount() {
        return menuCount;
    }

    public void setMenuCount(String menuCount) {
        this.menuCount = menuCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(img);
        dest.writeString(price);
        dest.writeString(item_type);
        dest.writeInt(quantity);
        dest.writeString(menuCount);
        dest.writeString(type);
        dest.writeTypedList(customizes);
        dest.writeString(catname);
    }
}
