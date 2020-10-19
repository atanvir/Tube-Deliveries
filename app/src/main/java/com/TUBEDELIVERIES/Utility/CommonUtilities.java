package com.TUBEDELIVERIES.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.TUBEDELIVERIES.Activity.FilterActivity;
import com.TUBEDELIVERIES.Model.CommonModel;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.TUBEDELIVERIES.Activity.EditMyProfileActivity;
import com.TUBEDELIVERIES.Activity.LoginActivity;
import com.TUBEDELIVERIES.Activity.MyCartActivity;
import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.bumptech.glide.Glide;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import okhttp3.ResponseBody;

import static android.view.View.GONE;


/**
 * Created by Mahipal Singh on 26,June,2018
 * mahisingh1@outlook.com
 */
public class CommonUtilities {
    public  static String path;
    public static CustomProgressBar customProgressBar;

    public static Uri fileUri;

    public static String getPaymentType(String payment_type) {

        String eatType="";

        switch (payment_type)
        {
            case "0":
                eatType= "Pick Up";
                break;

            case "1":
                eatType= "Home Delivery";
                break;

            case "2":
                eatType= "Eat At Restaurant";
                break;

        }
        return eatType;
    }


    public static void getHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("ABC", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("AABC", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("ABCCC", "printHashKey()", e);
        }

    }






    public static void showLoadingDialog(Activity activity){
        if(customProgressBar==null)
        customProgressBar = CustomProgressBar.show(activity, true, true);

        try {
            customProgressBar.setCancelable(false);
            customProgressBar.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static void dismissLoadingDialog(){
        try
        {
            if (null != customProgressBar && customProgressBar.isShowing()) {
                customProgressBar.dismiss();
                customProgressBar=null;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }





    public static void snackBar(Activity activity, String message)
    {
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setMinimumHeight(20);
        snackBarView.setBackgroundColor(Color.parseColor("#E9313D"));
        TextView tv = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        tv.setTextSize(13);
        //Fonts.OpenSans_Regular_Txt(tv, activity.getAssets());
        tv.setTextColor(Color.parseColor("#ffffff"));
        snackbar.show();
    }


    public static boolean isNetworkAvailable(Context context) {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mobile_info != null) {
                if (mobile_info.isConnectedOrConnecting()
                        || wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("" + e);
            return false;
        }
    }

    ///////email check string///////////

   public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static String MobilePattern = "[0-9]{9}";


    public static void setToolbar(final AppCompatActivity activity, androidx.appcompat.widget.Toolbar toolbar, TextView title, String  TitleContent){
        TextView tvClear=activity.findViewById(R.id.tvClear);
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_button);

        if(activity instanceof EditMyProfileActivity)
        {
            toolbar.getNavigationIcon().setVisible(false,false);
        }
        if(activity instanceof FilterActivity)
        {
            tvClear.setVisibility(View.VISIBLE);

        }

        title.setText(TitleContent);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              activity.onBackPressed();
            }
        });

    }




    public static  void getDeviceToken(Context context) {
       final Thread thread = new Thread() {
            public void run() {
//                Log.e(">>>>>>>>>>>>>>", "thred IS  running");
//              @Override
              try {

                    SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.DEVICETOKEN,"");

                    if (SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.DEVICETOKEN).isEmpty()) {
                        String token = FirebaseInstanceId.getInstance().getToken();
//                        String token = Settings.Secure.getString(getApplicationContext().getContentResolver(),
//                                Settings.Secure.ANDROID_ID);
                        Log.e("Generated Device Token", "-->" + token);

                        if (token == null) {
                            getDeviceToken(context);
                        } else {

                            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.DEVICETOKEN,token);
                            //mPreference.writeStringValue(SharedPreferenceKey.DEVICE_TOKEN, token);
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                super.run();
            }
        };
        thread.start();

    }




    //set image with glide
    public static void setImage(Context context,ProgressBar progressBar, final String imageUri, final ImageView imageView) {
        progressBar.setVisibility(View.VISIBLE);

        Glide.with(context.getApplicationContext())
                .load(imageUri)
                .centerCrop()
                .placeholder(R.drawable.food_thali)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("onLoadFailed", "yes");
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.e("onResourceReady", "yes");
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(GONE);
                        return false;
                    }
                }).into(imageView);
    }



    // show  alert dialog box
    public static void showAlertDialog(Context context) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_alert);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tvTitleDialog = dialog.findViewById(R.id.tvTitleDialog);

        TextView tvMsgDialog = dialog.findViewById(R.id.tvMsgDialog);

        tvTitleDialog.setText("Your are not loged in");

        tvMsgDialog.setText("Go to login activity ??");

        TextView tvYes = dialog.findViewById(R.id.tvYes);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, LoginActivity.class));
                dialog.dismiss();
                ((Activity)context).finishAffinity();


            }
        });

        TextView tvNo = dialog.findViewById(R.id.tvNo);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // show  alert dialog box
    public static void goToCarDialogue(Context context) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_alert);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tvTitleDialog = dialog.findViewById(R.id.tvTitleDialog);

        TextView tvMsgDialog = dialog.findViewById(R.id.tvMsgDialog);

        tvTitleDialog.setText("Remove item from cart");

        tvMsgDialog.setText(context.getString(R.string.remove_item));

        TextView tvYes = dialog.findViewById(R.id.tvYes);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MyCartActivity.class));
                dialog.dismiss();


            }
        });

        TextView tvNo = dialog.findViewById(R.id.tvNo);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public static String roundOff(String originalValue){
        double tvTotalPrice = Double.valueOf(originalValue);
        double sum1 = (int) Math.round(tvTotalPrice * 100) / (double) 100;
        double finalAmount = 0.00;
        return String.valueOf(sum1);
    }

    // show  Repeat order alert dialog box
    public static void repeatOrder(Context context) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_alert);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tvTitleDialog = dialog.findViewById(R.id.tvTitleDialog);

        TextView tvMsgDialog = dialog.findViewById(R.id.tvMsgDialog);

        tvTitleDialog.setText(context.getString(R.string.customization));

        tvMsgDialog.setText(context.getString(R.string.repeat_order));

        TextView tvYes = dialog.findViewById(R.id.tvYes);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MyCartActivity.class));
                dialog.dismiss();


            }
        });

        TextView tvNo = dialog.findViewById(R.id.tvNo);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void setSpinner(Context context, List<String> data, Spinner spinner) {
        ArrayAdapter genderArrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, data) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0)
                    return false;
                else
                    return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return super.getDropDownView(position, convertView, parent);
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                // Set the text color of spinner item
                tv.setTextColor(Color.TRANSPARENT);

                // Return the view
                return tv;
            }
        };

        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(genderArrayAdapter);
        spinner.performClick();
    }

    public static void errorResponse(Context context, ResponseBody errorBody) {
        CommonModel responseModel=new Gson().fromJson(errorBody.charStream(),CommonModel.class);
        Toast.makeText(context, ""+responseModel.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
