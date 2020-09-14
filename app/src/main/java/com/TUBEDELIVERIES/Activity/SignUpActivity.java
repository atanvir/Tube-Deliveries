package com.TUBEDELIVERIES.Activity;

import android.Manifest;
import android.content.Intent;

import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.Utility.FirebaseFacebookLogin;
import com.TUBEDELIVERIES.Utility.GoogleLogin;
import com.TUBEDELIVERIES.Utility.StartGooglePlayServices;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.AsteriskPasswordTransformationMethod;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.hbb20.CountryCodePicker;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvMatchedPass)
    TextView tvMatchedPass;

    @BindView(R.id.ed_signup_confirm_password)
    TextInputEditText ed_signup_confirm_password;

    @BindView(R.id.ed_signup_password)
    TextInputEditText ed_signup_password;

    @BindView(R.id.countryCodePicker)
    CountryCodePicker countryCodePicker;

    @BindView(R.id.ed_signup_address)
    TextInputEditText ed_signup_address;

    @BindView(R.id.ed_signup_first_name)
    TextInputEditText ed_signup_first_name;

    @BindView(R.id.ed_signup_last_name)
    TextInputEditText ed_signup_last_name;

    @BindView(R.id.ed_signup_email)
    TextInputEditText ed_signup_email;

    @BindView(R.id.ed_signup_phone)
    TextInputEditText ed_signup_phone;

    @BindView(R.id.menuIv)
    ImageView menuIv;

    @BindView(R.id.agreedCB)
    CheckBox agreedCB;

    private String longitudeFromSearch = "";
    private String latitudeFromSearch = "";
    private String addressFromSearch = "";
    private static final int GOOGLE_DATA = 26;
    private static final int FACEBOOK_DATA = 27;
    private String f_name = "";
    private String l_name = "";
    private String email = "";
    private String social_id = "";
    private String facebook_id = "";
    private String google_id = "";

    StartGooglePlayServices startGooglePlayServices = new StartGooglePlayServices(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        menuIv.setVisibility(View.GONE);
        new CommonUtil().setStatusBarGradiant(this, SignUpActivity.class.getSimpleName());
        CommonUtilities.setToolbar(this, mainToolbar, tvTitle, getResources().getString(R.string.small_sign_up));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setAstriskchar();
        getIntentData();
        setTextChangeListener();
    }

    private void setTextChangeListener() {
        ed_signup_confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(ed_signup_password.getText().toString()))
                    tvMatchedPass.setVisibility(View.VISIBLE);
                else
                    tvMatchedPass.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ed_signup_first_name.setText(bundle.getString(ParamEnum.F_NAME.theValue()));
            ed_signup_last_name.setText(bundle.getString(ParamEnum.L_NAME.theValue()));
            ed_signup_address.setText(bundle.getString(ParamEnum.SOCIAL_EMAIL.theValue()));
        }
    }

    private void setAstriskchar() {
        ed_signup_confirm_password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        ed_signup_password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
    }

    @OnClick({R.id.bt_signup, R.id.ed_signup_address,R.id.img_google,R.id.img_fb})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_signup:
            if (validation()) checkEmailRegistration();
            break;

            case R.id.ed_signup_address:
            if(locationPerimission()) setUpLocationSettingsTaskStuff();
            break;

            case R.id.img_google:
            gmailLogin();
            break;

            case R.id.img_fb:
            fbLogin();
            break;


        }
    }

    private void fbLogin() {
        Intent intent = new Intent(this, FirebaseFacebookLogin.class);
        startActivityForResult(intent, FACEBOOK_DATA);
    }


    private void gmailLogin() {
        Intent intent = new Intent(SignUpActivity.this, GoogleLogin.class);
        startActivityForResult(intent, GOOGLE_DATA);
    }


    /*For getting data from Google*/
    private void socialLogin(Intent data, String type) {
        try {
            String name = data.getExtras().getString(ParamEnum.NAME.theValue());
            f_name = data.getExtras().getString(ParamEnum.F_NAME.theValue());
            l_name = data.getExtras().getString(ParamEnum.L_NAME.theValue());
            social_id = data.getExtras().getString(ParamEnum.SOCIAL_ID.theValue());

            if (type.equals("fb"))
                facebook_id = social_id;
            else
                google_id = social_id;

            email = data.getExtras().getString(ParamEnum.SOCIAL_EMAIL.theValue());
            String pictureurl = data.getExtras().getString(ParamEnum.IMAGE.theValue());
            String gender = data.getExtras().getString(ParamEnum.GENDER.theValue());


            socialLogin(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void socialLogin(String type) {
        if (CommonUtilities.isNetworkAvailable(this)) {

            String device_token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.DEVICETOKEN);

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.socialMedialLogin(social_id, facebook_id, google_id, type, f_name, l_name, email, ParamEnum.ANDROID.theValue(), device_token);
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
                                        disaptchMainAct(bean.getSocialMediaLogin().getUser(), bean.getSocialMediaLogin().getAuth().getAccess_token());
                                    } else {
                                        CommonUtilities.snackBar(SignUpActivity.this, bean.getMessage());
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

    private boolean locationPerimission() {
        boolean ret=true;
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
               || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               ret = false;
               requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 11);

           }
       }

        return  ret;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0)
        {
            switch (requestCode)
            {


                case 11:
                   boolean coarseLocation= grantResults[0]==PackageManager.PERMISSION_GRANTED;
                   boolean fineLocation= grantResults[1]==PackageManager.PERMISSION_GRANTED;

                   if(coarseLocation && fineLocation)
                   {
                       setUpLocationSettingsTaskStuff();

                   }else
                   {
                       CommonUtilities.snackBar(this,"You need to Allow the Permission to use this functionality");
                   }
                break;

            }

        }


    }

    public  void setUpLocationSettingsTaskStuff() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(10 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    dispactchAddressPicker();
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {

                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(SignUpActivity.this, 17);

                            } catch (IntentSender.SendIntentException e) {
                            } catch (ClassCastException e) {
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // cannot do anything
                            break;
                    }
                }
            }
        });



    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            latitudeFromSearch = data.getStringExtra("LAT");
            longitudeFromSearch = data.getStringExtra("LONG");
            addressFromSearch = data.getStringExtra("ADDRESS");
            Log.e("address","aaaa"+addressFromSearch);
            ed_signup_address.setText(addressFromSearch);

        }else if(requestCode==17)
        {
            if(resultCode==RESULT_OK)
            {
                dispactchAddressPicker();

            }else if(resultCode==RESULT_CANCELED)
            {
                CommonUtilities.snackBar(this,"Please enable ");
                locationPerimission();
            }
        }
        else if(requestCode==GOOGLE_DATA) {
            if (resultCode == 122 && data != null)
                socialLogin(data, "google");
        }

        else if(requestCode==FACEBOOK_DATA) {
            if (resultCode == 121 && data != null)
                socialLogin(data, "fb");
        }
    }

    private void dispactchAddressPicker(){
        Intent intent=new Intent(this,AddressPicker.class);
        intent.putExtra("Direction","No");
        startActivityForResult(intent,101);
    }

    private boolean validation(){

        if(ed_signup_first_name.getText().toString().isEmpty()){
            CommonUtilities.snackBar(this,getResources().getString(R.string.enter_first_name));
            return false;
        }

        if(ed_signup_last_name.getText().toString().isEmpty()){
            CommonUtilities.snackBar(this,getResources().getString(R.string.enter_last_name));
            return false;
        }

        if(!ed_signup_email.getText().toString().matches(CommonUtilities.emailPattern)){
            CommonUtilities.snackBar(this,getResources().getString(R.string.enter_valid_email));
            return false;
        }

        if(!ed_signup_phone.getText().toString().matches(CommonUtilities.MobilePattern)){
            CommonUtilities.snackBar(this,getResources().getString(R.string.enter_mobile));
            return false;
        }

        if(ed_signup_address.getText().toString().isEmpty()
                &&latitudeFromSearch.isEmpty()&&longitudeFromSearch.isEmpty()){
            CommonUtilities.snackBar(this,getResources().getString(R.string.enter_loacation));
            return false;
        }

        if(ed_signup_password.getText().toString().length()<=7){
            CommonUtilities.snackBar(this,getResources().getString(R.string.atleast_six_digit));
            return false;
        }






        if(!ed_signup_confirm_password.getText().toString().matches(ed_signup_password.getText().toString())){
            CommonUtilities.snackBar(this,getResources().getString(R.string.enter_passwor_match));
            return false;
        }

        if(!agreedCB.isChecked())
        {
            CommonUtilities.snackBar(this,getResources().getString(R.string.agreed_to_term));
            return false;
        }

        return true;
    }


    //send data to otp screen
    private void dispatchVerifcationAct(){

        Intent intent=new Intent(this,VerificationActivity.class);
        intent.putExtra(ParamEnum.FIRST_NAME.theValue(),ed_signup_first_name.getText().toString());
        intent.putExtra(ParamEnum.LAST_NAME.theValue(),ed_signup_last_name.getText().toString());
        intent.putExtra(ParamEnum.PASS.theValue(),ed_signup_password.getText().toString());
        intent.putExtra(ParamEnum.PHONE.theValue(),ed_signup_phone.getText().toString());
        intent.putExtra(ParamEnum.EMAIL.theValue(),ed_signup_email.getText().toString());
        intent.putExtra(ParamEnum.ADDRESS.theValue(),ed_signup_address.getText().toString());
        intent.putExtra(ParamEnum.LATITUDE.theValue(),latitudeFromSearch);
        intent.putExtra(ParamEnum.LONGITUDE.theValue(),longitudeFromSearch);
        intent.putExtra(ParamEnum.COUNTRY_CODE.theValue(),countryCodePicker.getSelectedCountryCodeWithPlus());
        startActivity(intent);

    }

    //////////before-signup-check api///////
    public void checkEmailRegistration() {

        if (CommonUtilities.isNetworkAvailable(this)) {

            //String device_token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.DEVICETOKEN);

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.beforeSignUpCheck(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN)
                        ,ed_signup_phone.getText().toString(), ed_signup_email.getText().toString());

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
                                          if(bean.getMessage().equalsIgnoreCase("Not found")){
                                              dispatchVerifcationAct();
                                          }else {
                                              CommonUtilities.snackBar(SignUpActivity.this, "Your number is already registered with us.");
                                          }
                                    } else {
                                        CommonUtilities.snackBar(SignUpActivity.this, bean.getMessage());
                                    }
                                }
                                else
                                {
                                    //  CommonUtilities.snackBar(VerificationActivity.this, response.body().getMessage());
                                }

                            }
                            @Override
                            public void onFailure(Call<CommonModel> call, Throwable t) {
                                Log.e("Otpfail",t.getMessage());
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            CommonUtilities.snackBar(this, getString(R.string.no_internet));

        }
    }

    private void disaptchMainAct(ResponseBean responseBean, String token) {
        Intent intent = new Intent(this, MainActivity.class);
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.ISLOGIN, ParamEnum.LOGIN.theValue());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.USERID, responseBean.getId());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.TOKEN, "Bearer " + token);
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.USER_NAME, responseBean.getFirst_name() + " " + responseBean.getLast_name());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.EMAIL, responseBean.getEmail_id());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.ADDRESS, responseBean.getAddress());
        startActivity(intent);
        finishAffinity();
        Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
    }

}
