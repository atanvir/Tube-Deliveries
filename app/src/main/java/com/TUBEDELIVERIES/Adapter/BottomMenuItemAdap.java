package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomMenuItemAdap extends RecyclerView.Adapter<BottomMenuItemAdap.MenuItemViewHolder> {

    private Context context;

    private List<RestaurantResponse> menList;
    private OnMenuClick listener;

    public BottomMenuItemAdap(Context context,List<RestaurantResponse>menList,OnMenuClick listener) {
        this.context = context;
        this.menList = menList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(context).inflate(R.layout.layout_menu_items_bottom_sheet,viewGroup,false);
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder menuItemViewHolder, int i) {
        if(i==0){
            menuItemViewHolder.tv_memu_name.setTextColor(context.getResources().getColor(R.color.mahroom));
            menuItemViewHolder.tvItemCount.setTextColor(context.getResources().getColor(R.color.mahroom));
        }else {
            menuItemViewHolder.tv_memu_name.setTextColor(context.getResources().getColor(R.color.colorGrey));
            menuItemViewHolder.tvItemCount.setTextColor(context.getResources().getColor(R.color.colorGrey));
        }

        menuItemViewHolder.tvItemCount.setText(menList.get(i).getMenuCount());
        menuItemViewHolder.tv_memu_name.setText(menList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return menList!=null?menList.size():0;
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_memu_name)
        TextView tv_memu_name;

        @BindView(R.id.tvItemCount)
        TextView tvItemCount;

        @BindView(R.id.clParentMenu)
        ConstraintLayout clParentMenu;

        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            clParentMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onClickMenu(getAdapterPosition(),menList.get(getAdapterPosition()));
                }
            });

        }
    }


    public interface  OnMenuClick{
        void onClickMenu(int pos,RestaurantResponse response);
    }
}
