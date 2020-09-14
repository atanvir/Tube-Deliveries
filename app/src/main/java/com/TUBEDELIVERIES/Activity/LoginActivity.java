package com.TUBEDELIVERIES.Activity;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.RoomDatabase.Entity.OrderItemsEntity;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.AsteriskPasswordTransformationMethod;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.FirebaseFacebookLogin;
import com.TUBEDELIVERIES.Utility.GoogleLogin;
import com.TUBEDELIVERIES.Utility.ParamEnum;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.ed_login_password)
    TextInputEditText ed_login_password;

    private static final int GOOGLE_DATA = 26;
    private static final int FACEBOOK_DATA = 27;

    private ArrayList<OrderItemsEntity> offlineOrderList = new ArrayList<>();
    @BindView(R.id.ed_login_email)
    TextInputEditText ed_login_email;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.img_google)
    ImageView img_google;

    @BindView(R.id.img_fb)
    ImageView img_fb;

    @BindView(R.id.tvSkipLogin)
    TextView tvSkipLogin;

    private String f_name = "";
    private String l_name = "";
    private String email = "";
    private String social_id = "";
    private String facebook_id = "";
    private String google_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    new CommonUtil().setStatusBarGradiant(this,LoginActivity.class.getSimpleName());
    ButterKnife.bind(this);
    CommonUtilities.getDeviceToken(this);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    ed_login_password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
    }

    @OnClick({R.id.tv_forget_password, R.id.tv_Sign_Up, R.id.btnLogin, R.id.img_google, R.id.img_fb, R.id.tvSkipLogin})
    public void onClick(View view) {
        switch (view.getId()) {

        case R.id.tv_forget_password:
        startActivity(new Intent(this, ForgotPasswordActivity.class));
        break;

        case R.id.tv_Sign_Up:
        startActivity(new Intent(this, SignUpActivity.class));
        break;

        case R.id.btnLogin:
        loginServiceHit();
        break;

        case R.id.img_google:
        gmailLogin();
        break;

        case R.id.img_fb:
        fbLogin();
        break;

        case R.id.tvSkipLogin:
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
        break;

       }
    }
    //Sending data to backend
    private void sendDataToBackend(ArrayList<OrderItemsEntity> offlineOrderList) {
         try {
                JSONObject object = new JSONObject();
                JSONArray menuList = new JSONArray();

                for (int i = 0; i < offlineOrderList.size(); i++) {
                    JSONObject seprate = new JSONObject();
                    seprate.put("id", offlineOrderList.get(i).getId());
                    seprate.put("restaurant_id", offlineOrderList.get(i).getRestaurantId());
                    seprate.put("restaurant_name", offlineOrderList.get(i).getRestaurantName());
                    seprate.put("name", offlineOrderList.get(i).getName());
                    seprate.put("Image", offlineOrderList.get(i).getImg());
                    seprate.put("price", offlineOrderList.get(i).getPrice());
                    seprate.put("Item_type", offlineOrderList.get(i).getItem_type());
                    seprate.put("Quantity", offlineOrderList.get(i).getQuantity());
                    seprate.put("CatName", offlineOrderList.get(i).getCatname());
                    seprate.put("Addon", offlineOrderList.get(i).getAddon());
                    JSONArray customizes = new JSONArray();
                    for (int n = 0; n < offlineOrderList.get(i).getCustomizes().size(); n++) {
                        JSONArray addons = new JSONArray();
                        for (int m = 0; m < offlineOrderList.get(i).getCustomizes().get(n).getAddons().size(); m++) {
                            JSONObject addonsObj = new JSONObject();
                            if (offlineOrderList.get(i).getCustomizes().get(n).getAddons().get(m).isCheck()) {
                                //  addonsObj.put("Name", offlineOrderList.get(i).getCustomizes().get(n).getAddons().get(m).getName());
                                // addonsObj.put("ColumnId", offlineOrderList.get(i).getCustomizes().get(n).getAddons().get(m).getColumnId());
                                // addonsObj.put("Id", offlineOrderList.get(i).getCustomizes().get(n).getAddons().get(m).getId());
                                //addonsObj.put("Price", offlineOrderList.get(i).getCustomizes().get(n).getAddons().get(m).getPrice());
                                customizes.put(offlineOrderList.get(i).getCustomizes().get(n).getAddons().get(m).getId());
                            }

                        }
                        // customizes.put(addons);
                    }
                    seprate.put("customizes", customizes);
                    menuList.put(seprate);
                }

                object.put("menuList", menuList);
                addItemFromDB(object);
            } catch (Exception e) {
                Log.e("c", e.getMessage());
            }
    /*    }
    });*/
    }

    //////////add offlineItem///////
    public void addItemFromDB(JSONObject object) {
        if (CommonUtilities.isNetworkAvailable(LoginActivity.this)) {
            String token = SharedPreferenceWriter.getInstance(LoginActivity.this).getString(SPreferenceKey.TOKEN);
            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                MediaType json = MediaType.parse("application/json");
                RequestBody requestBody = RequestBody.create(json, object.toString());
                Call<CommonModel> loginCall = servicesInterface.addItemFromDb(map, requestBody);
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
                                        //Toast.makeText(LoginActivity.this, "Success offline data", Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        CommonUtilities.snackBar(LoginActivity.this, bean.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<CommonModel> call, Throwable t) {
                                Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            CommonUtilities.snackBar(LoginActivity.this, getString(R.string.no_internet));

        }
    }

    public void loginServiceHit() {

        if (CommonUtilities.isNetworkAvailable(this)) {

            String type = "";

            if (ed_login_email.getText().toString().matches(CommonUtilities.emailPattern))
                type = "email";
            else
                type = "phone";


            String device_token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.DEVICETOKEN);

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.login(ed_login_email.getText().toString(),
                        ed_login_password.getText().toString(), type, ParamEnum.ANDROID.theValue(), ed_login_email.getText().toString(), device_token);

                Log.w("DeviceToken-------->", SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.DEVICETOKEN));


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

                                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SPreferenceKey.CREDIT_BALANCE, bean.getWalletAmount());
                                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SPreferenceKey.SAVE_NAME, bean.getLoginResponse().getUser().getFirst_name()+" "+bean.getLoginResponse().getUser().getFirst_name());

                                        saveData(bean.getLoginResponse().getUser(), bean.getLoginResponse().getAuth().getAccess_token());
                                        //if not login then open this screen
                                        if (getIntent().getExtras() != null) {
                                            if (getIntent().getExtras().getString("Type").equalsIgnoreCase(MyCartActivity.class.getSimpleName())) {
                                                offlineOrderList = getIntent().getExtras().getParcelableArrayList("offlineData");
                                                sendDataToBackend(offlineOrderList);
                                            }
                                        } else {
                                            disaptchMainAct(bean.getLoginResponse().getUser(), bean.getLoginResponse().getAuth().getAccess_token());
                                        }

                                    } else {
                                        CommonUtilities.snackBar(LoginActivity.this, bean.getMessage());
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

    private void saveData(ResponseBean responseBean, String token) {
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.ISLOGIN, ParamEnum.LOGIN.theValue());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.USERID, responseBean.getId());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.TOKEN, "Bearer " + token);
//        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.NOTI_STATUS, commonResponse.getNotify_status());
//        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.PHONENUMBER, commonResponse.getPhone());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.USER_NAME, responseBean.getFirst_name() + " " + responseBean.getLast_name());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.EMAIL, responseBean.getEmail_id());
    }

    private void disaptchMainAct(ResponseBean responseBean, String token) {

        Intent intent = new Intent(this, MainActivity.class);
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.ISLOGIN, ParamEnum.LOGIN.theValue());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.USERID, responseBean.getId());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.TOKEN, "Bearer " + token);
//        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.NOTI_STATUS, commonResponse.getNotify_status());
//        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.PHONENUMBER, commonResponse.getPhone());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.USER_NAME, responseBean.getFirst_name() + " " + responseBean.getLast_name());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.EMAIL, responseBean.getEmail_id());
        SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.ADDRESS, responseBean.getAddress());

        startActivity(intent);
        finishAffinity();
        Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
    }

    private void gmailLogin() {
        Intent intent = new Intent(LoginActivity.this, GoogleLogin.class);
        startActivityForResult(intent, GOOGLE_DATA);
    }

    private void fbLogin() {
        Intent intent = new Intent(LoginActivity.this, FirebaseFacebookLogin.class);
        startActivityForResult(intent, FACEBOOK_DATA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case GOOGLE_DATA:
            if (resultCode == 122 && data != null)
            socialLogin(data, "google");
            break;

            case FACEBOOK_DATA:
            if (resultCode == 121 && data != null)
            socialLogin(data, "fb");
            break;

        }

    }
    /*For getting data from Google*/
