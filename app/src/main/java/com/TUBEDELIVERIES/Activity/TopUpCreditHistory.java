package com.TUBEDELIVERIES.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.TUBEDELIVERIES.Adapter.CreditStatementAdapter;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopUpCreditHistory extends AppCompatActivity {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.menuIv)
    ImageView menuIv;


    @BindView(R.id.tvBalance)
    TextView tvBalance;

    @BindView(R.id.rvCreditStatement)
    RecyclerView rvCreditStatement;

    private CreditStatementAdapter adapter=null;

    private List<RestaurantResponse> topUpHistoryList=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cedit_statement);
        ButterKnife.bind(this);
        CommonUtilities.setToolbar(this,mainToolbar,tvTitle,getResources().getString(R.string.credit_statement));
        new CommonUtil().setStatusBarGradiant(this,MyCartActivity.class.getSimpleName());
        menuIv.setVisibility(View.GONE);
        setYpRecyclerView();
        topUpHistory();
    }

    private void setYpRecyclerView() {
        rvCreditStatement.setLayoutManager(new LinearLayoutManager(this));
        topUpHistoryList=new ArrayList<>();
        adapter=new CreditStatementAdapter(this,topUpHistoryList);
        rvCreditStatement.setAdapter(adapter);
    }

    //////////top up list api///////

    public void topUpHistory() {

        if (CommonUtilities.isNetworkAvailable(this)) {

            String token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.topUpHistory(map);

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

                                        topUpHistoryList.clear();
                                        topUpHistoryList.addAll(bean.getTopupHistory());
                                        adapter.upDateList(topUpHistoryList);


                                        tvBalance.setText(getString(R.string.usd)+bean.getWalletAmount());



                                    } else {
                                        CommonUtilities.snackBar(TopUpCreditHistory.this, bean.getMessage());
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

}
