package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopUpOfferAdapter extends RecyclerView.Adapter<TopUpOfferAdapter.TopUpViewHolder> {
    private Context context;
    private List<RestaurantResponse> topUpOfferList=null;

    private OnProceed listener;

    public TopUpOfferAdapter(Context context,List<RestaurantResponse> topUpOfferList, OnProceed listener) {

        this.context = context;
        this.topUpOfferList = topUpOfferList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopUpViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(context).inflate(R.layout.layout_top_offers_items,viewGroup,false);
        return new TopUpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopUpViewHolder holder, int i) {

        holder.Topup_checkbox.setText("Dammy Daat");
//
//        if(topUpOfferList.get(i).isCheck())
//          holder.Topup_checkbox.setChecked(true);
//        else
//            holder.Topup_checkbox.setChecked(false);
//

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class TopUpViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.Topup_checkbox)
        CheckBox Topup_checkbox;

        public TopUpViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            Topup_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                            listener.onProceedClick(topUpOfferList.get(getAdapterPosition()));

                        for(int i=0;i<topUpOfferList.size();i++){
                         topUpOfferList.get(i).setCheck(false);
                         }
                          topUpOfferList.get(getAdapterPosition()).setCheck(true);
                          notifyDataSetChanged();
                    }else {

                         listener.onProceedUnClick(topUpOfferList.get(getAdapterPosition()));
                    }


                }
            });

        }
    }

    public void upDateList(List<RestaurantResponse> topUpOfferList){

        this.topUpOfferList=topUpOfferList;
        notifyDataSetChanged();

    }

    public interface  OnProceed{

        void onProceedClick(RestaurantResponse response);
        void onProceedUnClick(RestaurantResponse response);

    }
}
