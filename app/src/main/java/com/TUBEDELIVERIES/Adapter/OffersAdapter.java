package com.TUBEDELIVERIES.Adapter;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.TUBEDELIVERIES.Model.OffereDetail;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {
    private  Context context;

    private List<? extends Object>offerList;
    private onOfferClick listener;


    public OffersAdapter(Context context,List<? extends Object> offerList,onOfferClick listener) {
        this.context=context;
        this.offerList=offerList;
        this.listener=listener;
    }



    @NonNull
    @Override
    public OffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_offers,viewGroup,false);
        return new OffersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersAdapter.ViewHolder holder, int i) {



        if (offerList.get(i).getClass().getSimpleName().equalsIgnoreCase(RestaurantResponse.class.getSimpleName())) {

            if (!((RestaurantResponse) offerList.get(i)).getThumbnail().equalsIgnoreCase(""))


                CommonUtilities.setImage(context, holder.progressbar, ((RestaurantResponse) offerList.get(i)).getThumbnail(), holder.ivDiscount);
            else
                Glide.with(context).load(R.drawable.debit_card).override(65, 65).into(holder.ivDiscount);


            if (((RestaurantResponse) offerList.get(i)).getType().equalsIgnoreCase(ParamEnum.ONE.theValue()))
                holder.tvOffer.setText("Get " + ((RestaurantResponse) offerList.get(i)).getDiscount() + "% off on all orders");
            else
                holder.tvOffer.setText(((RestaurantResponse) offerList.get(i)).getNote());


            holder.tvValidity.setText("This offer is valid till " + ((RestaurantResponse) offerList.get(i)).getValid_date());

            holder.tvRestroName.setText(((RestaurantResponse) offerList.get(i)).getRes_name());

        }
        else if(offerList.get(i).getClass().getSimpleName().equalsIgnoreCase(OffereDetail.class.getSimpleName()))
        {
            if (!((OffereDetail) offerList.get(i)).getThumbnail().equalsIgnoreCase(""))
                CommonUtilities.setImage(context, holder.progressbar, ((OffereDetail) offerList.get(i)).getThumbnail(), holder.ivDiscount);
            else
                Glide.with(context).load(R.drawable.debit_card).override(65, 65).into(holder.ivDiscount);

            if (String.valueOf(((OffereDetail) offerList.get(i)).getType()).equalsIgnoreCase(ParamEnum.ONE.theValue()))
                holder.tvOffer.setText("Get " + ((OffereDetail) offerList.get(i)).getDiscount() + "% off on all orders");
            else if(((OffereDetail) offerList.get(i)).getNote()!=null)
                holder.tvOffer.setText(((OffereDetail) offerList.get(i)).getNote());

            holder.tvValidity.setText("This offer is valid till " + ((OffereDetail) offerList.get(i)).getValidDate());
            holder.tvRestroName.setText(((OffereDetail) offerList.get(i)).getEn_name());

        }
    }


    //refresh list
    public void updateList(List<? extends Object> offerList){
        this.offerList=offerList;
        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return offerList!=null ? offerList.size():0;

    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivDiscount)
        ImageView ivDiscount;

        @BindView(R.id.tvOffer)
        TextView tvOffer;

        @BindView(R.id.tvValidity)
        TextView tvValidity;

        @BindView(R.id.tvRestroName)
        TextView tvRestroName;

        @BindView(R.id.progressbar)
        ProgressBar progressbar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(listener!=null)
                    listener.onClickOffer(offerList.get(getAdapterPosition()));

//                    context.startActivity(new Intent(context, RestaurantDetailsEntity.class));
                }
            });
        }
    }


    //for callback on offer CLick from horizontal view------>
    public interface onOfferClick<T>{

        //passing the whole response on click
        void onClickOffer(T response);
    }


}
