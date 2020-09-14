package com.TUBEDELIVERIES.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.TUBEDELIVERIES.Utility.Validation;
import com.hbb20.CountryCodePicker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMyProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.mainToolbar) Toolbar mainToolbar;
    @BindView(R.id.btnSaveProfile) TextView btnSaveProfile;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.edtFirstName) TextInputEditText edtFirstName;
    @BindView(R.id.edtPhoneProfile) TextInputEditText edtPhoneProfile;
    @BindView(R.id.edtEmailMyProfile) TextInputEditText edtEmailMyProfile;
    @BindView(R.id.edtLastName) TextInputEditText edtLastName;
    @BindView(R.id.edtLandMark) TextInputEditText edtLandMark;
    @BindView(R.id.countryCodePicker) CountryCodePicker countryCodePicker;
    @BindView(R.id.edtLocation) TextInputEditText edtLocation;
    @BindView(R.id.edAddressType) TextInputEditText edAddressType;
    @BindView(R.id.edStreet) TextInputEditText edStreet;
    @BindView(R.id.edFloor) TextInputEditText edFloor;
    @BindView(R.id.edApartment) TextInputEditText edApartment;
    @BindView(R.id.edtBuilding) TextInputEditText edtBuilding;
    @BindView(R.id.menuIv) ImageView menuIv;
    String addressType[]={"Please Select","Home","Office"};
    @BindView(R.id.addressTypeSpinner) Spinner addressTypeSpinner;
    private String alternateAddress = "";
    private String longitudeFromSearch = "";
    private String latitudeFromSearch = "";
    private String addressFromSearch = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_my_profile);
    new CommonUtil().setStatusBarGradiant(this,EditMyProfileActivity.class.getSimpleName());
    ButterKnife.bind(this);
    menuIv.setVisibility(View.GONE);
    edtLocation.setOnClickListener(this::onClick);
    edAddressType.setOnClickListener(this::onClick);
    addressTypeSpinner.setOnItemSelectedListener(this);
    CommonUtilities.setToolbar(this, mainToolbar, tvTitle, getResources().getString(R.string.my_profile));
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    viewProfile();

   }

    @OnClick({R.id.btnSaveProfile})
    public void onClick(View view) {

        switch (view.getId()) {

        case R.id.btnSaveProfile:
        if(checkValidation()) editProfile();
        break;

        case R.id.edtLocation:
        if(locationPerimission()) {
            setUpLocationSettingsTaskStuff();
        }
        break;

        case R.id.edAddressType:
        addressTypeSpinner();
        break;

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
                                resolvable.startResolutionForResult(EditMyProfileActivity.this, 17);

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

    private boolean checkValidation() {

        boolean ret=true;

        if(!Validation.hasText(edtLocation))
        {
            CommonUtilities.snackBar(this,"Please select Location ");
            ret=false;
        }

        else if(!Validation.hasText(edAddressType)){
           CommonUtilities.snackBar(this,"Please select Address Type");
            ret=false;
        }

        else if(!Validation.hasText(edtLandMark))
        {
            CommonUtilities.snackBar(this,"Please enter Landmark");
            ret=false;
        }

        else if(!Validation.hasText(edtFirstName))
        {
            CommonUtilities.snackBar(this,"Please enter First Name");
            ret=false;

        }
        else if(!Validation.hasText(edtLastName))
        {
            CommonUtilities.snackBar(this,"Please enter Last Name");
            ret=false;
        }

        return ret;

    }

    private void addressTypeSpinner() {
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(addressType))
        {
            @Override
            public boolean isEnabled(int position) {
                if(position==0)
                {
                    return false;
                }else
                {
                    return true;
                }
            }

            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
               TextView textView=(TextView) super.getView(position, convertView, parent);
               if(position==0) textView.setTextColor(ContextCompat.getColor(EditMyProfileActivity.this,R.color.colorGrey));
               else textView.setTextColor(Color.TRANSPARENT);

               return textView;

            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addressTypeSpinner.setAdapter(adapter);
        addressTypeSpinner.performClick();


    }

    //////////view Profile api///////
    public void viewProfile() {
        if (CommonUtilities.isNetworkAvailable(this)) {

            String token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.userDetails(map);
                ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, this, true,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                        setData(bean.getUserDetails());
                                    } else {
                                        CommonUtilities.snackBar(EditMyProfileActivity.this, bean.getMessage());
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

    private void setData(ResponseBean response) {
    edtEmailMyProfile.setText(response.getEmail_id());
    edtPhoneProfile.setText(response.getPhone());
    edtLandMark.setText(response.getLandmark());
    edtLocation.setText(response.getAddress());
    edtFirstName.setText(response.getFirst_name());
    edtLastName.setText(response.getLast_name());
    latitudeFromSearch = response.getLatitude();
    longitudeFromSearch = response.getLongitude();
    addressFromSearch = response.getArea();
    if (response.getAddress_type().equals("0")) {
        edAddressType.setText(addressType[1]);
    } else {
        edAddressType.setText(addressType[2]);
    }
    edtBuilding.setText(response.getBuilding());
    edStreet.setText(response.getStreet());
    edFloor.setText(response.getFloor());
    edApartment.setText(response.getApartment());
   }

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
                    locationPerimission();
                }
                break;

            }

        }


    }


    private void dispactchAddressPicker() {
        Intent intent = new Intent(this, AddressPicker.class);
        intent.putExtra("Direction","No");
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            latitudeFromSearch = data.getStringExtra("LAT");
            longitudeFromSearch = data.getStringExtra("LONG");
            addressFromSearch = data.getStringExtra("ADDRESS");
            edtLocation.setText(addressFromSearch);
            //getSerchDataFromApi(type, latitudeFromSearch, longitudeFromSearch);

        }
        else if(requestCode==17)
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

    }


    //////////view Profile api///////
    public void editProfile() {
        if (CommonUtilities.isNetworkAvailable(this)) {
            String token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));
            String adressType="";

            if(edAddressType.getText().toString().equalsIgnoreCase("Home")) adressType="0";
            else if(edAddressType.getText().toString().equalsIgnoreCase("Office")) adressType="1";
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.updateProfile(map,
                        edtFirstName.getText().toString(),
                        edtLastName.getText().toString(),
                        edtEmailMyProfile.getText().toString(),
                        countryCodePicker.getDefaultCountryCodeWithPlus(),
                        edtPhoneProfile.getText().toString(),
                        edtLocation.getText().toString(),
                        latitudeFromSearch,
                        longitudeFromSearch,
                        edtLandMark.getText().toString(),
                        adressType,
                        edtLocation.getText().toString(),
                        edStreet.getText().toString(),
                        edtBuilding.getText().toString(),
                        edFloor.getText().toString(),
                        edApartment.getText().toString());

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
                                        finish();
                                    } else {
                                        CommonUtilities.snackBar(EditMyProfileActivity.this, bean.getMessage());
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position!=0)
        {
            edAddressType.setText(addressType[position]);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
