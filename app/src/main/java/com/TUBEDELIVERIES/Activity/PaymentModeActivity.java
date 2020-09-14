package com.TUBEDELIVERIES.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.TUBEDELIVERIES.Adapter.PaymentsAdapter;
import com.TUBEDELIVERIES.CallBacks.IPaymentRequestCallBack;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.Payment.PayFortData;
import com.TUBEDELIVERIES.Payment.PayFortPayment;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
import com.payfort.fort.android.sdk.base.callbacks.FortCallback;

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

import static com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey.kAmount;

public class PaymentModeActivity extends AppCompatActivity implements IPaymentRequestCallBack {


    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.confirm)
    Button confirm;

    @BindView(R.id.tvTitle)
    TextView tvTitle;


    @BindView(R.id.rvPayment)
    RecyclerView rvPayment;

    @BindView(R.id.rbCreditCard)
    RadioButton rbCreditCard;

    private  Dialog dialog;

    private PaymentsAdapter adapter=null;
    private List<ResponseBean> cardList=null;

    private String extra_note = "";
    private String latitude = "";
    private String longitude = "";
    private String pincode = "";
    private String area = "";
    private String landmark = "";
    private String house_no = "";
    private String user_current_address="";



    private String discount = "";
    private String price = "";
    private String total_amont_to_paid = "";

    @BindView(R.id.menuIv)
    ImageView menuIv;



    /// this true when user select cusome address else false
    private boolean isCustomAddress=false;

    private FortCallBackManager fortCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_mode);
        new CommonUtil().setStatusBarGradiant(this,PaymentModeActivity.class.getSimpleName());
        ButterKnife.bind(this);
        CommonUtilities.setToolbar(this, mainToolbar, tvTitle, getResources().getString(R.string.payment_mode));
        menuIv.setVisibility(View.GONE);
        initilizePayFortSDK();
        getIntentData();
        walletBalance();
        setUpRecyclerView();
    }

    @OnClick({R.id.tvAddToCart, R.id.confirm,R.id.ivGPlay,R.id.rbCreditCard,R.id.ivSamsangPay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivSamsangPay:
            case R.id.ivGPlay:
            case R.id.tvAddToCart:
                showComingSoonDialog();
                //showFilterDialog();
                break;

            case R.id.rbCreditCard:
                rbCreditCard.setChecked(false);
                showComingSoonDialog();
                break;

            case R.id.confirm:

//                placeOrder();
                Intent intent = new Intent(this,MainActivity.class);
                finish();
                startActivity(intent);

                break;

//            case R.id.ivJustBitePay:
//                //requestForPayfortPayment("MADA");
//                //requestForPayfortPayment("VISA");
//                //requestForPayfortPayment("MASTERCARD");
//                //requestForPayfortPayment("Transfer");
//                break;
        }
    }

    private void setUpRecyclerView() {
        rvPayment.setLayoutManager(new LinearLayoutManager(PaymentModeActivity.this,LinearLayoutManager.HORIZONTAL,false));
        cardList=new ArrayList<>();
        adapter=new PaymentsAdapter(PaymentModeActivity.this,cardList);
        rvPayment.setAdapter(adapter);
        rvPayment.setClipToPadding(false);
        rvPayment.setPadding(0,0,80,0);
    }

    private void showFilterDialog() {
        dialog = new Dialog(PaymentModeActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_add_cart);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button btnDone=(Button)dialog.findViewById(R.id.btnDone);
        EditText edNameOncard=(EditText)dialog.findViewById(R.id.edNameOncard);
        EditText edCardNo=(EditText)dialog.findViewById(R.id.edCardNo);
        EditText etCvv=(EditText)dialog.findViewById(R.id.etCvv);
        EditText etyear=(EditText)dialog.findViewById(R.id.etyear);
        EditText edMonth=(EditText)dialog.findViewById(R.id.edMonth);
        ImageView ivClose=(ImageView)dialog.findViewById(R.id.ivClose);


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addCard(edNameOncard.getText().toString(),
                        edCardNo.getText().toString(),
                        edMonth.getText().toString(),
                        etyear.getText().toString(),
                        etCvv.getText().toString());

            }
        });


        dialog.show();
    }



    //////////login api///////
    public void placeOrder() {

        if (CommonUtilities.isNetworkAvailable(this)) {


            String userId = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.USERID);


            ///if user choose custom address put empty value to usercurrrent address otherwise put intent value ;
            if(isCustomAddress)
                user_current_address="";


            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.place_order(userId, "COD", latitude, longitude,user_current_address , extra_note, landmark, area,house_no,pincode,price,discount,total_amont_to_paid,"","25","");


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


                                        Intent intent =new Intent(PaymentModeActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                        Toast.makeText(PaymentModeActivity.this, "Your order has been placed", Toast.LENGTH_SHORT).show();


                                    } else {
                                        CommonUtilities.snackBar(PaymentModeActivity.this, bean.getMessage());
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

            extra_note = getIntent().getStringExtra(ParamEnum.EXTRA_NOTE.theValue());
            latitude = getIntent().getStringExtra(ParamEnum.LATITUDE.theValue());
            longitude = getIntent().getStringExtra(ParamEnum.LONGITUDE.theValue());
            pincode = getIntent().getStringExtra(ParamEnum.PINCODE.theValue());
            area = getIntent().getStringExtra(ParamEnum.AREA.theValue());
            landmark = getIntent().getStringExtra(ParamEnum.LANDMARK.theValue());
            house_no = getIntent().getStringExtra(ParamEnum.HOUSE_NO.theValue());
            user_current_address = getIntent().getStringExtra(ParamEnum.USER_CURRENT_ADDRESS.theValue());
            isCustomAddress = getIntent().getBooleanExtra(ParamEnum.ISCUSTOM_ADDRESS.theValue(),false);


            price = getIntent().getStringExtra(ParamEnum.PRICE.theValue());
            discount = getIntent().getStringExtra(ParamEnum.DISCOUNT.theValue());
            total_amont_to_paid = getIntent().getStringExtra(ParamEnum.AMOUNT_TO_BE_PAID.theValue());



        }

    }


    //////////view Profile api///////
    public void walletBalance() {

        if (CommonUtilities.isNetworkAvailable(PaymentModeActivity.this)) {

            String token = SharedPreferenceWriter.getInstance(PaymentModeActivity.this).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.walletAmount(map);

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

                                        savedCarListing();



                                        SharedPreferenceWriter.getInstance(PaymentModeActivity.this).writeStringValue(SPreferenceKey.CREDIT_BALANCE,bean.getWalletAmount());

                                    } else {
                                        CommonUtilities.snackBar(PaymentModeActivity.this, bean.getMessage());
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

            CommonUtilities.snackBar(PaymentModeActivity.this, getString(R.string.no_internet));

        }
    }



    //////////add card api///////
    public void addCard(String cardName,String cardNo,String expMonth,String expYear, String cvv ) {

        if (CommonUtilities.isNetworkAvailable(PaymentModeActivity.this)) {


            String token = SharedPreferenceWriter.getInstance(PaymentModeActivity.this).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.save_card(map,cardName,cardNo,expMonth,expYear,cvv);



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


                                        dialog.dismiss();
                                        Toast.makeText(PaymentModeActivity.this, "Your card has been saved succesfully", Toast.LENGTH_SHORT).show();
                                        savedCarListing();

                                    } else {
                                        CommonUtilities.snackBar(PaymentModeActivity.this, bean.getMessage());
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

            CommonUtilities.snackBar(PaymentModeActivity.this, getString(R.string.no_internet));

        }
    }



    //////////save card list api///////
    public void savedCarListing() {

        if (CommonUtilities.isNetworkAvailable(PaymentModeActivity.this)) {

            String token = SharedPreferenceWriter.getInstance(PaymentModeActivity.this).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.savedCardListing(map);

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


                                        if(bean.getCardList()!=null&&bean.getCardList().size()>0){
                                            cardList.clear();
                                            cardList.addAll(bean.getCardList());
                                            adapter.updateList(cardList);


                                        }
                                        else {



                                        }


                                    } else {
                                        CommonUtilities.snackBar(PaymentModeActivity.this, bean.getMessage());
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

            CommonUtilities.snackBar(PaymentModeActivity.this, getString(R.string.no_internet));

        }
    }


