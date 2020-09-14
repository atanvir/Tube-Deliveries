package com.TUBEDELIVERIES.Activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.GPSTracker;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

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


public class VerificationActivity extends AppCompatActivity {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.clVerification)
    ImageView clVerification;

    @BindView(R.id.pinview)
    Pinview pinview;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvResendOtp)
    TextView tvResendOtp;

    @BindView(R.id.menuIv)
    ImageView menuIv;





    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    private String country_code = "";
    private String phone_no = "";
    private String first_name = "";
    private String last_name = "";
    private String otp = "";
    private String verificationCode = "";
    private String email = "";
    private String location = "";
    private String password = "";
    private String latitude = "";
    private String longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        new CommonUtil().setStatusBarGradiant(this,VerificationActivity.class.getSimpleName());
        ButterKnife.bind(this);
        menuIv.setVisibility(View.GONE);
        CommonUtilities.setToolbar(this, mainToolbar, tvTitle, getResources().getString(R.string.verification_act));
        mAuth = FirebaseAuth.getInstance();
        getIntentData();
        GPSTracker tracker= new GPSTracker(this);

        Log.e("lat",""+tracker.getLatitude());
        Log.e("long",""+tracker.getLongitude());

    }

    @OnClick({R.id.clVerification, R.id.tvResendOtp})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clVerification:
                verifyVerificationCode(pinview.getValue());
                break;


            case R.id.tvResendOtp:
                firebaseActions();

                break;
        }
    }
    private void getIntentData() {

        if (getIntent() != null) {

            country_code = getIntent().getStringExtra(ParamEnum.COUNTRY_CODE.theValue());
            phone_no = getIntent().getStringExtra(ParamEnum.PHONE.theValue());
            first_name = getIntent().getStringExtra(ParamEnum.FIRST_NAME.theValue());
            last_name = getIntent().getStringExtra(ParamEnum.LAST_NAME.theValue());
            email = getIntent().getStringExtra(ParamEnum.EMAIL.theValue());
            location = getIntent().getStringExtra(ParamEnum.ADDRESS.theValue());
            password = getIntent().getStringExtra(ParamEnum.PASS.theValue());
            latitude = getIntent().getStringExtra(ParamEnum.LATITUDE.theValue());
            longitude = getIntent().getStringExtra(ParamEnum.LONGITUDE.theValue());


            firebaseActions();
        }

    }


    private void firebaseActions() {
        CommonUtilities.showLoadingDialog(this);
        StartFirebaseLogin();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                country_code + phone_no,// Phone number to verify
                60,                // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,      // Activity (for callback binding)
                mCallback);// OnVerificationStateChangedCallback
    }


    private void StartFirebaseLogin() {
        CommonUtilities.showLoadingDialog(this);
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                String otp = phoneAuthCredential.getSmsCode();
                Log.d("", "" + otp);
                CommonUtilities.dismissLoadingDialog();

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e("failed",e.getMessage());
                Toast.makeText(VerificationActivity.this, "Number verification failed!", Toast.LENGTH_SHORT).show();
                CommonUtilities.dismissLoadingDialog();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                mResendToken = forceResendingToken;
                Log.e("OTP Code", s);
                Toast.makeText(VerificationActivity.this, "Sending  otp...", Toast.LENGTH_SHORT).show();
                CommonUtilities.dismissLoadingDialog();

            }
        };

    }

    private void verifyVerificationCode(String otp) {
        CommonUtilities.showLoadingDialog(this);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
        SigninWithPhone(credential);
    }

    private void SigninWithPhone(PhoneAuthCredential credential) {
        CommonUtilities.showLoadingDialog(this);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(VerificationActivity.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                            registrationApi();

                        } else {
                            Toast.makeText(VerificationActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                            CommonUtilities.dismissLoadingDialog();
                        }
                    }
                });
    }


    private void dispatchMainActivity(ResponseBean responseBean,String accessToken) {


        Intent intent = new Intent(this, MainActivity.class);
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.ISLOGIN, ParamEnum.LOGIN.theValue());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.USERID, responseBean.getId());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.TOKEN,"Bearer "+accessToken);
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.USER_NAME ,responseBean.getFirst_name()+" "+responseBean.getLast_name());

//        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.NOTI_STATUS, commonResponse.getNotify_status());
//        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.PHONENUMBER, commonResponse.getPhone());
//        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.NAME ,commonResponse.getFullname());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.EMAIL, responseBean.getEmail_id());
//        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.ASEC_USERID, commonResponse.getAsec_userid());

        startActivity(intent);
        finishAffinity();
        //Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();


    }


    //////////sign up api///////
    public void registrationApi() {

        if (CommonUtilities.isNetworkAvailable(this)) {

            String device_token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.DEVICETOKEN);

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.register(first_name, last_name, email, country_code, phone_no, latitude, longitude, location, password,device_token, ParamEnum.ANDROID.theValue());

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

                                        if (response.isSuccessful()) {
                                            dispatchMainActivity(bean.getRegisterResponse().getUser(),bean.getRegisterResponse().getAuth().getAccess_token());
                                        }

                                    } else {
                                        CommonUtilities.snackBar(VerificationActivity.this, bean.getMessage());
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


}
