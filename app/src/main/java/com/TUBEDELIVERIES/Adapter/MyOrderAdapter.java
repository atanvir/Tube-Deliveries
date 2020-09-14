package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.TUBEDELIVERIES.Model.OrderMenu;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderViewHolder> {
    private Context context;
    private List<RestaurantResponse> list;
    private Double exchangeRate;
    private String currencySymbol;
    private String currency;

    public MyOrderAdapter(Context context, List<RestaurantResponse> list,String currency) {
        this.context = context;
        this.list = list;
        Log.e("size", ""+list.size());
        this.exchangeRate=Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.EXCHANGE_RATE));
        this.currencySymbol=SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.CURRENCY_SYMBOL);
        this.currency=currency;
    }

    @NonNull
    @Override
    public MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_my_order_adapter,viewGroup,false);
        return new MyOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderViewHolder myOrderViewHolder, int i) {

           if(list.get(i).getImage()!=null && !list.get(i).getImage().isEmpty())
            Glide.with(context).load(list.get(i).getImage()).into(myOrderViewHolder.ivOrderAdapter);

        Double price;
        if(currency.equalsIgnoreCase("USD"))
        {
            price=Double.parseDouble(list.get(i).getPrice())*exchangeRate;
        }else
        {
            price=Double.parseDouble(list.get(i).getPrice());
        }

           myOrderViewHolder.tvOrderNameAdapter.setText(list.get(i).getName());
           myOrderViewHolder.tvCerditAdapter.setText(currencySymbol+" "+ CommonUtilities.roundOff(""+price));
           myOrderViewHolder.tcOrderQuantPrice.setText(list.get(i).getQuantity()+"x"+currency+" "+CommonUtilities.roundOff(""+price));


        if (list.get(i).getItem_type().equalsIgnoreCase("0")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myOrderViewHolder.ivVegOrNonVeg.setBackground(context.getResources().getDrawable(R.drawable.veg_symbol, null));
            } else {
                myOrderViewHolder.ivVegOrNonVeg.setBackground(ContextCompat.getDrawable(context, R.drawable.veg_symbol));
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myOrderViewHolder.ivVegOrNonVeg.setBackground(context.getResources().getDrawable(R.drawable.nonveg_symbol, null));
            } else {
                myOrderViewHolder.ivVegOrNonVeg.setBackground(ContextCompat.getDrawable(context, R.drawable.nonveg_symbol));

            }
        }

    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyOrderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivOrderAdapter)
        ImageView ivOrderAdapter;

        @BindView(R.id.ivVegOrNonVeg)
        ImageView ivVegOrNonVeg;

        @BindView(R.id.tvOrderNameAdapter)
        TextView tvOrderNameAdapter;

        @BindView(R.id.tvCerditAdapter)
        TextView tvCerditAdapter;

        @BindView(R.id.tcOrderQuantPrice)
        TextView tcOrderQuantPrice;



        public MyOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
