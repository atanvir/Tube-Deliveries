package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TUBEDELIVERIES.Model.StoryModel;
import com.TUBEDELIVERIES.R;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerStoriesAdapter extends RecyclerView.Adapter<CustomerStoriesAdapter.MyViewHolder> {
    private Context context;

    private List<StoryModel> list;


    public CustomerStoriesAdapter(Context context, List<StoryModel> list) {
        this.context = context;
        this.list=list;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_customer_stories,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Glide.with(context).load(list.get(i).getImage()).into(holder.ivProfile);
        holder.tvName.setText(list.get(i).getTitle());
        holder.tvDesc.setText(list.get(i).getDescription());

    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvDesc) TextView tvDesc;
        @BindView(R.id.ivProfile) CircleImageView ivProfile;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}