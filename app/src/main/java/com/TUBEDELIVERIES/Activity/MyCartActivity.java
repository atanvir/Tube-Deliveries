package com.TUBEDELIVERIES.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.TUBEDELIVERIES.Adapter.OffersAdapter;
import com.TUBEDELIVERIES.Fragments.IEatOptions;
import com.TUBEDELIVERIES.Fragments.Model.EatAtRestaurantModel;
import com.TUBEDELIVERIES.Fragments.Model.HomeDeliveryModel;
import com.TUBEDELIVERIES.Fragments.Model.PickUpModel;
import com.TUBEDELIVERIES.Model.CardDetailSaveModel;
import com.TUBEDELIVERIES.Model.CheckFund;
import com.TUBEDELIVERIES.Model.MyCurrentDetailsModel;
import com.TUBEDELIVERIES.Model.OffereDetail;
import com.TUBEDELIVERIES.Utility.LocationClass;
import com.TUBEDELIVERIES.Utility.SwitchFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.TUBEDELIVERIES.Adapter.MenuDetailAdapter;
import com.TUBEDELIVERIES.BottomSheet.ChooseYourTasteBottomSheet;
import com.TUBEDELIVERIES.CallBacks.AsyncClassCallbacks;
import com.TUBEDELIVERIES.Fragments.EatOptionFragments.HomeDeliveryFragment;
import com.TUBEDELIVERIES.Model.CommonModel;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.JsonObject;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCartActivity extends AppCompatActivity implements MenuDetailAdapter.OnCustomizeClickListener, ChooseYourTasteBottomSheet.oncustomizationClickListener, AsyncClassCallbacks, IEatOptions, OffersAdapter.onOfferClick {

    private static final int ADDRESS_REQ = 101;
    @BindView(R.id.recyclerMyCart)
    RecyclerView recyclerMyCart;
    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btnConfirmMyCart)
    Button btnConfirmMyCart;
    @BindView(R.id.tvDiscount)
    TextView tvDiscount;
    @BindView(R.id.tvDeliveryCharges)
    TextView tvDeliveryCharges;
    @BindView(R.id.tvTotalAmountPaid)
    TextView tvTotalAmountPaid;
    @BindView(R.id.tvEmptyCart)
    TextView tvEmptyCart;
    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.offersRV)
    RecyclerView offersRV;

    //offline model class
    private Double delivery_charges=0.0;
    OrderItemsEntity orderItemsEntity;
    private TextInputEditText etArea;
    private GoogleMap mGoogleMap;
    private SupportMapFragment suppMapFragment;
    private List<OrderItemsEntity> menuDetailsList = null;
    private MenuDetailAdapter menuDetailAdapter = null;
    private String latitude = "";
    private String longitude = "";
    private boolean customizationCLick = false;
    //get pos for cusomize menu
    private int customizationItemPos;
    // get quantity for customisaton item
    private int customizationItemQuantity;
    // 1 for normal category 2 for combo category
    private String catergoryCombo = "";
    private String restro_id = "";
    //For Database purpojqse
    private JustBiteDataBase dataBase;
    private boolean isCustomizationRepeat = false;
    private int menuRefrenceId;
    private String restaurantname = "";
    private String restaurantAddress = "";
    private List<OrderItemsEntity> offlineQuantityList = null;


    /// this true when user select cusome address else false
    private boolean isCustomAddress = false;


    private String extra_note = "";
    private String pincode = "";
    private String Area = "";
    private String Landmark = "";
    private String House_no = "";
    private String discount = "";
    private String price = "";
    private Double total_amont_to_paid=0.0 ;
    private List<OrderItemsEntity> offlineOrderList = new ArrayList<>();

    @BindView(R.id.menuIv)
    ImageView menuIv;

    @BindView(R.id.subCl)
    ConstraintLayout subCl;

    @BindView(R.id.billLayout)
    View billLayout;


    @BindView(R.id.tvcouponCode)
    TextView tvcouponCode;

    @BindView(R.id.view32)
    View view32;

    @BindView(R.id.view31)
    View aboveCouponClView;

    @BindView(R.id.applyCouponCl)
    ConstraintLayout applyCouponCl;
    private String offered_service="";


    @BindView(R.id.iv_arrow)
    ImageView iv_arrow;

    String eatType="";

    boolean homeDelivery=false;
    boolean pickUp=false;
    boolean eatRestaurant=false;
    String eat_option;

    String errorMsg="Please Add Details";

    private List<OffereDetail> offereDetails;

    String eatOption="";
    Object actualData;
    private String address;
    int storeType;
    private String restroAddres;
    private String restroLat,restroLong;
    private Double extraDeliveryCharge=0.0;
    private String currency,currencySymbol;
    private Double exchangeRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__cart);
        currency= SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.CURRENCY);
        exchangeRate=Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.EXCHANGE_RATE));
        currencySymbol=SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.CURRENCY_SYMBOL);
        getIntent();
        new CommonUtil().setStatusBarGradiant(this,MyCartActivity.class.getSimpleName());
        ButterKnife.bind(this);
        applyCouponCl.setOnClickListener(this::onClick);
        menuIv.setVisibility(View.GONE);
        CommonUtilities.setToolbar(this,mainToolbar,tvTitle,getString(R.string.my_cart));
        getIntentData();
        billLayout=findViewById(R.id.billLayout);
        loadData();
        setUpRecyclerView();
    }

    private void loadData() {

        dataBase = MyApplication.getInstance().getDatabase();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                if (dataBase != null) {
                    offlineOrderList = dataBase.restaurantDao().fetchAllOrderMenuAsList();
                }
            }
        });


        if (SharedPreferenceWriter.getInstance(MyCartActivity.this).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())) {
            myCarListing(true);
        } else {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    if (dataBase != null) {
                        if (dataBase.restaurantDao().fetchAllOrderMenu() == null) {
                            ///if database is empty show empty text
                            // User runOnUiThread to Upate to main UI Thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvEmptyCart.setVisibility(View.VISIBLE);
                                    btnConfirmMyCart.setVisibility(View.GONE);
                                }
                            });

                        } else {

                            offlineOrderList = dataBase.restaurantDao().fetchAllOrderMenuAsList();
                            float totalPrice = dataBase.restaurantDao().getTotalPrice();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvEmptyCart.setVisibility(View.GONE);
                                    btnConfirmMyCart.setVisibility(View.VISIBLE);
                                    tvDiscount.setText("0");
                                    if(currency.equalsIgnoreCase("USD"))
                                    {
                                        tvTotalPrice.setText(currencySymbol+" " + CommonUtilities.roundOff(""+totalPrice*exchangeRate));
                                        tvTotalAmountPaid.setText(currencySymbol+" " + CommonUtilities.roundOff(""+totalPrice*exchangeRate));

                                    }else
                                    {
                                        tvTotalPrice.setText(currencySymbol+" " + totalPrice);
                                        tvTotalAmountPaid.setText(currencySymbol+" " + totalPrice);

                                    }


                                    menuDetailsList.clear();
                                    menuDetailsList.addAll(offlineOrderList);
                                    menuDetailAdapter.updateMenuDetailsList(menuDetailsList);


                                }
                            });
                        }

                    }

                }
            });


        }

    }

    private void loadFragment(Fragment fragment) {
      SwitchFragment.changeFragment(getSupportFragmentManager(), fragment, true, true);
    }


    private void getIntentData() {

        if (getIntent() != null) {

            restaurantname = getIntent().getStringExtra(ParamEnum.RESTAURANT_NAME.theValue());
            restro_id = getIntent().getStringExtra(ParamEnum.RESTRO_ID.theValue());
            restaurantAddress = getIntent().getStringExtra(ParamEnum.RESTAURANT_ADDRESS.theValue());

        }

    }

    @OnClick({R.id.btnConfirmMyCart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirmMyCart:
                if(address==null)
                {
                    Toast.makeText(this, "Please Add Address", Toast.LENGTH_SHORT).show();
                }else
                {
                    final String[] option = {""};
                    View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_payment_option, null);
                    BottomSheetDialog dialog = new BottomSheetDialog(this);
                    dialog.setContentView(dialogView);
                    RadioGroup rgOption=dialog.findViewById(R.id.rgOption);
                    ConstraintLayout clContinue=dialog.findViewById(R.id.clContinue);
                    clContinue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(option.length>0 && !option[0].equalsIgnoreCase(""))
                            {
                             dialog.dismiss();
                             dispatchPaymentMode(option[0]);
                            }else
                            {
                                Toast.makeText(MyCartActivity.this, "Please select payment option", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    rgOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            switch (radioGroup.getCheckedRadioButtonId())
                            {
                                case R.id.rbCash:
                                option[0] ="COD";
                                break;

                                case R.id.rbOnline:
                                option[0] ="ONLINE";
                                break;
                            }
                        }
                    });
                    dialog.show();
                }
                break;

            case R.id.applyCouponCl:
            if (offersRV.getVisibility() == View.GONE) {
                iv_arrow.setImageResource(R.drawable.drop_down_ic);
                offersRV.setVisibility(View.VISIBLE);
                OffersAdapter adapter = new OffersAdapter(MyCartActivity.this, offereDetails, this);
                LinearLayoutManager manager = new LinearLayoutManager(this);
                manager.setOrientation(RecyclerView.VERTICAL);
                offersRV.setLayoutManager(manager);
                offersRV.setAdapter(adapter);
            } else {
                iv_arrow.setImageResource(R.drawable.drop_down_icon);
                offersRV.setVisibility(View.GONE);
            }
            break;
        }
    }


    private void setUpRecyclerView() {
            recyclerMyCart.setLayoutManager(new LinearLayoutManager(this));
            menuDetailsList = new ArrayList<>();
            offlineOrderList = new ArrayList<>();
            menuDetailAdapter = new MenuDetailAdapter(this, this, menuDetailsList);
            recyclerMyCart.setAdapter(menuDetailAdapter);
        }



    //////////restroDetails api///////
    public void myCarListing(boolean isLoader) {

        if (CommonUtilities.isNetworkAvailable(this)) {

            String user_id = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.USERID);


            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.myCart(user_id);
                ServicesConnection.getInstance().enqueueWithoutRetry(
                        loginCall,
                        this,
                        isLoader,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = response.body();
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {

                                        if (bean.getCartList() != null && bean.getCartList().size() > 0) {
                                            tvEmptyCart.setVisibility(View.GONE);
                                            billLayout.setVisibility(View.VISIBLE);
                                            applyCouponCl.setVisibility(View.VISIBLE);
                                            aboveCouponClView.setVisibility(View.VISIBLE);
                                            restroAddres=bean.getExtra_details().getRestaurent_address();
                                            restroLat=bean.getExtra_details().getLatitude();
                                            restroLong=bean.getExtra_details().getLongitude();
                                            if(isLoader){
                                                offered_service=bean.getExtra_details().getOffered_service();
                                                storeType=Integer.parseInt(bean.getExtra_details().getStore_type());
                                                Bundle bundle=new Bundle();
                                                String currentAddress="";
                                                if(bean.getExtra_details().getUser_current_address()!=null) {
                                                     currentAddress = bean.getExtra_details().getUser_current_address();
                                                     currentAddress+=" "+bean.getExtra_details().getStreet()!=null?bean.getExtra_details().getStreet()+",":"";
                                                     currentAddress+=" "+bean.getExtra_details().getBuilding()!=null?bean.getExtra_details().getBuilding()+",":"";
                                                     currentAddress+=" "+bean.getExtra_details().getFloor()!=null?bean.getExtra_details().getFloor()+",":"";
                                                    currentAddress+=" "+bean.getExtra_details().getApartment()!=null?bean.getExtra_details().getApartment()+",":"";
                                                    currentAddress+=" "+bean.getExtra_details().getLandmark()!=null?bean.getExtra_details().getLandmark()+",":"";
                                                }
                                                bundle.putString(ParamEnum.HOME.theValue(),currentAddress.trim());
                                                HomeDeliveryFragment fragment=new HomeDeliveryFragment();
                                                fragment.setListner(MyCartActivity.this);
                                                fragment.setArguments(bundle);
                                                loadFragment(fragment);
                                            }
                                            offereDetails= bean.getExtra_details().getOffereDetails();

                                            btnConfirmMyCart.setVisibility(View.VISIBLE);
                                            view32.setVisibility(View.VISIBLE);
                                            setDataToMenuList(bean, bean.getCartList());

                                        } else {
                                            subCl.setVisibility(View.GONE);
                                            tvEmptyCart.setVisibility(View.VISIBLE);
                                            billLayout.setVisibility(View.GONE);
                                            applyCouponCl.setVisibility(View.GONE);
                                            aboveCouponClView.setVisibility(View.GONE);
                                            btnConfirmMyCart.setVisibility(View.GONE);
                                            view32.setVisibility(View.GONE);                                        }

                                    } else {
                                        CommonUtilities.snackBar(MyCartActivity.this, bean.getMessage());
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
    public void onCustomizeClick(OrderItemsEntity response, int i) {

    }

    @Override
    public void onAddItemClick(OrderItemsEntity response, int quantity, int pos) {
        if (SharedPreferenceWriter.getInstance(MyCartActivity.this).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())) {
            addToCart(pos, quantity, restro_id, response.getType(), response.getId(), "", response.getPrice(), "", "plus");

        } else {
            //  CommonUtilities.showLoadingDialog(MyCartActivity.this);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    customizationItemPos = pos;
                    customizationCLick = false;
                    customizationItemQuantity = quantity;

                    setOrderEntity(response, quantity);

                    // check if database is empty or not if empty insert value otherwise update value
                    if (dataBase.restaurantDao().fetchAllOrderMenu() == null) {
                        if (response.getCustomizes().size() == 0)
                            dataBase.restaurantDao().insertOrderMenu(orderItemsEntity);//insert query
                        else {
                            dispatchCustomisationBottomSheet(response);
                        }

                    } else {
                        ////if database is not null we check if table contains same id from api (id)
                        // if matches update row
//                        if (!dataBase.restaurantDao().fetchAllOrderMenu().getRestaurantId().equalsIgnoreCase(restro_id)) {
//                             dataBase.clearAllTables();
//                             dataBase.restaurantDao().insertOrderMenu(orderItemsEntity);//insert query
//                        }
//                        if {

                        if (response.getCustomizes().size() > 0) {
                            dispatchCustomisationBottomSheet(response);

                        } else {
                            if (dataBase.restaurantDao().fetchOrderMenuListById(response.getId()).size() > 0) {

                                dataBase.restaurantDao().updateOrderPrice(Float.parseFloat(response.getOriginalPrice()), 1, response.getId(), 1);
                                float totalPrice = dataBase.restaurantDao().getTotalPrice();
                                offlineOrderList = dataBase.restaurantDao().fetchAllOrderMenuAsList();
                                updateOrderOnUIthread(String.valueOf(totalPrice));

                            } else {

                                dataBase.restaurantDao().insertOrderMenu(orderItemsEntity);//insert query
                                float totalPrice = dataBase.restaurantDao().getTotalPrice();
                                offlineOrderList = dataBase.restaurantDao().fetchAllOrderMenuAsList();
                                updateOrderOnUIthread(String.valueOf(totalPrice));

                            }
                        }

                    }
                }
            });
        }
    }

    @Override
    public void onRemoveItemClick(OrderItemsEntity response, int quantity, int pos) {
        //if order is placed from same restro then new_order will be "";
        // add comma seperator value if order from customisation tab assign this value to  (addon) key otherwise fill "":

//        //check to avoid negative count
//        if (response.getQuantity() != 0) {
//            addToCart(pos, quantity, restro_id, response.getType(), response.getId(), response.getAddon(), response.getPrice(), "", "minus");
//        }

        if (response.getQuantity() != 0) {

            if (SharedPreferenceWriter.getInstance(MyCartActivity.this).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())) {  //check to avoid negative count
                addToCart(pos, quantity, restro_id, response.getType(), response.getId(), response.getAddon(), response.getPrice(), "", "minus");
            } else {


                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {


                        if (dataBase != null) {

                            if (response.getCustomizes().size() == 0) {
                                /// if it is last item to remove then remove from db
                                if (quantity >= 1) {
                                    ///getting updated value
                                    float updatePrice = ((Float.valueOf(response.getPrice()) - Float.valueOf(response.getOriginalPrice())));
                                    int updateQuantity = quantity;
                                    dataBase.restaurantDao().removeOrder(updatePrice, updateQuantity, response.getId());
                                    offlineOrderList = dataBase.restaurantDao().fetchAllOrderMenuAsList();
                                    float totalPrice = dataBase.restaurantDao().getTotalPrice();
                                    updateOrderOnUIthread(String.valueOf(totalPrice));
                                } else {

                                    dataBase.restaurantDao().deleteOrderItem(response.getId());
                                    offlineOrderList = dataBase.restaurantDao().fetchAllOrderMenuAsList();

                                    float totalPrice = dataBase.restaurantDao().getTotalPrice();
                                    updateOrderOnUIthread(String.valueOf(totalPrice));

                                }
                            } else {

                                /// if it is last item to remove then remove from db
                                if (quantity >= 1) {
                                    ///getting updated value
                                    float updatePrice = ((Float.valueOf(response.getPrice()) - Float.valueOf(response.getOriginalPrice())));
                                    int updateQuantity = quantity;
                                    dataBase.restaurantDao().removeOrder(updatePrice, updateQuantity, response.getId());
                                    offlineOrderList = dataBase.restaurantDao().fetchAllOrderMenuAsList();
                                    float totalPrice = dataBase.restaurantDao().getTotalPrice();
                                    updateOrderOnUIthread(String.valueOf(totalPrice));
                                } else {

                                    dataBase.restaurantDao().deleteCustomizationOrder(response.getId(), response.getAddOns());
                                    offlineOrderList = dataBase.restaurantDao().fetchAllOrderMenuAsList();

                                    float totalPrice = dataBase.restaurantDao().getTotalPrice();
                                    updateOrderOnUIthread(String.valueOf(totalPrice));

                                }


                            }

                        }
                    }
                });


            }

        }
    }


    private void setDataToMenuList(CommonModel bean, List<OrderItemsEntity> menuLIst) {
        String area =bean.getExtra_details().getArea() != null ? bean.getExtra_details().getArea() : "";
        String landmark =bean.getExtra_details().getLandmark() != null ? bean.getExtra_details().getLandmark() : "";
        String pin = bean.getExtra_details().getPin() != null ? bean.getExtra_details().getPin() : "";
        String userCurrentAddress = bean.getExtra_details().getUser_current_address() != null ? bean.getExtra_details().getUser_current_address() : "";
        restro_id = (bean.getExtra_details().getRestaurent_id());

        discount = bean.getExtra_details().getTotal_price();
        total_amont_to_paid = Double.parseDouble(bean.getExtra_details().getTotal_paid_amount());
        price = bean.getExtra_details().getTotal_price();

        latitude = bean.getExtra_details().getLatitude();
        longitude = bean.getExtra_details().getLongitude();

        menuDetailsList.clear();
        menuDetailsList.addAll(menuLIst);
        menuDetailAdapter.updateMenuDetailsList(menuDetailsList);
        if(currency.equalsIgnoreCase("USD"))
        {
            
            tvTotalPrice.setText(currencySymbol+" " + CommonUtilities.roundOff(""+Double.parseDouble(bean.getExtra_details().getTotal_price())*exchangeRate));
            tvDiscount.setText(currencySymbol+" " + CommonUtilities.roundOff(""+Double.parseDouble(bean.getExtra_details().getDiscount())*exchangeRate));
            delivery_charges=Double.parseDouble(bean.getExtra_details().getDelivery_price())*exchangeRate;
            delivery_charges=delivery_charges+extraDeliveryCharge;
            tvDeliveryCharges.setText(currencySymbol+" " + CommonUtilities.roundOff(""+delivery_charges));
            total_amont_to_paid= (total_amont_to_paid*exchangeRate)+extraDeliveryCharge;
            tvTotalAmountPaid.setText(currencySymbol+" " + CommonUtilities.roundOff(""+total_amont_to_paid));


        }else {

            tvTotalPrice.setText(currencySymbol + " " + CommonUtilities.roundOff(""+Double.parseDouble(bean.getExtra_details().getTotal_price())));
            tvDiscount.setText(currencySymbol + " " + CommonUtilities.roundOff(""+Double.parseDouble(bean.getExtra_details().getDiscount())));
            delivery_charges = Double.parseDouble(bean.getExtra_details().getDelivery_price());
            delivery_charges = delivery_charges + extraDeliveryCharge;
            tvDeliveryCharges.setText(currencySymbol+" " + CommonUtilities.roundOff(""+delivery_charges));
            total_amont_to_paid = total_amont_to_paid + extraDeliveryCharge;
            tvTotalAmountPaid.setText(currencySymbol+" " + CommonUtilities.roundOff(""+total_amont_to_paid));
        }
    }


    ///type for food category catergory 1 for normal 2 for combo
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
                ServicesConnection.getInstance().enqueueWithoutRetry(
                        loginCall,
                        this,
                        true,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = response.body();
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {

                                        myCarListing(false);
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


        String addOnId = "";
        float totalCustomPrice = 0;
        float finalPrice = Float.parseFloat(response.getPrice());

        ///to add total value of customPrice

        ///  ArrayList<Integer>totalCustomPrice=new ArrayList<Integer>();

        ArrayList<String> ids = new ArrayList<>();

        for (int i = 0; i < selectedCustomization.size(); i++) {
            ids.add(selectedCustomization.get(i).getId());
            totalCustomPrice = totalCustomPrice + Float.parseFloat(selectedCustomization.get(i).getPrice());
        }

        addOnId = ids.toString();
        addOnId = addOnId.substring(1, addOnId.length() - 1).replace(", ", ",");

        // addToCart(customizationItemPos, customizationItemQuantity, restro_id, response.getType(), response.getId(), addOnId, String.valueOf(totalCustomPrice), "", "plus");

        if (SharedPreferenceWriter.getInstance(MyCartActivity.this).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())) {
            addToCart(customizationItemPos, customizationItemQuantity, restro_id, response.getType(), response.getId(), addOnId, String.valueOf(totalCustomPrice), "", "plus");
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
            orderItemsEntity.setOriginalPrice(response.getOriginalPrice());

            AddCustomisedMenuAsync async = new AddCustomisedMenuAsync(totalCustomPrice, restro_id, addOnId, orderItemsEntity, dataBase, response, MyCartActivity.this);
            async.execute();
        }

    }



    private void checkFundsApi() {
        if (CommonUtilities.isNetworkAvailable(this)) {
            String token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> call = servicesInterface.checkfundsApi(map,SharedPreferenceWriter.getInstance(MyCartActivity.this).getString(SPreferenceKey.USERID));

                ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<CommonModel>() {
                    @Override
                    public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                        if (response.isSuccessful()) {
                            CommonUtilities.dismissLoadingDialog();
                            CommonModel serverResponse = response.body();
                            if (serverResponse.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                showAlreadyAddedAmountPopUp(serverResponse.getCheckFund());
                            } else {
                                CommonUtilities.snackBar(MyCartActivity.this, serverResponse.getMessage());
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
        }
    }

    private void showAlreadyAddedAmountPopUp(CheckFund checkFund) {
        final Dialog dialog = new Dialog(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_deduct_amount);
        RadioGroup rgOption=dialog.findViewById(R.id.rgOption);
        TextView tvWallet=dialog.findViewById(R.id.tvWallet);
        TextView tvSubscription=dialog.findViewById(R.id.tvSubscription);
        TextView tvAppCredit=dialog.findViewById(R.id.tvAppCredit);
        RadioButton rbWallet=dialog.findViewById(R.id.rbWallet);
        RadioButton rbSubscription=dialog.findViewById(R.id.rbSubscription);
        RadioButton rbAppCredit=dialog.findViewById(R.id.rbAppCredit);
        final String[] paymentMode = {null};

        if(checkFund.getWallet().getStatus()==1)
        {
            rbWallet.setEnabled(false);
            tvWallet.setVisibility(View.GONE);
        }else
        {
            tvWallet.setText("$ "+checkFund.getWallet().getBalance());
        }

        if(checkFund.getSubscription().getStatus()==0 || checkFund.getSubscription().getStatus()==1)
        {
            rbSubscription.setEnabled(false);
            tvSubscription.setVisibility(View.GONE);
        }else
        {
            tvSubscription.setText("$ "+checkFund.getSubscription().getBalance());
        }

        if(checkFund.getCredit().getBalance()>0)
        {
            tvAppCredit.setText("$ "+checkFund.getCredit().getBalance());
        }
        else
        {
            rbAppCredit.setEnabled(false);
            tvAppCredit.setVisibility(View.GONE);
        }




        rgOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.rbAppCredit:
                        paymentMode[0] ="appcredit";
                    break;

                    case R.id.rbSubscription:
                        paymentMode[0] ="subscribe";
                    break;

                    case R.id.rbDebitCreditCard:
                        paymentMode[0] ="";
                    break;

                    case R.id.rbWallet:
                        paymentMode[0] ="wallet";
                    break;
                }

            }
        });

        Button btnContinue=dialog.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(paymentMode[0] ==null)
                {
                    Toast.makeText(MyCartActivity.this, "Please Choose Payment Option", Toast.LENGTH_SHORT).show();
                }else if(paymentMode[0].equalsIgnoreCase(""))
                {
                    dialog.dismiss();
                }else
                {
                    if(checkValidation(paymentMode[0],checkFund)) {
                        dialog.dismiss();
//                        placeOrder(paymentMode[0]);
                    }
                }
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    private boolean checkValidation(String paymentMode, CheckFund checkFund) {
        boolean ret=true;
//        long total_amont_to_paids=Double.parseDouble(total_amont_to_paid);

//        if(paymentMode.equalsIgnoreCase("subscribe"))
//        {
//
//            if(!(CommonUtilities.roundOff(""+checkFund.getSubscription().getBalance())>=total_amont_to_paids))
//            {
//                ret=false;
//                CommonUtilities.snackBar(this,"Sorry, You do not have sufficient Subcription balance");
//
//            }
//
//
//        }else if(paymentMode.equalsIgnoreCase("appcredit"))
//        {
//
//            if(!(CommonUtilities.roundOff(""+checkFund.getCredit().getBalance())>=total_amont_to_paids) || !(CommonUtilities.roundOff(""+checkFund.getCredit().getBalance())>200))
//            {
//                ret=false;
//                if(!(CommonUtilities.roundOff(""+checkFund.getCredit().getBalance())>=total_amont_to_paids))
//                    CommonUtilities.snackBar(this,"Sorry, You do not have sufficient App credit balance");
//                else if(!(CommonUtilities.roundOff(""+checkFund.getCredit().getBalance())>200))
//                    CommonUtilities.snackBar(this,"App credit balance must be greater than $ 200");
//
//            }
//
//
//        }else if(paymentMode.equalsIgnoreCase("wallet"))
//        {
//
//            if(!(CommonUtilities.roundOff(""+checkFund.getWallet().getBalance())>=total_amont_to_paids))
//            {
//                ret=false;
//                CommonUtilities.snackBar(this,"Sorry, You do not have sufficient Wallet balance");
//            }
//
//        }
//


        return ret;
    }

    public void placeOrder(String paymentMode,boolean isLoader,String total_amont_to_paid,String delivery_charges) {
        if (CommonUtilities.isNetworkAvailable(this)) {
            String userId = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.USERID);
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.place_order(userId, paymentMode, latitude, longitude,address, extra_note,Landmark , "",address,pincode,price,discount,""+total_amont_to_paid,"",""+delivery_charges,currency);
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
                                        Intent intent =new Intent(MyCartActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                        Toast.makeText(MyCartActivity.this, "Your order has been placed", Toast.LENGTH_SHORT).show();

                                    } else {
                                        CommonUtilities.snackBar(MyCartActivity.this, bean.getMessage());
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

    private void OnlinePaymentIntent(String total_amont_to_paid,String delivery_charges)
        {
            Intent intent = new Intent(MyCartActivity.this,WebviewActivity.class);
            intent.putExtra(ParamEnum.FROM.theValue(),"Cart");
            intent.putExtra("amount",""+total_amont_to_paid);
            intent.putExtra("url","paymentform");
            intent.putExtra("latitude",""+latitude);
            intent.putExtra("longitude",""+longitude);
            intent.putExtra("extra_note",extra_note);
            intent.putExtra("Landmark",Landmark);
            intent.putExtra("pincode",pincode);
            intent.putExtra("price",""+price);
            intent.putExtra("discount",""+discount);
            intent.putExtra("total_amont_to_paid",""+total_amont_to_paid);
            intent.putExtra("delivery_charges",""+delivery_charges);
            intent.putExtra("currency_code",""+currency);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            String addressFromSearch = data.getStringExtra("ADDRESS");
            latitude = data.getStringExtra("LAT");
            longitude = data.getStringExtra("LONG");
            etArea.setText(addressFromSearch);
            address=addressFromSearch;


        } else if (requestCode == 8 && resultCode == RESULT_OK) {
            myCarListing(true);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    dataBase.restaurantDao().deleteAllData();
                }
            });
        } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this,"Transaction Canceled!",Toast.LENGTH_SHORT).show();
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this,"Invalid Payment!",Toast.LENGTH_SHORT).show();
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
    }

    ///main UI thread///
    private void updateOrderOnUIthread(String totalPrice) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (offlineOrderList != null && offlineOrderList.size() > 0) {
                    tvEmptyCart.setVisibility(View.GONE);
                    btnConfirmMyCart.setVisibility(View.VISIBLE);
                } else {

                    tvEmptyCart.setVisibility(View.VISIBLE);
                    btnConfirmMyCart.setVisibility(View.GONE);
                }


                menuDetailsList.clear();
                menuDetailsList.addAll(offlineOrderList);
                menuDetailAdapter.updateMenuDetailsList(menuDetailsList);

                tvDiscount.setText("0");
                tvTotalPrice.setText("USD " + totalPrice);
                tvTotalAmountPaid.setText("USD " + totalPrice);

                //   CommonUtilities.dismissLoadingDialog();


            }
        });


    }


    /// set restaurant Entity for Dtatabase

    private void setOrderEntity(OrderItemsEntity response, int quantity) {
        orderItemsEntity = new OrderItemsEntity();
        orderItemsEntity.setId(response.getId());
        orderItemsEntity.setName(response.getName());
        orderItemsEntity.setImg(response.getImg());
        orderItemsEntity.setItem_type(response.getItem_type());
        orderItemsEntity.setQuantity(quantity);
        orderItemsEntity.setOriginalPrice(response.getOriginalPrice());
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
    }



    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("className",MyCartActivity.class.getSimpleName());
        startActivity(intent);
