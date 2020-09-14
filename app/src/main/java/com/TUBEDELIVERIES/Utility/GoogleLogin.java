package com.TUBEDELIVERIES.Utility;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.TUBEDELIVERIES.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


/**
 * Created by mahipal singh on 5/9/18.
 */
public class GoogleLogin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginModel";
    private static final int RC_SIGN_IN = 9001;
    private static final int GOOGLE_DATA = 122;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private String image;
    private String gender;
    private String socialid;
    private String email;
    private String name;
    private Intent signInIntent;
    private String f_name="";
    private String l_name = "";
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .build();*/
// ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestProfile()
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                signIn();
            }
        },4000);


//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();

        //gmailSignIn();



    }

//    void showProgressDialog(boolean show) {
//        if (show) {
//            serviceProgressDialog = new ServiceProgressDialog(this);
//            serviceProgressDialog.showCustomProgressDialog();
//        } else {
//            if (serviceProgressDialog != null) {
//                serviceProgressDialog.hideProgressDialog();
//            }
//        }
//    }


    //    gmail sigin
    private void gmailSignIn() {
        if (mGoogleApiClient.hasConnectedApi(Auth.GOOGLE_SIGN_IN_API)) {
            mGoogleApiClient.clearDefaultAccountAndReconnect();
        } else {
            signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);

        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();
//        CommonUtilities.showLoadingDialog(this);
//       // showProgressDialog(true);
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//        } else {
////            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    // hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

//    private void showProgressDialog() {
//        ProgressDialogUtils.showProgressDialog(this, "Please Wait...", false);
//    }
//
//    private void hideProgressDialog() {
//        ProgressDialogUtils.dismissProgressDialog();
//    }

//    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
//        if (result.isSuccess()) {
//            CommonUtilities.showLoadingDialog(this);
//
////            showProgressDialog(true);
//            GoogleSignInAccount acct = result.getSignInAccount();
//            Log.i("user name", "" + acct.getDisplayName());
//            Uri personPhoto = acct.getPhotoUrl();
//            if (personPhoto != null) {
//                image = personPhoto.toString();
//            } else {
//                image = "";
//            }
//            gender = "Male";
//            socialid = acct.getId();
//            email = acct.getEmail();
//            name = acct.getDisplayName();
//            if(name==null){
//                name="";
//
//            }
//            String[] u_name = null;
//          /*  if (name.contains(" ")) {
//                u_name = name.split(" ");
//                if (u_name.length > 1) {
//                    f_name = u_name[0];
//                    l_name = u_name[1];
//                } else {
//                    f_name = u_name[0];
//                    l_name = u_name[0];
//                }
//
//
//            }*/
//
//            signOut();
//            Log.i("Gmail LoginModel", "" + name + "" + email + "" + socialid + "" + image);
//        } else if (result.getStatus().getStatusCode() == 12501) {
//            CommonUtilities.dismissLoadingDialog();
//
//            //showProgressDialog(false);
//            Toast.makeText(this, "Request Cancel", Toast.LENGTH_SHORT).show();
//            // SnackBar.getSnackBar().showBar(GoogleLogin.this, "Request Cancel", Toast.LENGTH_SHORT);
//            Intent intent = new Intent();
//            intent = null;
//            setResult(GOOGLE_DATA, intent);
//            finish();
//
//        } else {
//            CommonUtilities.dismissLoadingDialog();
//
//            //showProgressDialog(false);
//            //finish();
//
//        }
//    }

    private void signOut() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (status.getStatus().getStatusCode() == 0) {
                                Intent intent = new Intent();
                                intent.putExtra("socialid", socialid);
                                intent.putExtra("email", email);
                                intent.putExtra("name", name);
                                intent.putExtra("f_name", f_name);
                                intent.putExtra("l_name", l_name);
                                intent.putExtra("image", image);
                                setResult(GOOGLE_DATA, intent);
                                CommonUtilities.dismissLoadingDialog();

                              //  showProgressDialog(false);
                                finish();
                            }
                        }
                    });
                } else {
                    signOut();
                }
            }
        });

    }
    //gmail signin finish

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            handleSignInResult(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            CommonUtilities.snackBar(GoogleLogin.this, "Authentication failed");
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void handleSignInResult(FirebaseUser result) {
        Log.d(TAG, "handleSignInResult:" + result);
        Log.d(TAG, "google uid:" + result.getUid());
        if (result!=null) {
            CommonUtilities.showLoadingDialog(this);
            Uri personPhoto = result.getPhotoUrl();
            if (personPhoto != null) {
                image = personPhoto.toString();
            } else {
                image = "";
            }
            gender = "Male";
            socialid = result.getUid();
            email = result.getEmail();
            name = result.getDisplayName();
            if(name==null){
                name="";

            }
            String[] u_name = null;
            if (name.contains(" ")) {
                u_name = name.split(" ");
                if (u_name.length > 1) {
                    f_name = u_name[0];
                    l_name = u_name[1];
                } else {
                    f_name = u_name[0];
                    l_name = u_name[0];
                }


            }

            signOut();
            Log.i("Gmail LoginModel", "" + name + "" + email + "" + socialid + "" + image);
        }
//        else  {
//            CommonUtilities.dismissLoadingDialog();
//
//            //showProgressDialog(false);
//            Toast.makeText(this, "Request Cancel", Toast.LENGTH_SHORT).show();
//            // SnackBar.getSnackBar().showBar(GoogleLogin.this, "Request Cancel", Toast.LENGTH_SHORT);
//            Intent intent = new Intent();
//            intent = null;
//            setResult(GOOGLE_DATA, intent);
//            finish();
//
//        }
//        else {
//            CommonUtilities.dismissLoadingDialog();
//
//            //showProgressDialog(false);
//            //finish();
//
//        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtilities.dismissLoadingDialog();
//        showProgressDialog(false);
        finish();
    }
}
