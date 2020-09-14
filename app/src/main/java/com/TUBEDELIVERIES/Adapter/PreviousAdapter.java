package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.TUBEDELIVERIES.Activity.ShareReviewActivity;
import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.bumptech.glide.Glide;

import java.text.MessageFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PreviousAdapter extends RecyclerView.Adapter<PreviousAdapter.PreviousViewHolder> {
    private Context context;
    private List<ResponseBean> list;
    private OnReaorder listener;
    private Double exchangeRate;
    private String currencySymbol;

    public PreviousAdapter(Context context, List<ResponseBean> list, OnReaorder listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.exchangeRate=Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.EXCHANGE_RATE));
        this.currencySymbol=SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.CURRENCY_SYMBOL);
    }

    @NonNull
    @Override
    public PreviousViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_previous_adapter, viewGroup, false);
        return new PreviousViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousViewHolder previousViewHolder, int i) {

        if (list.get(i).getRestaurant_image() != null && !list.get(i).getRestaurant_image().isEmpty())
            Glide.with(context).load(list.get(i).getRestaurant_image()).into(previousViewHolder.ivPreviousOrder);
            previousViewHolder.tvPreviousOrderName.setText(list.get(i).getRestaurent_name());
            previousViewHolder.tvOrderIdPrevious.setText("Order Id : " + list.get(i).getOrder_number());
        if(list.get(i).getCurrency().equalsIgnoreCase("USD"))
        {
            previousViewHolder.tvTotalPriceMyOrder.setText("Total Price: "+currencySymbol+" " + CommonUtilities.roundOff(""+Double.parseDouble(list.get(i).getTotalPrice())*exchangeRate));

        }else
        {
            previousViewHolder.tvTotalPriceMyOrder.setText("Total Price: "+currencySymbol+" " + CommonUtilities.roundOff(""+Double.parseDouble(list.get(i).getTotalPrice())));

        }
            previousViewHolder.rvPreviousOrder.setLayoutManager(new LinearLayoutManager(context));
            previousViewHolder.rvPreviousOrder.setAdapter(new MyOrderAdapter(context, list.get(i).getOrderMenu(),list.get(i).getCurrency()));
            previousViewHolder.tvAddressMyOrders.setText(list.get(i).getAddress());
            previousViewHolder.tvPaymentModeType.setText(list.get(i).getPayment_type());
            previousViewHolder.tvRestaurantNameBelow.setText(list.get(i).getRestaurant_name());
            previousViewHolder.tvDeliverytime.setText(list.get(i).getExpected_delivery_time());
            previousViewHolder.tvEatType.setText(MessageFormat.format("{0}", CommonUtilities.getPaymentType(String.valueOf(list.get(i).getEatOption()))));

            String address = list.get(i).getAddress();
            String filterAddress = address.replaceAll("null","");
    }

    @Override
    public int getItemCount() {

        return list != null ? list.size() : 0;
    }

    public class PreviousViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvRateOrder)
        TextView tvRateOrder;
        @BindView(R.id.tvReorderPrevious)
        TextView tvReorderPrevious;

        @BindView(R.id.tvTotalPriceMyOrder)
        TextView tvTotalPriceMyOrder;

        @BindView(R.id.tvPreviousOrderName)
        TextView tvPreviousOrderName;

        @BindView(R.id.tvOrderIdPrevious)
        TextView tvOrderIdPrevious;



        @BindView(R.id.ivPreviousDown)
        ImageView ivPreviousDown;

        @BindView(R.id.ivPreviousOrder)
        ImageView ivPreviousOrder;

        @BindView(R.id.cl_previous)
        ConstraintLayout cl_previous;

        @BindView(R.id.rvPreviousOrder)
        RecyclerView rvPreviousOrder;

        @BindView(R.id.cl_reorderPrevious)
        ConstraintLayout cl_reorderPrevious;

        @BindView(R.id.tvAddressMyOrders)
        TextView tvAddressMyOrders;

        @BindView(R.id.tvEatType)
        TextView tvEatType;

        @BindView(R.id.tvPaymentModeType)
        TextView tvPaymentModeType;

        @BindView(R.id.tvRestaurantNameBelow)
        TextView tvRestaurantNameBelow;

        @BindView(R.id.tvDeliverytime)
        TextView tvDeliverytime;

        @BindView(R.id.mainCl)
        ConstraintLayout mainCl;



        public PreviousViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvRateOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShareReviewActivity.class);
                    intent.putExtra(ParamEnum.RESTRO_ID.theValue(), list.get(getAdapterPosition()).getRestaurent_id());
                    intent.putExtra(ParamEnum.ORDER_ID.theValue(), list.get(getAdapterPosition()).getOrder_id());
                    context.startActivity(intent);
                }
            });

            tvAddressMyOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https")
                            .authority("www.google.com")
                            .appendPath("maps")
                            .appendPath("dir")
                            .appendPath("")
                            .appendQueryParameter("api", "1")
                            .appendQueryParameter("destination", Double.parseDouble(list.get(getAdapterPosition()).getLatitude()) + "," + Double.parseDouble(list.get(getAdapterPosition()).getLongitude()));
                    String url = builder.build().toString();
                    Log.d("Directions", url);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                }
            });


            cl_reorderPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onCLick(getAdapterPosition(), list.get(getAdapterPosition()));


                }
            });

            mainCl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cl_previous.getVisibility() == View.GONE) {
                        ivPreviousDown.setImageResource(R.drawable.drop_down_ic);
                        cl_previous.setVisibility(View.VISIBLE);
                    } else {
                        ivPreviousDown.setImageResource(R.drawable.drop_down_icon);
                        cl_previous.setVisibility(View.GONE);
                    }
                }
            });
        }
    }


    public interface OnReaorder {

        void onCLick(int pos, ResponseBean responseBean);

    }

}
