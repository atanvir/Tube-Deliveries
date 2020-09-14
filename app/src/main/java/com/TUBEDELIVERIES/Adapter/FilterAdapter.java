package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TUBEDELIVERIES.Model.FilterCategoryModel;
import com.TUBEDELIVERIES.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {

    private Context context;
    private CategorySelectedListner listner;
    private List<FilterCategoryModel> list;
    private int type;


    public FilterAdapter(Context context,int type, List<FilterCategoryModel> list, CategorySelectedListner listner)
    {
        this.context=context;
        this.list=list;
        this.listner=listner;
        this.type=type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.adapter_filter,viewGroup,false);
        return new FilterAdapter.MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        if(list.get(position).isChecked())
        {
            myViewHolder.tvItem.setTextColor(ContextCompat.getColor(context,R.color.white));
            myViewHolder.tvItem.setBackgroundTintList(context.getColorStateList(R.color.green));

        }else
        {
            myViewHolder.tvItem.setTextColor(ContextCompat.getColor(context,R.color.filter_grey));
            myViewHolder.tvItem.setBackgroundTintList(context.getColorStateList(R.color.filter_grey2));
        }

        myViewHolder.tvItem.setText(list.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tvItem)
        TextView tvItem;

        public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        tvItem.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.tvItem:
                if(listner!=null)
                {
                    for(int i=0;i<list.size();i++)
                    {
                        list.get(i).setChecked(false);
                    }

                    list.get(getAdapterPosition()).setChecked(true);
                    listner.onCategoryClick(list.get(getAdapterPosition()).getName(), type);
                    notifyDataSetChanged();
                    break;


                }
            }
        }
    }

    public interface CategorySelectedListner
    {
         void onCategoryClick(String type,int categoryType);
    }
}
