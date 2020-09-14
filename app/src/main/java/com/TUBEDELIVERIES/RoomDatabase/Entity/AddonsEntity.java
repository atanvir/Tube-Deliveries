package com.TUBEDELIVERIES.RoomDatabase.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "AddOns")
public class AddonsEntity  implements Parcelable {

    protected AddonsEntity(Parcel in) {
        columnId = in.readInt();
        id = in.readString();
        name = in.readString();
        isCheck = in.readByte() != 0;
        price = in.readString();
    }

    public AddonsEntity() {
    }

    public static final Creator<AddonsEntity> CREATOR = new Creator<AddonsEntity>() {
        @Override
        public AddonsEntity createFromParcel(Parcel in) {
            return new AddonsEntity(in);
        }

        @Override
        public AddonsEntity[] newArray(int size) {
            return new AddonsEntity[size];
        }
    };

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    @PrimaryKey(autoGenerate = true)
    private int columnId;


    @SerializedName("id")
    private String id;

    @SerializedName(value = "name",alternate = "en_name")
    private String name;

    private boolean isCheck;



    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @SerializedName("price")
    private String price;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(columnId);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeByte((byte) (isCheck ? 1 : 0));
        dest.writeString(price);
    }
}
