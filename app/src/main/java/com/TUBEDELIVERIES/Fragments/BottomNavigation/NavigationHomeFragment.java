package com.TUBEDELIVERIES.Fragments.BottomNavigation;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.TUBEDELIVERIES.Activity.MainActivity;
import com.TUBEDELIVERIES.Activity.RestaurantDetails;
import com.TUBEDELIVERIES.Adapter.HomeAdapter;
import com.TUBEDELIVERIES.Adapter.RestroListingAdapter;
import com.TUBEDELIVERIES.CallBacks.onClickEvents;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.NearByGrocery;
import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.GPSTracker;
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
/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationHomeFragment extends Fragment implements onClickEvents, AdapterView.OnItemSelectedListener, RestroListingAdapter.onRestaurantClick {
    @BindView(R.id.recyclerHome)
    RecyclerView recyclerHome;

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;

    @BindView(R.id.swipeRegresh)
    SwipeRefreshLayout swipeRegresh;

    private HomeAdapter homeAdapter=null;
    private List<RestaurantResponse>nearByRestroList=null;
    private List<RestaurantResponse>offersList=null;
    private List<RestaurantResponse>topRatedRestroList=null;
    private List<NearByGrocery> nearByGroceryList =null;
    private String lat="";
    private String lon="";
    private List<ResponseBean> cosineList = new ArrayList<>();
    private String cousinesId="";
    private List<RestaurantResponse> restroList;
    private RestroListingAdapter adapter;

    @BindView(R.id.recyclerFilterHome) RecyclerView recyclerFilterHome;

    GPSTracker tracker;
    public static AppCredit listner;
    private TextView tvCousines;
    @BindView(R.id.tvNoData) TextView tvNoData;
    @BindView(R.id.tvRestaurant) TextView tvRestaurant;
    public static void setListner(AppCredit listen) {
        listner=listen;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
        tracker=new GPSTracker(getActivity());
        ((MainActivity)getActivity()).setToolbar(getResources().getString(R.string.home),View.VISIBLE);
        ((MainActivity)getActivity()).tvTitle.setVisibility(View.GONE);
        ((MainActivity)getActivity()).edtSearchPlace.setVisibility(View.VISIBLE);

        mShimmerViewContainer.startShimmerAnimation();
//         lat=SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LATITUDE);
//         lon=SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LONGITUDE);
        setUpRecyclerView();
        lon= String.valueOf(tracker.getLongitude());
        lat= String.valueOf(tracker.getLatitude());
        new CommonUtil().setStatusBarGradiant(getActivity(), NavigationHomeFragment.class.getSimpleName());
        homeRestroList(getActivity(),lat,lon,false,false);
        swipeRegresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeRestroList(getActivity(),lat,lon,true,false);
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setUpRecyclerView() {
        nearByRestroList=new ArrayList<>();
        topRatedRestroList=new ArrayList<>();
        offersList=new ArrayList<>();
        nearByGroceryList = new ArrayList<>();

        recyclerHome.setLayoutManager(new LinearLayoutManager(getContext()));
        homeAdapter=new HomeAdapter(getContext(),offersList,nearByRestroList,topRatedRestroList,nearByGroceryList,this);
        recyclerHome.setAdapter(homeAdapter);

        recyclerFilterHome.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RestroListingAdapter(getContext(), restroList, NavigationHomeFragment.this);
        recyclerFilterHome.setAdapter(adapter);
    }


    //////////home restro api///////
    public void homeRestroList(Context context,String lat,String lon,boolean isRefersh,boolean fromSearch) {
        if (CommonUtilities.isNetworkAvailable(getContext())) {
            this.lat=lat;
            this.lon=lon;
            String userId = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.USERID);
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN);
            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.restorent_list(userId,lat,lon,map);
                //Call<CommonModel> loginCall = servicesInterface.restorent_list(userId,lat,lon);
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
                                        mShimmerViewContainer.stopShimmerAnimation();
                                        recyclerHome.setVisibility(View.VISIBLE);
                                        mShimmerViewContainer.setVisibility(View.GONE);
                                        setData(bean.getHomeList().getOffer(),bean.getHomeList().getNearBy(),bean.getHomeList().getTop_rated(),bean.getHomeList().getNearByGrocery());
                                        swipeRegresh.setRefreshing(false);
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

    //get cuisines list for filter
    private void getCuisines() {
        if(getActivity()!=null) {
            if (CommonUtilities.isNetworkAvailable(getContext())) {
                try {
                    ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                    Call<CommonModel> favoritesCall = servicesInterface.cusineList();
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
                                            cosineList = bean.getCuisine();
                                            if (getActivity() != null) {
                                                showDialog(cosineList);
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
                if (getActivity() != null) {
                    CommonUtilities.snackBar(getActivity(), getString(R.string.no_internet));
                }
            }
        }
    }

    private void showDialog(List<ResponseBean> beans) {
        List<String> cousineName= new ArrayList<>();
        for(int i=0;i<beans.size();i++)
        {
           cousineName.add(beans.get(i).getName());
        }
        cousineName.add(0,"Please Select Cuisine");
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_filter_restroraunt);
        dialog.setCancelable(true);
        Spinner cousineSpn= dialog.findViewById(R.id.cousineSpn);
        Button btnApply= dialog.findViewById(R.id.btnApply);
        tvCousines=dialog.findViewById(R.id.tvCousines);
        tvCousines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtilities.setSpinner(getActivity(),cousineName,cousineSpn);
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cousinesId.equalsIgnoreCase(""))
                {
                    dialog.dismiss();
                    mShimmerViewContainer.startShimmerAnimation();
                    recyclerHome.setVisibility(View.GONE);
                    mShimmerViewContainer.setVisibility(View.VISIBLE);
                    searchFilter(cousinesId);
                }else
                {
                    Toast.makeText(getActivity(), "Please Select Cuisine", Toast.LENGTH_SHORT).show();
//                    CommonUtilities.snackBar(getActivity(),"Please Select Cousine");
                }

            }
        });
        cousineSpn.setOnItemSelectedListener(this);
        dialog.show();
    }


    //get searchFilter for filter
    private void searchFilter(String cuisine) {
        if (CommonUtilities.isNetworkAvailable(getContext())) {
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN);
            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> favoritesCall = servicesInterface.searchFilter(map, "", "", "", "", cuisine);

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
                                        List<RestaurantResponse> list = bean.getFilter();
                                        mShimmerViewContainer.stopShimmerAnimation();
                                        mShimmerViewContainer.setVisibility(View.GONE);
                                        if (list != null && list.size() > 0) {
                                            //recyclerHome.setVisibility(View.GONE);
                                            restroList=list;
                                            tvRestaurant.setVisibility(View.VISIBLE);
                                            tvNoData.setVisibility(View.GONE);
                                            recyclerFilterHome.setVisibility(View.VISIBLE);
                                            setData(restroList);

                                        } else {
                                            recyclerFilterHome.setVisibility(View.GONE);
                                            tvRestaurant.setVisibility(View.GONE);
                                            tvNoData.setVisibility(View.VISIBLE);
                                        }

                                        swipeRegresh.setRefreshing(false);


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


    private void setData(List<RestaurantResponse> offers, List<RestaurantResponse> nearByRestroListData, List<RestaurantResponse> topRatedRestroListData, List<NearByGrocery> nearByGroceryList){
        nearByRestroList.clear();
        topRatedRestroList.clear();
        offersList.clear();
        this.nearByGroceryList.clear();

        nearByRestroList.addAll(nearByRestroListData);
        topRatedRestroList.addAll(topRatedRestroListData);
        offersList.addAll(offers);
        this.nearByGroceryList.addAll(nearByGroceryList);
        homeAdapter.updateList(offersList,nearByRestroList,topRatedRestroList,nearByGroceryList);
    }


    @Override
    public void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();
    }

    private void setData(List<RestaurantResponse> restroList) {
        this.restroList=restroList;
        adapter.updateList(restroList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
//        ((MainActivity)getActivity()).setToolbar(getResources().getString(R.string.home),View.VISIBLE);
//        ((MainActivity)getActivity()).tvTitle.setVisibility(View.GONE);
//        ((MainActivity)getActivity()).edtSearchPlace.setVisibility(View.VISIBLE);
//
//        mShimmerViewContainer.startShimmerAnimation();
//
////         lat=SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LATITUDE);
////         lon=SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LONGITUDE);
//
//        lon= String.valueOf(tracker.getLongitude());
//        lat= String.valueOf(tracker.getLatitude());
//         new CommonUtil().setStatusBarGradiant(getActivity(), NavigationHomeFragment.class.getSimpleName());
//         homeRestroList(getActivity(),lat,lon);




    }

    @Override
    public void onLinkClick(int position, String id,String distance) {
        dispatchRestroDetails(id,distance);
    }

    @Override
    public void onFavClick(int position, Object response,int homePosition) {

        if(SharedPreferenceWriter.getInstance(getContext()).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())) {
            /// 0 for fav 1 for unfav
            if(response instanceof RestaurantResponse) {
                if (((RestaurantResponse) response).getIs_favorite().equalsIgnoreCase(ParamEnum.ZERO.theValue())) {
                    markAsFav("0", ((RestaurantResponse) response).getId(), position, homePosition);
                } else {
                    markAsFav("1", ((RestaurantResponse) response).getId(), position, homePosition);
                }
            }else if(response instanceof NearByGrocery)
            {
                if (((NearByGrocery) response).getIsFavorite().equalsIgnoreCase(ParamEnum.ZERO.theValue())) {
                    markAsFav("0", ""+((NearByGrocery) response).getId(), position, homePosition);
                } else {
                    markAsFav("1", ""+((NearByGrocery) response).getId(), position, homePosition);
                }
            }
        }
        else
        {
            ((MainActivity)getActivity()).showAlertDialog();
        }

    }


    private void dispatchRestroDetails(String restro_id,String distance) {
        Intent intent=new Intent(getActivity(),RestaurantDetails.class);
        intent.putExtra(ParamEnum.RESTRO_ID.theValue(),restro_id);
        intent.putExtra(ParamEnum.DISTANCE.theValue(),distance);
        startActivity(intent);
    }


    //////////mark as fav api///////
    public void markAsFav(String isFav,String restro_id,int position,int homePosition) {
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
                                            if(homePosition==2){
                                                topRatedRestroList.get(position).setIs_favorite("1");
                                                homeAdapter.updateList(offersList,nearByRestroList,topRatedRestroList,nearByGroceryList);
                                            }else if(homePosition==0){
                                                nearByRestroList.get(position).setIs_favorite("1");
                                                homeAdapter.updateList(offersList,nearByRestroList,topRatedRestroList,nearByGroceryList);
                                            }else
                                            {
                                                nearByGroceryList.get(position).setIsFavorite("1");
                                                homeAdapter.updateList(offersList,nearByRestroList,topRatedRestroList,nearByGroceryList);
                                            }
                                        }else {
                                            if(homePosition==2){
                                                topRatedRestroList.get(position).setIs_favorite("0");
                                                homeAdapter.updateList(offersList,nearByRestroList,topRatedRestroList,nearByGroceryList);
                                            }else if(homePosition==0) {
                                                nearByRestroList.get(position).setIs_favorite("0");
                                                homeAdapter.updateList(offersList,nearByRestroList,topRatedRestroList,nearByGroceryList);
                                            }else
                                            {
                                                nearByGroceryList.get(position).setIsFavorite("0");
                                                homeAdapter.updateList(offersList,nearByRestroList,topRatedRestroList,nearByGroceryList);

                                            }

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

    //////////mark as fav filter api///////
    public void markAsFav(int pos, String isFav, String restro_id) {

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

                                        if (bean.getMessage().equalsIgnoreCase("favourite added")) {

                                            restroList.get(pos).setIs_favorite("1");
                                            adapter.updateList(restroList);
                                        } else {
                                            restroList.get(pos).setIs_favorite("0");
                                            adapter.updateList(restroList);
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId())
        {
            case R.id.cousineSpn:
            if(i!=0) {
                tvCousines.setText(cosineList.get(i-1).getName());
                cousinesId=cosineList.get(i-1).getId();

            }
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClickRestaurant(String restaurantId) {
        dispatchRestroDetails(restaurantId);

    }

    @Override
    public void onFavClick(int position, RestaurantResponse response) {
        if (response.getIs_favorite().equalsIgnoreCase(ParamEnum.ZERO.theValue())) {
            markAsFav(position, "0", response.getId());
        } else {
            markAsFav(position, "1", response.getId());
        }

    }

    public interface AppCredit
    {
        void creditPoint(String point);
    }


    private void dispatchRestroDetails(String restro_id) {
        Intent intent = new Intent(getActivity(), RestaurantDetails.class);
        intent.putExtra(ParamEnum.RESTRO_ID.theValue(), restro_id);
        startActivity(intent);

    }

}