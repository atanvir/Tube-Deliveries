//package com.JustBite.Adapter;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.JustBite.R;
//
//// Creating an Adapter Class
//public class MyAdapter extends ArrayAdapter {
//
//    public MyAdapter(Context context, int textViewResourceId,
//                     String[] objects) {
//        super(context, textViewResourceId, objects);
//    }
//
//    public View getCustomView(int position, View convertView,
//                              ViewGroup parent) {
//
//// Inflating the layout for the custom Spinner
//        LayoutInflater inflater = getLayoutInflater();
//        View layout = inflater.inflate(R.layout.spinner_layout, parent, false);
//
//
//
//
//        return layout;
//    }
//
//    // It gets a View that displays in the drop down
//    popup the data at the specified position
//    @Override
//    public View getDropDownView(int position, View convertView,
//                                ViewGroup parent) {
//        return getCustomView(position, convertView, parent);
//    }
//
//    // It gets a View that displays the data at the specified position
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        return getCustomView(position, convertView, parent);
//    }
//}