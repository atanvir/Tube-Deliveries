package com.TUBEDELIVERIES.Utility;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;


public class CommonUtil   {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarGradiant(Activity obj,String class_name) {


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window =((Activity) obj).getWindow();
//            Drawable background =null;
//
//            if(class_name.equalsIgnoreCase(ProfieFragmentWithoutLogin.class.getSimpleName()))
//            {
//                background = ((Activity) obj).getResources().getDrawable(R.drawable.common_green_toolbar);
//
//            }else
//            {
//                background = ((Activity) obj).getResources().getDrawable(R.drawable.common_green_toolbar);
//
//            }
//
//
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(((Activity) obj).getResources().getColor(android.R.color.transparent));
//            window.setNavigationBarColor(((Activity) obj).getResources().getColor(android.R.color.transparent));
//            window.setBackgroundDrawable(background);
//
//
//        }
    }





}
