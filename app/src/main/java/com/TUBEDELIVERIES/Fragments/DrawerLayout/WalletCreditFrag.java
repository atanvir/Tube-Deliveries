package com.TUBEDELIVERIES.Fragments.DrawerLayout;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TUBEDELIVERIES.Activity.TopUpCreditHistory;
import com.TUBEDELIVERIES.Activity.MainActivity;
import com.TUBEDELIVERIES.Activity.TopUpOfferActivity;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class WalletCreditFrag extends Fragment {

    @BindView(R.id.tv_credit_AED)
    TextView tv_credit_AED;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_just_bite_credit, container, false);
        ButterKnife.bind(this,view);
        walletBalance();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setToolbar("My Wallet",View.GONE);

    }


    @OnClick({R.id.tv_credit_view_statement,R.id.btnTopCredit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_credit_view_statement:
                startActivity(new Intent(getContext(), TopUpCreditHistory.class));
                break;
            case R.id.btnTopCredit:
                startActivity(new Intent(getContext(), TopUpOfferActivity.class));
                break;
        }
    }

    //////////view Profile api///////
    public void walletBalance() {

        if (CommonUtilities.isNetworkAvailable(getActivity())) {

            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.walletAmount(map);

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        loginCall,
                        getActivity(),
                        true,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                        tv_credit_AED.setText(getString(R.string.usd)+bean.getWalletAmount());
                                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.CREDIT_BALANCE,bean.getWalletAmount());
                                    } else {
                                        CommonUtilities.snackBar(getActivity(), bean.getMessage());
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

            CommonUtilities.snackBar(getActivity(), getString(R.string.no_internet));

        }
    }



}
