package com.TUBEDELIVERIES.Fragments.EatOptionFragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.TUBEDELIVERIES.Fragments.IEatOptions;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Utility.ParamEnum;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCartHomeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.optionGroup)
    RadioGroup optionGroup;

    @BindView(R.id.pickRB)
    RadioButton pickRB;

    @BindView(R.id.homeRB)
    RadioButton homeRB;

    @BindView(R.id.restaurantRB)
    RadioButton restaurantRB;

    @BindView(R.id.tvEatType)
    TextView tvEatType;


    String offered_service;

    private IEatOptions listner;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_my_cart_home,container,false);
        ButterKnife.bind(this,view);
        optionGroup.setOnCheckedChangeListener(this);
        offered_service=getArguments().getString(ParamEnum.OFFERED_SERVICE.theValue());
        settingEatOption(offered_service);
        return view;
    }



    private void settingEatOption(String offered_service) {

        if(offered_service!= null)
        {
           String offered[]= offered_service.split(",");

           for(int i=0;i<offered.length;i++)
           {
               if(offered[i].equalsIgnoreCase("0"))
               {
                   pickRB.setVisibility(View.VISIBLE);

               }else if(offered[i].equalsIgnoreCase("1"))
               {
                    homeRB.setVisibility(View.VISIBLE);
               }else if(offered[i].equalsIgnoreCase("2"))
               {

                   restaurantRB.setVisibility(View.VISIBLE);
               }

           }

        }

    }

    public void setListner(IEatOptions listner)
    {
        this.listner=listner;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(group.getCheckedRadioButtonId())
        {
            case R.id.homeRB:
                if(listner!=null)
                    listner.eatType("1");

                break;

            case R.id.pickRB:
                if(listner!=null)
                    listner.eatType("0");


                break;

            case R.id.restaurantRB:
                if(listner!=null)
                    listner.eatType("2");

                break;




        }


    }
}
