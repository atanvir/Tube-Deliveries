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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.TUBEDELIVERIES.Activity.MyCartActivity;
import com.TUBEDELIVERIES.Adapter.PreviousAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class PastOrdersFragment extends Fragment implements PreviousAdapter.OnReaorder {

    @BindView(R.id.recyclerPrevious)
    RecyclerView recyclerPrevious;

    @BindView(R.id.lottie)
    LottieAnimationView lottie;


    private BroadcastReceiver broadcastReceiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            serviceOnPrevious();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,new IntentFilter("updateOrderList"));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_previous, container, false);
        ButterKnife.bind(this,view);

        serviceOnPrevious();
        return view;
    }

    private void setUpRecyclerView(List<ResponseBean> responseBeanList) {
        recyclerPrevious.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerPrevious.setAdapter(new PreviousAdapter(getContext(),responseBeanList,this));
    }

    private void serviceOnPrevious() {
        if (CommonUtilities.isNetworkAvailable(getContext())) {

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> favoritesCall = servicesInterface.getPreviousOrder(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.USERID));

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
                                        if(bean.getPrevious_order().size()>0) {
                                            setUpRecyclerView(bean.getPrevious_order());
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


        onReorder(responseBean.getOrder_number());


    }


    private void onReorder(String order_id) {
        if (CommonUtilities.isNetworkAvailable(getContext())) {

            try {

                String userId = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.USERID);


                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> favoritesCall = servicesInterface.reoder(userId,order_id);

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


                                        Intent intent=new Intent(getActivity(), MyCartActivity.class);
                                        startActivity(intent);


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
