package com.TUBEDELIVERIES.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebviewActivity extends AppCompatActivity {

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        WebView webview=findViewById(R.id.webview);
        ProgressBar progress_bar=findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);
        webview.getSettings().setJavaScriptEnabled(true);
//        http://3.128.65.155/public/api/success/371TD02
        webview.loadUrl(" http://3.128.65.155/public/paymentform/"+ getIntent().getStringExtra("amount"));
        webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                Log.e("Url",url);
                if(url.contains("http://3.128.65.155/public/paymentform/"))
                {
                    progress_bar.setVisibility(View.GONE);
                }else if(url.contains("https://sandbox.payfast.co.za/eng/process/payment?p-sb="))
                {
                    progress_bar.setVisibility(View.GONE);
                }else if(url.contains("http://3.128.65.155/public/api/success/"))
                {
                    webview.setVisibility(View.GONE);
                    progress_bar.setVisibility(View.GONE);
                   paymentsuccess(url);

                }
            }
        });
    }

    private String getSuceesUrlName(String type) {
        if(type.equalsIgnoreCase("Wallet"))
        {
            return "wallet";
        }else if(type.equalsIgnoreCase("Subscribed")){
            return "subscribe";
        }else
        {
            return "";
        }
    }

    private void paymentsuccess(String url) {
        if (CommonUtilities.isNetworkAvailable(this)) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.paymentsuccess(url.split("http://3.128.65.155/public/api/success/")[1]);
                ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, this, true, new Callback<CommonModel>() {
                    @Override
                    public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {

                        if (response.isSuccessful()) {
                            if(response.body().getStatus().equalsIgnoreCase(ParamEnum.Success.theValue()))
                            {
                                if(response.body().getPayresponse().getPayment_status().equalsIgnoreCase("COMPLETE"))
                                {
                                    placeOrder(response.body().getPayresponse().getPf_payment_id());
                                }else
                                {
                                    Toast.makeText(WebviewActivity.this, response.body().getPayresponse().getPayment_status(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            }else
                            {
                              Toast.makeText(WebviewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                              finish();
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

    public void placeOrder(String transaction_id) {


        if (CommonUtilities.isNetworkAvailable(this)) {
            String userId = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.USERID);
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.place_order(userId, "ONLINE", getValue("latitude"), getValue("longitude"),SharedPreferenceWriter.getInstance(WebviewActivity.this).getString(SPreferenceKey.ADDRESS) , getValue("extra_note"), getValue("Landmark"), "","",getValue("pincode"),getValue("price"),!getValue("discount").equalsIgnoreCase("")?getValue("discount"):"0",getValue("total_amont_to_paid"),transaction_id,getValue("delivery_charges"),getValue("currency_code"),getValue("coupon_id"),getValue("coupon_code"));

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
                                        Intent intent =new Intent(WebviewActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                        Toast.makeText(WebviewActivity.this, "Your order has been placed", Toast.LENGTH_SHORT).show();


                                    } else {
                                        CommonUtilities.snackBar(WebviewActivity.this, bean.getMessage());
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

    private String getValue(String key) {
       return getIntent().getStringExtra(key);
    }



    @Override
    public void onBackPressed() {
        Toast.makeText(WebviewActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
        finish();
    }
}
