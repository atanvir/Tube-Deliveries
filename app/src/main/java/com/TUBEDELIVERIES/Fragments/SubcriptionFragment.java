package com.TUBEDELIVERIES.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.TUBEDELIVERIES.Activity.MainActivity;
import com.TUBEDELIVERIES.Activity.WebviewActivity;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.SubscriptionDetail;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubcriptionFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private RadioGroup typeRG;
    private Button btnCancel,btnSubmit;
    private ImageView tvAddItem,tvRemoveItem;
    private TextView tvAmount,tvApplyDate,tvType,tvExpireDate,tvTotalAmount;
    private ConstraintLayout clPlanDetails;
    private CardView cvChoosePlan;
    private RadioButton rbMonth,rbWeek;
    private String type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_subcription, container, false);
        tvAddItem=view.findViewById(R.id.tvAddItem);
        tvRemoveItem=view.findViewById(R.id.tvRemoveItem);
        tvAmount=view.findViewById(R.id.tvAmount);
        btnSubmit=view.findViewById(R.id.btnSubmit);
        typeRG=view.findViewById(R.id.typeRG);
        rbMonth=view.findViewById(R.id.rbMonth);
        rbWeek=view.findViewById(R.id.rbWeek);
        clPlanDetails=view.findViewById(R.id.clPlanDetails);
        cvChoosePlan=view.findViewById(R.id.cvChoosePlan);
        tvApplyDate=view.findViewById(R.id.tvApplyDate);
        tvType=view.findViewById(R.id.tvType);
        tvExpireDate=view.findViewById(R.id.tvExpireDate);
        tvTotalAmount=view.findViewById(R.id.tvTotalAmount);
        btnCancel=view.findViewById(R.id.btnCancel);
        tvAddItem.setOnClickListener(this);
        tvRemoveItem.setOnClickListener(this);
        typeRG.setOnCheckedChangeListener(this);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        CommonUtilities.showLoadingDialog(getActivity());
        getSubscriptionDetailApi();
        return view;
    }

    private void getSubscriptionDetailApi() {
        if (CommonUtilities.isNetworkAvailable(getActivity())) {
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN);
            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));
            try {

                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<JsonObject> call = servicesInterface.subscriptionDetail(map);

                ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), true, new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            CommonUtilities.dismissLoadingDialog();
                            JsonObject serverResponse = response.body();
                            JsonElement status = null;
                            try {
                                status=serverResponse.get("status");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (status.getAsString().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                cvChoosePlan.setVisibility(View.GONE);
                                clPlanDetails.setVisibility(View.VISIBLE);
                                Gson gson = new Gson();
                                String json = gson.toJson(response.body().getAsJsonObject("SubscriptionDetail"));
                                SubscriptionDetail model = new Gson().fromJson(json, SubscriptionDetail.class);
                                tvApplyDate.setText("Applied Date \n"+model.getStartDate());
                                tvExpireDate.setText("Valid Till Date \n"+model.getEndDate());
                                tvType.setText("Type \n"+model.getType());
                                tvTotalAmount.setText("$ "+model.getAmount());

                            } else {
                                cvChoosePlan.setVisibility(View.VISIBLE);
                                clPlanDetails.setVisibility(View.GONE);
                                tvAmount.setText("$0");
//                                CommonUtilities.snackBar(getActivity(), serverResponse.getMessage());
                            }
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
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setToolbar("My Subscription",View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnSubmit:
            if(checkValdiation())
            {
                Intent intent = new Intent(getActivity(), WebviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ParamEnum.FROM.theValue(),"Subscribed");
                intent.putExtra("url","subscribepaymentform");
                intent.putExtra("amount",""+tvAmount.getText().toString().split("\\$")[1]);
                intent.putExtra("type",type);
                intent.putExtra("change", "0");
                startActivity(intent);
            }
            break;

            case R.id.btnCancel:
            CommonUtilities.showLoadingDialog(getActivity());
            subscriptionCancelApi();
            break;

            case R.id.tvAddItem:
            if(!tvAmount.getText().toString().split("\\$")[1].equalsIgnoreCase("200"))
            {
               int amount = Integer.parseInt(tvAmount.getText().toString().split("\\$")[1]);
               tvAmount.setText("$"+(amount+5));;

            }else
            {
                CommonUtilities.snackBar(getActivity(),"You can subscribe upto $200");
            }
            break;

            case R.id.tvRemoveItem:
            if(!tvAmount.getText().toString().split("\\$")[1].equalsIgnoreCase("0"))
            {
                int amount = Integer.parseInt(tvAmount.getText().toString().split("\\$")[1]);
                tvAmount.setText("$"+(amount-5));;
            }
            break;

        }
    }

    private void subscriptionCancelApi() {
      if (CommonUtilities.isNetworkAvailable(getActivity())) {
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN);
            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> call = servicesInterface.subscriptionCancel(map);

                ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), false, new Callback<CommonModel>() {
                    @Override
                    public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                        if (response.isSuccessful()) {
                            CommonModel serverResponse=response.body();
                            if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue()))
                            {
                                getSubscriptionDetailApi();

                            }else
                            {
                                CommonUtilities.dismissLoadingDialog();
                                CommonUtilities.snackBar(getActivity(),serverResponse.getMessage());
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

    }

    private boolean checkValdiation() {
        boolean ret=true;

        if(type==null)
        {
            ret=false;
            CommonUtilities.snackBar(getActivity(),"Please select Subcription Type");
        }else if(tvAmount.getText().toString().split("\\$")[1].equalsIgnoreCase("0"))
        {
            ret=false;
            CommonUtilities.snackBar(getActivity(),"Please add Subscription Amount");
        }

        return ret;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId())
        {
            case R.id.rbWeek:  type="week"; break;

            case R.id.rbMonth:  type="month"; break;
        }
    }
}
