package com.TUBEDELIVERIES.Retrofit;


import com.TUBEDELIVERIES.Model.CardDetailSaveModel;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.FilterOptionModel;
import com.TUBEDELIVERIES.Model.MyCurrentDetailsModel;
import com.TUBEDELIVERIES.Utility.AppConstants;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mahipal on 26/Sep/18.
 **/

public interface ServicesInterface {


    @FormUrlEncoded
    @POST(AppConstants.LOGIN)
    Call<CommonModel> login(@Field("email") String email,
                            @Field("password") String password,
                            @Field("type") String type,
                            @Field("device_type") String device_type,
                            @Field("phone_no") String phone_no,
                            @Field("device_token") String device_token);


    @FormUrlEncoded
    @POST(AppConstants.SIGNUP)
    Call<CommonModel> register(@Field("first_name") String first_name,
                               @Field("last_name") String last_name,
                               @Field("email") String email,
                               @Field("country_code") String country_code,
                               @Field("phone") String phone,
                               @Field("latitude") String latitude,
                               @Field("longitude") String longitude,
                               @Field("address") String address,
                               @Field("password") String password,
                               @Field("device_token") String device_token,
                               @Field("device_type") String device_type);


    @FormUrlEncoded
    @POST(AppConstants.MENU_DETAILS)
    Call<CommonModel> menuDetails(@Field("restorent_id") String restorent_id,
                                  @Field("user_id") String user_id,
                                  @Field("category_id") String category_id,
                                  @Field("type") String type,
                                  @Field("store_type") int store_type);


    @FormUrlEncoded
    @POST(AppConstants.ADDTOCART)
    Call<CommonModel> addToCart(@HeaderMap Map<String, String> headers,
                                @Field("restaurant_id") String restaurant_id,
                                @Field("type") String type,
                                @Field("menu_id") String menu_id,
                                @Field("quantity") String quantity,
                                @Field("addon") String addon,
                                @Field("price") String price,
                                @Field("newOrder") String newOrder,
                                @Field("status") String status,
                                @Field("store_type") int store_type);


    @FormUrlEncoded
    @POST(AppConstants.RESTRO_LIST)
    Call<CommonModel> restorent_list(@Field("user_id") String user_id,
                                     @Field("latitude") String latitude,
                                     @Field("longitude") String longitude,
                                     @HeaderMap Map<String, String> map);

//    @FormUrlEncoded
//    @POST(AppConstants.RESTRO_LIST)
//    Call<CommonModel> restorent_list(@Field("user_id") String user_id,
//                                     @Field("latitude") String latitude,
//                                     @Field("longitude") String longitude);


    @FormUrlEncoded
    @POST(AppConstants.SAVE_CARD)
    Call<CommonModel> save_card(@HeaderMap Map<String, String> headers,
                                @Field("card_name") String card_name,
                                @Field("card_number") String card_number,
                                @Field("card_exp_month") String card_exp_month,
                                @Field("card_exp_year") String card_exp_year,
                                @Field("card_cvv") String card_cvv);


    @FormUrlEncoded
    @POST(AppConstants.DELETE_CARD)
    Call<CommonModel> deleteCard(@HeaderMap Map<String, String> headers,
                                 @Field("card_id") String card_id);


    @POST(AppConstants.ADD_TO_CART_FROM_DB)
    Call<CommonModel> addItemFromDb(@HeaderMap Map<String, String> headers,
                                    @Body RequestBody body);

    @FormUrlEncoded
    @POST(AppConstants.FORGET_PASS)
    Call<CommonModel> forgetPass(@Field("email") String email);


