package com.TUBEDELIVERIES.Fragments.BottomNavigation;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TUBEDELIVERIES.Activity.MainActivity;
import com.TUBEDELIVERIES.Fragments.BottomNavigation.MyOrdersFragment.OngoingOrdersFragment;
import com.TUBEDELIVERIES.Fragments.BottomNavigation.MyOrdersFragment.PastOrdersFragment;
import com.TUBEDELIVERIES.Fragments.BottomNavigation.MyOrdersFragment.UpcomingOrdersFragment;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Utility.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationOrdersFragment extends Fragment implements View.OnClickListener{


    @BindView(R.id.tvOnGoing)
    TextView tvOnGoing;

    @BindView(R.id.tvPast)
    TextView tvPast;

    @BindView(R.id.tvUpcoming)
    TextView tvUpcoming;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        ButterKnife.bind(this, view);
        tvPast.setOnClickListener(this);
        tvOnGoing.setOnClickListener(this);
        tvUpcoming.setOnClickListener(this);
        tvUpcoming.performClick();
        return view;
    }

    private void loadFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.frameMyOrders, fragment).commit();
    }


    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvOnGoing:

                setTabBackground("Ongoing");
                loadFragment(new OngoingOrdersFragment());

            break;

            case R.id.tvPast:

                setTabBackground("Past");
                loadFragment(new PastOrdersFragment());

            break;

            case R.id.tvUpcoming:

                setTabBackground("Upcoming");
                loadFragment(new UpcomingOrdersFragment());


            break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setToolbar(getResources().getString(R.string.my_orders), View.VISIBLE);
        new CommonUtil().setStatusBarGradiant(getActivity(), NavigationOrdersFragment.class.getSimpleName());
    }

    private void setTabBackground(String tab) {
        if (tab.equalsIgnoreCase("Past")) {
            tvPast.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            tvPast.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_layout_past));
            tvOnGoing.setBackground(null);
            tvOnGoing.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));
            tvUpcoming.setBackground(null);
            tvUpcoming.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));

        } else if (tab.equalsIgnoreCase("Ongoing")) {
            tvOnGoing.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            tvOnGoing.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_layout_ongoing));
            tvPast.setBackground(null);
            tvPast.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));
            tvUpcoming.setBackground(null);
            tvUpcoming.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));

        } else if (tab.equalsIgnoreCase("Upcoming")) {
            tvUpcoming.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            tvUpcoming.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_layout_upcoming));
            tvOnGoing.setBackground(null);
            tvOnGoing.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));
            tvPast.setBackground(null);
            tvPast.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));


        }


    }

}
