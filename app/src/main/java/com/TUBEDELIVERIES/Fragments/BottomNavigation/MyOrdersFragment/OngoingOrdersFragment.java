package com.TUBEDELIVERIES.Fragments.BottomNavigation.MyOrdersFragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.TUBEDELIVERIES.Adapter.OnGoingOrderAdapter;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.airbnb.lottie.LottieAnimationView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OngoingOrdersFragment extends Fragment implements OnGoingOrderAdapter.OnCancelOrderClick {


    @BindView(R.id.rvOngoingOrder)
    RecyclerView rvOngoingOrder;

    @BindView(R.id.lottie)
    LottieAnimationView  lottie;

    boolean iscrolling=false;
    int scrollItem, totalItem, visibleItem;
    List<ResponseBean> beans ;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //now update list after registering
            serviceOnGoingOrder();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        //Register it first

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,new IntentFilter("updateOrderList"));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ongoing, container, false);
        ButterKnife.bind(this,view);
        serviceOnGoingOrder();
        return view;
    }

    private void setUpReclerView(List<ResponseBean> responseBeanList) {
        rvOngoingOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOngoingOrder.setAdapter(new OnGoingOrderAdapter(getContext(),responseBeanList,this));
    }

    public void  serviceOnGoingOrder() {

        if (CommonUtilities.isNetworkAvailable(getContext())) {

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> favoritesCall = servicesInterface.getOngoingOrder(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.USERID));

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        favoritesCall,
                        getActivity(),
                        true,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                        if(bean.getOngoing_order().size()>0)
                                        {
                                            setUpReclerView(bean.getOngoing_order());

                                        }else
                                        {
                                            lottie.setVisibility(View.VISIBLE);

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


    @Override
    public void onCLick(int pos, ResponseBean responseBean) {
        cancelOrder(responseBean.getOrder_number());

    }

    @Override
    public void confirmOrder(int pos, ResponseBean responseBean) {
        confirmOrderApi(responseBean.getOrder_number());
    }

    private void confirmOrderApi(String order_number) {
        if (CommonUtilities.isNetworkAvailable(getContext())) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> favoritesCall = servicesInterface.confirmOrder(order_number);
                ServicesConnection.getInstance().enqueueWithoutRetry(favoritesCall, getActivity(), true, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if (response.isSuccessful()) {
                        CommonModel bean = ((CommonModel) response.body());
                        if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                            serviceOnGoingOrder();
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

    private void cancelOrder(String order_no) {
        if (CommonUtilities.isNetworkAvailable(getContext())) {

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> favoritesCall = servicesInterface.cancel_order(order_no);

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        favoritesCall,
                        getActivity(),
                        false,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                        Toast.makeText(getActivity(), "Your order has been canceled", Toast.LENGTH_SHORT).show();
                                        serviceOnGoingOrder();

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
