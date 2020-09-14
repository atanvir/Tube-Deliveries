package com.TUBEDELIVERIES.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.TUBEDELIVERIES.BuildConfig;
import com.TUBEDELIVERIES.Fragments.DrawerLayout.CustomerStoriesFrag;
import com.TUBEDELIVERIES.Fragments.DrawerLayout.WalletCreditFrag;
import com.TUBEDELIVERIES.Fragments.IEatOptions;
import com.TUBEDELIVERIES.Fragments.SubcriptionFragment;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.MyCurrentDetailsModel;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.Utility.LocationClass;
import com.amitshekhar.DebugDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.TUBEDELIVERIES.Fragments.BottomNavigation.MyOrdersFragment.PastOrdersFragment;
import com.TUBEDELIVERIES.Fragments.DrawerLayout.AboutUsFrag;
import com.TUBEDELIVERIES.Fragments.DrawerLayout.ContactUsFrag;
import com.TUBEDELIVERIES.Fragments.BottomNavigation.NavigationHomeFragment;
import com.TUBEDELIVERIES.Fragments.DrawerLayout.MyFavFragment;
import com.TUBEDELIVERIES.Fragments.BottomNavigation.NavigationOrdersFragment;
import com.TUBEDELIVERIES.Fragments.BottomNavigation.NavigationProfileFragment;
import com.TUBEDELIVERIES.Fragments.BottomNavigation.NavigationSearchFragment;
import com.TUBEDELIVERIES.Fragments.DrawerLayout.NotificationFragment;
import com.TUBEDELIVERIES.Fragments.DrawerLayout.OffersFragment;
import com.TUBEDELIVERIES.Fragments.DrawerLayout.ProfieFragmentWithoutLogin;
import com.TUBEDELIVERIES.Fragments.DrawerLayout.Setting_fragment;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.SwitchFragment;
import com.TUBEDELIVERIES.Fragments.DrawerLayout.WebViewFragment;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Receiver.NetworkChangeReceiver;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.AddressResultReceiver;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.FetchAddressService;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.TUBEDELIVERIES.Utility.StartGooglePlayServices;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.facebook.GraphRequest.TAG;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener, NavigationHomeFragment.AppCredit {

    @BindView(R.id.mainToolbar)  public Toolbar mainToolbar;
    @BindView(R.id.tvTitle) public TextView tvTitle;
    @BindView(R.id.edtSearchPlace) public EditText edtSearchPlace;
    @BindView(R.id.ivCart) ImageView ivCart;

    private boolean isLocServiceStarted = false;
    private LocationCallback locationCallback;
    private double lat = 0.0, lng = 0.0;
    private double Lat, Long;
    private boolean isFirstTimeLocation = true;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    private GoogleApiClient googleApiClient;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.lottieAnimationView)
    LottieAnimationView lottieAnimationView;
    @BindView(R.id.mainFrame)
    FrameLayout mainFrame;
    @BindView(R.id.mainLayout)
    ConstraintLayout mainLayout;
    @BindView(R.id.menuIv)
    ImageView menuIv;
    @BindView(R.id.WalletCl)
    ConstraintLayout WalletCl;
    @BindView(R.id.SubcriptionCl)
    ConstraintLayout SubcriptionCl;
    private NetworkChangeReceiver mNetworkReceiver;
    private boolean isFirstTime = false;
    private NavigationHomeFragment navigationHomeFragment = new NavigationHomeFragment();
    private NavigationSearchFragment navigationSearchFragment = new NavigationSearchFragment();
    private NavigationOrdersFragment navigationOrdersFragment = new NavigationOrdersFragment();
    private NavigationProfileFragment navigationProfileFragment = new NavigationProfileFragment();
    private String longitudeFromSearch="";
    private String latitudeFromSearch="";
    private Double latitudeFromPicker=0.0;
    private Double longitudeFromPicker=0.0;
    private String addressFromSearch="";
    private LocationRequest locationRequest;
    private LatLng currentLocationLatLngs;
    private Location mLastLocation;
    boolean doubleBackToExitPressedOnce = false;
    private boolean isHomeSet = false;
    protected final int REQ_CODE_GPS_SETTINGS = 150;
    private final int REQ_CODE_LOCATION = 107;
    private FusedLocationProviderClient mFusedLocationClient;
    private AddressResultReceiver mResultReceiver;
    private LocationClass locationClass;

    //Navigation Drawer
    private ConstraintLayout homeCl,myFavCl,myOffersCL,myPaymentCL,aboutusCl,
            notificationCl,settingCl,shareAppCl,cstoriesCl,
            termsAndConditionCl,contactUsCl,faqCl,helpCl;

    private TextView headerText,tvAppCredit;
    IEatOptions eatOptions;
    StartGooglePlayServices startGooglePlayServices=new StartGooglePlayServices(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new CommonUtil().setStatusBarGradiant(this,MainActivity.class.getSimpleName());
        NavigationHomeFragment.setListner(this);
        ButterKnife.bind(this);
        tvAppCredit= findViewById(R.id.tvAppCredit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        CommonUtilities.getHashKey(this);
        isFirstTime = true;
        edtSearchPlace.setVisibility(View.VISIBLE);
        ivCart.setVisibility(View.VISIBLE);
        locationClass= new LocationClass(MainActivity.this);
        if(locationClass.getLocation()!=null)
        {
            locationClass.getUpdateLocationListener();
        }
        menuIv.setOnClickListener(this::onClick);
        setBottomNavigationAction();
        setNavigationDrawer();
        if(getIntent().getStringExtra("type")!=null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new NotificationFragment()).commit();

        }else {
            if (!isFirstTimeLocation) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new NavigationHomeFragment()).commit();
                startLocationFunctioning();
            }
            if (getIntent().getStringExtra("className") != null) {
                if (getIntent().getStringExtra("className").equalsIgnoreCase(MyCartActivity.class.getSimpleName())) {
                    loadHomeFragment();
                }
            }
        }
    }



    private void setBottomNavigationAction() {

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction ft;

                switch (menuItem.getItemId()) {
                    case R.id.bottomHome:
                    isFirstTime = false;
                    mainToolbar.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.common_gradient_toolbar));
                    SwitchFragment.changeFragment(getSupportFragmentManager(), navigationHomeFragment,false,true);
                    tvTitle.setVisibility(View.GONE);
                    edtSearchPlace.setVisibility(View.VISIBLE);
                    ivCart.setVisibility(View.VISIBLE);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    return true;


                    case R.id.bottomSearch:
                    mainToolbar.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.common_gradient_toolbar));
                    SwitchFragment.changeFragment(getSupportFragmentManager(), navigationSearchFragment,false,true);
                    return true;

                    case R.id.bottomOrders:
                    mainToolbar.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.common_gradient_toolbar));
                    if(SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())) {
                        SwitchFragment.changeFragment(getSupportFragmentManager(), navigationOrdersFragment, false, true);
                    }
                    else {
                        showAlertDialog();
                    }
                    return true;

                    case R.id.bottomMyProfile:
                    if(SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())) {
                        mainToolbar.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.common_gradient_toolbar));
                        SwitchFragment.changeFragment(getSupportFragmentManager(), navigationProfileFragment, false, true);
                    }else {
                        tvTitle.setVisibility(View.VISIBLE);
                        edtSearchPlace.setVisibility(View.GONE);
                        ivCart.setVisibility(View.VISIBLE);
                        tvTitle.setText("My Profile");
                        mainToolbar.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.common_green_toolbar));
                        SwitchFragment.changeFragment(getSupportFragmentManager(), new ProfieFragmentWithoutLogin(), false, true);
                    }
                    return true;

                }
                return false;
            }
        });
    }




    private void setNavigationDrawer() {

        //Navigation Drawer
        headerText=findViewById(R.id.headerText);
        homeCl=findViewById(R.id.homeCl);
        myFavCl=findViewById(R.id.myFavCl);
        myOffersCL=findViewById(R.id.myOffersCL);
        myPaymentCL=findViewById(R.id.myPaymentCL);
        notificationCl=findViewById(R.id.notificationCl);
        settingCl=findViewById(R.id.settingCl);
        shareAppCl=findViewById(R.id.shareAppCl);
        cstoriesCl=findViewById(R.id.cstoriesCl);
        aboutusCl=findViewById(R.id.aboutusCl);
        termsAndConditionCl=findViewById(R.id.termsAndConditionCl);
        contactUsCl=findViewById(R.id.contactUsCl);
        faqCl=findViewById(R.id.faqCl);
        helpCl=findViewById(R.id.helpCl);

        //Drawer listner
        homeCl.setOnClickListener(this);
        myFavCl.setOnClickListener(this);
        myOffersCL.setOnClickListener(this);
        myPaymentCL.setOnClickListener(this);
        notificationCl.setOnClickListener(this);
        settingCl.setOnClickListener(this);
        shareAppCl.setOnClickListener(this);
        cstoriesCl.setOnClickListener(this);
        aboutusCl.setOnClickListener(this);
        termsAndConditionCl.setOnClickListener(this);
        contactUsCl.setOnClickListener(this);
        faqCl.setOnClickListener(this);
        helpCl.setOnClickListener(this);
        WalletCl.setOnClickListener(this);
        SubcriptionCl.setOnClickListener(this);
        headerText.setText(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.SAVE_NAME));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            hideKeyboard();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragWithAnim(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.replace(R.id.mainFrame, fragment);
        ft.commit();
    }

    public void setToolbar(String title, int visibility) {
        edtSearchPlace.setVisibility(View.GONE);
        ivCart.setVisibility(visibility);
        bottomNavigationView.setVisibility(visibility);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
    }


    private void registerNetworkBroadCast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(SPreferenceKey.COUNTRY_CODE, SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.COUNTRY_CODE));
        Log.e(SPreferenceKey.CURRENCY, SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.CURRENCY));
        Log.e(SPreferenceKey.EXCHANGE_RATE, SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.EXCHANGE_RATE));
        Log.e(SPreferenceKey.CURRENCY_SYMBOL, SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.CURRENCY_SYMBOL));
        if(SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.CURRENCY_SYMBOL).equalsIgnoreCase("")) {
            myCurrentDetailsApi();
        }

        if (googleApiClient != null) {
            startLocationUpdates();
            googleApiClient.connect();
        }else
        {
            if (startGooglePlayServices.isGPlayServicesOK(this)) {
                buildGoogleApiClient();
            }
        }
    }

    private void myCurrentDetailsApi() {
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService("http://ip-api.com/");
            Call<MyCurrentDetailsModel> call = servicesInterface.getMyDeatails();
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<MyCurrentDetailsModel>() {
            @Override
            public void onResponse(Call<MyCurrentDetailsModel> call, Response<MyCurrentDetailsModel> response) {
                getCurrencyByCountry(response.body().getCountryCode());
            }

            @Override
            public void onFailure(Call<MyCurrentDetailsModel> call, Throwable t) {
            }
        });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCurrencyByCountry(String countryCode) {
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService("https://restcountries.eu/rest/v1/");
            Call<MyCurrentDetailsModel> call = servicesInterface.getCurrency(countryCode);
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<MyCurrentDetailsModel>() {
                @Override
                public void onResponse(Call<MyCurrentDetailsModel> call, Response<MyCurrentDetailsModel> response) {
                    String currency=response.body().getCurrencies().get(0);
                    if(currency.equalsIgnoreCase("ZAR")) {
                        SharedPreferenceWriter.getInstance(MainActivity.this).writeStringValue(SPreferenceKey.CURRENCY, countryCode);
                    }else
                    {
                        SharedPreferenceWriter.getInstance(MainActivity.this).writeStringValue(SPreferenceKey.CURRENCY, "USD");
                    }
                    SharedPreferenceWriter.getInstance(MainActivity.this).writeStringValue(SPreferenceKey.COUNTRY_CODE,currency);
                    getExchangeRate(currency);
                }

                @Override
                public void onFailure(Call<MyCurrentDetailsModel> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getExchangeRate(final String currency) {
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService("https://api.exchangeratesapi.io/");
            Call<JsonObject> call = servicesInterface.getExchangeRate("ZAR","USD");
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JsonObject object= response.body().getAsJsonObject("rates");
                        String currenycyAmt= String.valueOf(object.getAsJsonPrimitive("USD"));
                        SharedPreferenceWriter.getInstance(MainActivity.this).writeStringValue(SPreferenceKey.EXCHANGE_RATE,""+currenycyAmt);
                        getCurrencySymbol(!currency.equalsIgnoreCase("ZAR")?"USD":"ZAR");
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

    private void getCurrencySymbol(String currency) {
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService("https://www.localeplanet.com/api/auto/");
            Call<JsonObject> call = servicesInterface.getAllCurrencySymbols();
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JsonObject object= response.body().getAsJsonObject(currency);
                        SharedPreferenceWriter.getInstance(MainActivity.this).writeStringValue(SPreferenceKey.CURRENCY_SYMBOL, ""+String.valueOf(object.getAsJsonPrimitive("symbol_native")).replace("\"", ""));
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

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                stopLocationUpdates();
                googleApiClient.disconnect();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterNetworkChanges();
        isFirstTimeLocation=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregisterNetworkChanges();

        if(googleApiClient!=null)
            googleApiClient.disconnect();
    }


    ////relaod fragment when your are online////////
    private void reloadFragment() {

        Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.mainFrame);
        if (f instanceof NavigationHomeFragment) {
            if (!isFirstTime) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, navigationHomeFragment).commit();

            }
        } else if (f instanceof NavigationSearchFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, navigationSearchFragment).commit();


        } else if (f instanceof NavigationOrdersFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, navigationOrdersFragment).commit();
        } else if (f instanceof NavigationProfileFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, navigationProfileFragment).commit();
        }


    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
    }


    private void dispactchAddressPicker(){
        Intent intent=new Intent(this,AddressPicker.class);
        intent.putExtra("Direction","No");
        startActivityForResult(intent,101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {

            latitudeFromPicker = Double.parseDouble(data.getStringExtra("LAT"));
            longitudeFromPicker = Double.parseDouble(data.getStringExtra("LONG"));
            addressFromSearch = data.getStringExtra("ADDRESS");
            edtSearchPlace.setText(addressFromSearch);
            if(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.ADDRESS).equalsIgnoreCase(""))
            {
                SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.ADDRESS,addressFromSearch);
            }

            SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.LATITUDE,data.getStringExtra("LAT"));
            SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.LONGITUDE,data.getStringExtra("LONG"));

            ///refresh api when select address from locatin picker class
            NavigationHomeFragment navigationHomeFragment = (NavigationHomeFragment) getSupportFragmentManager().findFragmentById(R.id.mainFrame);
            navigationHomeFragment.homeRestroList(this,String.valueOf(latitudeFromPicker),String.valueOf(longitudeFromPicker),false,true);

        }
        else if(requestCode==90)
        {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.mainFrame);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        else if(requestCode==REQ_CODE_GPS_SETTINGS)
        {

            if(resultCode==RESULT_OK)
            {
                loadHomeFragment();

            }else if(resultCode==RESULT_CANCELED)
            {
                Toast.makeText(this, "Please enable GPS for better stores near by you", Toast.LENGTH_SHORT).show();
                loadHomeFragment();
            }
        }

    }

    //    METHOD: TO START FULL PROCESS FOR FIRST TIME..
    public void startLocationFunctioning() {
        if (!CommonUtilities.isNetworkAvailable(this)) {
            Toast.makeText(this.getApplicationContext(), "Internet not available.", Toast.LENGTH_SHORT).show();
        } else {
            if (startGooglePlayServices.isGPlayServicesOK(this)) {
                buildGoogleApiClient();
            }

        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.w(TAG, "onConnected");
        setUpLocationSettingsTaskStuff(); // On Connected
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended: " + i);
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    //    METHOD TO GET CURRENT LOCATION OF DEVICE
    public void loadCurrentLoc() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    startGooglePlayServices.showDenyRationaleDialog(this,"You need to allow access to Device Location", new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which) {
                            case BUTTON_POSITIVE:
                            dialog.dismiss();
                            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE_LOCATION);
                            break;

                            case BUTTON_NEGATIVE:
                            dialog.dismiss();
                            finish();
                            break;
                            }

                        }
                    });

                    return;
                }

                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE_LOCATION);

                return;
            }


            /*DO THE LOCATION STUFF*/

            try {

                mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    Log.w(TAG, "addOnSuccessListener: location: " + location);
                                    locationCallBack(location);
                                } else {
                                    //Toast.makeText(MainActivity.this, "Make sure that Location is Enabled on the device.", Toast.LENGTH_LONG).show();
                                    isLocServiceStarted = false;
                                    Log.w(TAG, "addOnSuccessListener: location: " + null);
                                    if(locationClass.getLocation()!=null)
                                    {
                                        locationCallBack(locationClass.getLocation());
                                    }
                                    //locationCallBack(mLastLocation);
                                }
                            }
                        });


                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        for (Location location : locationResult.getLocations()) {
                            // Update UI with location data
                            if (location != null) {
                                Log.w(TAG, "LocationCallback:" + location);

                                if (!isLocServiceStarted) {
                                    locationCallBack(location);
                                }
                            }

                        }
                    }

                };


            } catch (Exception e) {
                e.printStackTrace();
            }


        } else    //    PRE-Marshmallow
        {

            /*DO THE LOCATION STUFF*/

            try {

                mFusedLocationClient.getLastLocation().addOnSuccessListener(this,
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object

                                    Log.w(TAG, "addOnSuccessListener: location: " + location);

                                    locationCallBack(location);


                                } else {
//                                    Toast.makeText(CreateYourBookClub.this, "Make sure that Location is Enabled on the device.", Toast.LENGTH_LONG).show();

                                    isLocServiceStarted = false;
                                    Log.w(TAG, "addOnSuccessListener: location: " + null);
//                                    MyApplication.makeASnack(CreateYourBookClub.this.binding.getRoot(), getString(R.string.no_location_detected));
                                }
                            }
                        });


                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        for (Location location : locationResult.getLocations()) {
                            // Update UI with location data
                            if (location != null) {
                                Log.w(TAG, "LocationCallback:" + location);

                                if (!isLocServiceStarted) {


                                    locationCallBack(location);


                                }
                            }

                        }
                    }

                };


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    // LOCATION SETTINGS SET UP
    public  void setUpLocationSettingsTaskStuff() {
        /* Initialize the pending result and LocationSettingsRequest */
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // GPS is already enabled no need of review_dialog
                Log.w(TAG, "onResult: Success 1");
                loadCurrentLoc(); //GET CURRENT LOCATION
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a review_dialog.
                    Log.w(TAG, "REQ_CODE_GPS_SETTINGS: REQ " + 0);

                    try {
                        // Show the review_dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        Log.w(TAG, "REQ_CODE_GPS_SETTINGS: REQ " + 1);
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this, REQ_CODE_GPS_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                        sendEx.printStackTrace();
                    }
                }
            }
        });
    }


    //   METHOD: TO  Create an instance of the GoogleAPIClient AND LocationRequest
    private void buildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this, 1 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        googleApiClient.connect();
        createLocationRequest();
    }


    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#reques-tPermissions for more details.
            return;
        }
        boolean b = mFusedLocationClient != null && locationRequest != null;
        if (b && locationCallback != null)
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }


    private void stopLocationUpdates() {
        boolean b = mFusedLocationClient != null;
        if (b && locationCallback != null)
            mFusedLocationClient.removeLocationUpdates(locationCallback);
    }


    public void locationCallBack(Location location) {
        mLastLocation = location;

        if(isFirstTimeLocation) {
            SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.LATITUDE, String.valueOf(mLastLocation.getLatitude()));
            SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.LONGITUDE, String.valueOf(mLastLocation.getLongitude()));

            latitudeFromPicker = mLastLocation.getLatitude();
            longitudeFromPicker = mLastLocation.getLongitude();

            if (getIntent().getExtras()!=null)
            {
                if(getIntent().getExtras().getString("type")!=null)
                {

                    if (getIntent().getExtras().getString("type").equalsIgnoreCase("delivered")
                            || getIntent().getExtras().getString("type").equalsIgnoreCase("rejected"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("type", PastOrdersFragment.class.getSimpleName());
                        navigationOrdersFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, navigationOrdersFragment).commit();
                        bottomNavigationView.setSelectedItemId(R.id.bottomOrders);
                    }
                    else
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, navigationOrdersFragment).commit();
                        bottomNavigationView.setSelectedItemId(R.id.bottomOrders);
                    }
                } else
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new NavigationHomeFragment()).commit();
                }
            }
            else
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new NavigationHomeFragment()).commit();
            }

        }
        Lat = mLastLocation.getLatitude();
        Long = mLastLocation.getLongitude();

        Intent intent = new Intent(this, FetchAddressService.class);
        intent.putExtra(FetchAddressService.FIND_BY, FetchAddressService.FIND_BY_LOCATION);
        intent.putExtra(FetchAddressService.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressService.LOCATION, mLastLocation);
        this.startService(intent);
        isLocServiceStarted = true;

        currentLocationLatLngs = new LatLng(lat, lng);

    }


    //Method: To create location request and set its priorities
    public  void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(10 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // CREATE A FUSED LOCATION CLIENT PROVIDER OBJECT
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        //      Initialize AddressResultReceiver class object
        mResultReceiver = new AddressResultReceiver(MainActivity.this,edtSearchPlace,null,isFirstTimeLocation);
    }



    // show  alert dialog box
    public void showAlertDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_alert);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tvTitleDialog = dialog.findViewById(R.id.tvTitleDialog);

        TextView tvMsgDialog = dialog.findViewById(R.id.tvMsgDialog);

        tvTitleDialog.setText("You are not logged in");

        tvMsgDialog.setText("Want to go to Login Screen ?");

        TextView tvYes = dialog.findViewById(R.id.tvYes);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
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

    @OnClick({R.id.ivCart,R.id.edtSearchPlace})
    public void onClick(View view) {

        Bundle bundle;
        WebViewFragment webViewFragment;

        switch (view.getId()) {

            case R.id.SubcriptionCl:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new SubcriptionFragment()).addToBackStack(OffersFragment.class.getName()).commit();
                drawerLayout.closeDrawers();
                break;

            case R.id.ivCart:
                startActivity(new Intent(this, MyCartActivity.class));
                break;

            case R.id.edtSearchPlace:
                dispactchAddressPicker();
                break;


            //Navigation Drawer
            case R.id.homeCl:
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new NavigationHomeFragment()).addToBackStack(NavigationHomeFragment.class.getName()).commit();
                tvTitle.setVisibility(View.GONE);
                edtSearchPlace.setVisibility(View.VISIBLE);
                ivCart.setVisibility(View.VISIBLE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                drawerLayout.closeDrawers();
                break;

            case R.id.myFavCl:
                if(SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())){
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new MyFavFragment()).addToBackStack(MyFavFragment.class.getName()).commit();
                    drawerLayout.closeDrawers();
                }
                else{
                    showAlertDialog();
                }
                break;

            case R.id.myOffersCL:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new OffersFragment()).addToBackStack(OffersFragment.class.getName()).commit();
                drawerLayout.closeDrawers();
                break;

            case R.id.myPaymentCL:
                showComingSoonDialog();
                break;

            case R.id.notificationCl:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new NotificationFragment()).addToBackStack(NotificationFragment.class.getName()).commit();
                drawerLayout.closeDrawers();
                break;


            case R.id.settingCl:
                if(SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue()))
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new Setting_fragment()).addToBackStack(Setting_fragment.class.getName()).commit();
                else
                    showAlertDialog();
                drawerLayout.closeDrawers();
                break;

            case R.id.shareAppCl:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Tube Deliveries");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }

                break;

            case R.id.cstoriesCl:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new CustomerStoriesFrag()).addToBackStack(ContactUsFrag.class.getName()).commit();
                drawerLayout.closeDrawers();
                break;

            case R.id.aboutusCl:
                bundle=new Bundle();

                bundle.putString(ParamEnum.WEB_URL.theValue(),"http://18.217.178.223/public/api/about-us");
                bundle.putString(ParamEnum.TITLE.theValue(),"About Us");
                webViewFragment=new WebViewFragment();
                webViewFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,webViewFragment).addToBackStack(AboutUsFrag.class.getName()).commit();
                drawerLayout.closeDrawers();
                break;


            case R.id.termsAndConditionCl:
                bundle=new Bundle();
                bundle.putString(ParamEnum.WEB_URL.theValue(),"http://18.217.178.223/public/api/terms");
                bundle.putString(ParamEnum.TITLE.theValue(),"Terms & Conditions");
                webViewFragment=new WebViewFragment();
                webViewFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,webViewFragment).addToBackStack(AboutUsFrag.class.getName()).commit();
                drawerLayout.closeDrawers();
                break;

            case R.id.contactUsCl:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new ContactUsFrag()).addToBackStack(ContactUsFrag.class.getName()).commit();
                drawerLayout.closeDrawers();
                break;


            case R.id.faqCl:
                bundle=new Bundle();
                bundle.putString(ParamEnum.WEB_URL.theValue(),"http://18.217.178.223/public/api/faq");
                bundle.putString(ParamEnum.TITLE.theValue(),"FAQ's");
                webViewFragment=new WebViewFragment();
                webViewFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,webViewFragment).addToBackStack(AboutUsFrag.class.getName()).commit();
                drawerLayout.closeDrawers();
                break;

            case R.id.helpCl:
                bundle=new Bundle();
                bundle.putString(ParamEnum.WEB_URL.theValue(),"http://18.217.178.223/public/api/help");
                bundle.putString(ParamEnum.TITLE.theValue(),"Help");
                webViewFragment=new WebViewFragment();
                webViewFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,webViewFragment).addToBackStack(AboutUsFrag.class.getName()).commit();
                drawerLayout.closeDrawers();
                break;

            case R.id.WalletCl:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,new WalletCreditFrag()).addToBackStack(AboutUsFrag.class.getName()).commit();
                drawerLayout.closeDrawers();
                break;


            case R.id.menuIv:
                drawerLayout.openDrawer(GravityCompat.START);
                hideKeyboard();
                break;

        }
    }


    public void showComingSoonDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_come_soon);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tvYes = dialog.findViewById(R.id.tvYes);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        NavigationHomeFragment navigationHomeFragment = (NavigationHomeFragment) getSupportFragmentManager().findFragmentByTag(NavigationHomeFragment.class.getName());
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }
//        }else if(homeCardFrag !=null && homeCardFrag.ivBack.getVisibility() == View.VISIBLE) {
//            homeCardFrag.onSearchBack();
//            return;
//        }
        else if(!isHomeSet) {
            loadHomeFragment();
            isHomeSet = true;
            return;
        }else if(doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }


    private void loadHomeFragment() {
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        SwitchFragment.changeFragment(getSupportFragmentManager(), navigationHomeFragment, false, false);
        // showBadge();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void creditPoint(String point) {
//        if(!point.equalsIgnoreCase(""))
//        {
//            if(Integer.parseInt(point)>1)
//            {
//                tvAppCredit.setText("App Credit : "+ point+" points");
//
//            }
//            else
//            {
//                tvAppCredit.setText("App Credit : "+ point+" point");
//
//            }
//        }
    }
}
