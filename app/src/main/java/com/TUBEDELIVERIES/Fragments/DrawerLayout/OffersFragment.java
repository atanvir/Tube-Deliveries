package com.TUBEDELIVERIES.Fragments.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TUBEDELIVERIES.Activity.MainActivity;
import com.TUBEDELIVERIES.Activity.RestaurantDetails;
import com.TUBEDELIVERIES.Adapter.OffersAdapter;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OffersFragment extends Fragment implements OffersAdapter.onOfferClick {

    @BindView(R.id.recyclerOffers)
    RecyclerView recyclerOffers;

    private OffersAdapter adapter=null;
    private List<RestaurantResponse>offerList=null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_offers, container, false);
        ButterKnife.bind(this,view);
        setUpRecyclerView();
        offerListing();
        return view;
    }

    private void setUpRecyclerView() {
        recyclerOffers.setLayoutManager(new LinearLayoutManager(getContext()));
        offerList=new ArrayList<>();
        adapter=new OffersAdapter(getActivity(),offerList,this);
        recyclerOffers.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setToolbar(getResources().getString(R.string.offers),View.GONE);
    }

    //offer listing api
    public void offerListing() {

        if (CommonUtilities.isNetworkAvailable(getActivity())) {

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.offers();
                ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, getActivity(), true, new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                        setofferList(bean.getOfferList());
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

    // send data to adapter
    private void setofferList(List<RestaurantResponse> response){

        offerList.clear();
        offerList.addAll(response);
        adapter.updateList(offerList);

    }

    @Override
    public void onClickOffer(Object response) {

        if(response instanceof RestaurantResponse) {

            Intent intent = new Intent(getActivity(), RestaurantDetails.class);
            intent.putExtra(ParamEnum.RESTRO_ID.theValue(), ((RestaurantResponse)response).getRestorent_id());
            startActivity(intent);
        }

    }
}
