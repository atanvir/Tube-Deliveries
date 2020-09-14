package com.TUBEDELIVERIES.backgroundClasses;

import android.os.AsyncTask;

import com.TUBEDELIVERIES.RoomDatabase.DB.JustBiteDataBase;
import com.TUBEDELIVERIES.RoomDatabase.Entity.OrderItemsEntity;

import java.util.ArrayList;
import java.util.List;

public class OrderedItemQuantity extends AsyncTask<String, Void, List<OrderItemsEntity>> {

    private AsynResponse listener;
    //For Database purpose
    private JustBiteDataBase dataBase;

    public OrderedItemQuantity(List<OrderItemsEntity> menuDetailsList, AsynResponse listener, JustBiteDataBase db) {
        this.listener = listener;
        this.listener = listener;
        this.dataBase = db;
        this.menuDetailsList = menuDetailsList;
    }

    private List<OrderItemsEntity> menuDetailsList = new ArrayList<>();

    private List<OrderItemsEntity> offlineQuantityList = new ArrayList<>();

    @Override
    protected void onPostExecute(List<OrderItemsEntity> orderItemsEntities) {
        super.onPostExecute(orderItemsEntities);

        if (listener != null) {
            listener.processFinish(orderItemsEntities);
        }


    }


    @Override
    protected List<OrderItemsEntity> doInBackground(String... strings) {

        if (dataBase != null) {
            ///get all list from according to input category
            offlineQuantityList = dataBase.restaurantDao().OrderMenuListViaCategory(strings[0]);

            if (offlineQuantityList != null && offlineQuantityList.size() > 0) {

                int quantity = 0;

                for (int i = 0; i < menuDetailsList.size(); i++) {

                    for (int j = 0; j < offlineQuantityList.size(); j++) {

                        /// if item present in DB then get total quantity and set to
                        // @PARAMS menuDetailsList list and send to mainUI thread
                        if(menuDetailsList.get(i).getId().equalsIgnoreCase(offlineQuantityList.get(j).getId())){

                            menuDetailsList.get(i).setQuantity(dataBase.restaurantDao().getTotalQuantity(offlineQuantityList.get(j).getId()));

                        }
                    }

                }

//                for (int i = 0; i < menuDetailsList.size(); i++) {
//
//                    for (int j = 0; j < offlineQuantityList.size(); j++) {
//
//                        /// if id matches in table set updated quantity of item menu
//                        if (menuDetailsList.get(i).getId().equalsIgnoreCase(offlineQuantityList.get(j).getId())) {
//                            if(offlineQuantityList.get(j).getCustomizes().size()==0){
//                            menuDetailsList.get(i).setQuantity(offlineQuantityList.get(j).getQuantity());
//                        }else {
//                                menuDetailsList.get(i).setQuantity(quantity+offlineQuantityList.get(j).getQuantity());
//                                quantity=offlineQuantityList.get(j).getQuantity();
//                            }
//
//                        }
//
//                    }
//                }
            }

        }

        return menuDetailsList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    // get call back to Restraunt Details page
    public interface AsynResponse {
        void processFinish(List<OrderItemsEntity> updateMenuitemFromDB);
    }

}
