package com.TUBEDELIVERIES.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.TUBEDELIVERIES.Adapter.FilterAdapter;
import com.TUBEDELIVERIES.Adapter.SubFilterAdapter;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.FilterCategoryModel;
import com.TUBEDELIVERIES.Model.FilterOptionModel;
import com.TUBEDELIVERIES.Model.FilterSubCategoryModel;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.GPSTracker;
import com.TUBEDELIVERIES.Utility.ParamEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, FilterAdapter.CategorySelectedListner, SubFilterAdapter.SubCategoryClickListner {
    @BindView(R.id.rgType) RadioGroup rgType;
    @BindView(R.id.rvSubCategory) RecyclerView rvSubCategory;
    @BindView(R.id.rvTypeCategrory) RecyclerView rvTypeCategrory;
    private String type;
    TextView tvClear;
    Map<String , List<FilterCategoryModel>> allCategoryDataMap;
    List<FilterOptionModel.Cuisine> cuisinesList=new ArrayList<>();
    List<FilterOptionModel.Category> typeList=new ArrayList<>();
    List<FilterOptionModel.Category_> groceryTypeList=new ArrayList<>();
    Map<String,List<FilterSubCategoryModel>> subCategoryListMap;
    String storeType="2";
    GPSTracker tracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        tvClear=findViewById(R.id.tvClear);
        findViewById(R.id.btnApply).setOnClickListener(this);
        TextView tvCount=findViewById(R.id.tvCount);
        tvCount.setText(getIntent().getStringExtra("count")+" Stores Found");
        tvClear.setOnClickListener(this);
        filterOptionsApi();
        new CommonUtil().setStatusBarGradiant(FilterActivity.this,FilterActivity.class.getSimpleName());
        findViewById(R.id.menuIv).setVisibility(View.GONE);
        CommonUtilities.setToolbar(this,findViewById(R.id.mainToolbar),findViewById(R.id.tvTitle),getString(R.string.filter));
        tracker= new GPSTracker(this);
    }

    private void filterOptionsApi() {
        if (CommonUtilities.isNetworkAvailable(this)) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<FilterOptionModel> favoritesCall = servicesInterface.filterOption();

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        favoritesCall, this, true,
                        new Callback<FilterOptionModel>() {
                            @Override
                            public void onResponse(Call<FilterOptionModel> call, Response<FilterOptionModel> response) {
                                if (response.isSuccessful()) {
                                    FilterOptionModel bean = ((FilterOptionModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                        cuisinesList=bean.getOptions().getFood().getCuisine();
                                        typeList=bean.getOptions().getFood().getCategory();
                                        groceryTypeList=bean.getOptions().getGrocery().getCategory();
                                        setttingData(cuisinesList,typeList,groceryTypeList);


                                    } else {
                                        CommonUtilities.snackBar(FilterActivity.this, bean.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<FilterOptionModel> call, Throwable t) {

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtilities.snackBar(FilterActivity.this, getString(R.string.no_internet));

        }
    }

    private void setttingData(List<FilterOptionModel.Cuisine> cuisinesList, List<FilterOptionModel.Category> typeList, List<FilterOptionModel.Category_> groceryTypeList) {
        this.cuisinesList=cuisinesList;
        this.typeList=typeList;
        this.groceryTypeList=groceryTypeList;
        allCategoryDataMap=new HashMap<>();
        subCategoryListMap=new HashMap<>();

        //Category-Wise
        allCategoryDataMap.put(""+1,getCategoryValues(1));
        allCategoryDataMap.put(""+2,getCategoryValues(2));

        //Sub-Category Wise
        //Grocery
        subCategoryListMap.put("Type",getSubCategoryList("Type"));
        subCategoryListMap.put("Distance",getSubCategoryList("Distance"));
        //Food
        subCategoryListMap.put("Cuisines",getSubCategoryList("Cuisines"));
        subCategoryListMap.put("TypeFood",getSubCategoryList("TypeFood"));
        subCategoryListMap.put("Eat Type",getSubCategoryList("Eat Type"));
        subCategoryListMap.put("Ratings",getSubCategoryList("Ratings"));
        setTypeAdapter(0,allCategoryDataMap.get("1"));
        rgType.setOnCheckedChangeListener(this);
    }


    private List<FilterSubCategoryModel> getSubCategoryList(String type) {
        List<FilterSubCategoryModel> list = new ArrayList<>();
        //Food
        if(type.equalsIgnoreCase("Type"))
        {
            for(int i=0;i<groceryTypeList.size();i++)
            {
                list.add(new FilterSubCategoryModel(""+groceryTypeList.get(i).getId(),groceryTypeList.get(i).getName(),false));
            }
        }
        else if(type.equalsIgnoreCase("Distance"))
        {
            list.add(new FilterSubCategoryModel("Under 2 KM",false));
            list.add(new FilterSubCategoryModel("Under 5 KM",false));
            list.add(new FilterSubCategoryModel("Under 10 KM",false));
            list.add(new FilterSubCategoryModel("Under 20 KM",false));
            list.add(new FilterSubCategoryModel("Under 45 KM",false));
        }

        // Grocery
        else if(type.equalsIgnoreCase("Cuisines"))
        {
            for(int i=0;i<cuisinesList.size();i++)
            {
                list.add(new FilterSubCategoryModel(""+cuisinesList.get(i).getId(),cuisinesList.get(i).getName(),false));

            }
        }else if(type.equalsIgnoreCase("TypeFood"))
        {
            for(int i=0;i<typeList.size();i++)
            {
                list.add(new FilterSubCategoryModel(""+typeList.get(i).getId(),typeList.get(i).getName(),false));
            }
        }

        else if(type.equalsIgnoreCase("Eat Type"))
        {
            list.add(new FilterSubCategoryModel("Veg",false));
            list.add(new FilterSubCategoryModel("Non Veg",false));
        }else if(type.equalsIgnoreCase("Ratings"))
        {
            list.add(new FilterSubCategoryModel("Ratings",false,true));
        }

        return list;
    }

    private List<FilterCategoryModel> getCategoryValues(int type) {
        List<FilterCategoryModel> list = new ArrayList();
        if(type==1) {
            list.add(new FilterCategoryModel("Grocery", "Type", true));
            list.add(new FilterCategoryModel("Grocery", "Distance", false));
        }else if(type==2) {
            list.add(new FilterCategoryModel("Food", "Cuisines", true));
            list.add(new FilterCategoryModel("Food", "Type", false));
            list.add(new FilterCategoryModel("Food", "Eat Type", false));
            list.add(new FilterCategoryModel("Food", "Ratings", false));
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvClear:
            subCategoryListMap.clear();
            allCategoryDataMap.clear();
            setttingData(cuisinesList,typeList,groceryTypeList);
            switch (rgType.getCheckedRadioButtonId())
            {
                case R.id.rbGrocery:
                setTypeAdapter(0,allCategoryDataMap.get("1"));
                break;

                case R.id.rbFood:
                setTypeAdapter(1,allCategoryDataMap.get("2"));
                break;
            }

            break;

            case R.id.btnApply:
            if(checkValidation()) {
                filterRestorentApi();
            }else
            {
                CommonUtilities.snackBar(this,"Please select option first");
            }
            break;
        }
    }

    private boolean checkValidation() {
        boolean ret=false;
        for(int i=0;i<subCategoryListMap.size();i++)
        {

                for (int j = 0; j < subCategoryListMap.get(subCategoryListMap.keySet().toArray()[i]).size(); j++) {
                    if (subCategoryListMap.get(subCategoryListMap.keySet().toArray()[i]).get(j).isCheck()) {
                        ret = true;
                        break;
                    }else if(subCategoryListMap.get(subCategoryListMap.keySet().toArray()[i]).get(j).getRating()!=0.0)
                    {
                        ret=true;
                        break;
                    }
                }


        }

        return ret;
    }

    private void filterRestorentApi() {
        if (CommonUtilities.isNetworkAvailable(this)) {
            String token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.filter(storeType,getCheckData("Cuisines",""),
                                                                       getCheckData(""+storeType!="2"?"Type":"TypeFood",""),
                                                                       getCheckData("Eat Type","Veg"),getCheckData("Eat Type","Non Veg"),
                                                                       getCheckData("Ratings",""),getCheckData("Distance",""),
                                                                        ""+tracker.getLatitude(),""+tracker.getLongitude());

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        loginCall,
                        this,
                        true,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                        Intent intent = new Intent();
                                        intent.putParcelableArrayListExtra("filterData", (ArrayList<? extends Parcelable>) bean.getFilter());
                                        setResult(RESULT_OK, intent);
                                        finish();

                                    } else {
                                        CommonUtilities.snackBar(FilterActivity.this, bean.getMessage());
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

    private String getCheckData(String type,String eatType) {
        String id="";
    if(storeType.equalsIgnoreCase("1"))
    {
        if(type.equalsIgnoreCase("Type"))
        {
            type="TypeFood";
        }

    }

        for(int i=0;i<subCategoryListMap.get(type).size();i++)
        {
            if(subCategoryListMap.get(type).get(i).isCheck())
            {
                if(type.equalsIgnoreCase("Eat type"))
                {
                    if(eatType.equalsIgnoreCase(subCategoryListMap.get(type).get(i).getName()))
                    {
                        id="1";
                    }

                    break;
                }else if(type.equalsIgnoreCase("Distance"))
                {
                   id= subCategoryListMap.get(type).get(i).getName().split("Under")[1].trim().split("KM")[0].trim();
                   break;
                }else
                {
                    id=subCategoryListMap.get(type).get(i).getId();
                    break;
                }
            }
            else if(type.equalsIgnoreCase("Ratings"))
            {
                if(subCategoryListMap.get(type).get(i).getRating()!=0.0) {
                    id = "" + subCategoryListMap.get(type).get(i).getRating();
                }
                break;
            }
        }

        return id;
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId())
        {
            case R.id.rbGrocery:
            storeType="2";
            setttingData(cuisinesList, typeList,groceryTypeList);
            setTypeAdapter(0,allCategoryDataMap.get("1"));
            break;

            case R.id.rbFood:
            storeType="1";
            setttingData(cuisinesList, typeList,groceryTypeList);
            setTypeAdapter(1,allCategoryDataMap.get("2"));
            break;
        }
    }

    private void setTypeAdapter(int type,List<FilterCategoryModel> list) {
        rvTypeCategrory.setLayoutManager(new LinearLayoutManager(this));
        rvTypeCategrory.setAdapter(new FilterAdapter(this,type,list,this));

        rvSubCategory.setLayoutManager(new LinearLayoutManager(this));
        rvSubCategory.setAdapter(new SubFilterAdapter(this,list.get(0).getName(),subCategoryListMap.get(list.get(0).getName()),this));
    }


    @Override
    public void onCategoryClick(String type, int categoryType) {
     Log.e("type", type);
     Log.e("categoryType", ""+categoryType);
        if(categoryType==1) {
            if (type.equalsIgnoreCase("Distance")) type = "DistanceGrocery";
            else if(type.equalsIgnoreCase("Type")) type="TypeFood";
        }

        rvSubCategory.setLayoutManager(new LinearLayoutManager(this));
        rvSubCategory.setAdapter(new SubFilterAdapter(this,type,subCategoryListMap.get(type),this));
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onItemSelected(String type,List<FilterSubCategoryModel> list) {
        subCategoryListMap.put(type,list);
    }
}
