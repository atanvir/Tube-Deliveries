package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingAndReviewsAdap extends RecyclerView.Adapter<RatingAndReviewsAdap.RatingAndReviewHolder> {
    private Context context;

    private List<RestaurantResponse> restroList=null;

    public RatingAndReviewsAdap(Context context,List<RestaurantResponse> restroList) {
        this.context = context;
        this.restroList = restroList;
    }

    @NonNull
    @Override
    public RatingAndReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_resto_info_items,viewGroup,false);

        return new RatingAndReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAndReviewHolder holder, int i) {

        holder.tv_name.setText(restroList.get(i).getUser_name());
        holder.tv_review.setText(restroList.get(i).getReview());
        holder.ratingbar.setRating(Float.parseFloat(restroList.get(i).getRating()));


    }


    public void updateRatingList(List<RestaurantResponse> restroLis){

        this.restroList=restroLis;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return restroList!=null?restroList.size():0;
    }

    public class RatingAndReviewHolder  extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_review)
        TextView tv_review;

        @BindView(R.id.ratingbar)
        RatingBar ratingbar;

        public RatingAndReviewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);


        }
    }
}
