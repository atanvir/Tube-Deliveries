package com.TUBEDELIVERIES.BottomSheet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.TUBEDELIVERIES.Adapter.CustomizationAdapter;
import com.TUBEDELIVERIES.Adapter.FullCustomizationAdap;
import com.TUBEDELIVERIES.Model.CustomizationModel;
import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.RoomDatabase.Entity.AddonsEntity;
import com.TUBEDELIVERIES.RoomDatabase.Entity.OrderItemsEntity;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Mahipal Singh on 23,May,2019
 * mahisingh1@outlook.com
 */

public class ChooseYourTasteBottomSheet extends BottomSheetDialogFragment implements CustomizationAdapter.onCustomiseItemClic, FullCustomizationAdap.onCustomiseItemClic {

    @BindView(R.id.rvChooseTaste)
    RecyclerView rvChooseTaste;

    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;

    private List<CustomizationModel> menuList = new ArrayList<>();
    private List<AddonsEntity> selectedCustomization = new ArrayList<>();
    private RestaurantResponse mainMenuItem = new RestaurantResponse();
    private OrderItemsEntity orderItemsEntity=new OrderItemsEntity();
    private oncustomizationClickListener listener;

    public ChooseYourTasteBottomSheet(){

    }

    @SuppressLint("ValidFragment")
    public  ChooseYourTasteBottomSheet(oncustomizationClickListener listener){
        this.listener=listener;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_choose_taste, container, false);
        ButterKnife.bind(this, view);
        getIntentData();
        return view;

    }

    @OnClick({R.id.iv_close, R.id.clAddToCart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.clAddToCart:

                if (listener != null) {
                    if(selectedCustomization.size()>0&&selectedCustomization!=null){
                    listener.addToCartCustomization(selectedCustomization,orderItemsEntity);
                    dismiss();
                    }else
                        Toast.makeText(getActivity(), "Please select customization", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void setUpRecyclerView() {
        rvChooseTaste.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChooseTaste.setAdapter(new FullCustomizationAdap(getContext(), menuList, this));
        rvChooseTaste.setNestedScrollingEnabled(false);
    }


    private void getIntentData() {

        Bundle bundle = this.getArguments();

        if (bundle != null) {

            menuList = bundle.getParcelableArrayList(ParamEnum.MENU.theValue());
            orderItemsEntity = bundle.getParcelable(ParamEnum.MAIN_MENU_ITEM.theValue());
            setUpRecyclerView();
        }

    }

    @Override
    public void onItemCheck(int pos, AddonsEntity response) {

        selectedCustomization.add(response);
        setPrice();
    }

    @Override
    public void onItemUncheck(int pos, AddonsEntity response) {

        //remove item on ItemCheck
        if (selectedCustomization != null && selectedCustomization.size() > 0) {
            selectedCustomization.remove(response);
        }
        setPrice();

    }

    @Override
    public void onItemCheck(int pos, ResponseBean response) {

    }

    @Override
    public void onItemUncheck(int pos, ResponseBean response) {

    }


    //set callback to mainActivity when customisaton Done
    // sending selected item Lising to RestroDetails Activity

    public interface oncustomizationClickListener {
        void addToCartCustomization(List<AddonsEntity> selectedCustomization,OrderItemsEntity mainMenuListing);
    }


    ///set customisation price dynamically//
    private void setPrice() {

        int price = 0;
        for (int i = 0; i < selectedCustomization.size(); i++) {

            price = price + Integer.parseInt(selectedCustomization.get(i).getPrice());
        }

        tvTotalPrice.setText(String.valueOf("USD " + price));
    }
}
