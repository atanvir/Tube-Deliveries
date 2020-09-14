package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TUBEDELIVERIES.Model.CustomizationModel;
import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.RoomDatabase.Entity.AddonsEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullCustomizationAdap extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SingleItemSelectAdapter.OnSingleAddOnClick, MultiItemAddAdapter.OnMutipleAddonClick {
    private Context context;

    private List<CustomizationModel> menList;
    private List<ResponseBean> singleSlectionMenu;
    private onCustomiseItemClic listener;

    SingleItemSelectAdapter singleItemSelAdapter;
    MultiItemAddAdapter multiItemAddAdapter;


    public static final int SINGLE_SELECT_ADDON = 0;
    public static final int MULTI_SELECT_ADDON = 1;

    public FullCustomizationAdap(Context context, List<CustomizationModel> menuLists, onCustomiseItemClic listener) {
        this.context = context;
        this.menList = menuLists;
        this.listener = listener;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

       // Section.ItemType currentItemType = getCurrentItemType(viewType);


        switch (viewType) {


            case SINGLE_SELECT_ADDON:
                View v1 = inflater.inflate(R.layout.layout_single_checkbox, parent, false);

                viewHolder = new SingleSelectionAddon(v1);
                break;

            case MULTI_SELECT_ADDON:
                View v2 = inflater.inflate(R.layout.layot_multicheckbox, parent, false);

                viewHolder = new MultiSelectionAddon(v2);
                break;


        }
        return viewHolder;


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

        switch (holder.getItemViewType()) {

            case SINGLE_SELECT_ADDON:

                SingleSelectionAddon singleSelectionHolder = (SingleSelectionAddon) holder;

                singleItemSelAdapter = new SingleItemSelectAdapter(context, null,this);


                singleSelectionHolder.bindSingleData(i,menList.get(i).getAddons());



                break;

            case MULTI_SELECT_ADDON:

                 MultiSelectionAddon multiSelectionAddon = (MultiSelectionAddon) holder;

                multiItemAddAdapter = new MultiItemAddAdapter(context, null,this);

                multiSelectionAddon.bindMultipleData(i,menList.get(i).getAddons());

                break;

        }


    }

    @Override
    public int getItemCount() {
        return menList != null ? menList.size() : 0;
    }

    @Override
    public void onSingleItemCheck(int pos, AddonsEntity response) {

        listener.onItemCheck(pos,response);

    }

    @Override
    public void onSingleItemUncheck(int pos, AddonsEntity response) {

        listener.onItemUncheck(pos,response);

    }

    @Override
    public void onMultipleItemCheck(int pos, AddonsEntity response) {

        listener.onItemCheck(pos,response);

    }

    @Override
    public void onMultiItemUncheck(int pos, AddonsEntity response) {


         listener.onItemUncheck(pos,response);


    }

    public class SingleSelectionAddon extends RecyclerView.ViewHolder {


        @BindView(R.id.TvTitle)
        TextView TvTitle;

        @BindView(R.id.rvSingleCheckBox)
        RecyclerView rvSingleCheckBox;


        public SingleSelectionAddon(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);



        }
        public void bindSingleData(final int postion, List<AddonsEntity> addOns) {

            rvSingleCheckBox.setLayoutManager(new LinearLayoutManager(context));
            rvSingleCheckBox.setAdapter(singleItemSelAdapter);
            singleItemSelAdapter.updateList(addOns);
            TvTitle.setText(menList.get(postion).getItemHeading());

        }

    }


    public class MultiSelectionAddon extends RecyclerView.ViewHolder {

        @BindView(R.id.TvTitle)
        TextView TvTitle;

        @BindView(R.id.rvMultiCheckBox)
        RecyclerView rvMultiCheckBox;

        public MultiSelectionAddon(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);



        }
        public void bindMultipleData(final int postion, List<AddonsEntity> addOns) {

            rvMultiCheckBox.setLayoutManager(new LinearLayoutManager(context));
            rvMultiCheckBox.setAdapter(multiItemAddAdapter);
            multiItemAddAdapter.updateList(addOns);
            TvTitle.setText(menList.get(postion).getItemHeading());

        }



    }

    public interface onCustomiseItemClic {

        void onItemCheck(int pos, AddonsEntity response);

        void onItemUncheck(int pos, AddonsEntity response);

    }

    @Override
    public int getItemViewType(int position) {

        /// 1 for single selection menu  and 2 for mutliple selectin menu
        if (menList.get(position).getMenuViewType().equalsIgnoreCase("1"))
            return SINGLE_SELECT_ADDON;
        else
            return MULTI_SELECT_ADDON;

    }

}