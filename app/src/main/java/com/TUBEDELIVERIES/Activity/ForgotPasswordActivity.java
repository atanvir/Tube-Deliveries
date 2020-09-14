package com.TUBEDELIVERIES.Activity;

import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ed_forgot_email)
    TextInputEditText ed_forgot_email;

    @BindView(R.id.menuIv)
    ImageView menuIv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        new CommonUtil().setStatusBarGradiant(this,ForgotPasswordActivity.class.getSimpleName());
        ButterKnife.bind(this);
        menuIv.setVisibility(View.GONE);
        ed_forgot_email.setOnEditorActionListener(this);
        CommonUtilities.setToolbar(this,mainToolbar,tvTitle,getResources().getString(R.string.forgot_password));
    }

    @OnClick({R.id.bt_forgot})
    public  void onClick(View view){
        switch (view.getId()){
            case R.id.bt_forgot:
                hideKeyboard();
                if(validation()) forgetPassApiHit();
                break;
        }
    }
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.bt_forgot).getWindowToken(), 0);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
            findViewById(R.id.bt_forgot).performClick();
        }
        return false;
    }

    //////////forget pass api///////
    public void forgetPassApiHit() {

        if (CommonUtilities.isNetworkAvailable(this)) {

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.forgetPass(ed_forgot_email.getText().toString());

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
                                        if(response.isSuccessful()){
                                            Toast.makeText(ForgotPasswordActivity.this, "Your password has been sent to your email id", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                                        }

                                    } else {
                                        CommonUtilities.snackBar(ForgotPasswordActivity.this, bean.getMessage());
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

    private boolean validation (){
        if(!ed_forgot_email.getText().toString().matches(CommonUtilities.emailPattern)){
            CommonUtilities.snackBar(this,"Enter valid email id");
            return false;
        }
        return true;
    }

}
