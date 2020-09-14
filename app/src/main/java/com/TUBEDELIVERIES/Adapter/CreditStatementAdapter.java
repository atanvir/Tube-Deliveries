package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Utility.ParamEnum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreditStatementAdapter extends RecyclerView.Adapter<CreditStatementAdapter.CreditViewHolder> {
    private Context context;

    private List<RestaurantResponse> topUpHistoryList = null;


    public CreditStatementAdapter(Context context, List<RestaurantResponse> topUpHistoryList) {
        this.context = context;
        this.topUpHistoryList = topUpHistoryList;
    }

    @NonNull
    @Override
    public CreditViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_credit_statement_items, viewGroup, false);
        return new CreditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditViewHolder holder, int i) {

        holder.tvDecription.setText(topUpHistoryList.get(i).getDescription());
        holder.tvDate.setText(topUpHistoryList.get(i).getCreated_at());

        if (topUpHistoryList.get(i).getType().equals(ParamEnum.ONE.theValue())) {

            holder.tvPrice.setTextColor(context.getResources().getColor(R.color.colorGreen));
            holder.tvPrice.setText("+" + context.getString(R.string.usd) + topUpHistoryList.get(i).getPrice());

        } else {

            holder.tvPrice.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.tvPrice.setText("-" + context.getString(R.string.aed) + topUpHistoryList.get(i).getPrice());


        }
    }

    @Override
    public int getItemCount() {
        return topUpHistoryList != null ? topUpHistoryList.size() : 0;
    }

    public class CreditViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tvDecription)
        TextView tvDecription;

        @BindView(R.id.tvDate)
        TextView tvDate;

        @BindView(R.id.tvPrice)
        TextView tvPrice;

        public CreditViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void upDateList(List<RestaurantResponse> topUpOfferList) {

        this.topUpHistoryList = topUpOfferList;
        notifyDataSetChanged();

    }

}
