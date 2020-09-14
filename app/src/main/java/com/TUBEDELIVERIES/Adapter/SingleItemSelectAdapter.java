package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.RoomDatabase.Entity.AddonsEntity;

import java.util.List;

import android.os.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingleItemSelectAdapter extends RecyclerView.Adapter<SingleItemSelectAdapter.ChooseTasteHolder> {
    private Context context;

    private List<AddonsEntity> menList;

    private OnSingleAddOnClick listener;

    private Handler handler;

    public SingleItemSelectAdapter(Context context, List<AddonsEntity> menuLists, OnSingleAddOnClick listener) {
        this.context = context;
        this.menList = menuLists;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ChooseTasteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_choose_taste_items, viewGroup, false);
        return new ChooseTasteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseTasteHolder holder, int i) {

        if (menList.get(i).isCheck()) {
            holder.checkBox.setChecked(true);
            holder.tvPrice.setTextColor(ContextCompat.getColor(context, R.color.mahroom));
        }
        else {
            holder.checkBox.setChecked(false);
            holder.tvPrice.setTextColor(ContextCompat.getColor(context, R.color.colorGrey));
        }

        holder.checkBox.setText(menList.get(i).getName());
        holder.tvPrice.setText(context.getResources().getString(R.string.usd) + menList.get(i).getPrice());


    }

    @Override
    public int getItemCount() {
        return menList != null ? menList.size() : 0;
    }

    public class ChooseTasteHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.checkBox)
        CheckBox checkBox;

        @BindView(R.id.tvPrice)
        TextView tvPrice;

        public ChooseTasteHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if (isChecked) {
                        listener.onSingleItemCheck(getAdapterPosition(), menList.get(getAdapterPosition()));

                        resetCheckMenu(getAdapterPosition());

                    } else {

                        listener.onSingleItemUncheck(getAdapterPosition(), menList.get(getAdapterPosition()));
                    }


                }
            });


        }
    }

    public interface OnSingleAddOnClick {

        void onSingleItemCheck(int pos, AddonsEntity response);

        void onSingleItemUncheck(int pos, AddonsEntity response);

    }


    public void updateList(List<AddonsEntity> menList) {

        this.menList = menList;
        notifyDataSetChanged();

    }

    public void resetCheckMenu(int pos) {

        for (int i = 0; i < menList.size(); i++) {
            menList.get(i).setCheck(false);
        }
        menList.get(pos).setCheck(true);

        Handler   handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        },2);

    }

}