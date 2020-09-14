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

import com.TUBEDELIVERIES.Adapter.RestroListingAdapter;

import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */



public class MyFavFragment extends Fragment implements RestroListingAdapter.onRestaurantClick {


    @BindView(R.id.recyclerMyFev)
    RecyclerView recyclerMyFev;

    private List<RestaurantResponse>favList=null;
    private RestroListingAdapter adapter=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_fevorite, container, false);
        ButterKnife.bind(this,view);
        setUpRecyclerView();
        hitFavoritesApi();
        return view;
    }

    private void setUpRecyclerView() {

        favList=new ArrayList<>();
        adapter=new RestroListingAdapter(getActivity(),favList,this);
        recyclerMyFev.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerMyFev.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setToolbar(getResources().getString(R.string.my_favorites),View.GONE);
    }

    public void hitFavoritesApi() {
        if (CommonUtilities.isNetworkAvailable(getContext())) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> favoritesCall = servicesInterface.getFavorites(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN));

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
                                        List<RestaurantResponse> list =  bean.getFavourite();
                                        favList.clear();
                                        favList.addAll(list);
                                        adapter.updateList(favList);

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
    public void onClickRestaurant(String restaurantId) {

        dispatchRestroDetails(restaurantId);
    }

    @Override
    public void onFavClick(int position, RestaurantResponse response) {

        /// 0 for fav 1 for unfav
        if(response.getIs_favorite().equalsIgnoreCase(ParamEnum.ZERO.theValue())){
            markAsFav(position,"0",response.getId());
        }
        else {
            markAsFav(position,"1",response.getId());
        }

    }

    private void dispatchRestroDetails(String restro_id){

        Intent intent=new Intent(getActivity(), RestaurantDetails.class);
        intent.putExtra(ParamEnum.RESTRO_ID.theValue(),restro_id);
        startActivity(intent);

    }

    //////////mark as fav api///////
    public void markAsFav(int pos,String isFav,String restro_id) {

        if (CommonUtilities.isNetworkAvailable(getActivity())) {


            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.markFav(map, restro_id, isFav);

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        loginCall,
                        getActivity(),
                        false,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {

                                        if(bean.getMessage().equalsIgnoreCase("favourite added")){

                                            favList.get(pos).setIs_favorite("1");
                                            adapter.updateList(favList);

                                        }else {

                                            favList.get(pos).setIs_favorite("0");
                                            adapter.updateList(favList);
                                            adapter.removeItem(pos);


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


}
