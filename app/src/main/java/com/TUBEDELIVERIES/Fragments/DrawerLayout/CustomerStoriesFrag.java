package com.TUBEDELIVERIES.Fragments.DrawerLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TUBEDELIVERIES.Activity.MainActivity;
import com.TUBEDELIVERIES.Adapter.CustomerStoriesAdapter;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerStoriesFrag extends Fragment {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view=  inflater.inflate(R.layout.fragment_customer_stories,container,false);
      ButterKnife.bind(this,view);

      storiesApi();




      return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setToolbar(getResources().getString(R.string.customer_stories),View.GONE);
    }


    private void storiesApi() {
        if (CommonUtilities.isNetworkAvailable(getActivity())) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> call = servicesInterface.stories();
                ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), true, new Callback<CommonModel>() {
                    @Override
                    public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {

                        if (response.isSuccessful()) {
                            CommonModel serverResponse = response.body();
                            if (serverResponse.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recycleView.setAdapter(new CustomerStoriesAdapter(getActivity(), serverResponse.getStories()));


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
}