    @FormUrlEncoded
    @POST(AppConstants.RESTRO_DETAILS + "{restaurant_id}")
    Call<CommonModel> restro_details(@Path("restaurant_id") String restaurant_id,
                                     @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(AppConstants.TOP_RATED_RESTRO)
    Call<CommonModel> top_rated_restro(@HeaderMap Map<String, String> headers,
                                       @Field("user_id") String user_id,
                                       @Field("latitude") String latitude,
                                       @Field("longitude") String longitude);


    @FormUrlEncoded
    @POST(AppConstants.NEAR_BY_RESTRO)
    Call<CommonModel> near_by_restro(@Field("user_id") String user_id,
                                     @Field("latitude") String latitude,
                                     @Field("longitude") String longitude);


    @FormUrlEncoded
    @POST(AppConstants.PLACE_ORDER)
    Call<CommonModel> place_order(@Field("user_id") String user_id,
                                  @Field("payment_type") String payment_type,
                                  @Field("latitude") String latitude,
                                  @Field("longitude") String longitude,
                                  @Field("user_current_address") String user_current_address,
                                  @Field("extra_notes") String extra_notes,
                                  @Field("landmark") String landmark,
                                  @Field("area") String area,
                                  @Field("address") String address,
                                  @Field("pin") String pin,
                                  @Field("price") String price,
                                  @Field("discount") String discount,
                                  @Field("totalPrice") String totalPrice,
                                  @Field("transaction_id") String transaction_id,
                                  @Field("delivery_charges") String delivery_charges,
                                  @Field("currency") String currency,
                                  @Field("coupon_id") String coupon_id,
                                  @Field("coupon_code") String coupon_code);

    @FormUrlEncoded
    @POST(AppConstants.ADD_BALANCE_TOPUP)
    Call<CommonModel> addBalance(@HeaderMap Map<String, String> headers,
                                 @Field("topup_amount") String topup_id,@Field("transcation_id") String transcation_id);


    @GET(AppConstants.USER_DETAILS)
    Call<CommonModel> userDetails(@HeaderMap Map<String, String> headers);


    @GET(AppConstants.WALLET_AMOUNT)
    Call<CommonModel> walletAmount(@HeaderMap Map<String, String> headers);


    @GET(AppConstants.TOP_UP_LIST)
    Call<CommonModel> topUpList(@HeaderMap Map<String, String> headers);


    @GET(AppConstants.TOP_UP_HISTORY)
    Call<CommonModel> topUpHistory(@HeaderMap Map<String, String> headers);


    @GET(AppConstants.SAVED_CARD_LISTING)
    Call<CommonModel> savedCardListing(@HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST(AppConstants.UPDATE_PROFILE)
    Call<CommonModel> updateProfile(@HeaderMap Map<String, String> headers,
                                    @Field("first_name") String first_name,
                                    @Field("last_name") String last_name,
                                    @Field("email") String email,
                                    @Field("country_code") String country_code,
                                    @Field("phone") String phone,
                                    @Field("area") String area,
                                    @Field("latitude") String latitude,
                                    @Field("longitude") String longitude,
                                    @Field("landmark") String landmark,
                                    @Field("address_type") String address_type,
                                    @Field("address") String address,
                                    @Field("street") String street,
                                    @Field("building") String building,
                                    @Field("floor") String floor,
                                    @Field("apartment") String sapartment);



    @FormUrlEncoded
    @POST(AppConstants.LIST_MENU_FILTER)
    Call<CommonModel> listmenuFilter(@HeaderMap Map<String, String> headers,
                                     @Field("restorent_id") String restorent_id,
                                     @Field("category_id") String category_id,
                                     @Field("type") String type,
                                     @Field("item_type") String item_type,
                                     @Field("eat_type") String eat_type,
                                     @Field("taste") String taste,
                                     @Field("store_type") int storeType);


    @FormUrlEncoded
    @POST(AppConstants.RATING_REVIEW)
    Call<CommonModel> rating_review(@HeaderMap Map<String, String> headers,
                                    @Field("restorent_id") String restorent_id,
                                    @Field("order_id") String order_id,
                                    @Field("rating") String rating,
                                    @Field("review") String review);


    @FormUrlEncoded
    @POST(AppConstants.FILTER_RESTAURANT)
    Call<CommonModel> searchFilter(@HeaderMap Map<String, String> headers,
                                   @Field("price") String price,
                                   @Field("is_veg") String is_veg,
                                   @Field("is_non_veg") String is_non_veg,
                                   @Field("rating") String rating,
                                   @Field("cuisine") String cuisine);

    @FormUrlEncoded
    @POST(AppConstants.SAVE_FAVOURITE)
    Call<CommonModel> markFav(@HeaderMap Map<String, String> headers,
                              @Field("restaurant_id") String restaurant_id,
                              @Field("is_favourite") String is_favourite);


    @GET(AppConstants.LOGOUT)
    Call<CommonModel> logout(@HeaderMap Map<String, String> headers);

    @GET(AppConstants.USER_FAVORITE)
    Call<CommonModel> getFavorites(@Header("Authorization") String Authorization);

    @FormUrlEncoded
    @POST(AppConstants.CART_LISTING)
    Call<CommonModel> myCart(@Field("user_id") String user_id);


    @GET(AppConstants.RESTRO_INFO + "{restaurant_id}")
    Call<CommonModel> restaurantInfo(@Path("restaurant_id") String restaurant_id);


    @GET(AppConstants.OFFERS)
    Call<CommonModel> offers();


    @GET(AppConstants.CUISINES_LIST)
    Call<CommonModel> cusineList();


    @FormUrlEncoded
    @POST(AppConstants.SEARCH_RESTAURANT)
    Call<CommonModel> getSearchRestaurants(
            @Field("user_id") String user_id,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude);


    @FormUrlEncoded
    @POST(AppConstants.GLOBAL_SEARCH)
    Call<CommonModel> GlobalSearch(@Field("user_id") String user_id,
                                   @Field("search_key") String search_key);


    @FormUrlEncoded
    @POST(AppConstants.USER_PASSWORD)
    Call<CommonModel> changePassword(@Header("Authorization") String Authorization,
                                     @Field("old_password") String old_password,
                                     @Field("new_password") String new_password);

    @FormUrlEncoded
    @POST(AppConstants.ONGOING_ORDER)
    Call<CommonModel> getOngoingOrder(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(AppConstants.UPCOMING_ORDER)
    Call<CommonModel> getUpcomingOrder(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(AppConstants.PREVIOUS_ORDER)
    Call<CommonModel> getPreviousOrder(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(AppConstants.CANCEL_ORDER)
    Call<CommonModel> cancel_order(@Field("order_number") String order_number);

    @FormUrlEncoded
    @POST(AppConstants.REORDER)
    Call<CommonModel> reoder(@Field("user_id") String user_id,
                             @Field("order_id") String order_id);


    @FormUrlEncoded
    @POST(AppConstants.SOCIAL_MEDIA_LOGIN)
    Call<CommonModel> socialMedialLogin(@Field("social_id") String social_id,
                                        @Field("facebook_id") String facebook_id,
                                        @Field("google_id") String google_id,
                                        @Field("signup_by") String signup_by,
                                        @Field("first_name") String first_name,
                                        @Field("last_name") String last_name,
                                        @Field("email") String email,
                                        @Field("device_type") String device_type,
                                        @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST(AppConstants.BEFORE_SIGNUP_CHECK)
    Call<CommonModel> beforeSignUpCheck(@Header("Authorization") String Authorization,
                                        @Field("phone") String phone,
                                        @Field("email") String email);



    @POST(AppConstants.CART_DETAIL_SAVE)
    Call<CommonModel> cardDetailSave(@HeaderMap Map<String, String> map,
                                     @Body CardDetailSaveModel model);


    @POST(AppConstants.MY_NOTIFICATION)
    Call<CommonModel> getNotification(@HeaderMap Map<String, String> map);


    @GET(AppConstants.STORIES)
    Call<CommonModel> stories();

    @GET("paymenterror")
    Call<CommonModel> paymentError(@Query("token") String token);

    @GET("success/{m_payment_id}")
    Call<CommonModel> paymentsuccess(@Path("m_payment_id") String m_payment_id);

    @GET(AppConstants.SUBSCRIPTION_DETAILS)
    Call<JsonObject> subscriptionDetail(@HeaderMap Map<String, String> map);

    @FormUrlEncoded
    @POST(AppConstants.ADD_SUBSCRIPTIONS)
    Call<CommonModel> addSubcription(@HeaderMap Map<String,String> headerToken,@Field("transcation_id") String transcation_id,@Field("type") String type,@Field("amount") String amount,@Field("change") String change);

    @GET(AppConstants.CENCEL_SUBSCRIPTION)
    Call<CommonModel> subscriptionCancel(@HeaderMap Map<String, String> map);

    @FormUrlEncoded
    @POST(AppConstants.CHECK_FUNDS)
    Call<CommonModel> checkfundsApi(@HeaderMap Map<String, String> map,@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(AppConstants.NEAR_BY_GROCERY)
    Call<CommonModel> nearByGrocery(@HeaderMap Map<String, String> headers,
                                    @Field("user_id") String user_id,
                                    @Field("latitude") String latitude,
                                    @Field("longitude") String longitude);

    @GET(AppConstants.FILTER_OPTION)
    Call<FilterOptionModel> filterOption();

    @FormUrlEncoded
    @POST(AppConstants.RESTRO_FILTER)
    Call<CommonModel> filter(@Field("store_type") String store_type,
                             @Field("cuisine") String cuisine,
                             @Field("categories") String categories,
                             @Field("is_veg") String is_veg,
                             @Field("is_non_veg") String is_non_veg,
                             @Field("rating") String rating,
                             @Field("distance") String distance,
                             @Field("latitude") String latitude,
                             @Field("longitude") String longitude);

    @GET(AppConstants.GET_MY_DETAILS)
    Call<MyCurrentDetailsModel> getMyDeatails();

    @GET(AppConstants.GET_CURRENCY)
    Call<MyCurrentDetailsModel> getCurrency(@Path("code") String countryCode);


    @GET(AppConstants.EXCHAGE_RATE)
    Call<JsonObject> getExchangeRate(@Query("base") String base, @Query("symbols") String symbols);

    @GET(AppConstants.GET_CURRENCY_SYMBOL)
    Call<JsonObject> getAllCurrencySymbols();

    @FormUrlEncoded
    @POST(AppConstants.CONFIRM_ORDER)
    Call<CommonModel> confirmOrder(@Field("order_number") String order_number);

    @FormUrlEncoded
    @POST(AppConstants.APPLY_COUPON)
    Call<CommonModel> applyCoupon(@Field("user_id") String user_id, @Field("coupon_code") String coupon_code);
}






