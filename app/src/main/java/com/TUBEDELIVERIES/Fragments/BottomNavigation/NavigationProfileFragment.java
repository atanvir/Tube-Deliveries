package com.TUBEDELIVERIES.Fragments.BottomNavigation;


import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TUBEDELIVERIES.Activity.EditMyProfileActivity;
import com.TUBEDELIVERIES.Activity.MainActivity;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import java.util.HashMap;
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
public class NavigationProfileFragment extends Fragment {

    @BindView(R.id.edtEmailMyProfile)
    TextInputEditText edtEmailMyProfile;

    @BindView(R.id.tvProfileUserName)
    TextView tvProfileUserName;

    @BindView(R.id.edtAreaMyProfile)
    TextInputEditText edtAreaMyProfile;

    @BindView(R.id.ed_signup_phone)
    TextInputEditText ed_signup_phone;


    @BindView(R.id.edtLandMark)
    TextInputEditText edtLandMark;

    @BindView(R.id.edStreet)
    TextInputEditText edStreet;

    @BindView(R.id.edtBuilding)
    TextInputEditText edtBuilding;

    @BindView(R.id.edApartment)
    TextInputEditText edApartment;


    @BindView(R.id.edFloor)
    TextInputEditText edFloor;

    @BindView(R.id.edAddressType)
    TextInputEditText edAddressType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_my_profile, container, false);
        new CommonUtil().setStatusBarGradiant(getActivity(), NavigationProfileFragment.class.getSimpleName());

        ButterKnife.bind(this,view);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setToolbar(getResources().getString(R.string.my_profile),View.VISIBLE);
        new CommonUtil().setStatusBarGradiant(getActivity(), NavigationProfileFragment.class.getSimpleName());
        viewProfile();
    }

    @OnClick({R.id.btnEditProfile})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnEditProfile:
                startActivity(new Intent(getContext(), EditMyProfileActivity.class));
                break;
        }
    }

    //////////view Profile api///////
    public void viewProfile() {

        if (CommonUtilities.isNetworkAvailable(getActivity())) {

            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.userDetails(map);

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        loginCall,
                        getActivity(),
                        true,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {

                                        setData(bean.getUserDetails());

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


    private void setData(ResponseBean response){

        edtEmailMyProfile.setText(response.getEmail_id());
        ed_signup_phone.setText(response.getPhone());
        edtLandMark.setText(response.getLandmark());
        edtAreaMyProfile.setText(response.getAddress());
        tvProfileUserName.setText(response.getFirst_name()+" "+response.getLast_name());
        edStreet.setText(response.getStreet());
        edtBuilding.setText(response.getBuilding());
        edFloor.setText(response.getFloor());
        edApartment.setText(response.getApartment());

        if (response.getAddress_type().equals("0")) {
            edAddressType.setText("Home");
        } else {
            edAddressType.setText("Office");
        }
    }



}
