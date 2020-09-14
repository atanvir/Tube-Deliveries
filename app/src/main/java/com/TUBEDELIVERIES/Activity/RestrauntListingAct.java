package com.TUBEDELIVERIES.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

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
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RestrauntListingAct extends AppCompatActivity implements RestroListingAdapter.onRestaurantClick {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvRestauListing)
    RecyclerView rvRestauListing;


    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;

    String From = "";

    private List<RestaurantResponse> listResponse = null;
    private RestroListingAdapter adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_rated);
        findViewById(R.id.menuIv).setVisibility(View.GONE);
        ButterKnife.bind(this);
        getIntentData();
        setUpRecyclerView();

    }

    private void getIntentData() {
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String title = bundle.getString(ParamEnum.TITLE.theValue());
                From = bundle.getString(ParamEnum.FROM.theValue());
                CommonUtilities.setToolbar(this, mainToolbar, tvTitle, title);
            }
        }
    }

    private void setUpRecyclerView() {
        rvRestauListing.setLayoutManager(new LinearLayoutManager(this));
        listResponse = new ArrayList<>();
        adapter = new RestroListingAdapter(this, listResponse, this);
        rvRestauListing.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        String lat = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.LATITUDE);
        String lon = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.LONGITUDE);


        if (From.equals(ParamEnum.NEAR_BY_RESTRO.theValue())) {
            nearByRestro(lat, lon);
        } else if(From.equalsIgnoreCase(ParamEnum.NEAR_BY_GROCERY_STORE.theValue())){
            nearbyGrocery(lat,lon);

        } else {
            topRatedRestro(lat,lon);
        }

    }

    public void nearbyGrocery(String lat, String lon) {
        if (CommonUtilities.isNetworkAvailable(this)) {
            String userId = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.USERID);
            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.LOCALISATION.theValue(),ParamEnum.ENGLISH.theValue());
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.nearByGrocery(map,userId,lat,lon);
                ServicesConnection.getInstance().enqueueWithoutRetry(
                        loginCall,
                        this,
                        false,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                        mShimmerViewContainer.stopShimmerAnimation();
                                        rvRestauListing.setVisibility(View.VISIBLE);
                                        mShimmerViewContainer.setVisibility(View.GONE);
                                        setData(bean.getNearBy());
                                    } else {
                                        CommonUtilities.snackBar(RestrauntListingAct.this, bean.getMessage());
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

            CommonUtilities.snackBar(this, getString(R.string.no_internet));

        }
    }


    //////////home restro api///////
    public void nearByRestro(String lat, String lon) {

        if (CommonUtilities.isNetworkAvailable(this)) {

            String userId = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.USERID);
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.near_by_restro(userId, lat, lon);


                ServicesConnection.getInstance().enqueueWithoutRetry(
                        loginCall,
                        this,
                        false,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                        setData(bean.getNearBy());
                                    } else {
                                        CommonUtilities.snackBar(RestrauntListingAct.this, bean.getMessage());
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

            CommonUtilities.snackBar(this, getString(R.string.no_internet));

        }
    }


    ////top ratedRestro api
    //////////home restro api///////
    public void topRatedRestro(String latitute,String longitute) {
        if (CommonUtilities.isNetworkAvailable(this)) {
            String userId = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.USERID);
            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.LOCALISATION.theValue(),ParamEnum.ENGLISH.theValue());
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.top_rated_restro(map,userId,latitute,longitute);
                ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, this, false, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if (response.isSuccessful()) {
                        CommonModel bean = ((CommonModel) response.body());
                        if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                            mShimmerViewContainer.stopShimmerAnimation();
                            rvRestauListing.setVisibility(View.VISIBLE);
                            mShimmerViewContainer.setVisibility(View.GONE);
                            setData(bean.getTop_rated());
                        } else {
                            CommonUtilities.snackBar(RestrauntListingAct.this, bean.getMessage());
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

            CommonUtilities.snackBar(this, getString(R.string.no_internet));

        }
    }


    @Override
    public void onClickRestaurant(String restaurantId) {

        dispatchRestroDetails(restaurantId);

    }

    @Override
    public void onFavClick(int position, RestaurantResponse response) {

        /// 0 for fav 1 for unfav
        if (response.getIs_favorite().equalsIgnoreCase(ParamEnum.ZERO.theValue())) {
            markAsFav(position, "0", response.getId());
        } else {
            markAsFav(position, "1", response.getId());
        }
    }


    private void setData(List<RestaurantResponse> list) {


        mShimmerViewContainer.stopShimmerAnimation();
        rvRestauListing.setVisibility(View.VISIBLE);
        mShimmerViewContainer.setVisibility(View.GONE);


        listResponse.clear();
        listResponse.addAll(list);
        adapter.updateList(listResponse);

    }


    private void dispatchRestroDetails(String restro_id) {

        Intent intent = new Intent(this, RestaurantDetails.class);
        intent.putExtra(ParamEnum.RESTRO_ID.theValue(), restro_id);
        startActivity(intent);

    }

    //////////mark as fav api///////
    public void markAsFav(int pos, String isFav, String restro_id) {

        if (CommonUtilities.isNetworkAvailable(this)) {


            String token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.markFav(map, restro_id, isFav);

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        loginCall,
                        this,
                        false,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {

                                        if (bean.getMessage().equalsIgnoreCase("favourite added")) {

                                            listResponse.get(pos).setIs_favorite("1");

// for(int i=0;i<restroList.size();i++){
// restroList.get(i).setHeartAnimCheck(false);
// }
// restroList.get(pos).setHeartAnimCheck(true);
                                            adapter.updateList(listResponse);


                                        } else {
//
// for(int i=0;i<restroList.size();i++){
// restroList.get(i).setHeartAnimCheck(false);
// }
                                            listResponse.get(pos).setIs_favorite("0");
                                            adapter.updateList(listResponse);
                                        }


                                    } else {
                                        CommonUtilities.snackBar(RestrauntListingAct.this, bean.getMessage());
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

            CommonUtilities.snackBar(this, getString(R.string.no_internet));

        }
    }


}