//    @Override
//    public void onDeleteClick(ResponseBean responseBean) {
//
//        deleteCard(responseBean,responseBean.getId());
//
//    }
//
//    @Override
//    public void onSelect(ResponseBean responseBean) {
//
//    }
//
//    @Override
//    public void onUnSelect(ResponseBean responseBean) {
//
//    }


    //////////delete card api///////
    public void deleteCard(ResponseBean responseBean,String cardId) {

        if (CommonUtilities.isNetworkAvailable(PaymentModeActivity.this)) {

            String token = SharedPreferenceWriter.getInstance(PaymentModeActivity.this).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.deleteCard(map,cardId);

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


                                        Toast.makeText(PaymentModeActivity.this, "Card has been deleted", Toast.LENGTH_SHORT).show();
                                        cardList.remove(responseBean);
                                        adapter.updateList(cardList);


                                    } else {
                                        CommonUtilities.snackBar(PaymentModeActivity.this, bean.getMessage());
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

            CommonUtilities.snackBar(PaymentModeActivity.this, getString(R.string.no_internet));

        }
    }

    private void initilizePayFortSDK() {
        fortCallback = FortCallback.Factory.create();
//        String deviceId = FortSdk.getDeviceId(PaymentMethod.this);
//        Toast.makeText(PaymentMethod.this, deviceId + "", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, deviceId + "");
    }

    private void requestForPayfortPayment(String paymentMethod) {
        PayFortData payFortData = new PayFortData();
//        if (!TextUtils.isEmpty(etAmount.getText().toString())) {
        String amount = SharedPreferenceWriter.getInstance(this).getString(kAmount);
        if(!amount.isEmpty())
            payFortData.amount = String.valueOf((int) (Float.parseFloat(amount) * 100));// Multiplying with 100, bcz amount should not be in decimal format
        else
            payFortData.amount = "999";
        payFortData.command = PayFortPayment.PURCHASE;
        payFortData.currency = PayFortPayment.CURRENCY_TYPE;
        payFortData.customerEmail = "nandan.kumar@mobulous.com";//change
        payFortData.language = PayFortPayment.LANGUAGE_TYPE;
        payFortData.merchantReference = String.valueOf(System.currentTimeMillis());
        PayFortPayment payFortPayment = new PayFortPayment(this, this.fortCallback, this, paymentMethod);
        payFortPayment.requestForPayment(payFortData);
//        }
    }

    @Override
    public void onPaymentRequestResponse(int responseType, PayFortData payFortData) {
        if (responseType == PayFortPayment.RESPONSE_GET_TOKEN) {
            Toast.makeText(this, "Generated", Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Token not generated");
        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_CANCEL) {
            Toast.makeText(this, R.string.cancel, Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Payment cancelled");
        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_FAILURE) {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Payment failed");
        } else {
            Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Payment successful");
           // pay("card");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PayFortPayment.RESPONSE_PURCHASE) {
            fortCallback.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showComingSoonDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
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
}
