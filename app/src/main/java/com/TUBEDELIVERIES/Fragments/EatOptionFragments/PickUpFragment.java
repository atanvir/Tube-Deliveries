package com.TUBEDELIVERIES.Fragments.EatOptionFragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.TUBEDELIVERIES.Fragments.IEatOptions;
import com.TUBEDELIVERIES.Fragments.Model.PickUpModel;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PickUpFragment extends Fragment implements View.OnClickListener,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.eatType)
    TextView eatType;

    @BindView(R.id.edpickUpDate)
    TextInputEditText edpickUpDate;

    @BindView(R.id.pickUpTime)
    TextInputEditText pickUpTime;

    private IEatOptions listner;
    Date date= Calendar.getInstance().getTime();
    PickUpModel model=new PickUpModel();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_pick_up,container,false);
        ButterKnife.bind(this,view);
        eatType.setText(getArguments().getString(ParamEnum.EAT_OPTION.theValue()));
        edpickUpDate.setOnClickListener(this);
        pickUpTime.setOnClickListener(this);





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
            case R.id.edpickUpDate:

                DatePickerDialog dialog=new DatePickerDialog(getActivity(), android.app.AlertDialog.THEME_HOLO_LIGHT,this,
                                    Integer.parseInt(new SimpleDateFormat("yyyy").format(date)),
                        Integer.parseInt(new SimpleDateFormat("MM").format(date))-1,
                                    Integer.parseInt(new SimpleDateFormat("dd").format(date)));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
                break;

            case R.id.pickUpTime:
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
       edpickUpDate.setText(builder);
       model.setPickUpDate(edpickUpDate.getText().toString());
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
            pickUpTime.setText(hourOfDay+":"+minute+"");
            model.setPickUpTime(pickUpTime.getText().toString());
            if(listner!=null)
            {
                listner.eatOptionData(model);
            }

    }
}
