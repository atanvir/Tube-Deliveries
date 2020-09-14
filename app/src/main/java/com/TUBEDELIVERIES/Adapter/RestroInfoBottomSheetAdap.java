package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TUBEDELIVERIES.R;

public class RestroInfoBottomSheetAdap extends RecyclerView.Adapter<RestroInfoBottomSheetAdap.INfoViewHolder> {
    private Context context;

    public RestroInfoBottomSheetAdap(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public INfoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_resto_info_items, viewGroup, false);

        return new INfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull INfoViewHolder iNfoViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class INfoViewHolder extends RecyclerView.ViewHolder {
        public INfoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
