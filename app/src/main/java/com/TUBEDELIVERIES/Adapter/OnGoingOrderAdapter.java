package com.TUBEDELIVERIES.Adapter;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.badoualy.stepperindicator.StepperIndicator;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnGoingOrderAdapter extends RecyclerView.Adapter<OnGoingOrderAdapter.OnGoingViewHolder> {
    private Context context;
    List<ResponseBean> onGoingList;
   private OnCancelOrderClick listenr;
    private Double exchangeRate;
    private String currencySymbol;
    List<String> statusList;

    public OnGoingOrderAdapter(Context context, List<ResponseBean> onGoingList,OnCancelOrderClick listenr) {
        this.context = context;
        this.onGoingList = onGoingList;
        this.listenr = listenr;
        this.exchangeRate=Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.EXCHANGE_RATE));
        this.currencySymbol=SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.CURRENCY_SYMBOL);
        this.statusList=new ArrayList<>();
        statusList.add(0,"Please select status");
        statusList.add(1,"Delivery has arrived");
    }

    @NonNull
    @Override
    public OnGoingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_ongoing_items,viewGroup,false);
        return new OnGoingViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull OnGoingViewHolder onGoingViewHolder, int i) {
        if(i==0)
        {
            onGoingViewHolder.ivDropDownOrder.setImageResource(R.drawable.drop_down_ic);
            onGoingViewHolder.constraintLayout.setBackground(context.getDrawable(R.drawable.bg_expand));
            onGoingViewHolder.cl_myOrders.setVisibility(View.VISIBLE);

        }
        onGoingViewHolder.tvRestaurantName.setText(onGoingList.get(i).getRestaurant_name());
        onGoingViewHolder.tvRestaurantNameBelow.setText(onGoingList.get(i).getRestaurant_name());
        onGoingViewHolder.tvOrderId.setText(context.getResources().getString(R.string.order_id)+onGoingList.get(i).getOrder_number());
        onGoingViewHolder.tvDateAndTime.setText(onGoingList.get(i).getOrderDate());
        Log.e("address", ""+onGoingList.get(i).getAddress());
        String filterAddress="";
        if(onGoingList.get(i).getAddress()!=null)
        {
            onGoingViewHolder.tvAddressMyOrders.setText(onGoingList.get(i).getAddress().replace("null", "").replace(",", ""));
        }else{
            onGoingViewHolder.tvAddressMyOrders.setText(onGoingList.get(i).getCartAddress().replace("null", "").replace(",", ""));
        }
        onGoingViewHolder.tvPaymentModeType.setText(onGoingList.get(i).getPayment_type());
        onGoingViewHolder.tvEatType.setText(MessageFormat.format("{0}", CommonUtilities.getPaymentType(String.valueOf(onGoingList.get(i).getEatOption()))));
        onGoingViewHolder.tvExpectedTime.setText(onGoingList.get(i).getExpected_delivery_time());
        onGoingViewHolder.tvDeliveryPerson.setText(onGoingList.get(i).getDelivery_peroson());
        onGoingViewHolder.tvPhoneNumber.setText(onGoingList.get(i).getPhone_number());
        if (onGoingList.get(i).getCancel_status().equalsIgnoreCase("1")) {
            // startTimer( onGoingViewHolder,((Long.parseLong(onGoingList.get(i).getCancel_time())*1000)));
            onGoingViewHolder.tvCancelOrder.setVisibility(View.VISIBLE);
            onGoingViewHolder.tvTimeOrder.setVisibility(View.VISIBLE);
            try {
                CountDownTimer timer = new CountDownTimer((Long.parseLong(onGoingList.get(i).getCancel_time())*1000), 1000) {
                    @Override
                    public void onFinish() {
                        onGoingViewHolder.tvTimeOrder.setVisibility(View.GONE);
                        onGoingViewHolder.tvCancelOrder.setVisibility(View.GONE);
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {
                        int hrs = (int) TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                        int min = (int) TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                        int sec = (int) TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                        onGoingViewHolder.tvTimeOrder.setText(min +":"+sec);
                    }
                }.start();

            } catch (Exception e) {
                System.out.println("NumberFormatException: " + e.getMessage());
            }
        }

        else {
            onGoingViewHolder.tvCancelOrder.setVisibility(View.GONE);
            onGoingViewHolder.tvTimeOrder.setVisibility(View.GONE);

        }
        if(onGoingList.get(i).getExpected_delivery_time().isEmpty()&&onGoingList.get(i).getDelivery_peroson().isEmpty()) {
            onGoingViewHolder.clDeliveryDetails.setVisibility(View.VISIBLE);
        }
        if(onGoingList.get(i).getCurrency().equalsIgnoreCase("USD"))
        {
            onGoingViewHolder.tvTotalPriceMyOrder.setText("Total Price: "+currencySymbol+" " + CommonUtilities.roundOff(""+Double.parseDouble(onGoingList.get(i).getTotalPrice())*exchangeRate));

        }else
        {
            onGoingViewHolder.tvTotalPriceMyOrder.setText("Total Price: "+currencySymbol+" " + CommonUtilities.roundOff(""+Double.parseDouble(onGoingList.get(i).getTotalPrice())));

        }
        if(onGoingList.get(i).getRestaurant_image()!=null && !onGoingList.get(i).getRestaurant_image().isEmpty()) {
            Glide.with(context).load(onGoingList.get(i).getRestaurant_image()).into(onGoingViewHolder.ivOrderLogo);
        }
        orderStatus(onGoingViewHolder, onGoingList.get(i).getDelivery_status());
        onGoingViewHolder.edStatus.setText(onGoingList.get(i).getDelivery_status());
        onGoingViewHolder.recyclerMyOrders.setLayoutManager(new LinearLayoutManager(context));
        onGoingViewHolder.recyclerMyOrders.setAdapter(new MyOrderAdapter(context,onGoingList.get(i).getOrderMenu(),onGoingList.get(i).getCurrency()));

    }


    @Override
    public int getItemCount() {
        return onGoingList!=null?onGoingList.size():0;
    }

    public class OnGoingViewHolder extends RecyclerView.ViewHolder {

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

        @BindView(R.id.tvRestaurantNameBelow)
        TextView tvRestaurantNameBelow;

        @BindView(R.id.tvAddressMyOrders)
        TextView tvAddressMyOrders;

        @BindView(R.id.tvCancelOrder)
        TextView tvCancelOrder;

        @BindView(R.id.tvPaymentModeType)
        TextView tvPaymentModeType;

        @BindView(R.id.tvExpectedTime)
        TextView tvExpectedTime;

        @BindView(R.id.tvDeliveryPerson)
        TextView tvDeliveryPerson;

        @BindView(R.id.tvPhoneNumber)
        TextView tvPhoneNumber;

        @BindView(R.id.clDeliveryDetails)
        ConstraintLayout clDeliveryDetails;

        @BindView(R.id.stepCounter)
        StepperIndicator stepCounter;

        @BindView(R.id.constraintLayout)
        ConstraintLayout constraintLayout;

        @BindView(R.id.mainCl)
        ConstraintLayout mainCl;

        @BindView(R.id.tvEatType)
        TextView tvEatType;
        @BindView(R.id.clChangeStatus)
        ConstraintLayout clChangeStatus;

        @BindView(R.id.edStatus)
        TextInputEditText edStatus;


        public OnGoingViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick({R.id.mainCl,R.id.tvCancelOrder,R.id.tvAddressMyOrders,R.id.edStatus})
        public void onClick(View view){
            switch (view.getId()){
                case R.id.mainCl:
                if(cl_myOrders.getVisibility()==View.GONE){
                    ivDropDownOrder.setImageResource(R.drawable.drop_down_ic);
                    constraintLayout.setBackground(context.getDrawable(R.drawable.bg_expand));
                    cl_myOrders.setVisibility(View.VISIBLE);
                }else {
                    ivDropDownOrder.setImageResource(R.drawable.drop_down_icon);
                    constraintLayout.setBackground(context.getDrawable(R.drawable.bg_collapse));
                    cl_myOrders.setVisibility(View.GONE);
                }
                break;

                case R.id.tvCancelOrder:
                listenr.onCLick(getAdapterPosition(),onGoingList.get(getAdapterPosition()));
                break;

                case R.id.tvAddressMyOrders:
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("www.google.com")
                        .appendPath("maps")
                        .appendPath("dir")
                        .appendPath("")
                        .appendQueryParameter("api", "1")
                        .appendQueryParameter("destination", Double.parseDouble(onGoingList.get(getAdapterPosition()).getLatitude()) + "," + Double.parseDouble(onGoingList.get(getAdapterPosition()).getLongitude()));
                String url = builder.build().toString();
                Log.d("Directions", url);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
                break;
            }
        }
    }

    private void orderStatus(OnGoingViewHolder viewHolder,String status){
        CharSequence[] labelArray=new String[5];
        labelArray[0] = "Confirmed";
        labelArray[1] = "Item getting Ready";
        labelArray[2] = "Out for Delivery";
        labelArray[3] = "Delivery has Arrived";
        labelArray[4] = "Delivered";
        viewHolder.stepCounter.setLabels(labelArray);
        switch (status) {
            case "Confirmed": viewHolder.stepCounter.setCurrentStep(1); break;
            case "Item getting ready": viewHolder.stepCounter.setCurrentStep(2); break;
            case "Out for Delivery": viewHolder.stepCounter.setCurrentStep(3); break;
            case "Delivery has Arrived": viewHolder.stepCounter.setCurrentStep(4); break;
            case "Delivered": viewHolder.stepCounter.setCurrentStep(5); break;
        }
    }



    public interface OnCancelOrderClick{
        void onCLick(int pos,ResponseBean responseBean);
        void confirmOrder(int pos,ResponseBean responseBean);
    }

    private void startTimer(OnGoingViewHolder holder,long duration){

        new CountDownTimer(duration,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int hrs = (int) TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                int min = (int) TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                int sec = (int) TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;

//                tvCOuntDown.setText("TIME : " + String.format("%02d", minutes)
//                        + ":" + String.format("%02d", seconds));


                //tvCOuntDown.setText("TIME : "+hrs+ ":"+min +":"+sec);
                holder.tvTimeOrder.setText(min +":"+sec);

            }
            @Override
            public void onFinish() {

                holder.tvTimeOrder.setVisibility(View.GONE);
                holder.tvCancelOrder.setVisibility(View.GONE);

            }
        }.start();
    }


}
