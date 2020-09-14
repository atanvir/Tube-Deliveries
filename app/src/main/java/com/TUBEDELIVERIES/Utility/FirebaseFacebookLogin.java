package com.TUBEDELIVERIES.Utility;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FirebaseFacebookLogin extends AppCompatActivity {

    private static final String TAG = "LoginModel";
    private static final int FB_LOGIN = 121;
    CallbackManager callbackManager;
    AccessToken accessToken;
    private FirebaseAuth mAuth;
    private String email;
    private String username;
    private String name;
    private String socialid;
    private String image;
    private String f_name;
    private String l_name;
    private String id;
    private String gender;
    private String phone_number;
    private List<String> values = new CopyOnWriteArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        getFBKeyHash();
        fbSignIn();
    }

    // Add code to print out the key hash
    private void getFBKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.OWIN",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void fbSignIn() {
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
//// LoginManager.getInstance().logOut();
//
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                handleFacebookAccessToken(accessToken);
            }

            @Override
            public void onCancel() {
                Toast.makeText(FirebaseFacebookLogin.this, "Request Cancel", Toast.LENGTH_SHORT).show();
                Log.i("facebook onCancel----->", "cancel");
                Intent intent = new Intent();
                intent = null;
                setResult(FB_LOGIN, intent);
                finish();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(FirebaseFacebookLogin.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
//                if (error instanceof FacebookAuthorizationException) {
//                    if (AccessToken.getCurrentAccessToken() != null) {
//                        LoginManager.getInstance().logOut();
//                    }
//                }
            }
        });
    }

    // get user detail using firebase facebook login
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if(user!=null){
                                id = user.getProviderId();
                                if(email==null){
                                    email = "";
                                }else {
                                    email = user.getEmail();
                                    int index = email.indexOf("@");
                                    username = email.substring(0,index);
                                }
                            }
                            if (gender!=null){
                                gender = "";
                            }else {
                                gender = "male";
                            }

                            if (user != null) {
                                name = user.getDisplayName();
                                String[] u_name = null;
                                if(name.contains(" ")){
                                    u_name = name.split(" ");
                                    if(u_name.length>1){
                                        f_name = u_name[0];
                                        l_name = u_name[1];
                                    }else {
                                        f_name = u_name[0];
                                        l_name = u_name[0];
                                    }
                                }
                                socialid = token.getUserId();
//                                SharedPreference sharedPreference=SharedPreference.getInstance(FirebaseFacebookLogin.this);
//                                sharedPreference.saveData("socialLogin",socialid);
                                phone_number = user.getPhoneNumber();
                                Uri uri = user.getPhotoUrl();
                                image = String.valueOf(uri);
                                image = "https://graph.facebook.com/" + socialid + "/picture?type=large";
                                if(image==null){
                                    image = "";
                                }
                            }

                            Intent intent = new Intent();
                            intent.putExtra("id", id);
                            intent.putExtra("email", email);
                            intent.putExtra("username", username);
                            intent.putExtra("name", name);
                            intent.putExtra("f_name", f_name);
                            intent.putExtra("l_name", l_name);
                            intent.putExtra("socialid", socialid);
                            intent.putExtra("image", image);
                            intent.putExtra("gender", gender);
                            intent.putExtra("number",phone_number);
                            setResult(FB_LOGIN,intent);
                            finish();

                        } else {
                            Log.e("error",task.getException().getMessage());
                            CommonUtilities.snackBar(FirebaseFacebookLogin.this,task.getException().getMessage());

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();

                                }
                            },4000);



                        }

// ...
                    }
                });
    }

    //fb sign in finish
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


//signOut();
    }


    public void signOut() {
// [START auth_sign_out]

// [END auth_sign_out]
    }
}