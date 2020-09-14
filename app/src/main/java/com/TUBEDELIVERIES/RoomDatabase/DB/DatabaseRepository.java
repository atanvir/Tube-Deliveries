package com.TUBEDELIVERIES.RoomDatabase.DB;

import androidx.room.Room;
import android.content.Context;

public class DatabaseRepository {

    private String DB_NAME = "justbite_db";

    private JustBiteDataBase asecDataBase;

    public DatabaseRepository(Context context) {
        asecDataBase = Room.databaseBuilder(context, JustBiteDataBase.class, DB_NAME).build();
    }

//    public void insertTestList(final TestListEntity testListEntity){
//        new AsyncTask<void,void,void>()
//    }



}
