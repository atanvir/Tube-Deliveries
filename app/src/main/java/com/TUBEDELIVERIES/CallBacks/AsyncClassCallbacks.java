package com.TUBEDELIVERIES.CallBacks;

import com.TUBEDELIVERIES.RoomDatabase.Entity.OrderItemsEntity;

import java.util.List;

public interface AsyncClassCallbacks {


    void onTaskFinish(List<OrderItemsEntity> orderItemsEntityList,float totalPrice);

}
