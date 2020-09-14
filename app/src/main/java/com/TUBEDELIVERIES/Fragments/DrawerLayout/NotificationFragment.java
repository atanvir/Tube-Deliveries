package com.TUBEDELIVERIES.Fragments.DrawerLayout;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TUBEDELIVERIES.Activity.MainActivity;
import com.TUBEDELIVERIES.Adapter.NotificationAdapter;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {


    @BindView(R.id.notificationRV)
    RecyclerView notificationRV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notification,container,false);
        ButterKnife.bind(this,view);
        notificationApi();
        return view;
    }

    private void notificationApi() {
        if (CommonUtilities.isNetworkAvailable(getActivity())) {
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN);
            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.getNotification(map);

                ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, getActivity(), true, new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                        NotificationAdapter adapter = new NotificationAdapter(getActivity(),bean.getNotificationList());
                                        notificationRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        notificationRV.setAdapter(adapter);

                                    } else {
                                        CommonUtilities.snackBar(getActivity(), bean.getMessage());

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<CommonModel> call, Throwable t) {
                            }
                        });
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            CommonUtilities.snackBar(getActivity(), getString(R.string.no_internet));

        }




    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setToolbar(getResources().getString(R.string.notifications),View.GONE);
    }
}
