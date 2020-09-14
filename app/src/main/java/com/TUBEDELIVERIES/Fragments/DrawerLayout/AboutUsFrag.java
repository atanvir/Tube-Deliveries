package com.TUBEDELIVERIES.Fragments.DrawerLayout;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TUBEDELIVERIES.Activity.MainActivity;
import com.TUBEDELIVERIES.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFrag extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setToolbar(getResources().getString(R.string.about_us),View.GONE);
    }
}
