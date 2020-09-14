package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.TUBEDELIVERIES.Activity.FilterActivity;
import com.TUBEDELIVERIES.Model.FilterSubCategoryModel;
import com.TUBEDELIVERIES.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubFilterAdapter extends RecyclerView.Adapter<SubFilterAdapter.ViewHolder> {

    private Context context;
    private List<FilterSubCategoryModel> list;
    private SubCategoryClickListner listner;
    private String categoryType;
    boolean isRatingSet=false;

    public SubFilterAdapter(Context context,String categoryType,List<FilterSubCategoryModel> list,SubCategoryClickListner listner)
    {
        this.context=context;
        this.list=list;
        this.listner=listner;
        this.categoryType=categoryType;
        isRatingSet=false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.adapter_sub_category,parent, false);
        return new SubFilterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e(categoryType+"["+(position+1)+"]",list.get(position).getName()+" \n isChecked:"+list.get(position).isCheck());
        if(list.get(position).isCheck()) {
            holder.checkBox.setTextColor(ContextCompat.getColor(context,R.color.green));
        }else
        {
            holder.checkBox.setTextColor(ContextCompat.getColor(context,R.color.black));

        }
        if(list.get(position).getName().equalsIgnoreCase("Veg") || list.get(position).getName().equalsIgnoreCase("Non Veg"))
        {
            holder.ivType.setVisibility(View.VISIBLE);
            if(list.get(position).getName().equalsIgnoreCase("Veg")) holder.ivType.setBackground(ContextCompat.getDrawable(context,R.drawable.veg_symbol));
            else if(list.get(position).getName().equalsIgnoreCase("Non Veg")) holder.ivType.setBackground(ContextCompat.getDrawable(context,R.drawable.nonveg_symbol));
        }

        if(list.get(position).getName().equalsIgnoreCase("Ratings"))
        {
            holder.checkBox.setVisibility(View.GONE);
            holder.ratingBar.setRating(list.get(position).getRating());
            holder.ratingBar.setVisibility(View.VISIBLE);
            if(position==(list.size()-1) && !isRatingSet)
            {
                isRatingSet=true;
            }
        }else
        {
            holder.ratingBar.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.VISIBLE);
        }

        holder.checkBox.setText(list.get(position).getName());
        holder.checkBox.setChecked(list.get(position).isCheck());


    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {
        @BindView(R.id.checkBox) CheckBox checkBox;
        @BindView(R.id.mainCl) ConstraintLayout mainCl;
        @BindView(R.id.ivType) ImageView ivType;
        @BindView(R.id.ratingBar) RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            mainCl.setOnClickListener(this);
            checkBox.setOnClickListener(this);
            ratingBar.setOnRatingBarChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.mainCl:
                if(listner!=null) {
                    for (int i = 0; i < list.size(); i++) {
                            list.get(i).setCheck(false);
                    }
                    list.get(getAdapterPosition()).setCheck(true);
                    listner.onItemSelected(categoryType,list);
                    notifyDataSetChanged();
                }
                break;

                case R.id.checkBox:
                mainCl.performClick();
                break;
            }
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            if(isRatingSet) {
                if (listner != null) {
                    list.get(getAdapterPosition()).setRating(ratingBar.getRating());
                    listner.onItemSelected(categoryType, list);
                    notifyDataSetChanged();
                }
            }
        }
    }


    public interface SubCategoryClickListner
    {
       void onItemSelected(String type,List<FilterSubCategoryModel> list);
    }
}
