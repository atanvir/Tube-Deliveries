package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.TUBEDELIVERIES.Activity.RestaurantDetails;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Mahipal Singh on 14,JUne,2019
 * mahisingh1@outlook.com
 */

public class HomeOfferViewPagerAdap extends PagerAdapter {

    private Context context;
    private List<RestaurantResponse> offersList;

    public HomeOfferViewPagerAdap(Context context,List<RestaurantResponse>offers) {
        this.context = context;
        this.offersList = offers;
    }

    @Override
    public int getCount() {
        return offersList!=null?offersList.size():0;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_offer_home, container, false);

        ImageView ivOffers=(ImageView)itemView.findViewById(R.id.ivOffers);
        ProgressBar progressbar=(ProgressBar)itemView.findViewById(R.id.progressbar);

        if(!offersList.get(position).getThumbnail().equalsIgnoreCase(""))
            setImage(progressbar,offersList.get(position).getThumbnail(),ivOffers);
        else
            Glide.with(context).load(R.drawable.debit_card).override(110, 150).into(ivOffers);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, RestaurantDetails.class);
                intent.putExtra(ParamEnum.RESTRO_ID.theValue(), offersList.get(position).getRestorent_id());
                context.startActivity(intent);
            }
        });
        container.addView(itemView);
        return itemView;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ConstraintLayout)object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);

    }

    private void setImage(ProgressBar progressBar, final String imageUri, final ImageView imageView) {

        progressBar.setVisibility(View.VISIBLE);

        Glide.with(context.getApplicationContext())
                .load(imageUri)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }
                }).into(imageView);
    }

    public void updateList(List<RestaurantResponse>offers){

        this.offersList=offers;
        notifyDataSetChanged();

    }
}
