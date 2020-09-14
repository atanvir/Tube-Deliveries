package com.TUBEDELIVERIES.RoomDatabase.Entity;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class AddOnsDataConverter {
    private static Gson gson = new Gson();
    @TypeConverter
    public static List<AddonsEntity> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<AddonsEntity>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ListToString(List<AddonsEntity> someObjects) {
        return gson.toJson(someObjects);
    }





}