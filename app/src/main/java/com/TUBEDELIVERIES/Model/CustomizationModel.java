package com.TUBEDELIVERIES.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.TUBEDELIVERIES.RoomDatabase.Entity.AddOnsDataConverter;
import com.TUBEDELIVERIES.RoomDatabase.Entity.AddonsEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class CustomizationModel implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int columnId;

    public CustomizationModel() {
    }

    protected CustomizationModel(Parcel in) {
        columnId = in.readInt();
        itemHeading = in.readString();
        menuViewType = in.readString();
        addons = in.createTypedArrayList(AddonsEntity.CREATOR);
    }

    public static final Creator<CustomizationModel> CREATOR = new Creator<CustomizationModel>() {
        @Override
        public CustomizationModel createFromParcel(Parcel in) {
            return new CustomizationModel(in);
        }

        @Override
        public CustomizationModel[] newArray(int size) {
            return new CustomizationModel[size];
        }
    };

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    @SerializedName("itemHeading")
    private String itemHeading;


    @SerializedName("type")
    private String menuViewType;


    public String getItemHeading() {
        return itemHeading;
    }

    public void setItemHeading(String itemHeading) {
        this.itemHeading = itemHeading;
    }

    public List<AddonsEntity> getAddons() {
        return addons;
    }

    public void setAddons(List<AddonsEntity> addons) {
        this.addons = addons;
    }

    @SerializedName("addons")
    @TypeConverters(AddOnsDataConverter.class)
    private List<AddonsEntity>addons;


    public String getMenuViewType() {
        return menuViewType;
    }

    public void setMenuViewType(String menuViewType) {
        this.menuViewType = menuViewType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(columnId);
        dest.writeString(itemHeading);
        dest.writeString(menuViewType);
        dest.writeTypedList(addons);
    }
}
