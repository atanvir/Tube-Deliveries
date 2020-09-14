package com.TUBEDELIVERIES.Fragments.BottomNavigation.MyOrdersFragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.TUBEDELIVERIES.Adapter.OnUpcomingAdapter;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.Model.UpcomingOrder;
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

public class UpcomingOrdersFragment extends Fragment implements OnUpcomingAdapter.OnCancelOrderClick {

    List<ResponseBean> list;

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.lottie)
    LottieAnimationView lottie;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_upcoming_orders,container,false);
        ButterKnife.bind(this,view);

        serviceUpcomingOrder();

       return view;
    }


    public void  serviceUpcomingOrder() {

        if (CommonUtilities.isNetworkAvailable(getContext())) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> favoritesCall = servicesInterface.getUpcomingOrder(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.USERID));

                ServicesConnection.getInstance().enqueueWithoutRetry(favoritesCall, getActivity(), true, new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                        if(bean.getUpcomingOrder().size()>0)
                                        {
                                            settingAdapter(bean.getUpcomingOrder());

                                        }else
                                        {
                                            rv.setVisibility(View.GONE);
                                            lottie.setVisibility(View.VISIBLE);
                                        }

                                    } else {
                                        CommonUtilities.snackBar(getActivity(), bean.getMessage());
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<CommonModel> call, Throwable t) {
                                Log.e("error",t.getMessage());

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtilities.snackBar(getActivity(), getString(R.string.no_internet));

        }


    }

    private void settingAdapter(List<UpcomingOrder> upcomingOrder) {
        OnUpcomingAdapter adapter=new OnUpcomingAdapter(getActivity(),upcomingOrder, this);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
        rv.getAdapter().notifyDataSetChanged();

    }


    @Override
    public void onCLick(int pos, UpcomingOrder list) {
        cancelOrder(list.getOrderNumber());
    }

    private void cancelOrder(String order_no) {
        if (CommonUtilities.isNetworkAvailable(getContext())) {

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> favoritesCall = servicesInterface.cancel_order(order_no);

                ServicesConnection.getInstance().enqueueWithoutRetry(favoritesCall, getActivity(), false, new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {

                                        Toast.makeText(getActivity(), "Your order has been canceled", Toast.LENGTH_SHORT).show();
                                        serviceUpcomingOrder();

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
