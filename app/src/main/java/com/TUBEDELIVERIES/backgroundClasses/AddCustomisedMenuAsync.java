package com.TUBEDELIVERIES.backgroundClasses;

import android.os.AsyncTask;

import com.TUBEDELIVERIES.CallBacks.AsyncClassCallbacks;
import com.TUBEDELIVERIES.RoomDatabase.DB.JustBiteDataBase;
import com.TUBEDELIVERIES.RoomDatabase.Entity.OrderItemsEntity;

import java.util.ArrayList;
import java.util.List;

public class AddCustomisedMenuAsync extends AsyncTask<String,Void, List<OrderItemsEntity>> {

    OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
    private JustBiteDataBase dataBase;

    OrderItemsEntity response=new OrderItemsEntity();

    AsyncClassCallbacks listener;
    float totalPrice=0;

    List<OrderItemsEntity>orderedItemList=new ArrayList<>();

    String addOnId="";
    String restro_id="";
    float totalCustomPrice=0;

    public AddCustomisedMenuAsync(float totalCustomPrice, String restro_id,
                                  String addOnId, OrderItemsEntity orderItemsEntity, JustBiteDataBase db,
                                  OrderItemsEntity response, AsyncClassCallbacks listener){

        this.listener=listener;
        this.orderItemsEntity=orderItemsEntity;
        this.response=response;
        this.dataBase=db;
        this.addOnId=addOnId;
        this.restro_id=restro_id;
        this.totalCustomPrice=totalCustomPrice;


    }

    @Override
    protected void onPostExecute( List<OrderItemsEntity> orderItemsEntityList) {
        super.onPostExecute(orderItemsEntityList);

        if(listener!=null){
            listener.onTaskFinish(orderItemsEntityList,totalPrice);

        }
    }



    @Override
    protected List<OrderItemsEntity> doInBackground(String... strings) {

        if(dataBase!=null){


            // check if database is empy or not if empty insert value otherwise update value
            if (dataBase.restaurantDao().fetchAllOrderMenu() == null) {
                dataBase.restaurantDao().insertOrderMenu(orderItemsEntity);//insert query


            } else {
                ////if database is not null we check if table contains same id from api (id)
                // if matches update row
                if (!dataBase.restaurantDao().fetchAllOrderMenu().getRestaurantId().equalsIgnoreCase(restro_id)) {
                    dataBase.clearAllTables();
                    dataBase.restaurantDao().insertOrderMenu(orderItemsEntity);//insert query



                } else {

                    boolean isContainCustomSame = false;

                    /// check a item contains customization or not
                    /// @params isContainCustomisation ==false when does not cotain any customization

                    if(dataBase.restaurantDao().getSameAddonItems(response.getId(),addOnId)){
                        dataBase.restaurantDao().updateCustomizedOrder(Float.parseFloat(String.valueOf(totalCustomPrice)), 1, response.getId(), 1,addOnId);
                        //orderedItemList = dataBase.restaurantDao().fetchAllOrderMenuAsList();
                    }else {
                        dataBase.restaurantDao().insertOrderMenu(orderItemsEntity);//insert query

                    }


//                     for (int i = 0; i < dataBase.restaurantDao().fetchOrderMenuListById(response.getId()).size(); i++) {
//
//                         if (dataBase.restaurantDao().fetchOrderMenuListById(response.getId()).get(i).getAddOns().equalsIgnoreCase(addOnId))
//                             isContainCustomSame = true;
//                         else
//                             isContainCustomSame = false;
//
//                     }
//
//                     if (isContainCustomSame) {
//                         dataBase.restaurantDao().updateCustomizedOrder(Float.parseFloat(String.valueOf(totalCustomPrice)), 1, response.getId(), 1,addOnId);
//                         orderedItemList = dataBase.restaurantDao().fetchAllOrderMenuAsList();
//
//                     } else {
//                         dataBase.restaurantDao().insertOrderMenu(orderItemsEntity);//insert query
//
//
//                     }


                }
            }
            orderedItemList = dataBase.restaurantDao().fetchAllOrderMenuAsList();
            totalPrice=dataBase.restaurantDao().getTotalPrice();

        }


        return orderedItemList;
    }
}