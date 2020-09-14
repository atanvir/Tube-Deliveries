package com.TUBEDELIVERIES.Fragments.BottomNavigation;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.TUBEDELIVERIES.Activity.FilterActivity;
import com.TUBEDELIVERIES.Activity.MainActivity;
import com.TUBEDELIVERIES.Activity.RestaurantDetails;
import com.TUBEDELIVERIES.Adapter.RestroListingAdapter;
import com.TUBEDELIVERIES.Adapter.SpinnerAdapter;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtil;
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
 * A simple {@link Fragment} subclass.
 */
public class NavigationSearchFragment extends Fragment implements RestroListingAdapter.onRestaurantClick, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerSearch)
    RecyclerView recyclerSearch;

    @BindView(R.id.edtSearchFrag)
    EditText edtSearchFrag;

    @BindView(R.id.swipeSearch)
    SwipeRefreshLayout swipeSearch;

    @BindView(R.id.tvNoData)
    TextView tvNoData;

    private Spinner spCusines;

    private ArrayAdapter<ResponseBean> arrayAdapter;

    private Dialog dialog;
    private RestroListingAdapter adapter = null;

    private List<ResponseBean> cuisinesList = new ArrayList<>();
    private String RatingValue = "";
    private String filterPrice = "";
    private String isVeg = "";
    private String isNonVeg = "";
    private String cuisineId = "";
    int count;

    private List<RestaurantResponse> restroList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        setUpRecyclerView();
        hitSearchApi(true);
        getCuisines();
        swipeSearch.setOnRefreshListener(this);

        edtSearchFrag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equalsIgnoreCase(""))
                {
                    hitSearchApi(true);
                }
                else
                {
                    globalSearch(s.toString(),false);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        edtSearchFrag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Your piece of code on keyboard search click


                    globalSearch(edtSearchFrag.getText().toString(),true);

                    return true;
                }
                return false;
            }
        });

        return view;
    }

    @OnClick({R.id.ivFilterSearch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivFilterSearch:
            Intent intent = new Intent(getActivity(), FilterActivity.class);
            intent.putExtra("count", count);
            startActivityForResult(intent,90);
            break;
        }
    }

    private void setData(List<RestaurantResponse> restroList) {
        this.restroList=restroList;
        adapter.updateList(restroList);

    }


    //// filter dialogue
    private void showFilterDialog() {
        dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_search_filter);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView btn_cancel = dialog.findViewById(R.id.btn_cancel);
        TextView btn_apply = dialog.findViewById(R.id.btn_apply);
        SeekBar seekBar = dialog.findViewById(R.id.seekBar);
        TextView tvPrice = dialog.findViewById(R.id.tvPrice);
        spCusines = dialog.findViewById(R.id.spCusines);

        RadioGroup rgRating = dialog.findViewById(R.id.rgRating);
        RadioGroup rgVegNonVeg = dialog.findViewById(R.id.rgVegNonVeg);

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), cuisinesList);
        spCusines.setAdapter(spinnerAdapter);


        spCusines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(getApplicationContext(), cuisinesList.get(position).getId(), Toast.LENGTH_LONG).show();
                cuisineId = cuisinesList.get(position).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rgRating.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.rbRatingOne:
                        RatingValue = "1";
                        break;

                    case R.id.rbRatingTwo:
                        RatingValue = "2";
                        break;

                    case R.id.rbRatingThree:
                        RatingValue = "3";
                        break;

                    case R.id.rbRatingFour:
                        RatingValue = "4";
                        break;

                    case R.id.rbRatingFive:
                        RatingValue = "5";
                        break;
                }


            }
        });


        rgVegNonVeg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.rbVeg:
                        isVeg = "0";
                        isNonVeg = "1";
                        break;

                    case R.id.rbNonVeg:
                        isVeg = "1";
                        isNonVeg = "0";

                        break;
                }


            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                tvPrice.setText("USD " + String.valueOf(progress * 100));
                filterPrice = String.valueOf(progress * 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFilter(String.valueOf(filterPrice), isVeg, isNonVeg, RatingValue, cuisineId);
                RatingValue = "";
                filterPrice = "";
                isVeg = "";
                isNonVeg = "";
                cuisineId = "";
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setToolbar(getResources().getString(R.string.search), View.VISIBLE);
        new CommonUtil().setStatusBarGradiant(getActivity(), NavigationHomeFragment.class.getSimpleName());
    }

    private void hitSearchApi(boolean isLoader) {
        if (CommonUtilities.isNetworkAvailable(getContext())) {

                String userId = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.USERID);

                try {
                    ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                    Call<CommonModel> favoritesCall = servicesInterface.getSearchRestaurants(userId, "28.6258584", "77.3751766");

                    ServicesConnection.getInstance().enqueueWithoutRetry(favoritesCall, getActivity(), isLoader, new Callback<CommonModel>() {
                                @Override
                                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                    if (response.isSuccessful()) {
                                        CommonModel bean = ((CommonModel) response.body());
                                        if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                            List<RestaurantResponse> list = bean.getSearch();

                                            if (list != null && list.size() > 0) {
                                                setData(list);
                                                count=list.size();
                                                tvNoData.setVisibility(View.GONE);
                                                recyclerSearch.setVisibility(View.VISIBLE);
                                            } else {
                                                tvNoData.setVisibility(View.VISIBLE);
                                                recyclerSearch.setVisibility(View.GONE);

                                            }
                                            swipeSearch.setRefreshing(false);


                                        } else {
                                            CommonUtilities.snackBar(getActivity(), bean.getMessage());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<CommonModel> call, Throwable t) {
                                    Toast.makeText(getActivity(), "failure", Toast.LENGTH_SHORT).show();
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


                                        cuisinesList = bean.getCuisine();

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


    private void dispatchRestroDetails(String restro_id) {

        Intent intent = new Intent(getActivity(), RestaurantDetails.class);
        intent.putExtra(ParamEnum.RESTRO_ID.theValue(), restro_id);
        startActivity(intent);

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

    //get searchFilter for filter
    private void searchFilter(String price, String isVeg, String is_non_veg, String rating, String cuisine) {

        if (CommonUtilities.isNetworkAvailable(getContext())) {

            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN);
            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> favoritesCall = servicesInterface.searchFilter(map, price, isVeg, is_non_veg, rating, cuisine);

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


                                        List<RestaurantResponse> list = bean.getFilter();

                                        if (list != null && list.size() > 0) {
                                            setData(list);
                                            tvNoData.setVisibility(View.GONE);
                                            recyclerSearch.setVisibility(View.VISIBLE);
                                        } else {
                                            tvNoData.setVisibility(View.VISIBLE);
                                            recyclerSearch.setVisibility(View.GONE);
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

    //////////mark as fav api///////
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
// for(int i=0;i<restroList.size();i++){
// restroList.get(i).setHeartAnimCheck(false);
// }
// restroList.get(pos).setHeartAnimCheck(true);
                                            adapter.updateList(restroList);


                                        } else {
//
// for(int i=0;i<restroList.size();i++){
// restroList.get(i).setHeartAnimCheck(false);
// }
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


    public void setUpRecyclerView() {

        restroList = new ArrayList<>();
        recyclerSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RestroListingAdapter(getContext(), restroList, this);
        recyclerSearch.setAdapter(adapter);
        count=restroList.size();
        //recyclerSearch.setAdapter(new RestroListingAdapter(getContext(),list));

    }

    @Override
    public void onRefresh() {

        if(edtSearchFrag.getText().toString().trim().equalsIgnoreCase(""))
        {
            hitSearchApi(false);
        }
        else
        {
            globalSearch(edtSearchFrag.getText().toString().trim(),false);
        }
    }


    //get searchFilter for filter
    private void globalSearch(String keywords,boolean isLoader) {

        if (CommonUtilities.isNetworkAvailable(getContext())) {

            String userId = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.USERID);


            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> favoritesCall = servicesInterface.GlobalSearch(userId, keywords);

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        favoritesCall,
                        getActivity(),
                        isLoader,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {

                                        List<RestaurantResponse> list = bean.getSearch();

                                        if (list != null && list.size() > 0) {
                                            setData(list);
                                            tvNoData.setVisibility(View.GONE);
                                            recyclerSearch.setVisibility(View.VISIBLE);
                                        } else {
                                            tvNoData.setVisibility(View.VISIBLE);
                                            recyclerSearch.setVisibility(View.GONE);

                                        }
                                        swipeSearch.setRefreshing(false);


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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode)
        {
            case 90:
            if(resultCode==getActivity().RESULT_OK)
            {
                setData(data.getParcelableArrayListExtra("filterData"));

            }else if(resultCode==getActivity().RESULT_CANCELED)
            {
               hitSearchApi(true);
            }
            break;
        }
    }
}