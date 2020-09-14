package com.TUBEDELIVERIES.RoomDatabase.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.RoomDatabase.Entity.OrderItemsEntity;

import java.util.List;

@Dao
public interface RestaurantDao {


    @Insert
    void insertAll(RestaurantResponse RestaurantDetailsEntity);


    /**
     * insert orderMenu on Plus click
     *
     */
    @Insert
    void insertOrderMenu(OrderItemsEntity orderItemsEntity);


    @Query("SELECT * FROM OrderItemsEntity")
    OrderItemsEntity fetchAllOrderMenu();


    /**
     * get order menuList
     *
     */
    @Query("SELECT * FROM OrderItemsEntity")
    List<OrderItemsEntity> fetchAllOrderMenuAsList();

    /**
     * get order menuList Total Price
     *
     */
    @Query("SELECT SUM(price) from OrderItemsEntity")
    float getTotalPrice();


    /**
     * get order menuList Total Quantity
     *
     */
    @Query("SELECT SUM(quantity) from OrderItemsEntity where id=:id")
    int getTotalQuantity(String id);

    /**
     * get order menuList using category
     *
     */
    @Query("SELECT * FROM OrderItemsEntity  where catname=:catergory")
    List<OrderItemsEntity> OrderMenuListViaCategory(String catergory);



    /**
     * get order menuList by id
     *
     */
    @Query("SELECT * FROM OrderItemsEntity where id=:id")
    List<OrderItemsEntity> fetchOrderMenuListById(String id);



    /**
     * Updating only price nd quantitiy
     * By  id for orderMenuTable
     */
//    @Query("UPDATE OrderItemsEntity SET price=:price+price, quantity=:quantity+quantity   WHERE id = :id")

    //    void updateOrderPricefor Normal menu(int price, int quantity,String id);
    @Query("UPDATE OrderItemsEntity SET price=:price+price, quantity=:quantity+quantity,TotalQuantity=:totalQuantity+TotalQuantity  WHERE id = :id")
    void updateOrderPrice(float price, int quantity,String id,int totalQuantity);


    //    void updateOrderPricefor Customized menu(int price, int quantity,String id);
    @Query("UPDATE OrderItemsEntity SET price=:price+price, quantity=:quantity+quantity,TotalQuantity=:totalQuantity+TotalQuantity  WHERE id = :id AND addOns=:addOns")
    void updateCustomizedOrder(float price, int quantity,String id,int totalQuantity,String addOns);



    // remove item by one value
    @Query("UPDATE OrderItemsEntity SET price=:price, quantity=:quantity  WHERE id = :id")
    void removeOrder(float price, int quantity,String id);


    //delete orderItem from db
    @Query("Delete  FROM OrderItemsEntity  where id = :id")
    void deleteOrderItem(String id);



    //delete Customization orderItem from db
    @Query("Delete  FROM OrderItemsEntity  where id = :id AND addOns=:addOns")
    void deleteCustomizationOrder(String id,String addOns);

    //get Customization orderItem with same addOn from db
    @Query("SELECT *  FROM OrderItemsEntity  where id = :id AND addOns=:addOns")
    boolean getSameAddonItems(String id,String addOns);


    @Query("SELECT * FROM RestaurantResponse")
    RestaurantResponse fetchAll();


    @Query("SELECT * FROM RestaurantResponse Where id=:ID")
    RestaurantResponse getRestaurantFromId(String ID);

    @Update
    void updateAll(RestaurantResponse RestaurantDetailsEntity);


    //delete all orderItem from db
    @Query("DELETE FROM OrderItemsEntity")
    public void deleteAllData();

}