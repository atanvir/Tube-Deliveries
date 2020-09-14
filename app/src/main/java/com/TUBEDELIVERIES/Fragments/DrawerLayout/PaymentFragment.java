package com.TUBEDELIVERIES.Fragments.DrawerLayout;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.TUBEDELIVERIES.Activity.MainActivity;
import com.TUBEDELIVERIES.Adapter.PaymentsAdapter;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;

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


/**
 * Created by Mahipal Singh on 14,JUne,2019
 * mahisingh1@outlook.com
 */

public class PaymentFragment extends Fragment {

    @BindView(R.id.rvPayment)
    RecyclerView rvPayment;

    private  Dialog dialog;

    private PaymentsAdapter adapter=null;
    private List<ResponseBean>cardList=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_payment, container, false);
        ButterKnife.bind(this,view);
        setUpRecyclerView();
        savedCarListing();
        return view;
    }

    @OnClick({R.id.tvAddToCart})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tvAddToCart:
                showFilterDialog();
                break;
        }
    }

    private void showFilterDialog() {
        dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
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

    private void setUpRecyclerView() {
        rvPayment.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        cardList=new ArrayList<>();
        adapter=new PaymentsAdapter(getActivity(),cardList);
        rvPayment.setAdapter(adapter);
        rvPayment.setClipToPadding(false);
        rvPayment.setPadding(0,0,80,0);
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setToolbar(getResources().getString(R.string.payment),View.GONE);
    }


    //////////add card api///////
    public void addCard(String cardName,String cardNo,String expMonth,String expYear, String cvv ) {

        if (CommonUtilities.isNetworkAvailable(getActivity())) {


            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.save_card(map,cardName,cardNo,expMonth,expYear,cvv);



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


                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), "Your card has been saved succesfully", Toast.LENGTH_SHORT).show();
                                        savedCarListing();

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



    //////////save card list api///////
    public void savedCarListing() {

        if (CommonUtilities.isNetworkAvailable(getActivity())) {

            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.savedCardListing(map);

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


                                        if(bean.getCardList()!=null&&bean.getCardList().size()>0){
                                              cardList.clear();
                                              cardList.addAll(bean.getCardList());
                                              adapter.updateList(cardList);


                                        }
                                        else {



                                        }


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

        if (CommonUtilities.isNetworkAvailable(getActivity())) {


            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.deleteCard(map,cardId);



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


                                        Toast.makeText(getActivity(), "Card has been deleted", Toast.LENGTH_SHORT).show();
                                         cardList.remove(responseBean);
                                         adapter.updateList(cardList);


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