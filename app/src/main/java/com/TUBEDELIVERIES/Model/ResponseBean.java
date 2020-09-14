package com.TUBEDELIVERIES.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahipal Singh on 23,May,2019
 * mahisingh1@outlook.com
 */

public class ResponseBean implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("en_name")
    private String en_name;
    private boolean isCheck;
    @SerializedName("order_id")
    private String order_id;
    @SerializedName("restaurant_image")
    private String restaurant_image;
    @SerializedName(value="email_id",alternate = {"email"})
    private String email_id;
    @SerializedName("area")
    private String area;
    @SerializedName("order_place_time")
    private String order_place_time;
    @SerializedName("orderDate")
    private String orderDate;
    @SerializedName("card_name")
    private String card_name;
    @SerializedName("card_number")
    private String card_number;
    @SerializedName("card_exp_month")
    private String card_exp_month;
    @SerializedName("card_exp_year")
    private String card_exp_year;
    @SerializedName("payment_mode")
    private String payment_mode;
    @SerializedName("expected_delivery_time")
    private String expected_delivery_time;
    @SerializedName("delivery_peroson")
    private String delivery_peroson;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("payment_type")
    private String payment_type;
    @SerializedName("restorent_image")
    private String restorent_image;
    @SerializedName("deliveryStatus")
    private String deliveryStatus;
    @SerializedName("book_date")
    private String book_date;
    @SerializedName("order_number")
    private String order_number;
    @SerializedName("orderd_time")
    private String orderd_time;
    @SerializedName("image")
    private String image;
    @SerializedName("delivered_time")
    private String delivered_time;
    @SerializedName("menu_data")
    private List<RestaurantResponse> menu_data;
    @SerializedName("orderMenu")
    private List<RestaurantResponse> orderMenu;
    @SerializedName("price")
    private String price;
    @SerializedName(value = "restaurent_name",alternate = "restorent_name")
    private String restaurent_name;
    @SerializedName("add_on_name")
    private String add_on_name;
    @SerializedName("total_price")
    private String total_price;
    @SerializedName("total_paid_amount")
    private String total_paid_amount;
    @SerializedName("discount")
    private String discount;
    @SerializedName(value = "restaurant_id",alternate = "restorent_id")
    private String restaurent_id;
    @SerializedName("add_on_price")
    private String add_on_price;
    @SerializedName("restorent_time")
    private ArrayList<String> restorent_time;
    @SerializedName("address")
    private String address;
    @SerializedName("address_type")
    private String address_type;
    @SerializedName("phone")
    private String phone;
    @SerializedName("landmark")
    private String landmark;
    @SerializedName("pin")
    private String pin;
    @SerializedName("name")
    private String name;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("country_code")
    private String country_code;
    @SerializedName("access_token")
    private String access_token;
    @SerializedName("nearBy")
    private List<RestaurantResponse> nearBy;
    @SerializedName("top_rated")
    private List<RestaurantResponse> top_rated;
    @SerializedName("offer")
    private List<RestaurantResponse> offer;
    @SerializedName("restorent_review")
    private List<RestaurantResponse> restorent_review;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("description")
    private String description;
     @SerializedName("total_review")
    private String total_review;
     @SerializedName("cancel_status")
     private String cancel_status;
     @SerializedName("cancel_time")
     private String cancel_time;
    @SerializedName( "restaurant_name")
    private String restaurant_name;
    @SerializedName("restaurent_address")
    private String restaurent_address;
    @SerializedName("totalPrice")
    private String totalPrice;
    @SerializedName("user_current_address")
    private String user_current_address;
    @SerializedName("total_rating")
    private String total_rating;

    @SerializedName("street")
    private String street;

    @SerializedName("building")
    private String building;

    @SerializedName("floor")
    private String floor;

    @SerializedName("apartment")
    private String apartment;


    @SerializedName("offered_service")
    private String offered_service;

    @SerializedName("offere_details")
    @Expose
    private List<OffereDetail> offereDetails = null;

    @SerializedName("nearByGrocery")
    @Expose
    private List<NearByGrocery> nearByGrocery = null;

    @SerializedName("eat_option")
    @Expose
    private long eatOption;
    @SerializedName("pickup_date")
    @Expose
    private String pickupDate;
    @SerializedName("pickup_time")
    @Expose
    private String pickupTime;
    @SerializedName("vist_date")
    @Expose
    private String vistDate;
    @SerializedName("vist_time")
    @Expose
    private String vistTime;
    @SerializedName("no_of_people")
    @Expose
    private String noOfPeople;
    @SerializedName("cart_address")
    @Expose
    private String cartAddress;
    @SerializedName("appCredit")
    @Expose
    private String appCredit;

    @SerializedName("store_type")
    @Expose
    private String store_type;

    @SerializedName("pf_payment_id")
    @Expose
    private String pf_payment_id;

    @SerializedName("payment_status")
    @Expose
    private String payment_status;

    @SerializedName("delivery_price")
    @Expose
    private String delivery_price;
    @SerializedName("currency")
    @Expose
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public String getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(String delivery_price) {
        this.delivery_price = delivery_price;
    }

    public String getPf_payment_id() {
        return pf_payment_id;
    }

    public void setPf_payment_id(String pf_payment_id) {
        this.pf_payment_id = pf_payment_id;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    protected ResponseBean(Parcel in) {
        id = in.readString();
        en_name = in.readString();
        isCheck = in.readByte() != 0;
        order_id = in.readString();
        restaurant_image = in.readString();
        email_id = in.readString();
        area = in.readString();
        order_place_time = in.readString();
        orderDate = in.readString();
        card_name = in.readString();
        card_number = in.readString();
        card_exp_month = in.readString();
        card_exp_year = in.readString();
        payment_mode = in.readString();
        expected_delivery_time = in.readString();
        delivery_peroson = in.readString();
        phone_number = in.readString();
        payment_type = in.readString();
        restorent_image = in.readString();
        deliveryStatus = in.readString();
        book_date = in.readString();
        order_number = in.readString();
        orderd_time = in.readString();
        image = in.readString();
        delivered_time = in.readString();
        menu_data = in.createTypedArrayList(RestaurantResponse.CREATOR);
        orderMenu = in.createTypedArrayList(RestaurantResponse.CREATOR);
        price = in.readString();
        restaurent_name = in.readString();
        add_on_name = in.readString();
        total_price = in.readString();
        total_paid_amount = in.readString();
        discount = in.readString();
        restaurent_id = in.readString();
        add_on_price = in.readString();
        restorent_time = in.createStringArrayList();
        address = in.readString();
        address_type = in.readString();
        phone = in.readString();
        landmark = in.readString();
        pin = in.readString();
        name = in.readString();
        first_name = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        country_code = in.readString();
        access_token = in.readString();
        nearBy = in.createTypedArrayList(RestaurantResponse.CREATOR);
        top_rated = in.createTypedArrayList(RestaurantResponse.CREATOR);
        offer = in.createTypedArrayList(RestaurantResponse.CREATOR);
        restorent_review = in.createTypedArrayList(RestaurantResponse.CREATOR);
        last_name = in.readString();
        description = in.readString();
        total_review = in.readString();
        cancel_status = in.readString();
        cancel_time = in.readString();
        restaurant_name = in.readString();
        restaurent_address = in.readString();
        totalPrice = in.readString();
        user_current_address = in.readString();
        total_rating = in.readString();
        street = in.readString();
        building = in.readString();
        floor = in.readString();
        apartment = in.readString();
        offered_service = in.readString();
        offereDetails = in.createTypedArrayList(OffereDetail.CREATOR);
        eatOption = in.readLong();
        pickupDate = in.readString();
        pickupTime = in.readString();
        vistDate = in.readString();
        vistTime = in.readString();
        noOfPeople = in.readString();
        cartAddress = in.readString();
        appCredit = in.readString();
        store_type = in.readString();
        pf_payment_id=in.readString();
        payment_status=in.readString();
        delivery_price=in.readString();
        currency=in.readString();
    }

    public static final Creator<ResponseBean> CREATOR = new Creator<ResponseBean>() {
        @Override
        public ResponseBean createFromParcel(Parcel in) {
            return new ResponseBean(in);
        }

        @Override
        public ResponseBean[] newArray(int size) {
            return new ResponseBean[size];
        }
    };

    public String getStore_type() {
        return store_type;
    }

    public void setStore_type(String store_type) {
        this.store_type = store_type;
    }

    public List<OffereDetail> getOffereDetails() {
        return offereDetails;
    }

    public void setOffereDetails(List<OffereDetail> offereDetails) {
        this.offereDetails = offereDetails;
    }

    public String getOffered_service() {
        return offered_service;
    }

    public void setOffered_service(String offered_service) {
        this.offered_service = offered_service;
    }

    public long getEatOption() {
        return eatOption;
    }

    public void setEatOption(long eatOption) {
        this.eatOption = eatOption;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getVistDate() {
        return vistDate;
    }

    public void setVistDate(String vistDate) {
        this.vistDate = vistDate;
    }

    public String getVistTime() {
        return vistTime;
    }

    public void setVistTime(String vistTime) {
        this.vistTime = vistTime;
    }

    public String getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(String noOfPeople) {
        this.noOfPeople = noOfPeople;
    }

    public String getCartAddress() {
        return cartAddress;
    }

    public void setCartAddress(String cartAddress) {
        this.cartAddress = cartAddress;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getRestaurant_image() {
        return restaurant_image;
    }

    public void setRestaurant_image(String restaurant_image) {
        this.restaurant_image = restaurant_image;
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCard_exp_month() {
        return card_exp_month;
    }

    public void setCard_exp_month(String card_exp_month) {
        this.card_exp_month = card_exp_month;
    }

    public String getCard_exp_year() {
        return card_exp_year;
    }

    public void setCard_exp_year(String card_exp_year) {
        this.card_exp_year = card_exp_year;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getDelivered_time() {
        return delivered_time;
    }

    public void setDelivered_time(String delivered_time) {
        this.delivered_time = delivered_time;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrderd_time() {
        return orderd_time;
    }

    public void setOrderd_time(String orderd_time) {
        this.orderd_time = orderd_time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<RestaurantResponse> getOrderMenu() {
        return orderMenu;
    }

    public void setOrderMenu(List<RestaurantResponse> orderMenu) {
        this.orderMenu = orderMenu;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public List<RestaurantResponse> getMenu_item() {
        return menu_data;
    }

    public void setMenu_item(List<RestaurantResponse> menu_data) {
        this.menu_data = menu_data;
    }

    public String getDelivery_status() {
        return deliveryStatus;
    }

    public void setDelivery_status(String delivery_status) {
        this.deliveryStatus = delivery_status;
    }

    public String getBook_date() {
        return book_date;
    }

    public void setBook_date(String book_date) {
        this.book_date = book_date;
    }

    public String getRestorent_image() {
        return restorent_image;
    }

    public void setRestorent_image(String restorent_image) {
        this.restorent_image = restorent_image;
    }

    public String getOrder_place_time() {
        return order_place_time;
    }

    public void setOrder_place_time(String order_place_time) {
        this.order_place_time = order_place_time;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getExpected_delivery_time() {
        return expected_delivery_time;
    }

    public void setExpected_delivery_time(String expected_delivery_time) {
        this.expected_delivery_time = expected_delivery_time;
    }

    public String getDelivery_peroson() {
        return delivery_peroson;
    }

    public void setDelivery_peroson(String delivery_peroson) {
        this.delivery_peroson = delivery_peroson;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public List<RestaurantResponse> getMenu_data() {
        return menu_data;
    }

    public void setMenu_data(List<RestaurantResponse> menu_data) {
        this.menu_data = menu_data;
    }

    public String getTotal_paid_amount() {
        return total_paid_amount;
    }

    public void setTotal_paid_amount(String total_paid_amount) {
        this.total_paid_amount = total_paid_amount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRestaurent_id() {
        return restaurent_id;
    }

    public void setRestaurent_id(String restaurent_id) {
        this.restaurent_id = restaurent_id;
    }

    public ArrayList<String> getRestorent_time() {
        return restorent_time;
    }

    public void setRestorent_time(ArrayList<String> restorent_time) {
        this.restorent_time = restorent_time;
    }

    public List<RestaurantResponse> getOffer() {
        return offer;
    }

    public void setOffer(List<RestaurantResponse> offer) {
        this.offer = offer;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public List<RestaurantResponse> getRestorent_review() {
        return restorent_review;
    }

    public void setRestorent_review(List<RestaurantResponse> restorent_review) {
        this.restorent_review = restorent_review;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCancel_status() {
        return cancel_status;
    }

    public void setCancel_status(String cancel_status) {
        this.cancel_status = cancel_status;
    }

    public String getCancel_time() {
        return cancel_time;
    }

    public void setCancel_time(String cancel_time) {
        this.cancel_time = cancel_time;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRestaurent_name() {
        return restaurant_name;
    }

    public void setRestaurent_name(String restaurent_name) {
        this.restaurent_name = restaurent_name;
    }

    public String getRestaurent_address() {
        return restaurent_address;
    }

    public void setRestaurent_address(String restaurent_address) {
        this.restaurent_address = restaurent_address;
    }

    public String getUser_current_address() {
        return user_current_address;
    }

    public void setUser_current_address(String user_current_address) {
        this.user_current_address = user_current_address;
    }

    public String getTotal_review() {
        return total_review;
    }

    public void setTotal_review(String total_review) {
        this.total_review = total_review;
    }

    public String getTotal_rating() {
        return total_rating;
    }

    public void setTotal_rating(String total_rating) {
        this.total_rating = total_rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAdd_on_name() {
        return add_on_name;
    }

    public void setAdd_on_name(String add_on_name) {
        this.add_on_name = add_on_name;
    }

    public String getAdd_on_price() {
        return add_on_price;
    }

    public void setAdd_on_price(String add_on_price) {
        this.add_on_price = add_on_price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public List<NearByGrocery> getNearByGrocery() {
        return nearByGrocery;
    }

    public void setNearByGrocery(List<NearByGrocery> nearByGrocery) {
        this.nearByGrocery = nearByGrocery;
    }

    public String getAppCredit() {
        return appCredit;
    }

    public void setAppCredit(String appCredit) {
        this.appCredit = appCredit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(en_name);
        parcel.writeByte((byte) (isCheck ? 1 : 0));
        parcel.writeString(order_id);
        parcel.writeString(restaurant_image);
        parcel.writeString(email_id);
        parcel.writeString(area);
        parcel.writeString(order_place_time);
        parcel.writeString(orderDate);
        parcel.writeString(card_name);
        parcel.writeString(card_number);
        parcel.writeString(card_exp_month);
        parcel.writeString(card_exp_year);
        parcel.writeString(payment_mode);
        parcel.writeString(expected_delivery_time);
        parcel.writeString(delivery_peroson);
        parcel.writeString(phone_number);
        parcel.writeString(payment_type);
        parcel.writeString(restorent_image);
        parcel.writeString(deliveryStatus);
        parcel.writeString(book_date);
        parcel.writeString(order_number);
        parcel.writeString(orderd_time);
        parcel.writeString(image);
        parcel.writeString(delivered_time);
        parcel.writeTypedList(menu_data);
        parcel.writeTypedList(orderMenu);
        parcel.writeString(price);
        parcel.writeString(restaurent_name);
        parcel.writeString(add_on_name);
        parcel.writeString(total_price);
        parcel.writeString(total_paid_amount);
        parcel.writeString(discount);
        parcel.writeString(restaurent_id);
        parcel.writeString(add_on_price);
        parcel.writeStringList(restorent_time);
        parcel.writeString(address);
        parcel.writeString(address_type);
        parcel.writeString(phone);
        parcel.writeString(landmark);
        parcel.writeString(pin);
        parcel.writeString(name);
        parcel.writeString(first_name);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(country_code);
        parcel.writeString(access_token);
        parcel.writeTypedList(nearBy);
        parcel.writeTypedList(top_rated);
        parcel.writeTypedList(offer);
        parcel.writeTypedList(restorent_review);
        parcel.writeString(last_name);
        parcel.writeString(description);
        parcel.writeString(total_review);
        parcel.writeString(cancel_status);
        parcel.writeString(cancel_time);
        parcel.writeString(restaurant_name);
        parcel.writeString(restaurent_address);
        parcel.writeString(totalPrice);
        parcel.writeString(user_current_address);
        parcel.writeString(total_rating);
        parcel.writeString(street);
        parcel.writeString(building);
        parcel.writeString(floor);
        parcel.writeString(apartment);
        parcel.writeString(offered_service);
        parcel.writeTypedList(offereDetails);
        parcel.writeLong(eatOption);
        parcel.writeString(pickupDate);
        parcel.writeString(pickupTime);
        parcel.writeString(vistDate);
        parcel.writeString(vistTime);
        parcel.writeString(noOfPeople);
        parcel.writeString(cartAddress);
        parcel.writeString(appCredit);
        parcel.writeString(store_type);
        parcel.writeString(pf_payment_id);
        parcel.writeString(payment_status);
        parcel.writeString(delivery_price);
        parcel.writeString(currency);
    }
}
