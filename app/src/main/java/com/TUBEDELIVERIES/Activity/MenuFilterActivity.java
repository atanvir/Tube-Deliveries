package com.TUBEDELIVERIES.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.TUBEDELIVERIES.Model.CommonModel;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuFilterActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{

    //Toolbar
    @BindView(R.id.menuIv)
    ImageView menuIv;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;


    //Layout
    @BindView(R.id.applyFilterBtn)
    Button applyFilterBtn;
    @BindView(R.id.EatTypeGroup)
    RadioGroup EatTypeGroup;
    @BindView(R.id.TypeGroup)
    RadioGroup TypeGroup;
    @BindView(R.id.tasteRG)
    RadioGroup tasteRG;
    @BindView(R.id.EatTypeCB)
    CheckBox EatTypeCB;
    @BindView(R.id.TypeCB)
    CheckBox TypeCB;
    @BindView(R.id.tasteCB) CheckBox tasteCB;



    //ITEM_TYPE
    @BindView(R.id.vegRB)
    RadioButton vegRB;
    @BindView(R.id.noVegRB) RadioButton noVegRB;


    //EAT_TYPE
    @BindView(R.id.LunchRB) RadioButton LunchRB;
    @BindView(R.id.breakFastRB) RadioButton breakFastRB;
    @BindView(R.id.dinnerRB) RadioButton dinnerRB;


    //TASTE
    @BindView(R.id.sweetRB) RadioButton sweetRB;
    @BindView(R.id.saltyRB) RadioButton saltyRB;
    @BindView(R.id.mixedRB) RadioButton mixedRB;


    //Variables
    String eatType="",itemType="",taste="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_filter);
        ButterKnife.bind(this);
        setValues();
        init();

    }


    private void init() {
        menuIv.setVisibility(View.GONE);
        applyFilterBtn.setOnClickListener(this);
        tasteRG.setOnCheckedChangeListener(this);
        TypeGroup.setOnCheckedChangeListener(this);
        EatTypeGroup.setOnCheckedChangeListener(this);
        CommonUtilities.setToolbar(this,mainToolbar,tvTitle,getString(R.string.filter));
    }

    private void setValues() {


        if(getIntent().getStringExtra(ParamEnum.ITEM_TYPE.theValue())!=null)
        {
            itemType=getIntent().getStringExtra(ParamEnum.ITEM_TYPE.theValue());
            setText("ITEM_TYPE",itemType);

        }
        if(getIntent().getStringExtra(ParamEnum.EAT_TYPE.theValue())!=null)
        {
            eatType=getIntent().getStringExtra(ParamEnum.EAT_TYPE.theValue());
            setText("EAT_TYPE",eatType);
        }
        if(getIntent().getStringExtra(ParamEnum.TASTE.theValue())!=null)
        {
            taste=getIntent().getStringExtra(ParamEnum.TASTE.theValue());
            setText("TASTE",taste);
        }

    }

    private void setText(String type, String value) {
        switch (type)
        {
            case "ITEM_TYPE":
                switch (value){

                    case "0": vegRB.setChecked(true); TypeCB.setChecked(true); break;
                    case "1": noVegRB.setChecked(true); TypeCB.setChecked(true); break;
                }

                break;


            case "EAT_TYPE":
                switch (value) {

                    case "1": LunchRB.setChecked(true); EatTypeCB.setChecked(true); break;
                    case "2": breakFastRB.setChecked(true); EatTypeCB.setChecked(true); break;
                    case "3": dinnerRB.setChecked(true); EatTypeCB.setChecked(true); break;
                }

                break;

            case "TASTE":
                switch (value)
                {
                    case "1": sweetRB.setChecked(true); tasteCB.setChecked(true); break;
                    case "2": saltyRB.setChecked(true); tasteCB.setChecked(true); break;
                    case "3": mixedRB.setChecked(true); tasteCB.setChecked(true); break;
                }
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.applyFilterBtn:
                dishFilter(getIntent().getStringExtra(ParamEnum.RESTRO_ID.theValue()),
                        getIntent().getStringExtra(ParamEnum.CATEGORY_ID.theValue()),
                        getIntent().getStringExtra(ParamEnum.CATEGORY_COMBO.theValue()),
                        itemType,eatType,taste);


                break;

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId())
        {

            case R.id.EatTypeGroup:
                switch (group.getCheckedRadioButtonId())
                {
                    case R.id.LunchRB:
                        eatType="1";
                        EatTypeCB.setChecked(true);

                        break;

                    case R.id.breakFastRB:
                        eatType="2";
                        EatTypeCB.setChecked(true);
                        break;

                    case R.id.dinnerRB:
                        eatType="3";
                        EatTypeCB.setChecked(true);
                        break;

                }


                break;

            case R.id.TypeGroup:

                switch (group.getCheckedRadioButtonId())
                {
                    case R.id.vegRB:
                        itemType="0";
                        TypeCB.setChecked(true);
                        break;

                    case R.id.noVegRB:
                        itemType="1";
                        TypeCB.setChecked(true);
                        break;


                }


                break;

            case R.id.tasteRG:
                switch (group.getCheckedRadioButtonId())
                {
                    case R.id.sweetRB:
                        taste="1";
                        tasteCB.setChecked(true);
                        break;

                    case R.id.saltyRB:
                        taste="2";
                        tasteCB.setChecked(true);
                        break;


                    case R.id.mixedRB:
                        taste="3";
                        tasteCB.setChecked(true);
                        break;

                }

                break;

        }

    }


    public void dishFilter(String restroId, String category_id, String type, String itemType, String eatType,String taste) {

        if (CommonUtilities.isNetworkAvailable(this)) {
            String token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.listmenuFilter(map, restroId, category_id, type, itemType, eatType,taste,getIntent().getIntExtra(ParamEnum.STORE_TYPE.theValue(),-1));

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

                                        Intent intent=new Intent();
                                        Log.v("values:",itemType+":"+eatType+":"+taste);
                                        intent.putExtra(ParamEnum.ITEM_TYPE.theValue(),""+itemType);
                                        intent.putExtra(ParamEnum.EAT_TYPE.theValue(),""+eatType);
                                        intent.putExtra(ParamEnum.TASTE.theValue(),""+taste);
                                        intent.putParcelableArrayListExtra("menuDetailsList", (ArrayList<? extends Parcelable>) bean.getMenuList());
                                        setResult(RESULT_OK,intent);
                                        finish();

                                    } else {
                                        CommonUtilities.snackBar(MenuFilterActivity.this, bean.getMessage());
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
