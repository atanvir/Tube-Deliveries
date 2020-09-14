package com.TUBEDELIVERIES.Fragments.DrawerLayout;


import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TUBEDELIVERIES.Activity.LoginActivity;
import com.TUBEDELIVERIES.Activity.MainActivity;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.AsteriskPasswordTransformationMethod;
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
public class Setting_fragment extends Fragment {

    @BindView(R.id.edtCurrentPass)
    TextInputEditText edtCurrentPass;

    @BindView(R.id.edtNewPass)
    TextInputEditText edtNewPass;

    @BindView(R.id.edtConfirmPass)
    TextInputEditText edtConfirmPass;

    @BindView(R.id.clChangePass)
    ConstraintLayout clChangePass;

    @BindView(R.id.tvChangePass)
    TextView tvChangePass;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this,view);
        setAstrikCharacter();

        return view;
    }

    private void setAstrikCharacter() {
        edtCurrentPass.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        edtNewPass.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        edtConfirmPass.setTransformationMethod(new AsteriskPasswordTransformationMethod());
    }

    @OnClick({R.id.tvLogout,R.id.tvChangePass,R.id.btnSaveSettings})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tvLogout:
                logout();
                break;
            case R.id.tvChangePass:
                changePasswordVisibility();
                break;

            case R.id.btnSaveSettings:
                if(validation())
                   hitChangePassword();
                break;


        }
    }


    private void changePasswordVisibility() {
        if(clChangePass.getVisibility()==View.GONE){
            clChangePass.setVisibility(View.VISIBLE);
            tvChangePass.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.drop_down_ic,0);
        }else {
            clChangePass.setVisibility(View.GONE);
            tvChangePass.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.drop_dwn,0);
        }
    }

    private boolean validation(){
        if(!edtNewPass.getText().toString().matches(edtConfirmPass.getText().toString())){
            CommonUtilities.snackBar(getActivity(),"New Password and Confirm Password do not match");
            return false;
        }
        return true;
    }

    // hit change password api
    private void hitChangePassword() {
        if (CommonUtilities.isNetworkAvailable(getContext())) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> favoritesCall = servicesInterface.changePassword(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN),
                             edtCurrentPass.getText().toString(),edtNewPass.getText().toString());

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
                                        clChangePass.setVisibility(View.GONE);
                                        tvChangePass.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.drop_dwn,0);
                                        edtCurrentPass.setText("");
                                        edtNewPass.setText("");
                                        edtConfirmPass.setText("");
                                        CommonUtilities.snackBar(getActivity(), bean.getMessage());
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
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setToolbar(getResources().getString(R.string.settings),View.GONE);
    }


    //////////logout api///////
    public void logout() {

        if (CommonUtilities.isNetworkAvailable(getActivity())) {

            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.logout(map);

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

                                        if (response.isSuccessful()){

                                            dispatchLoginAct();
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

    private void dispatchLoginAct(){

        Intent intent=new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.ISLOGIN, "Logout");
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.DEVICETOKEN, "");
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.USER_NAME ,"");

        getActivity().finishAffinity();

    }

}