//    private void socialLoginFromGoogle(Intent data) {
//        try {
//
//            String name = data.getExtras().getString(ParamEnum.NAME.theValue());
//            String f_name = data.getExtras().getString(ParamEnum.F_NAME.theValue());
//            String l_name = data.getExtras().getString(ParamEnum.L_NAME.theValue());
//            String google_id = data.getExtras().getString(ParamEnum.SOCIAL_ID.theValue());
//            String email = data.getExtras().getString(ParamEnum.SOCIAL_EMAIL.theValue());
//            String pictureurl = data.getExtras().getString(ParamEnum.IMAGE.theValue());
//            String gender = data.getExtras().getString(ParamEnum.GENDER.theValue());
//            String type = ("Gmail");
////            if (pictureurl != null && !pictureurl.isEmpty()) {
////                downloadImage(pictureurl);
////            } else {
////                view_update(firstname, lastname, currentPhotoPath, id, headline, industry);
////            }
//            //TODO Work here
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
//            Bundle bundle = new Bundle();
//            bundle.putString(ParamEnum.NAME.theValue(),data.getExtras().getString(ParamEnum.NAME.theValue()));
//            bundle.putString(ParamEnum.F_NAME.theValue(),data.getExtras().getString(ParamEnum.F_NAME.theValue()));
//            bundle.putString(ParamEnum.L_NAME.theValue(),data.getExtras().getString(ParamEnum.L_NAME.theValue()));
//            bundle.putString(ParamEnum.SOCIAL_ID.theValue(),data.getExtras().getString(ParamEnum.SOCIAL_ID.theValue()));
//            bundle.putString(ParamEnum.SOCIAL_EMAIL.theValue(),data.getExtras().getString(ParamEnum.SOCIAL_EMAIL.theValue()));
//            bundle.putString(ParamEnum.IMAGE.theValue(),data.getExtras().getString(ParamEnum.IMAGE.theValue()));
//            bundle.putString(ParamEnum.GENDER.theValue(),data.getExtras().getString(ParamEnum.GENDER.theValue()));
//
//            Intent intent = new Intent(this,SignUpActivity.class);
//            intent.putExtras(bundle);
//            startActivity(intent);

//            if (pictureurl != null && !pictureurl.isEmpty()) {
//                downloadImage(pictureurl);
//            } else {
//                view_update(firstname, lastname, currentPhotoPath, id, headline, industry);
//            }
            //TODO Work here
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
                                        CommonUtilities.snackBar(LoginActivity.this, bean.getMessage());
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
        else {
            CommonUtilities.snackBar(this, getString(R.string.no_internet));
        }
    }

}
