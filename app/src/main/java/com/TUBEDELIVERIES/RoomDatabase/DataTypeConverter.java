package com.TUBEDELIVERIES.RoomDatabase;

import androidx.room.TypeConverter;

import com.TUBEDELIVERIES.Model.ResponseBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class DataTypeConverter {
    private static Gson gson = new Gson();
    @TypeConverter
    public static List<ResponseBean> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<ResponseBean>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ListToString(List<ResponseBean> someObjects) {
        return gson.toJson(someObjects);
    }





}