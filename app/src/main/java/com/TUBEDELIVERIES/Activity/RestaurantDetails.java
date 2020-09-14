package com.TUBEDELIVERIES.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TUBEDELIVERIES.Adapter.MenuDetailAdapter;
import com.TUBEDELIVERIES.Adapter.MenuAdapter;
import com.TUBEDELIVERIES.BottomSheet.ChooseYourTasteBottomSheet;
import com.TUBEDELIVERIES.BottomSheet.MenuItemBottomSheet;
import com.TUBEDELIVERIES.BottomSheet.RestroInfoBottomSheet;
import com.TUBEDELIVERIES.CallBacks.AsyncClassCallbacks;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.MenuListModel;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.RoomDatabase.DB.JustBiteDataBase;
import com.TUBEDELIVERIES.RoomDatabase.Entity.AddonsEntity;
import com.TUBEDELIVERIES.RoomDatabase.Entity.OrderItemsEntity;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.MyApplication;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.TUBEDELIVERIES.backgroundClasses.AddCustomisedMenuAsync;
import com.TUBEDELIVERIES.backgroundClasses.OrderedItemQuantity;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Mahipal Singh on 14,JUne,2019
 * mahisingh1@outlook.com
 */


public class RestaurantDetails extends AppCompatActivity implements MenuDetailAdapter.OnCustomizeClickListener, MenuAdapter.onMenuClick, ChooseYourTasteBottomSheet.oncustomizationClickListener, OrderedItemQuantity.AsynResponse, AsyncClassCallbacks {

    @BindView(R.id.tvAvgRating)
    TextView tvAvgRating;

    @BindView(R.id.recycerAddToCartHori)
    RecyclerView recyclerAddToCartHori;

    @BindView(R.id.tvRestroTitle)
    TextView tvRestroTitle;

    @BindView(R.id.lottieAnimationView)
    LottieAnimationView lottieAnimationView;

    @BindView(R.id.tvMinOrderAddToCard)
    TextView tvMinOrderAddToCard;

    @BindView(R.id.tvTimeAddToCard)
    TextView tvTimeAddToCard;

    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    @BindView(R.id.tvRestroAddress)
    TextView tvRestroAddress;

    @BindView(R.id.tvAddToCart)
    TextView tvAddToCart;

    @BindView(R.id.ivFav)
    ImageView ivFav;

    @BindView(R.id.ivRestroImage)
    ImageView ivRestroImage;

    @BindView(R.id.tvReview)
    TextView tvReview;

    @BindView(R.id.ivRating)
    RatingBar ivRating;

    @BindView(R.id.recyclerAddToCartVertically)
    RecyclerView recyclerAddToCartVertically;

    @BindView(R.id.tvdistance)
    TextView tvdistance;

    @BindView(R.id.llCousine)
    LinearLayout llCousine;

    RestaurantResponse serverResponse;

    private MenuItemBottomSheet menuItemBottomSheet;
    private RestaurantResponse restaurantResponse = new RestaurantResponse();
    private ArrayList<MenuListModel> menu = new ArrayList<>();
    private RestaurantResponse menuListModel;
    private String restro_id = "";
    private String item_type="";
    private String taste="";
    private String eat_type="";
    private String vegNogType = "";
    private List<RestaurantResponse> menuList = null;
    private List<OrderItemsEntity> menuDetailsList = null;
    private MenuAdapter menuAdapter = null;
    private MenuDetailAdapter menuDetailAdapter = null;
    private String filterPrice = "";
    private String itemType = "";
    private String catergory_id = "";
    private int storeType;


    //check if customiztion is repeat or not

    //For Database purpose
    private JustBiteDataBase dataBase;
    private boolean isCustomizationRepeat = false;
    private int menuRefrenceId;
    private String restaurantname = "";
    private String restaurantAddress = "";
    // offline model class
    OrderItemsEntity orderItemsEntity;
    private List<OrderItemsEntity> offlineQuantityList = null;


    boolean customizationCLick = false;


    String addOnId = "";

    float totalCustomPrice = 0;

    //get pos for cusomize menu
    private int customizationItemPos;

    // get quantity for customisaton item
    private int customizationItemQuantity;


    // 1 for normal category 2 for combo category
    private String catergoryCombo = "";

    ////is fav/////
    private String isFavStatus;

