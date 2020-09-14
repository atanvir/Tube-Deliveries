package com.TUBEDELIVERIES.Fragments.EatOptionFragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.TUBEDELIVERIES.Fragments.IEatOptions;
import com.TUBEDELIVERIES.Fragments.Model.EatAtRestaurantModel;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EatRestaurantFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, TextWatcher {

    @BindView(R.id.eatType)
    TextView eatType;

    private IEatOptions listner;

    @BindView(R.id.edvisitingDate)
    TextInputEditText edvisitingDate;

    @BindView(R.id.edVisitingTime)
    TextInputEditText edVisitingTime;

    @BindView(R.id.edPeople)
    TextInputEditText edPeople;

    Date date= Calendar.getInstance().getTime();

    EatAtRestaurantModel model = new EatAtRestaurantModel();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_eat_at_restaurant,container,false);
        ButterKnife.bind(this,view);
        eatType.setText(getArguments().getString(ParamEnum.EAT_OPTION.theValue()));
        edvisitingDate.setOnClickListener(this);
        edVisitingTime.setOnClickListener(this);
        edPeople.addTextChangedListener(this);




        return view;
    }

    public void setListner(IEatOptions listner)
    {
        this.listner=listner;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.edvisitingDate:
                DatePickerDialog dialog=new DatePickerDialog(getActivity(), android.app.AlertDialog.THEME_HOLO_LIGHT,this,
                        Integer.parseInt(new SimpleDateFormat("yyyy").format(date)),
                        Integer.parseInt(new SimpleDateFormat("MM").format(date))-1,
                        Integer.parseInt(new SimpleDateFormat("dd").format(date)));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();

                break;

            case R.id.edVisitingTime:
                TimePickerDialog timerDailog=new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,this,
                        Integer.parseInt(new SimpleDateFormat("hh").format(date)),
                        Integer.parseInt(new SimpleDateFormat("mm").format(date)),true);
                timerDailog.show();

                break;






        }
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date= null;
        try {
            date = simpleDateFormat.parse(year+"-"+(month+1)+"-"+dayOfMonth);
            StringBuilder builder=new StringBuilder();
            builder.append(simpleDateFormat.format(date));
            edvisitingDate.setText(builder);
            model.setVisitingDate(edvisitingDate.getText().toString());
            if(listner!=null)
            {
                listner.eatOptionData(model);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        String AMPMValue = "AM";
//        if(hourOfDay>11){
//            AMPMValue="PM";
//            hourOfDay=hourOfDay-12;
//        }
//        if(hourOfDay==0)
//        {
//            hourOfDay=12;
//        }
        edVisitingTime.setText(hourOfDay+":"+minute+"");
        model.setVisitingTime(edVisitingTime.getText().toString());
        if(listner!=null)
        {
            listner.eatOptionData(model);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!s.toString().equalsIgnoreCase("")) {
            if (listner != null) {
                model.setNoPeople(s.toString());
                listner.eatOptionData(model);
            }
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
