package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Mahipal Singh on 14,JUne,2019
 * mahisingh1@outlook.com
 */


public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ViewHolder> {
    private Context context;
    List<ResponseBean> list = null;


    public PaymentsAdapter(Context context, List<ResponseBean> Cardlist) {
        this.context = context;
        this.list = Cardlist;
//        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_payment, viewGroup, false);
        return new PaymentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

//        viewHolder.tvCardNo.setText(list.get(i).getCard_number());
//        viewHolder.tvCarHolderName.setText(list.get(i).getCard_name());
//        viewHolder.tvMonthYear.setText(list.get(i).getCard_exp_month() + "/" + list.get(i).getCard_exp_year());
//
//
//
//        viewHolder.tvCardNo.setText(replaceWithStars(list.get(i).getCard_number()));


//
//        for(int j=0;j<list.get(i).getCard_number().length()-4;j++){
//
//
//            String[] buil=list.get(i).getCard_number();
//
//            string.replace(list.get(i).getCard_number().charAt(j),"*");
//        }


    }


    public void updateList(List<ResponseBean> Cardlist) {

        this.list = Cardlist;
        notifyDataSetChanged();
    }

    public void removeItem(ResponseBean Cardlist) {

        list.remove(Cardlist);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.tvCarHolderName)
//        TextView tvCarHolderName;
//
//        @BindView(R.id.tvMonthYear)
//        TextView tvMonthYear;
//
//        @BindView(R.id.tvCardNo)
//        TextView tvCardNo;
//
//        @BindView(R.id.ivCardDelete)
//        ImageView ivCardDelete;
//
//       @BindView(R.id.ivSelect)
//       CheckBox ivSelect;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
//
//            if(context instanceof PaymentModeActivity){
//
//                ivSelect.setVisibility(View.VISIBLE);
//                ivCardDelete.setVisibility(View.GONE);
//
//            }else {
//
//                ivSelect.setVisibility(View.GONE);
//                ivCardDelete.setVisibility(View.VISIBLE);
//
//            }
//
//
//            ivSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    if(isChecked){
//
//                        listener.onSelect(list.get(getAdapterPosition()));
//                    }else {
//                        listener.onUnSelect(list.get(getAdapterPosition()));
//
//
//                    }
//
//
//                }
//            });
//
//            ivCardDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    listener.onDeleteClick(list.get(getAdapterPosition()));
//
//                }
//            });
//        }





//    public interface OnDelete {
//
//        void onDeleteClick(ResponseBean responseBean);
//        void onSelect(ResponseBean responseBean);
//        void onUnSelect(ResponseBean responseBean);
//
//    }
//
//
//    ///card patter
//    public static ArrayList<String> listOfPattern()
//    {
//        ArrayList<String> listOfPattern=new ArrayList<String>();
//
//        String ptVisa = "^4[0-9]$";
//
//        listOfPattern.add(ptVisa);
//
//        String ptMasterCard = "^5[1-5]$";
//
//        listOfPattern.add(ptMasterCard);
//
//        String ptDiscover = "^6(?:011|5[0-9]{2})$";
//
//        listOfPattern.add(ptDiscover);
//
//        String ptAmeExp = "^3[47]$";
//
//        listOfPattern.add(ptAmeExp);
//
//        return listOfPattern;
//    }
//
//////////////for hiding card no using (*) //////
//    private StringBuilder replaceWithStars(String inputString){
//
//        int length=inputString.length();
//
//        String newString="";
//        StringBuilder builder=new StringBuilder();
//
//         for(int i=0;i<length-4;i++){
//
//             builder.append("*");
//
//         }
//
//         builder.append(inputString.substring((length-4),length));
//
//        return builder;
//
//    }

