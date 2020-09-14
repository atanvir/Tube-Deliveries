package com.TUBEDELIVERIES.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.TUBEDELIVERIES.Model.CommonModel;
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
 * Created by Mahipal Singh on 23,May,2019
 * mahisingh1@outlook.com
 */


public class ShareReviewActivity extends AppCompatActivity {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.etReview)
    EditText etReview;

   @BindView(R.id.ratingBar)
   RatingBar ratingBar;

    private String restro_id="";
    private String order_id="";


    private float ratingReview=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_review);
        ButterKnife.bind(this);
        CommonUtilities.setToolbar(this,mainToolbar,tvTitle,getResources().getString(R.string.share_reviews));
        findViewById(R.id.menuIv).setVisibility(View.GONE);
        new CommonUtil().setStatusBarGradiant(this,ShareReviewActivity.class.getSimpleName());

        getIntentData();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingReview=rating;
            }
        });

    }

    private void getIntentData() {

        if(getIntent()!=null){

            restro_id=getIntent().getStringExtra(ParamEnum.RESTRO_ID.theValue());
            order_id=getIntent().getStringExtra(ParamEnum.ORDER_ID.theValue());

        }

    }

    @OnClick({R.id.tvShareReviewsSubmit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tvShareReviewsSubmit:

                if(etReview.getText().toString().length()>30){

                    shareReview();
                }else {

                    Toast.makeText(this, "Write atleast 30 chararacter", Toast.LENGTH_SHORT).show();

                }

                break;
        }
    }

    //////////login api///////
    public void shareReview() {

        if (CommonUtilities.isNetworkAvailable(this)) {

            String token = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);

            Map<String, String> map = new HashMap<>();
            map.put(ParamEnum.AUTHORIZATION.theValue(), String.valueOf(token));

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.rating_review(map,restro_id,order_id,String.valueOf(ratingReview),etReview.getText().toString());
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

                                        Toast.makeText(ShareReviewActivity.this, "Thanks for the ratings", Toast.LENGTH_SHORT).show();
                                                   finish();

                                    } else {
                                        CommonUtilities.snackBar(ShareReviewActivity.this, bean.getMessage());
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
