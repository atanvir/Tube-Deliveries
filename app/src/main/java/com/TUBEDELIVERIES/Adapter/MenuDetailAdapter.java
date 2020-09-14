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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.RoomDatabase.Entity.OrderItemsEntity;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.amitshekhar.DebugDB;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MenuDetailAdapter extends RecyclerView.Adapter<MenuDetailAdapter.MenuDetailsViewholder> implements Filterable {
    private Context context;
    public OnCustomizeClickListener onCustomizeClickListener;


    private List<OrderItemsEntity> menuDetailsList;
    private List<OrderItemsEntity> filtermenuDetailsList;
    private Double exchangeRate;
    private String symbol;
    private String currency;

    public MenuDetailAdapter(Context context, OnCustomizeClickListener onCustomizeClickListener, List<OrderItemsEntity> menuDetailsList) {
        this.context = context;
        this.onCustomizeClickListener = onCustomizeClickListener;
        this.menuDetailsList = menuDetailsList;
        filtermenuDetailsList = menuDetailsList;
        this.currency= SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.CURRENCY);
        this.exchangeRate=Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.EXCHANGE_RATE));
        this.symbol=SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.CURRENCY_SYMBOL);
    }

    @NonNull
    @Override
    public MenuDetailsViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_add_cart_vertically, viewGroup, false);
        return new MenuDetailsViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuDetailsViewholder holder, int i) {

        if(menuDetailsList.get(i).getQuantity()>0)
        {
            holder.tvRemoveItem.setImageResource(R.drawable.sub_a);
        }
        else
        {
            holder.tvRemoveItem.setImageResource(R.drawable.sub);
        }


        holder.tvName.setText(filtermenuDetailsList.get(i).getName());
        if(currency.equalsIgnoreCase("USD"))
        {
            holder.tvPrice.setText(symbol+" " + CommonUtilities.roundOff(""+Double.parseDouble(filtermenuDetailsList.get(i).getPrice())*exchangeRate));
        }else {
            holder.tvPrice.setText(symbol + " " + filtermenuDetailsList.get(i).getPrice());
        }
        if (!filtermenuDetailsList.get(i).getImg().equalsIgnoreCase(""))
            CommonUtilities.setImage(context, holder.progressbar, filtermenuDetailsList.get(i).getImg(), holder.ivImage);
        else
            Glide.with(context).load(R.drawable.food_icon).override(65, 65).into(holder.ivImage);


        holder.tvOrderCount.setText(String.valueOf(filtermenuDetailsList.get(i).getQuantity()));


        if (filtermenuDetailsList.get(i).getItem_type().equalsIgnoreCase("0")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.IvDishType.setBackground(context.getResources().getDrawable(R.drawable.veg, null));
            } else {
                holder.IvDishType.setBackground(ContextCompat.getDrawable(context, R.drawable.veg));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.IvDishType.setBackground(context.getResources().getDrawable(R.drawable.nonveg, null));
            } else {
                holder.IvDishType.setBackground(ContextCompat.getDrawable(context, R.drawable.nonveg));

            }
        }

        if (filtermenuDetailsList.get(i).getCustomizes() != null && !filtermenuDetailsList.get(i).getCustomizes().isEmpty())
            holder.tvCustomiseMenu.setVisibility(View.GONE);
        else
            holder.tvCustomiseMenu.setVisibility(View.GONE);


        //if(menuDetailsList.get(i).getType().equalsIgnoreCase(ParamEnum.TOKEN_EXPIRE))

    }

    public void updateMenuDetailsList(List<OrderItemsEntity> menuDetailsList) {
        this.filtermenuDetailsList = menuDetailsList;
        notifyDataSetChanged();
    }


    public void removeItem(int pos) {

        menuDetailsList.remove(pos);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return filtermenuDetailsList != null ? filtermenuDetailsList.size() : 0;
    }

    @Override
    public Filter getFilter() {


        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                // String charString = charSequence.toString();
                final String charString = constraint.toString().toLowerCase().trim();
                if (charString.isEmpty()) {
                    filtermenuDetailsList = menuDetailsList;
                } else {

                    ArrayList<OrderItemsEntity> filteredList = new ArrayList<>();

                    for (OrderItemsEntity coffeeCategory : menuDetailsList) {
                        //for uper case or lower case use start with
                        if (coffeeCategory.getName().toLowerCase().startsWith(charString)) {

                            filteredList.add(coffeeCategory);
                        }
                    }
                    filtermenuDetailsList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filtermenuDetailsList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtermenuDetailsList = (ArrayList<OrderItemsEntity>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MenuDetailsViewholder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvPrice)
        TextView tvPrice;

        @BindView(R.id.tvCustomiseMenu)
        TextView tvCustomiseMenu;

        @BindView(R.id.tvAddItem)
        ImageView tvAddItem;

        @BindView(R.id.tvOrderCount)
        TextView tvOrderCount;

        @BindView(R.id.tvRemoveItem)
        ImageView tvRemoveItem;

        @BindView(R.id.IvDishType)
        ImageView IvDishType;

        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.progressbar)
        ProgressBar progressbar;

        public MenuDetailsViewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.tvCustomiseMenu, R.id.tvRemoveItem, R.id.tvAddItem})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvCustomiseMenu:
                    onCustomizeClickListener.onCustomizeClick(menuDetailsList.get(getAdapterPosition()), getAdapterPosition());
                    break;

                case R.id.tvRemoveItem:
                    onCustomizeClickListener.onRemoveItemClick(menuDetailsList.get(getAdapterPosition()), menuDetailsList.get(getAdapterPosition()).getQuantity() - 1, getAdapterPosition());
                    break;

                case R.id.tvAddItem:
                    onCustomizeClickListener.onAddItemClick(menuDetailsList.get(getAdapterPosition()), menuDetailsList.get(getAdapterPosition()).getQuantity() + 1, getAdapterPosition());
                    break;
            }
        }


    }

    ///set callback to Attach Activity /////
    public interface OnCustomizeClickListener {

        void onCustomizeClick(OrderItemsEntity response, int i);

        void onAddItemClick(OrderItemsEntity response, int quantity, int pos);

        void onRemoveItemClick(OrderItemsEntity response, int quantity, int pos);
    }


}
