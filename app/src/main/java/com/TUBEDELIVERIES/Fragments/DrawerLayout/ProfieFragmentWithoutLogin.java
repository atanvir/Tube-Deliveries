package com.TUBEDELIVERIES.Fragments.DrawerLayout;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.TUBEDELIVERIES.Activity.EditMyProfileActivity;
import com.TUBEDELIVERIES.Activity.LoginActivity;
import com.TUBEDELIVERIES.Activity.SignUpActivity;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.FirebaseFacebookLogin;
import com.TUBEDELIVERIES.Utility.ParamEnum;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfieFragmentWithoutLogin extends Fragment implements View.OnClickListener {
    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.signupBtn)
    Button signupBtn;


    @BindView(R.id.img_fb)
    ImageView img_fb;

    @BindView(R.id.img_google)
    ImageView img_google;

    private static final int GOOGLE_DATA = 26;
    private static final int FACEBOOK_DATA = 27;
    private String f_name = "";
    private String l_name = "";
    private String email = "";
    private String social_id = "";
    private String facebook_id = "";
    private String google_id = "";






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.profile_without_login, container, false);
        new CommonUtil().setStatusBarGradiant(getActivity(),ProfieFragmentWithoutLogin.class.getSimpleName());
        ButterKnife.bind(this,view);
        clickListner();






        return view;
    }

    private void clickListner() {
        btnLogin.setOnClickListener(this);
        signupBtn.setOnClickListener(this);
        img_fb.setOnClickListener(this);
        img_google.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.btnLogin:
            callingIntent("Login");
            break;

            case R.id.signupBtn:
            callingIntent("Sign up");
            break;
            
            case R.id.img_google:
            break;
            
            case R.id.img_fb: {
                fbLogin();
            }
            break;    
        }
    }

    private void fbLogin() {
        Intent intent = new Intent(getActivity(), FirebaseFacebookLogin.class);
        startActivityForResult(intent, FACEBOOK_DATA);
    }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {

                case GOOGLE_DATA:
                    if (resultCode == 122 && data != null)
                        socialLogin(data, "google");
                    break;

                case FACEBOOK_DATA:
                    if (resultCode == 121 && data != null)
                        socialLogin(data, "fb");

                    break;

            }


    }

    private void callingIntent(String type) {

        Intent intent=null;
        if(type.equalsIgnoreCase("Login"))
        {
            intent=new Intent(getActivity(), LoginActivity.class);
        }else if(type.equalsIgnoreCase("Sign up"))
        {
            intent=new Intent(getActivity(), SignUpActivity.class);
        }

        startActivity(intent);

    }

    private void socialLogin(Intent data, String type) {
        try {
            String name = data.getExtras().getString(ParamEnum.NAME.theValue());
            f_name = data.getExtras().getString(ParamEnum.F_NAME.theValue());
            l_name = data.getExtras().getString(ParamEnum.L_NAME.theValue());
            social_id = data.getExtras().getString(ParamEnum.SOCIAL_ID.theValue());

            if (type.equals("fb"))
                facebook_id = social_id;
            else
                google_id = social_id;

            email = data.getExtras().getString(ParamEnum.SOCIAL_EMAIL.theValue());
            String pictureurl = data.getExtras().getString(ParamEnum.IMAGE.theValue());
            String gender = data.getExtras().getString(ParamEnum.GENDER.theValue());


            socialLogin(type);
//            Bundle bundle = new Bundle();
//            bundle.putString(ParamEnum.NAME.theValue(),data.getExtras().getString(ParamEnum.NAME.theValue()));
//            bundle.putString(ParamEnum.F_NAME.theValue(),data.getExtras().getString(ParamEnum.F_NAME.theValue()));
//            bundle.putString(ParamEnum.L_NAME.theValue(),data.getExtras().getString(ParamEnum.L_NAME.theValue()));
//            bundle.putString(ParamEnum.SOCIAL_ID.theValue(),data.getExtras().getString(ParamEnum.SOCIAL_ID.theValue()));
//            bundle.putString(ParamEnum.SOCIAL_EMAIL.theValue(),data.getExtras().getString(ParamEnum.SOCIAL_EMAIL.theValue()));
//            bundle.putString(ParamEnum.IMAGE.theValue(),data.getExtras().getString(ParamEnum.IMAGE.theValue()));
//            bundle.putString(ParamEnum.GENDER.theValue(),data.getExtras().getString(ParamEnum.GENDER.theValue()));
//
//            Intent intent = new Intent(this,SignUpActivity.class);
//            intent.putExtras(bundle);
//            startActivity(intent);

//            if (pictureurl != null && !pictureurl.isEmpty()) {
//                downloadImage(pictureurl);
//            } else {
//                view_update(firstname, lastname, currentPhotoPath, id, headline, industry);
//            }
            //TODO Work here
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void socialLogin(String type) {
        if (CommonUtilities.isNetworkAvailable(getActivity())) {

            String device_token = SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.DEVICETOKEN);

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.socialMedialLogin(social_id, facebook_id, google_id, type, f_name, l_name, email, ParamEnum.ANDROID.theValue(), device_token);
                ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, getActivity(), true, new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {

                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {
                                        Intent intent = new Intent(getContext(), EditMyProfileActivity.class);
                                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.ISLOGIN, ParamEnum.LOGIN.theValue());
                                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.USERID, bean.getSocialMediaLogin().getUser().getId());
                                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.TOKEN, "Bearer " + bean.getSocialMediaLogin().getAuth().getAccess_token());
                                        startActivity(intent);

                                        Toast.makeText(getActivity(), R.string.login_success, Toast.LENGTH_SHORT).show();
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
