package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.TUBEDELIVERIES.Model.UpcomingOrder;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.bumptech.glide.Glide;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnUpcomingAdapter  extends RecyclerView.Adapter<OnUpcomingAdapter.OnUpcomingViewHolder> {
    private Context context;
    List<UpcomingOrder> upcomingOrderList;
    OnUpcomingAdapter.OnCancelOrderClick  listenr;
    private Double exchangeRate;
    private String currencySymbol;


    public OnUpcomingAdapter(Context context, List<UpcomingOrder> upcomingOrderList,OnUpcomingAdapter.OnCancelOrderClick listenr) {
        this.context = context;
        this.upcomingOrderList = upcomingOrderList;
        this.listenr=listenr;
        this.exchangeRate=Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.EXCHANGE_RATE));
        this.currencySymbol=SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.CURRENCY_SYMBOL);
    }


    @NonNull
    @Override
    public OnUpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_upcoming_item, viewGroup, false);
        return new OnUpcomingAdapter.OnUpcomingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnUpcomingAdapter.OnUpcomingViewHolder holder, int i) {
        upcomingOrderList.get(i).setEatOption(1);

        holder.tvRestaurantName.setText(upcomingOrderList.get(i).getRestaurantName());
        holder.tvOrderId.setText(context.getResources().getString(R.string.order_id) + upcomingOrderList.get(i).getOrderNumber());
        holder.tvDateAndTime.setText(upcomingOrderList.get(i).getOrderDate());


        if(upcomingOrderList.get(i).getAddress()!=null)
        {
            holder.tvAddressMyOrders.setText(upcomingOrderList.get(i).getAddress().replace("null", "").replace(",", ""));
        }else{
            holder.tvAddressMyOrders.setText(upcomingOrderList.get(i).getCartAddress().replace("null", "").replace(",", ""));
        }


        holder.tvPaymentModeType.setText(upcomingOrderList.get(i).getPaymentType());
        holder.tvEatType.setText(MessageFormat.format("{0}", CommonUtilities.getPaymentType(String.valueOf(upcomingOrderList.get(i).getEatOption()))));

        if(String.valueOf(upcomingOrderList.get(i).getEatOption()).equalsIgnoreCase("0"))
        {
            holder.pickUpLayout.setVisibility(View.VISIBLE);
            holder.eatRestroLayout.setVisibility(View.GONE);
            holder.homeDeliveryLayout.setVisibility(View.GONE);
            holder.tvpickupDate.setText(upcomingOrderList.get(i).getPickupDate());
            holder.tvPickUpTime.setText(upcomingOrderList.get(i).getPickupTime());
            holder.pickUpRestroName.setText(upcomingOrderList.get(i).getRestaurantName());
            holder.tvpickStatus.setText(upcomingOrderList.get(i).getDeliveryStatus());

        }else if(String.valueOf(upcomingOrderList.get(i).getEatOption()).equalsIgnoreCase("1"))
        {
            holder.pickUpLayout.setVisibility(View.GONE);
            holder.eatRestroLayout.setVisibility(View.GONE);
            holder.homeDeliveryLayout.setVisibility(View.VISIBLE);
            holder.tvdeliveryAdd.setText(upcomingOrderList.get(i).getCartAddress());
            holder.homeRestroNameText.setText(upcomingOrderList.get(i).getRestaurantName());
            holder.tvStatus.setText(upcomingOrderList.get(i).getDeliveryStatus());

        } else if(String.valueOf(upcomingOrderList.get(i).getEatOption()).equalsIgnoreCase("2"))
        {
            holder.pickUpLayout.setVisibility(View.GONE);
            holder.eatRestroLayout.setVisibility(View.VISIBLE);
            holder.homeDeliveryLayout.setVisibility(View.GONE);
            holder.tvDate.setText(upcomingOrderList.get(i).getVistDate());
            holder.tvTime.setText(upcomingOrderList.get(i).getVistTime());
            holder.peopleText.setText(upcomingOrderList.get(i).getNoOfPeople());
            holder.tvStatus2.setText(upcomingOrderList.get(i).getDeliveryStatus());
            holder.eatRestroName.setText(upcomingOrderList.get(i).getRestaurantName());
            holder.tvStatus.setText(upcomingOrderList.get(i).getDeliveryStatus());
        }



        if (String.valueOf(upcomingOrderList.get(i).getCancelStatus()).equalsIgnoreCase("1")) {
            holder.tvCancelOrder.setVisibility(View.VISIBLE);
            holder.tvTimeOrder.setVisibility(View.VISIBLE);


            try {

                CountDownTimer timer = new CountDownTimer((Long.parseLong(""+upcomingOrderList.get(i).getCancelTime()) * 1000), 1000) {

                    @Override
                    public void onFinish() {
                        holder.tvTimeOrder.setVisibility(View.GONE);
                        holder.tvCancelOrder.setVisibility(View.GONE);
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {
                        int hrs = (int) TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                        int min = (int) TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                        int sec = (int) TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                        holder.tvTimeOrder.setText(min + ":" + sec);


                    }
                }.start();

            } catch (Exception e) {

                System.out.println("NumberFormatException: " + e.getMessage());
            }
        } else {
            holder.tvCancelOrder.setVisibility(View.GONE);
            holder.tvTimeOrder.setVisibility(View.GONE);

        }


        if (upcomingOrderList.get(i).getExpectedDeliveryTime().isEmpty() && upcomingOrderList.get(i).getDeliveryPeroson().isEmpty())
            holder.clDeliveryDetails.setVisibility(View.VISIBLE);

        if(upcomingOrderList.get(i).getCurrency().equalsIgnoreCase("USD"))
        {
            holder.tvTotalPriceMyOrder.setText("Total Price: "+currencySymbol+" " + CommonUtilities.roundOff(""+upcomingOrderList.get(i).getTotalPrice()*exchangeRate));

        }else
        {
            holder.tvTotalPriceMyOrder.setText("Total Price: "+currencySymbol+" " + CommonUtilities.roundOff(""+upcomingOrderList.get(i).getTotalPrice()));

        }


        if (upcomingOrderList.get(i).getRestaurantImage() != null && !upcomingOrderList.get(i).getRestaurantImage().isEmpty())
            Glide.with(context).load(upcomingOrderList.get(i).getRestaurantImage()).into(holder.ivOrderLogo);

        holder.recyclerMyOrders.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerMyOrders.setAdapter(new UpcomingOrderMenuAdapter(context, upcomingOrderList.get(i).getOrderMenu(),upcomingOrderList.get(i).getCurrency()));
        holder.cl_myOrders.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return upcomingOrderList != null ? upcomingOrderList.size() : 0;
    }

    public class OnUpcomingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recyclerMyOrders)
        RecyclerView recyclerMyOrders;

        @BindView(R.id.cl_myOrders)
        ConstraintLayout cl_myOrders;

        @BindView(R.id.ivDropDownOrder)
        ImageView ivDropDownOrder;

        @BindView(R.id.ivOrderLogo)
        ImageView ivOrderLogo;

        @BindView(R.id.tvRestaurantName)
        TextView tvRestaurantName;

        @BindView(R.id.tvTotalPriceMyOrder)
        TextView tvTotalPriceMyOrder;

        @BindView(R.id.tvTimeOrder)
        TextView tvTimeOrder;

        @BindView(R.id.tvOrderId)
        TextView tvOrderId;

        @BindView(R.id.tvDateAndTime)
        TextView tvDateAndTime;


        @BindView(R.id.tvAddressMyOrders)
        TextView tvAddressMyOrders;

        @BindView(R.id.tvCancelOrder)
        TextView tvCancelOrder;


        @BindView(R.id.clDeliveryDetails)
        ConstraintLayout clDeliveryDetails;

        @BindView(R.id.constraintLayout)
        ConstraintLayout constraintLayout;

        @BindView(R.id.tvEatType)
        TextView tvEatType;


        @BindView(R.id.tvPaymentModeType)
        TextView tvPaymentModeType;

        @BindView(R.id.mainCl)
        ConstraintLayout mainCl;

        @BindView(R.id.tvStatus2)
        TextView tvStatus2;



        View homeDeliveryLayout,eatRestroLayout,pickUpLayout;

        @BindView(R.id.tvStatus) TextView tvStatus;


        //pickUpLayout
        @BindView(R.id.tvPickUpTime) TextView tvPickUpTime;
        @BindView(R.id.tvpickupDate) TextView tvpickupDate;
        @BindView(R.id.pickUpRestroName) TextView pickUpRestroName;
        @BindView(R.id.tvpickStatus) TextView tvpickStatus;


        //eatRestroLayout
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.tvDate) TextView tvDate;
        @BindView(R.id.peopleText) TextView peopleText;
        @BindView(R.id.eatRestroName) TextView eatRestroName;

        //homeDeliveryLayout
        @BindView(R.id.tvdeliveryAdd) TextView tvdeliveryAdd;
        @BindView(R.id.homeRestroNameText) TextView homeRestroNameText;















        public OnUpcomingViewHolder(@NonNull View itemView) {
            super(itemView);
            homeDeliveryLayout=itemView.findViewById(R.id.homeDeliveryLayout);
            eatRestroLayout=itemView.findViewById(R.id.eatRestroLayout);
            pickUpLayout=itemView.findViewById(R.id.pickUpLayout);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.mainCl, R.id.tvCancelOrder,R.id.tvAddressMyOrders})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.mainCl:
                    if (cl_myOrders.getVisibility() == View.GONE) {
                        ivDropDownOrder.setImageResource(R.drawable.drop_down_ic);
                        constraintLayout.setBackground(context.getDrawable(R.drawable.bg_expand));
                        cl_myOrders.setVisibility(View.VISIBLE);
                    } else {
                        ivDropDownOrder.setImageResource(R.drawable.drop_down_icon);
                        constraintLayout.setBackground(context.getDrawable(R.drawable.bg_collapse));
                        cl_myOrders.setVisibility(View.GONE);
                    }
                    break;

                case R.id.tvCancelOrder:
                    listenr.onCLick(getAdapterPosition(), upcomingOrderList.get(getAdapterPosition()));
                    break;

                case R.id.tvAddressMyOrders:
                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https")
                            .authority("www.google.com")
                            .appendPath("maps")
                            .appendPath("dir")
                            .appendPath("")
                            .appendQueryParameter("api", "1")
                            .appendQueryParameter("destination", Double.parseDouble(upcomingOrderList.get(getAdapterPosition()).getLatitude()) + "," + Double.parseDouble(upcomingOrderList.get(getAdapterPosition()).getLongitude()));
                    String url = builder.build().toString();
                    Log.d("Directions", url);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                    break;
            }
        }
    }

    public interface OnCancelOrderClick{

        void onCLick(int pos,UpcomingOrder list);

    }




}
