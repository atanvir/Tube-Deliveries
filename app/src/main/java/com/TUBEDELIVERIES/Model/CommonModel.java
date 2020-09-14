package com.TUBEDELIVERIES.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.TUBEDELIVERIES.RoomDatabase.Entity.OrderItemsEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonModel implements Parcelable {

    @SerializedName("loginResponse")
    private AuthModel loginResponse;

    @SerializedName("walletAmount")
    private String walletAmount;

    @SerializedName("topupList")
    private List<RestaurantResponse> topupList;

    @SerializedName("topupHistory")
    private List<RestaurantResponse> topupHistory;

    @SerializedName("extra_details")
    private ResponseBean extra_details;

    @SerializedName("ongoing_order")
    private List<ResponseBean> ongoing_order;

    @SerializedName("previous_order")
    private List<ResponseBean> previous_order;

    @SerializedName("cartList")
    private List<OrderItemsEntity> cartList;

    @SerializedName("menu_item")
    private List<RestaurantResponse> menu_item;

    @SerializedName("socialMediaLogin")
    private AuthModel socialMediaLogin;

    @SerializedName("userDetails")
    private ResponseBean userDetails;

    @SerializedName("menuList")
    private List<OrderItemsEntity> menuList;

    @SerializedName("favourite")
    private List<RestaurantResponse> favourite;

    @SerializedName("cuisine")
    List<ResponseBean> cuisine;

    @SerializedName("registerResponse")
    private AuthModel registerResponse;

    @SerializedName("category")
    private List<RestaurantResponse> category;

    @SerializedName("search")
    private List<RestaurantResponse> search;

    @SerializedName("filter")
    private List<RestaurantResponse> filter;

    @SerializedName("message")
    private String message;

    @SerializedName("cardList")
    private List<ResponseBean> cardList;


    @SerializedName("offerList")
    private List<RestaurantResponse> offerList;

    @SerializedName("nearBy")
    private List<RestaurantResponse> nearBy;

    @SerializedName("top_rated")
    private List<RestaurantResponse> top_rated;

    @SerializedName("detailsRestorent")
    private RestaurantResponse detailsRestorent;

    @SerializedName("homeList")
    private ResponseBean homeList;

    @SerializedName("status")
    private String status;

    @SerializedName("restorentInfo")
    private ResponseBean restorentInfo;

    @SerializedName("upcoming_order")
    @Expose
    private List<UpcomingOrder> upcomingOrder = null;

    @SerializedName("notificationList")
    @Expose
    private List<NotificationList> notificationList = null;

    @SerializedName("stories")
    @Expose
    private List<StoryModel> stories = null;

    @SerializedName("checkpayouttype")
    @Expose
    private CheckFund checkFund;

    @SerializedName("appCredit")
    @Expose
    private String appCredit;

    @SerializedName("payresponse")
    @Expose
    private ResponseBean payresponse;

    public ResponseBean getPayresponse() {
        return payresponse;
    }

    public void setPayresponse(ResponseBean payresponse) {
        this.payresponse = payresponse;
    }

    public String getAppCredit() {
        return appCredit;
    }

    public void setAppCredit(String appCredit) {
        this.appCredit = appCredit;
    }

    public CheckFund getCheckFund() {
        return checkFund;
    }

    public void setCheckFund(CheckFund checkFund) {
        this.checkFund = checkFund;
    }

    public List<StoryModel> getStories() {
        return stories;
    }

    public void setStories(List<StoryModel> stories) {
        this.stories = stories;
    }

    protected CommonModel(Parcel in) {
        walletAmount = in.readString();
        topupList = in.createTypedArrayList(RestaurantResponse.CREATOR);
        topupHistory = in.createTypedArrayList(RestaurantResponse.CREATOR);
        extra_details = in.readParcelable(ResponseBean.class.getClassLoader());
        ongoing_order = in.createTypedArrayList(ResponseBean.CREATOR);
        previous_order = in.createTypedArrayList(ResponseBean.CREATOR);
        cartList = in.createTypedArrayList(OrderItemsEntity.CREATOR);
        menu_item = in.createTypedArrayList(RestaurantResponse.CREATOR);
        userDetails = in.readParcelable(ResponseBean.class.getClassLoader());
        menuList = in.createTypedArrayList(OrderItemsEntity.CREATOR);
        favourite = in.createTypedArrayList(RestaurantResponse.CREATOR);
        cuisine = in.createTypedArrayList(ResponseBean.CREATOR);
        category = in.createTypedArrayList(RestaurantResponse.CREATOR);
        search = in.createTypedArrayList(RestaurantResponse.CREATOR);
        filter = in.createTypedArrayList(RestaurantResponse.CREATOR);
        message = in.readString();
        cardList = in.createTypedArrayList(ResponseBean.CREATOR);
        offerList = in.createTypedArrayList(RestaurantResponse.CREATOR);
        nearBy = in.createTypedArrayList(RestaurantResponse.CREATOR);
        top_rated = in.createTypedArrayList(RestaurantResponse.CREATOR);
        detailsRestorent = in.readParcelable(RestaurantResponse.class.getClassLoader());
        homeList = in.readParcelable(ResponseBean.class.getClassLoader());
        status = in.readString();
        restorentInfo = in.readParcelable(ResponseBean.class.getClassLoader());
    }

    public static final Creator<CommonModel> CREATOR = new Creator<CommonModel>() {
        @Override
        public CommonModel createFromParcel(Parcel in) {
            return new CommonModel(in);
        }

        @Override
        public CommonModel[] newArray(int size) {
            return new CommonModel[size];
        }
    };

    public List<NotificationList> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<NotificationList> notificationList) {
        this.notificationList = notificationList;
    }

    public List<UpcomingOrder> getUpcomingOrder() {
        return upcomingOrder;
    }

    public void setUpcomingOrder(List<UpcomingOrder> upcomingOrder) {
        this.upcomingOrder = upcomingOrder;
    }




    public List<RestaurantResponse> getTopupHistory() {
        return topupHistory;
    }

    public void setTopupHistory(List<RestaurantResponse> topupHistory) {
        this.topupHistory = topupHistory;
    }


    public List<RestaurantResponse> getTopupList() {
        return topupList;
    }

    public void setTopupList(List<RestaurantResponse> topupList) {
        this.topupList = topupList;
    }

    public String getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(String walletAmount) {
        this.walletAmount = walletAmount;
    }


    public List<ResponseBean> getPrevious_order() {
        return previous_order;
    }

    public void setPrevious_order(List<ResponseBean> previous_order) {
        this.previous_order = previous_order;
    }

    public List<ResponseBean> getOngoing_order() {
        return ongoing_order;
    }

    public void setOngoing_order(List<ResponseBean> ongoing_order) {
        this.ongoing_order = ongoing_order;
    }


    public List<RestaurantResponse> getMenu_item() {
        return menu_item;
    }

    public void setMenu_item(List<RestaurantResponse> menu_item) {
        this.menu_item = menu_item;
    }

    public List<OrderItemsEntity> getCartList() {
        return cartList;
    }

    public void setCartList(List<OrderItemsEntity> cartList) {
        this.cartList = cartList;
    }


    public AuthModel getSocialMediaLogin() {
        return socialMediaLogin;
    }

    public void setSocialMediaLogin(AuthModel socialMediaLogin) {
        this.socialMediaLogin = socialMediaLogin;
    }


    public ResponseBean getExtra_details() {
        return extra_details;
    }

    public void setExtra_details(ResponseBean extra_details) {
        this.extra_details = extra_details;
    }


    public List<ResponseBean> getCuisine() {
        return cuisine;
    }

    public void setCuisine(List<ResponseBean> cuisine) {
        this.cuisine = cuisine;
    }


    public void setFavourite(List<RestaurantResponse> favourite) {
        this.favourite = favourite;
    }

    public List<RestaurantResponse> getSearch() {
        return search;
    }

    public void setSearch(List<RestaurantResponse> search) {
        this.search = search;
    }


    public List<RestaurantResponse> getFilter() {
        return filter;
    }

    public void setFilter(List<RestaurantResponse> filter) {
        this.filter = filter;
    }

    public List<RestaurantResponse> getOfferList() {
        return offerList;
    }

    public void setOfferList(List<RestaurantResponse> offerList) {
        this.offerList = offerList;
    }

    public List<RestaurantResponse> getFavourite() {
        return favourite;
    }

    public List<OrderItemsEntity> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<OrderItemsEntity> menuList) {
        this.menuList = menuList;
    }


    public List<RestaurantResponse> getNearBy() {
        return nearBy;
    }

    public void setNearBy(List<RestaurantResponse> nearBy) {
        this.nearBy = nearBy;
    }


    public List<RestaurantResponse> getTop_rated() {
        return top_rated;
    }

    public void setTop_rated(List<RestaurantResponse> top_rated) {
        this.top_rated = top_rated;
    }

    public List<RestaurantResponse> getCategory() {
        return category;
    }

    public void setCategory(List<RestaurantResponse> category) {
        this.category = category;
    }


    public ResponseBean getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(ResponseBean userDetails) {
        this.userDetails = userDetails;
    }

    public RestaurantResponse getDetailsRestorent() {
        return detailsRestorent;
    }

    public void setDetailsRestorent(RestaurantResponse detailsRestorent) {
        this.detailsRestorent = detailsRestorent;
    }

    public ResponseBean getHomeList() {
        return homeList;
    }

    public void setHomeList(ResponseBean homeList) {
        this.homeList = homeList;
    }

    public AuthModel getRegisterResponse() {
        return registerResponse;
    }

    public void setRegisterResponse(AuthModel registerResponse) {
        this.registerResponse = registerResponse;
    }

    public AuthModel getLoginResponse() {
        return loginResponse;
    }

    public void setLoginResponse(AuthModel loginResponse) {
        this.loginResponse = loginResponse;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public List<ResponseBean> getCardList() {
        return cardList;
    }

    public void setCardList(List<ResponseBean> cardList) {
        this.cardList = cardList;
    }


    public ResponseBean getRestorentInfo() {
        return restorentInfo;
    }

    public void setRestorentInfo(ResponseBean restorentInfo) {
        this.restorentInfo = restorentInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(walletAmount);
        dest.writeTypedList(topupList);
        dest.writeTypedList(topupHistory);
        dest.writeParcelable(extra_details, flags);
        dest.writeTypedList(ongoing_order);
        dest.writeTypedList(previous_order);
        dest.writeTypedList(cartList);
        dest.writeTypedList(menu_item);
        dest.writeParcelable(userDetails, flags);
        dest.writeTypedList(menuList);
        dest.writeTypedList(favourite);
        dest.writeTypedList(cuisine);
        dest.writeTypedList(category);
        dest.writeTypedList(search);
        dest.writeTypedList(filter);
        dest.writeString(message);
        dest.writeTypedList(cardList);
        dest.writeTypedList(offerList);
        dest.writeTypedList(nearBy);
        dest.writeTypedList(top_rated);
        dest.writeParcelable(detailsRestorent, flags);
        dest.writeParcelable(homeList, flags);
        dest.writeString(status);
        dest.writeParcelable(restorentInfo, flags);
    }
}
