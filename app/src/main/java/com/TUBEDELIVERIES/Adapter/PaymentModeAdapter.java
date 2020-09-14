package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TUBEDELIVERIES.R;

public class PaymentModeAdapter extends RecyclerView.Adapter<PaymentModeAdapter.PaymentHolder> {
    private Context context;

    public PaymentModeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_payment_mode_items,viewGroup,false);
        return new PaymentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHolder paymentHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class PaymentHolder extends RecyclerView.ViewHolder {
        public PaymentHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
