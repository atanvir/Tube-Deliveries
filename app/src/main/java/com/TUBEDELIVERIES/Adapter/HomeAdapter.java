package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.TUBEDELIVERIES.Activity.RestrauntListingAct;
import com.TUBEDELIVERIES.CallBacks.onClickEvents;
import com.TUBEDELIVERIES.Fragments.DrawerLayout.OffersFragment;
import com.TUBEDELIVERIES.Model.NearByGrocery;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Utility.ParamEnum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter {
    public Context context;
    private HomeOfferViewPagerAdap homeOfferViewPagerAdap;
    private HomeViewPagerRestroAdap homeViewPagerRestroAdap;
    public static final int OFFER_VIEW = 3;
    public static final int RESTRO_VIEW = 0;
    public static final int GROCERY_VIEW = 1;
    public static final int TOP_RATED_RESTRO_VIEW = 2;
    private onClickEvents onClickEvents;
    private List<RestaurantResponse> nearByRestroList=null;
    private List<RestaurantResponse> offersList=null;
    private List<RestaurantResponse>topRatedRestroList=null;
    private List<NearByGrocery> nearByGroceryList=null;


    public HomeAdapter(Context context, List<RestaurantResponse> noffers, List<RestaurantResponse> nearByRestroList, List<RestaurantResponse> topRatedRestroList, List<NearByGrocery> nearByGroceryList, onClickEvents onClickEvents) {
        this.context = context;
        this.nearByRestroList = nearByRestroList;
        this.topRatedRestroList = topRatedRestroList;
        this.offersList = noffers;
        this.onClickEvents =onClickEvents;
        this.nearByGroceryList=nearByGroceryList;
        homeOfferViewPagerAdap = new HomeOfferViewPagerAdap(context,offersList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == OFFER_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_offer_home_item, parent, false);
            return new OfferViewHolder(view);
        } else if(viewType == RESTRO_VIEW)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_home_restaurant_adapter, parent, false);
            return new RestroListViewHolder(view);
        }else if(viewType == GROCERY_VIEW)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_home_restaurant_adapter, parent, false);
            return new RestroListViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_home_restaurant_adapter, parent, false);
            return new RestroListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){

            case OFFER_VIEW:
            if(offersList.size()>0){
            homeOfferViewPagerAdap.updateList(offersList);
            ((OfferViewHolder) holder).vpOffer.setAdapter(homeOfferViewPagerAdap);
            ((OfferViewHolder) holder).tvAvailableOffers.setText("Available Offers");
            }
            else {
                ((OfferViewHolder) holder).tvAvailableOffers.setVisibility(View.GONE);
                ((OfferViewHolder) holder).vpOffer.setVisibility(View.GONE);
                ((OfferViewHolder) holder).tvViewMore.setVisibility(View.GONE);
                ((OfferViewHolder) holder).view.setVisibility(View.GONE);
            }
            break;


            case RESTRO_VIEW:
            if(position==0) {
                if (nearByRestroList.size() > 0) {
                    homeViewPagerRestroAdap = new HomeViewPagerRestroAdap(context, nearByRestroList, onClickEvents, 0);
                    ((RestroListViewHolder) holder).vpHomeRestaurants.setAdapter(homeViewPagerRestroAdap);
                    ((RestroListViewHolder) holder).tvRestaurant.setText("Restaurants");
                } else {
                    ((RestroListViewHolder) holder).tvViewMore.setVisibility(View.GONE);
                    ((RestroListViewHolder) holder).vpHomeRestaurants.setVisibility(View.GONE);
                    ((RestroListViewHolder) holder).tvRestaurant.setVisibility(View.GONE);
                    ((RestroListViewHolder) holder).view.setVisibility(View.GONE);
                }
            }else {
                ((RestroListViewHolder) holder).tvViewMore.setVisibility(View.GONE);
                ((RestroListViewHolder) holder).vpHomeRestaurants.setVisibility(View.GONE);
                ((RestroListViewHolder) holder).tvRestaurant.setVisibility(View.GONE);
                ((RestroListViewHolder) holder).view.setVisibility(View.GONE);
            }
            break;

            case GROCERY_VIEW:
            if(position==1) {
                if (nearByGroceryList.size() > 0) {
                    homeViewPagerRestroAdap = new HomeViewPagerRestroAdap(context, nearByGroceryList, 1, onClickEvents);
                    ((RestroListViewHolder) holder).vpHomeRestaurants.setAdapter(homeViewPagerRestroAdap);
                    ((RestroListViewHolder) holder).tvRestaurant.setText("Grocery Stores");
                } else {
                    ((RestroListViewHolder) holder).tvRestaurant.setVisibility(View.GONE);
                    ((RestroListViewHolder) holder).vpHomeRestaurants.setVisibility(View.GONE);
                    ((RestroListViewHolder) holder).tvViewMore.setVisibility(View.GONE);
                }
            }

            break;

            case TOP_RATED_RESTRO_VIEW:
                if(position==2) {
                    if (topRatedRestroList.size() > 0) {
                        homeViewPagerRestroAdap = new HomeViewPagerRestroAdap(context, topRatedRestroList, onClickEvents, 2);
                        ((RestroListViewHolder) holder).vpHomeRestaurants.setAdapter(homeViewPagerRestroAdap);
                        ((RestroListViewHolder) holder).tvRestaurant.setText("Top-Rated Restaurants & Groceries");
                    } else {
                        ((RestroListViewHolder) holder).tvRestaurant.setVisibility(View.GONE);
                        ((RestroListViewHolder) holder).vpHomeRestaurants.setVisibility(View.GONE);
                        ((RestroListViewHolder) holder).tvViewMore.setVisibility(View.GONE);
                    }
                }
            break;
        }

    }

    @Override
    public int getItemCount() {
        if(nearByRestroList != null && nearByRestroList.size() > 0 && topRatedRestroList.size() > 0 && topRatedRestroList != null && nearByGroceryList!=null && nearByGroceryList.size()>0 && offersList!=null && offersList.size()>0)
        {
            return 4;
        }else if(nearByRestroList!=null && nearByRestroList.size()>0)
        {
            return 4;
        }else if(nearByGroceryList!=null && nearByGroceryList.size()>0)
        {
            return 4;
        }
        else if(topRatedRestroList!=null && topRatedRestroList.size()>0)
        {
            return 4;
        }
        else if(offersList!=null && offersList.size()>0)
        {
            return 1;
        }else
        {
            return 0;
        }
    }

    public void updateList(List<RestaurantResponse> offers,List<RestaurantResponse> nearByRestroList,List<RestaurantResponse> topRatedRestroList,List<NearByGrocery> nearByGroceryList){
        this.nearByRestroList=nearByRestroList;
        this.topRatedRestroList=topRatedRestroList;
        this.offersList=offers;
        this.nearByGroceryList=nearByGroceryList;
        notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {
        if(getItemCount()-1==position && offersList.size()>0 && offersList!=null) return OFFER_VIEW;
        else if(getItemCount()-2==position && topRatedRestroList.size()>0 && topRatedRestroList!=null) return TOP_RATED_RESTRO_VIEW;
        else if(getItemCount()-3==position && nearByGroceryList.size()>0 && nearByGroceryList!=null) return GROCERY_VIEW;
        else return RESTRO_VIEW;
    }

    class OfferViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.vpOffer)
        ViewPager vpOffer;

        @BindView(R.id.tvAvailableOffers)
        TextView tvAvailableOffers;

        @BindView(R.id.tvViewMore)
        TextView tvViewMore;

        @BindView(R.id.view)
        View view;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            vpOffer.setClipToPadding(false);
            vpOffer.setPadding(30, 0, 80, 0);
            tvViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
                    Fragment myFragment = new OffersFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, myFragment).addToBackStack(null).commit();
                }
            });

        }
    }

    class RestroListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vpHomeRestaurants) ViewPager vpHomeRestaurants;
        @BindView(R.id.tvRestaurant) TextView tvRestaurant;
        @BindView(R.id.tvViewMore) TextView tvViewMore;
        @BindView(R.id.view) View view;

        public RestroListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            vpHomeRestaurants.setClipToPadding(false);
            vpHomeRestaurants.setPadding(30, 0, 80, 0);
            tvViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition()==2) {
                        Intent intent = new Intent(context, RestrauntListingAct.class);
                        intent.putExtra(ParamEnum.FROM.theValue(), ParamEnum.TOP_RATED_RESTRO.theValue());
                        intent.putExtra(ParamEnum.TITLE.theValue(), "Top-Rated Restaurants");
                        context.startActivity(intent);

                    }else if (getAdapterPosition()==0){
                        Intent intent = new Intent(context, RestrauntListingAct.class);
                        intent.putExtra(ParamEnum.FROM.theValue(), ParamEnum.NEAR_BY_RESTRO.theValue());
                        intent.putExtra(ParamEnum.TITLE.theValue(), "Near-by Restaurants");
                        context.startActivity(intent);
                    }else
                    {
                        Intent intent = new Intent(context, RestrauntListingAct.class);
                        intent.putExtra(ParamEnum.FROM.theValue(), ParamEnum.NEAR_BY_GROCERY_STORE.theValue());
                        intent.putExtra(ParamEnum.TITLE.theValue(), "Near-by Grocery Stores");
                        context.startActivity(intent);

                    }
                }
            });

        }
    }

}
