package com.TUBEDELIVERIES.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.TUBEDELIVERIES.Model.ResponseBean;
import com.TUBEDELIVERIES.R;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;

    List<ResponseBean> mData;

    public SpinnerAdapter(Context applicationContext, List<ResponseBean>mDatas) {
        this.context = applicationContext;
        this.mData = mDatas;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return mData!=null?mData.size():0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_layout, null);

        TextView text1 = (TextView) view.findViewById(R.id.tvSpinner);
        text1.setText(mData.get(i).getName());

        return view;
    }
}