package com.TUBEDELIVERIES.RoomDatabase.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.TUBEDELIVERIES.Model.CustomizationModel;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.RoomDatabase.Dao.RestaurantDao;
import com.TUBEDELIVERIES.RoomDatabase.Entity.MenuDetailsEntity;
import com.TUBEDELIVERIES.RoomDatabase.Entity.OrderItemsEntity;


@Database(entities = {RestaurantResponse.class, MenuDetailsEntity.class, OrderItemsEntity.class, CustomizationModel.class}, version = 1, exportSchema = false)
public abstract class JustBiteDataBase extends RoomDatabase {
   public abstract RestaurantDao restaurantDao();

}
