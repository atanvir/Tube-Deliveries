package com.TUBEDELIVERIES.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.SharedPrefrence.SPreferenceKey;
import com.TUBEDELIVERIES.SharedPrefrence.SharedPreferenceWriter;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_TIMER = 3000;
    private static final int PERMISSION_REQUEST_CODE = 12;

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_acitivity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getDeviceToken();
        CommonUtilities.getHashKey(this);

        if (checkPermissions()) {
                try {
                    final Timer timer = new Timer();
                    TimerTask task = new TimerTask() {

                        @Override
                        public void run() {
                            if (SharedPreferenceWriter.getInstance(SplashActivity.this).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())) {
                                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            } else {
                                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    };
                    timer.schedule(task, 2000);
                } catch (Exception ex) {
                }
            }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            switch (requestCode) {
                case PERMISSION_REQUEST_CODE:
                    if (SharedPreferenceWriter.getInstance(SplashActivity.this).getString(SPreferenceKey.ISLOGIN).equals(ParamEnum.LOGIN.theValue())) {
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    } else {
                        Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(mainIntent);
                        finish();

                    }
                    break;
            }
        } else {
            Toast.makeText(this, "Please accept permissions due to security purpose", Toast.LENGTH_SHORT).show();
            checkPermissions();
        }
    }

    private void getDeviceToken() {

        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    if (SharedPreferenceWriter.getInstance(SplashActivity.this).getString(SPreferenceKey.DEVICETOKEN).isEmpty()) {
                        String token = FirebaseInstanceId.getInstance().getToken();
                        Log.e("Generated Device Token", "-->" + token);
                        if (token == null) {
                            getDeviceToken();
                        } else {

                            SharedPreferenceWriter.getInstance(SplashActivity.this).writeStringValue(SPreferenceKey.DEVICETOKEN, token);
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



}
