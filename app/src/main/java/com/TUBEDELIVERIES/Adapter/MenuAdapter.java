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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.AddToCartViewHolder> {
    private Context context;
    private List<RestaurantResponse> menuList=new ArrayList<>();

   private onMenuClick listener;

    public MenuAdapter(Context context, List<RestaurantResponse> menu,onMenuClick listener) {
        this.context = context;
        this.menuList = menu;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddToCartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_add_cart_items_horizontal,viewGroup,false);
        return new AddToCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddToCartViewHolder holder, final int position) {

        holder.tvMenuitems.setText(menuList.get(position).getName());


        if(menuList.get(position).isCheck()){
            holder.tabViewLine.setVisibility(View.VISIBLE);
            holder.tvMenuitems.setTextColor(context.getResources().getColor(R.color.mahroom));
        }else {
            holder.tvMenuitems.setTextColor(context.getResources().getColor(R.color.colorGrey));
            holder.tabViewLine.setVisibility(View.GONE);

        }

        holder.clParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for(int i=0;i<menuList.size();i++){
                    menuList.get(i).setCheck(false);
                }
                menuList.get(position).setCheck(true);
                notifyDataSetChanged();


                if(listener!=null)
                    listener.onClickMenu(menuList.get(position));
            }
        });

    }


    //update Menu List
    public void refreshMenuList(List<RestaurantResponse> menuList){

        this.menuList=menuList;
        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return menuList!=null?menuList.size():0;
    }

    public class AddToCartViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvMenuitems)
        TextView tvMenuitems;

        @BindView(R.id.clParent)
        ConstraintLayout clParent;

        @BindView(R.id.tabViewLine)
        View tabViewLine;


        public AddToCartViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }



    //for callback on Menu CLick from horizontal view------>
    public interface onMenuClick{

        //passing the whole response on click
        void onClickMenu(RestaurantResponse response);
    }


}