    @BindView(R.id.ivSearchAddToCart)
    SearchView ivSearchAddToCart;

    @BindView(R.id.tvMenu)
    TextView tvMenu;

    @BindView(R.id.ivDistance)
    ImageView ivDistance;
    private String currency,symbol;
    private Double exchangeRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_to_card);
        currency= SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.CURRENCY);
        exchangeRate=Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.EXCHANGE_RATE));
        symbol=SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.CURRENCY_SYMBOL);
        new CommonUtil().setStatusBarGradiant(this,RestaurantDetails.class.getSimpleName());
        ButterKnife.bind(this);

        tvdistance.setOnClickListener(this::onClick);
        ivDistance.setOnClickListener(this::onClick);

        getIntentData();
        restroDetails(restro_id);
        setUpHorizontalRecycler();

        ivSearchAddToCart.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    tvMenu.setVisibility(View.GONE);
                } else {
                    tvMenu.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick({R.id.ivInfo, R.id.ivSideMenu, R.id.tvAddToCart,
            R.id.tvReview, R.id.ivBackAddToCart,
            R.id.ivFilterAddToCart, R.id.ivFav})

    public void onClick(View view) {
        Bundle bundle;
        Intent intent;
        switch (view.getId()) {

            case R.id.ivInfo:
            RestroInfoBottomSheet bottomSheet = new RestroInfoBottomSheet();
            bundle = new Bundle();
            bundle.putString(ParamEnum.RESTRO_ID.theValue(), restro_id);
            bottomSheet.setArguments(bundle);
            bottomSheet.show(getSupportFragmentManager(), "");
            break;

            case R.id.ivSideMenu:
            menuItemBottomSheet = new MenuItemBottomSheet();
            bundle = new Bundle();
            bundle.putParcelableArrayList(ParamEnum.MENU.theValue(), (ArrayList<? extends Parcelable>) menuList);
            menuItemBottomSheet.setArguments(bundle);
            menuItemBottomSheet.show(getSupportFragmentManager(), "");
            setClick();
            break;

            case R.id.tvAddToCart:
            intent = new Intent(this, MyCartActivity.class);
            intent.putExtra(ParamEnum.RESTAURANT_NAME.theValue(), restaurantname);
            intent.putExtra(ParamEnum.RESTRO_ID.theValue(), restro_id);
            intent.putExtra(ParamEnum.RESTAURANT_ADDRESS.theValue(), restaurantAddress);
            startActivity(intent);
            break;

            case R.id.tvReview:
            intent = new Intent(this, RatingAndReviewActivity.class);
            intent.putExtra(ParamEnum.RESTRO_ID.theValue(), restro_id);
            startActivity(intent);
            break;

            case R.id.ivBackAddToCart:
            onBackPressed();
            break;

            case R.id.ivFilterAddToCart:
            Intent intent1=new Intent(RestaurantDetails.this, MenuFilterActivity.class);
            intent1.putExtra(ParamEnum.RESTRO_ID.theValue(),restro_id);
            intent1.putExtra(ParamEnum.CATEGORY_ID.theValue(),catergory_id);
            intent1.putExtra(ParamEnum.CATEGORY_COMBO.theValue(),catergoryCombo);
            intent1.putExtra(ParamEnum.ITEM_TYPE.theValue(),item_type);
            intent1.putExtra(ParamEnum.EAT_TYPE.theValue(),eat_type);
            intent1.putExtra(ParamEnum.TASTE.theValue(),taste);
            intent1.putExtra(ParamEnum.STORE_TYPE.theValue(),storeType);
            startActivityForResult(intent1,101);
            break;

            case R.id.ivFav:
            if (isFavStatus.equalsIgnoreCase(ParamEnum.ZERO.theValue())) {
                markAsFav("0");
                // startHeartAnimation();
            } else {
                markAsFav("1");
                //startHeartAnimation();
            }
            break;


            case R.id.tvdistance:
            break;


            case R.id.ivDistance:
            dispactchAddressPicker();
            break;

        }
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void dispactchAddressPicker(){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.google.com")
                .appendPath("maps")
                .appendPath("dir")
                .appendPath("")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("destination", Double.parseDouble(serverResponse.getLatitude()) + "," + Double.parseDouble(serverResponse.getLongitude()));
        String url = builder.build().toString();
        Log.d("Directions", url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void setUpHorizontalRecycler() {
        recyclerAddToCartHori.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        menuList = new ArrayList<>();
        menuAdapter = new MenuAdapter(this, menuList, this);
        recyclerAddToCartHori.setAdapter(menuAdapter);


        /////// menu details adpater////
        recyclerAddToCartVertically.setLayoutManager(new LinearLayoutManager(this));
        menuDetailsList = new ArrayList<>();

        ivSearchAddToCart.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                menuDetailAdapter.getFilter().filter(newText);
                return true;
            }
        });

        menuDetailAdapter = new MenuDetailAdapter(this, this, menuDetailsList);
        recyclerAddToCartVertically.setAdapter(menuDetailAdapter);
        recyclerAddToCartVertically.setNestedScrollingEnabled(false);
    }


    @Override
    public void onCustomizeClick(OrderItemsEntity response, int position) {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 101:
            if(resultCode==RESULT_OK)
            {
                menuDetailsList.clear();
                menuDetailsList.addAll(data.getParcelableArrayListExtra("menuDetailsList"));
                menuDetailAdapter.updateMenuDetailsList(menuDetailsList);
                item_type=data.getStringExtra(ParamEnum.ITEM_TYPE.theValue());
                eat_type=data.getStringExtra(ParamEnum.EAT_TYPE.theValue());
                taste=data.getStringExtra(ParamEnum.TASTE.theValue());
            }

            break;

        }

    }

    @Override
    public void onAddItemClick(OrderItemsEntity response, int quantity, int pos) {

        //if order is placed from same restro then new_order will be "";
        // add comma seperator value if order from customisation tab assign this value to  (addon) key otherwise fill "":
        if (SharedPreferenceWriter.getInstance(RestaurantDetails.this).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())) {
            addToCart(pos, quantity, restro_id, catergoryCombo, response.getId(), "", response.getPrice(), "", "plus");
//            if (response.getCustomizes().isEmpty())
//
//            else {
//                customizationItemPos = pos;
//                customizationItemQuantity = quantity;
//                dispatchCustomisationBottomSheet(response);
//            }
        } else {
            Thread back = new Thread() {
                @Override
                public void run() {
                    super.run();
                    customizationItemPos = pos;
                    customizationCLick = false;
                    customizationItemQuantity = quantity;
                    orderItemsEntity = new OrderItemsEntity();
                    orderItemsEntity.setId(response.getId());
                    orderItemsEntity.setName(response.getName());
                    orderItemsEntity.setImg(response.getImg());
                    orderItemsEntity.setItem_type(response.getItem_type());
                    orderItemsEntity.setQuantity(quantity);
                    orderItemsEntity.setRestaurantId(restro_id);
                    orderItemsEntity.setPrice(response.getPrice());
                    orderItemsEntity.setCatname(response.getCatname());
                    orderItemsEntity.setRestaurantAddress(restaurantAddress);
                    orderItemsEntity.setRestaurantName(restaurantname);
                    orderItemsEntity.setAddOns("");
                    orderItemsEntity.setCustomizes(response.getCustomizes());
                    orderItemsEntity.setRestaurantLat("2525.54");
                    orderItemsEntity.setRestaurantLong("454.444");
                    orderItemsEntity.setTotalQuantity(String.valueOf(quantity));
                    orderItemsEntity.setOriginalPrice(response.getPrice());


                    // check if database is empty or not if empty insert value otherwise update value
                    if (dataBase.restaurantDao().fetchAllOrderMenu() == null) {

                        if (response.getCustomizes() != null) {
                            if (response.getCustomizes().size() == 0) {
                                dataBase.restaurantDao().insertOrderMenu(orderItemsEntity);//insert query
                                updateOrderOnUIthread(pos, quantity);
                            } else {
                                dispatchCustomisationBottomSheet(response);
                                customizationCLick = true;
                            }
                        }


                    } else {
                        ////if database is not null we check if table contains same id from api (id)
                        // if matches update row
                        if (dataBase.restaurantDao().fetchAllOrderMenu().getRestaurantId() != null && !dataBase.restaurantDao().fetchAllOrderMenu().getRestaurantId().equalsIgnoreCase(restro_id)) {
                            dataBase.clearAllTables();
                            dataBase.restaurantDao().insertOrderMenu(orderItemsEntity);//insert query
                            tvAddToCart.setVisibility(View.VISIBLE);
                            updateOrderOnUIthread(pos, quantity);
                        } else {

                            if (response.getCustomizes().size() > 0) {
                                dispatchCustomisationBottomSheet(response);
                                customizationCLick = true;

                            } else {
                                if (dataBase.restaurantDao().fetchOrderMenuListById(response.getId()).size() > 0) {

                                    dataBase.restaurantDao().updateOrderPrice(Float.parseFloat(response.getPrice()), 1, response.getId(), 1);
                                    updateOrderOnUIthread(pos, quantity);
                                } else {
                                    dataBase.restaurantDao().insertOrderMenu(orderItemsEntity);//insert query
                                    updateOrderOnUIthread(pos, quantity);
                                }
                            }

                        }
                    }

                }

            };
            back.start();

            try {
                back.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


//            if(!customizationCLick){
//                //update count on item
//                menuDetailsList.get(pos).setQuantity(quantity);
//                menuDetailAdapter.updateMenuDetailsList(menuDetailsList);
//                tvAddToCart.setVisibility(View.VISIBLE);
//
//            }


        }
//

    }

    @Override
    public void onRemoveItemClick(OrderItemsEntity response, int quantity, int pos) {
        //if order is placed from same restro then new_order will be "";
        // add comma seperator value if order from customisation tab assign this value to  (addon) key otherwise fill "":
        if (quantity == 0)
            tvAddToCart.setVisibility(View.GONE);

        if (response.getQuantity() != 0) {
            if (response.getCustomizes().isEmpty()) {

                if (SharedPreferenceWriter.getInstance(RestaurantDetails.this).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())) {//check to avoid negative count
                    addToCart(pos, quantity, restro_id, catergoryCombo, response.getId(), "", response.getPrice(), "", "minus");
                } else {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {

                            if (dataBase != null) {

                                if (quantity >= 1) {
                                    ///getting updated value
                                    // float updatePrice=((Integer.parseInt(response.getPrice())*(quantity+1)-Integer.parseInt(response.getPrice())));
                                    float updatePrice = ((Float.valueOf(response.getPrice()) * (quantity + 1) - Float.valueOf(response.getPrice())));
                                    int updateQuantity = quantity;
                                    dataBase.restaurantDao().removeOrder(updatePrice, updateQuantity, response.getId());

                                } else {
                                    dataBase.restaurantDao().deleteOrderItem(response.getId());
                                }
                            }
                        }
                    });

                    //update count on item
                    menuDetailsList.get(pos).setQuantity(quantity);
                    menuDetailAdapter.updateMenuDetailsList(menuDetailsList);
                }
            } else {
                CommonUtilities.goToCarDialogue(this);
            }
        }
    }



    //////////restroDetails api///////
    public void restroDetails(String restroId) {

        if (CommonUtilities.isNetworkAvailable(this)) {

            String user_id = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.USERID);
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.restro_details(restroId, user_id);
                ServicesConnection.getInstance().enqueueWithoutRetry(
                        loginCall,
                        this,
                        true,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {

                                        serverResponse= bean.getDetailsRestorent();
                                        setData(bean.getDetailsRestorent(), bean.getCategory());


                                        storeType=bean.getDetailsRestorent().getStore_type();
                                        //hit api to get data on first time ------>>>>>
                                        menuDetails(restro_id, bean.getCategory().get(0).getId(), bean.getCategory().get(0).getType(),bean.getDetailsRestorent().getStore_type());

                                        catergory_id = bean.getCategory().get(0).getId();
                                        catergoryCombo = bean.getCategory().get(0).getType();

                                        if(storeType==2)
                                        {
                                            findViewById(R.id.ivFilterAddToCart).setVisibility(View.GONE);
                                        }


                                    } else {
                                        CommonUtilities.snackBar(RestaurantDetails.this, bean.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<CommonModel> call, Throwable t) {
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            CommonUtilities.snackBar(this, getString(R.string.no_internet));

        }
    }

    private void getIntentData() {

        if (getIntent() != null) {

            restro_id = getIntent().getStringExtra(ParamEnum.RESTRO_ID.theValue());

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        dataBase = MyApplication.getInstance().getDatabase();



    }


    /// set restraunt details
    private void setData(RestaurantResponse response, List<RestaurantResponse> menuItem) {

        if (!SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN)) {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                    if (dataBase != null && dataBase.restaurantDao().fetchAllOrderMenu() != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvAddToCart.setVisibility(View.VISIBLE);

                            }
                        });
                    }
                }
            });

        }

        tvAvgRating.setText(""+Double.parseDouble(response.getRating()));
        tvRestroTitle.setText(response.getName());
        tvRestroAddress.setText(response.getAddress());
        ivRating.setRating(Float.parseFloat(response.getRating()));


        restaurantAddress = response.getAddress();
        restaurantname = response.getName();


        tvTimeAddToCard.setText((response.getDelivery_time() + " mins."));
        if(currency.equalsIgnoreCase("USD"))
        {
            tvMinOrderAddToCard.setText("Min. order : "+symbol+" " + CommonUtilities.roundOff(""+Double.parseDouble(response.getMin_order_value())*exchangeRate));

        }else
        {
            tvMinOrderAddToCard.setText("Min. order : "+symbol+" " + response.getMin_order_value());

        }

        tvReview.setText("(" + response.getReview() + " reviews" + ")");


        //// to check if cart has item or not if cart has already item then show view cart tab
        if (response.getCart_value().equals(ParamEnum.ONE.theValue()))
            tvAddToCart.setVisibility(View.VISIBLE);
        else
            tvAddToCart.setVisibility(View.GONE);


        menuList.clear();
        menuItem.get(0).setCheck(true);
        menuList.addAll(menuItem);
        menuAdapter.refreshMenuList(menuList);

        tvdistance.setText(getIntent().getStringExtra(ParamEnum.DISTANCE.theValue()));


        for (int i = 0; i < response.getCuisines().size(); i++) {
        TextView  textView=new TextView(this);
        textView.setText(response.getCuisines().get(i).getName());
        textView.setCompoundDrawablePadding(10);
        textView.setTextSize(11);
        textView.setTextColor(ContextCompat.getColor(this,R.color.light_grey));
        textView.setPadding(0,0,15,0);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bg_dot_circle2,0,0,0);

        llCousine.addView(textView);


        }

        isFavStatus = response.getIs_favorite();

        if (response.getIs_favorite().equalsIgnoreCase("0")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivFav.setBackground(getResources().getDrawable(R.drawable.unfav, null));
            } else {
                ivFav.setBackground(ContextCompat.getDrawable(this, R.drawable.unfav));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivFav.setBackground(getResources().getDrawable(R.drawable.heart_like_, null));
            } else {
                ivFav.setBackground(ContextCompat.getDrawable(this, R.drawable.heart_like_));

            }
        }

        Log.e("iamge", "----->>>"+response.getImage());
        if (!response.getImage().equalsIgnoreCase(""))
            CommonUtilities.setImage(this, progressbar, response.getImage(), ivRestroImage);
        else
            progressbar.setVisibility(View.GONE);
    }


    //////////menu details  api///////
    public void menuDetails(String restroId, String catergoryId, String type,int storeType) {

        if (CommonUtilities.isNetworkAvailable(this)) {

            String userId = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.USERID);

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.menuDetails(restroId, userId, catergoryId, type,storeType);

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        loginCall,
                        this,
                        true,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {

                                        setMenuDetailsData(bean.getMenuList());


                                    } else {
                                        CommonUtilities.snackBar(RestaurantDetails.this, bean.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<CommonModel> call, Throwable t) {
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            CommonUtilities.snackBar(this, getString(R.string.no_internet));

        }
    }


    @Override
    public void onClickMenu(RestaurantResponse response) {

        //when click menu refressh menudetails list
        menuDetails(restro_id, response.getId(), response.getType(),storeType);

        catergory_id = response.getId();
        catergoryCombo = response.getType();
//        storeType=response.getStore_type();
        eat_type="";
        item_type="";
        taste="";

    }


    // send data to adapter
    private void setMenuDetailsData(List<OrderItemsEntity> menu) {

        if (SharedPreferenceWriter.getInstance(RestaurantDetails.this).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())) {
            menuDetailsList.clear();
            menuDetailsList.addAll(menu);
            menuDetailAdapter.updateMenuDetailsList(menuDetailsList);

        } else {

            OrderedItemQuantity asyntask = new OrderedItemQuantity(menu, this, dataBase);
            asyntask.execute(menu.get(0).getCatname());

        }


    }

    //////////addToCart api///////
    public void addToCart(int pos, int quantity, String restroId, String type, String menu_id, String addon, String price, String new_order, String status) {
        if (CommonUtilities.isNetworkAvailable(this)) {
            String token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
            String device_token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.DEVICETOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.addToCart(map, restroId, type, menu_id, String.valueOf(quantity), addon, price, new_order, status,storeType);

                ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, this, true, new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                        //update count on item
                                        menuDetailsList.get(pos).setQuantity(quantity);
                                        menuDetailAdapter.updateMenuDetailsList(menuDetailsList);
                                        if (quantity == 0)
                                            tvAddToCart.setVisibility(View.GONE);
                                        else
                                            tvAddToCart.setVisibility(View.VISIBLE);
                                    } else {
                                        showAlertDialog(pos, quantity, restroId, type, menu_id, addon, price, "1");
                                        //CommonUtilities.snackBar(RestaurantDetailsEntity.this, bean.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<CommonModel> call, Throwable t) {
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            CommonUtilities.snackBar(this, getString(R.string.no_internet));

        }
    }


    // remove item dialogue//

    // show  alert dialog box
    private void showAlertDialog(int pos, int quantity, String restroId, String type, String menu_id, String addon, String price, String new_order) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_alert);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tvTitleDialog = dialog.findViewById(R.id.tvTitleDialog);

        TextView tvMsgDialog = dialog.findViewById(R.id.tvMsgDialog);

        TextView tvYes = dialog.findViewById(R.id.tvYes);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(pos, quantity, restro_id, type, menu_id, addon, price, new_order, "plus");
                dialog.dismiss();
            }
        });

        TextView tvNo = dialog.findViewById(R.id.tvNo);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }




    private void dispatchCustomisationBottomSheet(OrderItemsEntity response) {

        ChooseYourTasteBottomSheet chooseYourTasteBottomSheet = new ChooseYourTasteBottomSheet(this);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ParamEnum.MENU.theValue(), (ArrayList<? extends Parcelable>) response.getCustomizes());
        bundle.putParcelable(ParamEnum.MAIN_MENU_ITEM.theValue(), response);
        chooseYourTasteBottomSheet.setArguments(bundle);
        chooseYourTasteBottomSheet.show(getSupportFragmentManager(), "");


    }

    @Override
    public void addToCartCustomization(List<AddonsEntity> selectedCustomization, OrderItemsEntity response) {
        isCustomizationRepeat = true;
        addOnId = "";
        totalCustomPrice = 0;
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < selectedCustomization.size(); i++) {
            ids.add(selectedCustomization.get(i).getId());
            totalCustomPrice = totalCustomPrice + Float.parseFloat(selectedCustomization.get(i).getPrice());
        }

        addOnId = ids.toString();
        addOnId = addOnId.substring(1, addOnId.length() - 1).replace(", ", ",");


        if (SharedPreferenceWriter.getInstance(RestaurantDetails.this).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())) {
            addToCart(customizationItemPos, customizationItemQuantity, restro_id, catergoryCombo, response.getId(), addOnId, String.valueOf(totalCustomPrice), "", "plus");
        } else {

            orderItemsEntity = new OrderItemsEntity();
            orderItemsEntity.setId(response.getId());
            orderItemsEntity.setName(response.getName());
            orderItemsEntity.setImg(response.getImg());
            orderItemsEntity.setItem_type(response.getItem_type());
            orderItemsEntity.setQuantity(1);
            orderItemsEntity.setRestaurantId(restro_id);
            orderItemsEntity.setPrice(String.valueOf(totalCustomPrice));
            orderItemsEntity.setCustomizes(response.getCustomizes());
            orderItemsEntity.setAddOns(addOnId);
            orderItemsEntity.setCatname(response.getCatname());
            orderItemsEntity.setRestaurantAddress(restaurantAddress);
            orderItemsEntity.setRestaurantName(restaurantname);
            orderItemsEntity.setRestaurantLat("28.5355");
            orderItemsEntity.setRestaurantLong("77.3910");
            orderItemsEntity.setTotalQuantity(String.valueOf(customizationItemQuantity));
            orderItemsEntity.setOriginalPrice(response.getPrice());

            AddCustomisedMenuAsync async = new AddCustomisedMenuAsync(totalCustomPrice, restro_id, addOnId, orderItemsEntity, dataBase, response, RestaurantDetails.this);
            async.execute();
        }
    }


    //////////mark as fav api///////
    public void markAsFav(String isFav) {

        if (CommonUtilities.isNetworkAvailable(this)) {


            String token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.markFav(map, restro_id, isFav);

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        loginCall,
                        this,
                        false,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {

                                        if (bean.getMessage().equalsIgnoreCase("favourite added")) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                ivFav.setBackground(getResources().getDrawable(R.drawable.heart_like_, null));
                                            } else {
                                                ivFav.setBackground(ContextCompat.getDrawable(RestaurantDetails.this, R.drawable.heart_like_));
                                            }

                                            isFavStatus = "1";
                                            //   disableofflineAnimation();

                                        } else {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                ivFav.setBackground(getResources().getDrawable(R.drawable.unfav, null));
                                            } else {
                                                ivFav.setBackground(ContextCompat.getDrawable(RestaurantDetails.this, R.drawable.unfav));
                                            }
                                            isFavStatus = "0";
                                            // disableofflineAnimation();
                                        }

                                    } else {
                                        CommonUtilities.snackBar(RestaurantDetails.this, bean.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<CommonModel> call, Throwable t) {
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            CommonUtilities.snackBar(this, getString(R.string.no_internet));

        }
    }


    ////start heart animation when user click on fav icon
    private void startHeartAnimation() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.setAnimation("362-like.json");
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);

    }

    ////disable heart animation when user click on fav icon
    public void disableofflineAnimation() {
        if (lottieAnimationView != null) {
            lottieAnimationView.pauseAnimation();
        }
        lottieAnimationView.setVisibility(View.GONE);

    }

    // show  alert dialog box
    private void showAlertDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_alert);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tvTitleDialog = dialog.findViewById(R.id.tvTitleDialog);

        TextView tvMsgDialog = dialog.findViewById(R.id.tvMsgDialog);

        tvTitleDialog.setText("You are not logged in");

        tvMsgDialog.setText("Go to login activity ??");

        TextView tvYes = dialog.findViewById(R.id.tvYes);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantDetails.this, LoginActivity.class));
                dialog.dismiss();
            }
        });

        TextView tvNo = dialog.findViewById(R.id.tvNo);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void setClick() {

        menuItemBottomSheet.initilizeClick(new MenuItemBottomSheet.OnMenuItemClick() {
            @Override
            public void menuItemClick(int pos, RestaurantResponse response) {

                //when click menu refressh menudetails list
                recyclerAddToCartHori.scrollToPosition(pos);


                if (menuList != null) {
                    for (int i = 0; i < menuList.size(); i++) {
                        menuList.get(i).setCheck(false);
                    }
                }
                menuList.get(pos).setCheck(true);
                menuAdapter.refreshMenuList(menuList);
                menuDetails(restro_id, response.getId(), response.getType(),storeType);
                menuItemBottomSheet.dismiss();


            }
        });


    }

    // show  Repeat order alert dialog box
    public void repeatOrder() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_alert);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tvTitleDialog = dialog.findViewById(R.id.tvTitleDialog);
        TextView tvMsgDialog = dialog.findViewById(R.id.tvMsgDialog);
        tvTitleDialog.setText(getString(R.string.customization));
        tvMsgDialog.setText(getString(R.string.repeat_order));
        TextView tvNo = dialog.findViewById(R.id.tvNo);
        TextView tvYes = dialog.findViewById(R.id.tvYes);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public void processFinish(List<OrderItemsEntity> offlineQuantity) {

        menuDetailsList.clear();
        menuDetailsList.addAll(offlineQuantity);
        menuDetailAdapter.updateMenuDetailsList(menuDetailsList);

    }

    @Override
    public void onTaskFinish(List<OrderItemsEntity> offlineQuantityList, float totalPrice) {

        //when customisation menu has been addes to database
        menuDetailsList.get(customizationItemPos).setQuantity(customizationItemQuantity);
        menuDetailAdapter.updateMenuDetailsList(menuDetailsList);
        tvAddToCart.setVisibility(View.VISIBLE);

    }

    ///main UI thread///
    private void updateOrderOnUIthread(int pos, int quantity) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //update count on item
                menuDetailsList.get(pos).setQuantity(quantity);
                menuDetailAdapter.updateMenuDetailsList(menuDetailsList);
                tvAddToCart.setVisibility(View.VISIBLE);

            }
        });
    }
}