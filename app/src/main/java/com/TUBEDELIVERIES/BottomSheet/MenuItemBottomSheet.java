package com.TUBEDELIVERIES.BottomSheet;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.TUBEDELIVERIES.Adapter.BottomMenuItemAdap;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuItemBottomSheet extends BottomSheetDialogFragment implements BottomMenuItemAdap.OnMenuClick {
    @BindView(R.id.menu_item_recycler)
    RecyclerView menu_item_recycler;
    private List<RestaurantResponse> menuList=new ArrayList<>();

    private  OnMenuItemClick listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bootmsheet_menu_item,container,false);
        ButterKnife.bind(this,view);
        getIntentData();
        setUpRecyclerView();
        return view;

    }


    @OnClick({R.id.iv_close})
    public  void onClick(View view){
        switch (view.getId()){
            case R.id.iv_close:
            dismiss();
            break;
        }
    }

    private void setUpRecyclerView() {
        menu_item_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        menu_item_recycler.setAdapter(new BottomMenuItemAdap(getActivity(),menuList,this));
    }


    private void getIntentData(){
        Bundle bundle=this.getArguments();
        if(bundle!=null){
            menuList = bundle.getParcelableArrayList(ParamEnum.MENU.theValue());
        }

    }

    @Override
    public void onClickMenu(int pos,RestaurantResponse response) {

        if(listener!=null)
            listener.menuItemClick(pos,response);

    }
    public interface OnMenuItemClick{
        void menuItemClick(int pos,RestaurantResponse response);
    }

    public void initilizeClick(OnMenuItemClick listener){
        this.listener=listener;
    }
}