//        if(getSupportFragmentManager().getBackStackEntryCount()>0)
//        {
//            int count=getSupportFragmentManager().getBackStackEntryCount();
//            String className=getSupportFragmentManager().getBackStackEntryAt(count-1).getName();
//            Log.wtf("ClassName",className);
//
//             if(className.equalsIgnoreCase(MyCartHomeFragment.class.getName()))
//            {
//                Intent intent=new Intent(this,MainActivity.class);
//                intent.putExtra("className",MyCartActivity.class.getSimpleName());
//                startActivity(intent);
//            }
//             else
//            {
//                CartActivityIntent();
//            }
//        }else
//        {
//            Intent intent=new Intent(this,MainActivity.class);
//            intent.putExtra("className",MyCartActivity.class.getSimpleName());
//            startActivity(intent);
//
//        }
    }

    private void CartActivityIntent() {
        finish();
        Intent intent=new Intent(this,MyCartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTaskFinish(List<OrderItemsEntity> orderItemsEntityList, float totatlPrice) {
        menuDetailsList.clear();
        menuDetailsList.addAll(orderItemsEntityList);
        menuDetailAdapter.updateMenuDetailsList(menuDetailsList);
        tvDiscount.setText("0");
        tvTotalPrice.setText("USD " + totatlPrice);
        tvTotalAmountPaid.setText("USD " + totatlPrice);

    }



    @Override
    public void eatOptionData(Object data) {
       if(data!=null) {
           actualData=data;
           if (data instanceof HomeDeliveryModel) {
               Log.e("HomeDeliveryModel",data.toString());
               if(((HomeDeliveryModel) data).getAddress()!=null)
               {
                   homeDelivery=true;
                   eatOption="1";
                   latitude=((HomeDeliveryModel) data).getLat();
                   longitude=((HomeDeliveryModel) data).getLongt();
                   address=((HomeDeliveryModel) data).getAddress();
                   new DistaceClass().execute();


               }



           } else if (data instanceof EatAtRestaurantModel) {

               Log.e("EatAtRestaurantModel",data.toString());
               if(((EatAtRestaurantModel) data).getNoPeople()==null
                  || ((EatAtRestaurantModel) data).getVisitingDate()==null
                  || ((EatAtRestaurantModel) data).getVisitingTime()==null)
               {

                   if(((EatAtRestaurantModel) data).getNoPeople()==null)
                   {
                       errorMsg="Please enter No. Of People";

                   }else if(((EatAtRestaurantModel) data).getVisitingDate()==null)
                   {
                       errorMsg="Please select Visiting Date";

                   }else if(((EatAtRestaurantModel) data).getVisitingTime()==null)
                   {
                       errorMsg="Please select Visiting Time";
                   }

               }
               else
               {
                   eatRestaurant=true;
                   eatOption="2";
               }





           } else if (data instanceof PickUpModel) {

               Log.e("PickUpModel",data.toString());

               if(((PickUpModel) data).getPickUpDate()==null || ((PickUpModel) data).getPickUpTime()==null)
               {
                   if(((PickUpModel) data).getPickUpDate()==null)
                   {
                       errorMsg="Please select Pickup Date";

                   }else if(((PickUpModel) data).getPickUpTime()==null)
                   {
                       errorMsg="Please select Pickup Time";
                   }

               }else
               {
                   pickUp=true;
                   eatOption="0";
               }

           }
       }

    }

    @Override
    public void eatType(String type) {
        eatOption=type;
    }

    @Override
    public void onClickOffer(Object response) {

    }


    private void dispatchPaymentMode(String paymentMode) {
        CommonUtilities.showLoadingDialog(this);
        if (CommonUtilities.isNetworkAvailable(this)) {

            String pickup_date = "", pickup_time = "", vist_date = "", vist_time = "", address = "", no_of_people = "";


            if (actualData instanceof HomeDeliveryModel) {
                address = ((HomeDeliveryModel) actualData).getAddress();
            } else if (actualData instanceof PickUpModel) {
                pickup_date = ((PickUpModel) actualData).getPickUpDate();
                pickup_time = ((PickUpModel) actualData).getPickUpTime();

            } else if (actualData instanceof EatAtRestaurantModel) {
                vist_time = ((EatAtRestaurantModel) actualData).getVisitingTime();
                vist_date = ((EatAtRestaurantModel) actualData).getVisitingDate();
                no_of_people = ((EatAtRestaurantModel) actualData).getNoPeople();
            }

            CardDetailSaveModel model = new CardDetailSaveModel(SharedPreferenceWriter.getInstance(MyCartActivity.this).getString(SPreferenceKey.USERID), "1", pickup_date, pickup_time, vist_date, vist_time, address, no_of_people);

            String token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> call = servicesInterface.cardDetailSave(map, model);

                ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<CommonModel>() {
                    @Override
                    public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {

                        if (response.isSuccessful()) {
                            CommonModel serverResponse = response.body();
                            if (serverResponse.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                if(currency.equalsIgnoreCase("USD")) {
                                    getExchangeRate(paymentMode);
                                }else {
                                    if(paymentMode.equalsIgnoreCase("COD"))
                                    placeOrder(paymentMode,true,""+total_amont_to_paid,""+delivery_charges);
                                    else OnlinePaymentIntent(""+total_amont_to_paid,""+delivery_charges);
                                }

                            } else {
                                CommonUtilities.dismissLoadingDialog();
                                CommonUtilities.snackBar(MyCartActivity.this, serverResponse.getMessage());
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
        }
    }

    private void getExchangeRate(String paymentMode) {
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService("https://api.exchangeratesapi.io/");
            Call<JsonObject> call = servicesInterface.getExchangeRate("USD","ZAR");
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true, new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JsonObject object= response.body().getAsJsonObject("rates");
                        String currenycyAmt= String.valueOf(object.getAsJsonPrimitive("ZAR"));
                        String amount=CommonUtilities.roundOff(currenycyAmt);
                        if(paymentMode.equalsIgnoreCase("COD")) {
                            placeOrder(paymentMode,true,CommonUtilities.roundOff(""+Double.parseDouble(amount)*total_amont_to_paid),CommonUtilities.roundOff(""+Double.parseDouble(amount)*delivery_charges));
                        }else
                        {
                            OnlinePaymentIntent(CommonUtilities.roundOff(""+Double.parseDouble(amount)*total_amont_to_paid),CommonUtilities.roundOff(""+Double.parseDouble(amount)*delivery_charges));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class DistaceClass extends AsyncTask<Integer, Void ,String>
    {
        @Override
        protected void onPostExecute(String o) {
            Log.e("km", o);
            if(o.contains("km"))
            {
              Double kms= Double.parseDouble( o.split("km")[0].trim());
              if(kms>=8 && !currency.equalsIgnoreCase("USD"))
              {
                  extraDeliveryCharge=kms*1.80;
                  delivery_charges=delivery_charges+extraDeliveryCharge;
                  tvDeliveryCharges.setText(currencySymbol+" " + CommonUtilities.roundOff(""+delivery_charges));
                  total_amont_to_paid= total_amont_to_paid+extraDeliveryCharge;
                  tvTotalAmountPaid.setText(currencySymbol+" " + CommonUtilities.roundOff(""+total_amont_to_paid));
              }else if(kms>=8 && currency.equalsIgnoreCase("USD"))
              {
                  extraDeliveryCharge=kms*1.80*exchangeRate;
                  delivery_charges=delivery_charges+extraDeliveryCharge;
                  tvDeliveryCharges.setText(currencySymbol+" " + CommonUtilities.roundOff(""+delivery_charges));
                  total_amont_to_paid= total_amont_to_paid+extraDeliveryCharge;
                  tvTotalAmountPaid.setText(currencySymbol+" " + CommonUtilities.roundOff(""+total_amont_to_paid));
              }
            }
        }

        @Override
        protected String doInBackground(Integer... voids) {
            Double prelatitute ;
            Double prelongitude ;
            prelatitute=Double.parseDouble(latitude);
            prelongitude=Double.parseDouble(longitude);

            final String[] result_in_kms = {""};

            String url = "https://maps.google.com/maps/api/directions/xml?origin="
                    + restroLat + "," + restroLong + "&destination=" + prelatitute
                    + "," + prelongitude + "&key="+getString(R.string.google_map_api_key);

            String tag[] = { "text" };
            HttpResponse response = null;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(url);
                response = httpClient.execute(httpPost, localContext);
                Log.e("response",response.toString());

                InputStream is = response.getEntity().getContent();


                DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder();
                Document doc = builder.parse(is);
                if (doc != null) {
                    NodeList nl;
                    ArrayList args = new ArrayList();
                    for (String s : tag) {
                        nl = doc.getElementsByTagName(s);
                        if (nl.getLength() > 0) {
                            Node node = nl.item(nl.getLength() - 1);
                            args.add(node.getTextContent());
                        } else {
                            args.add(" - ");
                        }
                    }
                    result_in_kms[0] = String.format("%s", args.get(0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result_in_kms[0];
        }
    }
}