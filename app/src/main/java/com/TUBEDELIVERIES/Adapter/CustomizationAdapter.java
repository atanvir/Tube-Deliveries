package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomizationAdapter extends RecyclerView.Adapter<CustomizationAdapter.ChooseTasteHolder> {
    private Context context;

    private List<ResponseBean> menList;

    private onCustomiseItemClic listener;

    public CustomizationAdapter(Context context, List<ResponseBean> menuLists,onCustomiseItemClic listener) {
        this.context = context;
        this.menList = menuLists;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ChooseTasteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_choose_taste_items,viewGroup,false);
        return new ChooseTasteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseTasteHolder holder, int i) {
        holder.checkBox.setText(menList.get(i).getName());
        holder.tvPrice.setText(menList.get(i).getPrice());
    }

    @Override
    public int getItemCount() {
        return menList!=null?menList.size():0;
    }

    public class ChooseTasteHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.checkBox)
        CheckBox checkBox;

        @BindView(R.id.tvPrice)
        TextView tvPrice;

        public ChooseTasteHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                        if(listener!=null)
                            listener.onItemCheck(getAdapterPosition(),menList.get(getAdapterPosition()));

                    }else {

                        if(listener!=null)
                            listener.onItemUncheck(getAdapterPosition(),menList.get(getAdapterPosition()));
                    }


                }
            });


        }
    }

    public interface onCustomiseItemClic{

        void onItemCheck(int pos,ResponseBean response);
        void onItemUncheck(int pos,ResponseBean response);

    }
}