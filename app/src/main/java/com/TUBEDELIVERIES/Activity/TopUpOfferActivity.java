package com.TUBEDELIVERIES.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.TUBEDELIVERIES.Adapter.TopUpOfferAdapter;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mahipal Singh on 14,JUne,2019
 * mahisingh1@outlook.com
 */


public class TopUpOfferActivity extends AppCompatActivity {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.menuIv)
    ImageView menuIv;

    @BindView(R.id.edWalletAmt)
    TextInputEditText edWalletAmt;

    private List<RestaurantResponse> topUpOfferList=null;
    private List<RestaurantResponse> checkOutItem=null;
    private TopUpOfferAdapter adapter=null;

    private String topUpId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_offer);
        ButterKnife.bind(this);
        CommonUtilities.setToolbar(this,mainToolbar,tvTitle,getResources().getString(R.string.top_up_offers));
        new CommonUtil().setStatusBarGradiant(this,MyCartActivity.class.getSimpleName());
        menuIv.setVisibility(View.GONE);
    }

    @OnClick({R.id.btnProccedTopOffers})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnProccedTopOffers:
                if(edWalletAmt.getText().toString().length()>0)
                {
                    dispatchPaymentMode();
                }else
                {
                    CommonUtilities.snackBar(this,"Please add wallet amount");
                }
                break;
        }
    }


    private void dispatchPaymentMode() {
        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtra(ParamEnum.FROM.theValue(),"Wallet");
        intent.putExtra("url","walletpaymentform");
        intent.putExtra("amount",edWalletAmt.getText().toString().trim());
        startActivity(intent);

    }




}
