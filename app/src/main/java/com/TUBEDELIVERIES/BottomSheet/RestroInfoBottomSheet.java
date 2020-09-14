package com.TUBEDELIVERIES.BottomSheet;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.TUBEDELIVERIES.Activity.RatingAndReviewActivity;
import com.TUBEDELIVERIES.Adapter.RatingAndReviewsAdap;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.TUBEDELIVERIES.Model.RestaurantResponse;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Retrofit.ServicesConnection;
import com.TUBEDELIVERIES.Retrofit.ServicesInterface;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestroInfoBottomSheet extends BottomSheetDialogFragment {


    @BindView(R.id.restro_info_recycler)
    RecyclerView restro_info_recycler;

    @BindView(R.id.tv_timing)
    TextView tv_timing;

    @BindView(R.id.tv_description)
    TextView tv_description;

    @BindView(R.id.spnRestauInfo)
    Spinner spnRestauInfo;

    private String  restro_id="";

    private ArrayList<String> workingHrs;

   private RatingAndReviewsAdap adap=null;
   private List<RestaurantResponse> ratingList=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_restau_info,container,false);
        ButterKnife.bind(this,view);
        setUpRecyclerView();
        getIntentData();

        return view;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog=(BottomSheetDialog)super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog dialogc = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet =  dialogc.findViewById(R.id.design_bottom_sheet);

            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return bottomSheetDialog;
    }

    @OnClick({R.id.iv_close,R.id.tv_viewAll,R.id.tv_timing})
    public  void onClick(View view){
        switch (view.getId()){
            case R.id.iv_close:
                dismiss();
                break;

            case R.id.tv_viewAll:
                Intent intent= new Intent(getContext(),RatingAndReviewActivity.class);
                intent.putExtra(ParamEnum.RESTRO_ID.theValue(),restro_id);
                startActivity(intent);
                break;

            case R.id.tv_timing:

                spnRestauInfo.performClick();
                break;

        }
    }

    private void setWorkingHrsSpinner(ArrayList<String>timmings) {
        workingHrs=new ArrayList<>();

        this.workingHrs  = timmings;

         ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,workingHrs);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spnRestauInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                tv_timing.setText(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnRestauInfo.setAdapter(adapter);
    }

    private void setUpRecyclerView() {
        restro_info_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        restro_info_recycler.setFocusable(false);
        ratingList=new ArrayList<>();
        adap=new RatingAndReviewsAdap(getActivity(),ratingList);
        restro_info_recycler.setAdapter(adap);
    }


    //////////restroinfo api///////
    public void restroInfo(String restroId) {

        if (CommonUtilities.isNetworkAvailable(getActivity())) {

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonModel> loginCall = servicesInterface.restaurantInfo(restroId);

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        loginCall,
                        getActivity(),
                        false,
                        new Callback<CommonModel>() {
                            @Override
                            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                                if (response.isSuccessful()) {
                                    CommonModel bean = ((CommonModel) response.body());
                                    if (bean.getStatus().equalsIgnoreCase(ParamEnum.Success.theValue())) {

                                         tv_description.setText(bean.getRestorentInfo().getDescription());

                                        ratingList.clear();
                                        ratingList.addAll(bean.getRestorentInfo().getRestorent_review());
                                        adap.updateRatingList(ratingList);
                                        setWorkingHrsSpinner(bean.getRestorentInfo().getRestorent_time());

                                    } else {
                                        CommonUtilities.snackBar(getActivity(), bean.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<CommonModel> call, Throwable t) {
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            CommonUtilities.snackBar(getActivity(), getString(R.string.no_internet));

        }
    }


    private void getIntentData(){

        Bundle bundle=this.getArguments();

        if(bundle!=null){

            restro_id = bundle.getString(ParamEnum.RESTRO_ID.theValue());

            restroInfo(restro_id);
        }

    }

}
