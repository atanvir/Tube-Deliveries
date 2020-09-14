package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.TUBEDELIVERIES.Model.OrderMenu;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpcomingOrderMenuAdapter extends RecyclerView.Adapter<UpcomingOrderMenuAdapter.MyOrderViewHolder> {

    private Context context;
    private List<OrderMenu> list;
    private Double exchangeRate;
    private String currencySymbol;
    private String currency;

    public UpcomingOrderMenuAdapter(Context context, List<OrderMenu > list,String currency) {
        this.context = context;
        this.list = list;
        this.exchangeRate=Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.EXCHANGE_RATE));
        this.currencySymbol=SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.CURRENCY_SYMBOL);
        this.currency=currency;
    }


    @NonNull
    @Override
    public UpcomingOrderMenuAdapter.MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_my_order_adapter,parent,false);
        return new UpcomingOrderMenuAdapter.MyOrderViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull UpcomingOrderMenuAdapter.MyOrderViewHolder holder, int i) {

        if (((OrderMenu) list.get(i)).getImage() != null && !((OrderMenu) list.get(i)).getImage().isEmpty())
            Glide.with(context).load(((OrderMenu) list.get(i)).getImage()).into(holder.ivOrderAdapter);

        holder.tvOrderNameAdapter.setText(((OrderMenu) list.get(i)).getName());
        Double price;
        if(currency.equalsIgnoreCase("USD"))
        {
            price=((OrderMenu) list.get(i)).getPrice()*exchangeRate;

        }else
        {
            price=((OrderMenu) list.get(i)).getPrice();
        }

        ;
        holder.tvCerditAdapter.setText(currencySymbol+" "+ CommonUtilities.roundOff(""+price));

        holder.tcOrderQuantPrice.setText(((OrderMenu) list.get(i)).getQuantity() + "x"+currency+" " + CommonUtilities.roundOff(""+price));


        if (String.valueOf(((OrderMenu) list.get(i)).getItemType()).equalsIgnoreCase("0")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ivVegOrNonVeg.setBackground(context.getResources().getDrawable(R.drawable.veg_symbol, null));
            } else {
                holder.ivVegOrNonVeg.setBackground(ContextCompat.getDrawable(context, R.drawable.veg_symbol));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ivVegOrNonVeg.setBackground(context.getResources().getDrawable(R.drawable.nonveg_symbol, null));
            } else {
                holder.ivVegOrNonVeg.setBackground(ContextCompat.getDrawable(context, R.drawable.nonveg_symbol));

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
