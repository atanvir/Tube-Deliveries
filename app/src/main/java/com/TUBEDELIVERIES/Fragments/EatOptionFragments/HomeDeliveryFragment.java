package com.TUBEDELIVERIES.Fragments.EatOptionFragments;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.TUBEDELIVERIES.Activity.AddressPicker;
import com.TUBEDELIVERIES.Fragments.IEatOptions;
import com.TUBEDELIVERIES.Fragments.Model.HomeDeliveryModel;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.ParamEnum;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class HomeDeliveryFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.eatType)
    TextView eatType;

    @BindView(R.id.edtLocation)
    EditText edLocation;

    @BindView(R.id.ivhome)
    ImageView ivhome;


    Double longitudeFromPicker=0.0d,latitudeFromPicker =0.0d;

    private IEatOptions listner;

    String addressType="";
    HomeDeliveryModel model=new HomeDeliveryModel();




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home_delivery,container,false);
        ButterKnife.bind(this,view);
        eatType.setText("Home Delivery");
        edLocation.setOnClickListener(this);
        ivhome.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
       switch (v.getId())
       {
           case R.id.edtLocation:
               Intent intent=new Intent(getActivity(), AddressPicker.class);
               addressType= ParamEnum.CURRENT.theValue();
               startActivityForResult(intent,101);
               break;
           case R.id.ivhome:
               addressType="Home";
               edLocation.setText("" );
               edLocation.setText(getArguments().getString(ParamEnum.HOME.theValue()).replace("null", " ").replace("India", ""));
               if(listner!=null)
               {
                   model.setType(addressType);
                   model.setAddress(edLocation.getText().toString().trim());
                   model.setLat(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LATITUDE));
                   model.setLongt(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LONGITUDE));
                   listner.eatOptionData(model);
               }

               break;

       }
    }

    public String getAddress(Double latitude,Double longitude) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        String result="";
        try{
        List<Address> addressList = geocoder.getFromLocation(
                latitude, longitude, 1);
        if (addressList != null && addressList.size() > 0) {
            Address address = addressList.get(0);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                sb.append(address.getAddressLine(i)).append("\n");
            }
            sb.append(address.getLocality()).append("\n");
            sb.append(address.getPostalCode()).append("\n");
            sb.append(address.getCountryName());
            result = sb.toString();
        }}catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public void setListner(IEatOptions listner)
    {
        this.listner=listner;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 101:
                if (resultCode == RESULT_OK) {

                    latitudeFromPicker = Double.parseDouble(data.getStringExtra("LAT"));
                    longitudeFromPicker = Double.parseDouble(data.getStringExtra("LONG"));
                    edLocation.setText(data.getStringExtra("ADDRESS"));
                    if(listner!=null)
                    {
                        model.setType(addressType);
                        model.setAddress(edLocation.getText().toString().trim());
                        model.setLat(""+latitudeFromPicker);
                        model.setLongt(""+longitudeFromPicker);
                        listner.eatOptionData(model);

                    }
                    break;

                }

        }
    }


}
