package com.TUBEDELIVERIES.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TUBEDELIVERIES.Adapter.RatingAndReviewsAdap;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingAndReviewActivity extends AppCompatActivity {

    @BindView(R.id.rating_recycle)
    RecyclerView rating_recycle;

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvTotalReviews)
    TextView tvTotalReviews;

    @BindView(R.id.tvRating)
    TextView tvRating;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.menuIv)
    ImageView menuIV;


    private RatingAndReviewsAdap adap=null;
    private List<RestaurantResponse> ratingList=null;
    private String restro_id="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_and_review);
        new CommonUtil().setStatusBarGradiant(this,RatingAndReviewActivity.class.getSimpleName());
        ButterKnife.bind(this);
        menuIV.setVisibility(View.GONE);
        CommonUtilities.setToolbar(this,mainToolbar,tvTitle,getResources().getString(R.string.ratings_and_reviews));
        setUpRecyclerView();
        getIntentData();
    }

    private void setUpRecyclerView() {
//        rating_recycle.xsetLayoutManager(new LinearLayoutManager(this));
//        rating_recycle.setAdapter(new RatingAndReviewsAdap(this,null));

        rating_recycle.setLayoutManager(new LinearLayoutManager(this));
        rating_recycle.setFocusable(false);
        ratingList=new ArrayList<>();
        adap=new RatingAndReviewsAdap(this,ratingList);
        rating_recycle.setAdapter(adap);

    }

    //////////restroinfo api///////
    public void restroInfo(String restroId) {

        if (CommonUtilities.isNetworkAvailable(this)) {

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.restaurantInfo(restroId);

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

                                        tvRating.setText(Double.parseDouble(bean.getRestorentInfo().getTotal_rating())+" Ratings");
                                        ratingBar.setRating(Float.parseFloat(bean.getRestorentInfo().getTotal_rating()));
//                                        ratingBar.setRating("Restaurant's Latest Reviews(200+)"+bean.getRestorentInfo().getTotal_review());
                                        tvTotalReviews.setText("Restaurant's Latest Reviews "+"("+bean.getRestorentInfo().getTotal_review()+")");

                                        ratingList.clear();
                                        ratingList.addAll(bean.getRestorentInfo().getRestorent_review());
                                        adap.updateRatingList(ratingList);


                                    } else {
                                        CommonUtilities.snackBar(RatingAndReviewActivity.this, bean.getMessage());
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

    private void getIntentData() {

        if (getIntent() != null) {

            restro_id = getIntent().getStringExtra(ParamEnum.RESTRO_ID.theValue());

            restroInfo(restro_id);
        }

    }

}
