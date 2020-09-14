//package com.JustBite.KotlinClass
//
//import android.content.Intent
//import android.support.v7.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.view.WindowManager
//import com.JustBite.R
//import butterknife.ButterKnife
//import com.JustBite.Activity.ForgotPasswordActivity
//import com.JustBite.Activity.SignUpActivity
//import com.JustBite.Model.CommonModel
//import com.JustBite.Retrofit.ServicesConnection
//import com.JustBite.SharedPrefrence.SPreferenceKey
//import com.JustBite.SharedPrefrence.SharedPreferenceWriter
//import com.JustBite.Utility.*
//import kotlinx.android.synthetic.main.activity_login.*
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class LoginActivity : AppCompatActivity(), View.OnClickListener {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//        ButterKnife.bind(this)
//        window.setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
//        ed_login_password!!.transformationMethod = AsteriskPasswordTransformationMethod()
//
//
//
//        btnLogin.setOnClickListener(this)
//        tv_Sign_Up.setOnClickListener(this)
//        tv_forget_password.setOnClickListener(this)
//        img_google.setOnClickListener(this)
//
//    }
//
//    override fun onClick(view: View) {
//        when (view.id) {
//
//            R.id.tv_forget_password -> startActivity(Intent(this, ForgotPasswordActivity::class.java))
//
//            R.id.tv_Sign_Up -> startActivity(Intent(this, SignUpActivity::class.java))
//
//            R.id.btnLogin -> {
//                if(loginValidation())
//                     loginServiceHit()
//            }
//
//            R.id.img_google ->googleLogin()
//
//            R.id.img_fb ->fbLogin()
//        }
//    }
//
//
//    //////////login api///////
//    fun loginServiceHit() {
//        if (CommonUtilities.isNetworkAvailable(this)) {
//
//
//            var type = ""
//
//            if (ed_login_email.text!!.toString().matches(CommonUtilities.emailPattern.toRegex()))
//                type = "email"
//            else
//                type = "phone"
//
//
//
//            try {
//                val servicesInterface = ServicesConnection.getInstance().createService()
//                val loginCall = servicesInterface.login(ed_login_email.text!!.toString(),
//                        ed_login_password.text!!.toString(), type, ParamEnum.ANDROID.theValue(), ed_login_email.text!!.toString())
//
//                Log.w("DeviceToken-------->", SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.DEVICETOKEN))
//
//                     ServicesConnection.getInstance().enqueueWithoutRetry(loginCall,
//                             this,
//                             true,
//                             object :Callback<CommonModel>{
//                                 override fun onFailure(call: Call<CommonModel>, t: Throwable) {
//                                     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                                 }
//
//                                 override fun onResponse(call: Call<CommonModel>, response: Response<CommonModel>) {
//                                     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                                 }
//                             })
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//        } else {
//
//            CommonUtilities.snackBar(this, getString(R.string.no_internet))
//
//        }
//    }
//
//    private fun loginValidation():Boolean{
//
//        if (ed_login_email!!.text.toString().length <= 0) {
//            CommonUtilities.snackBar(this, "Enter phone /email id")
//            return false
//        } else if (ed_login_password!!.text.toString().isEmpty()) {
//            CommonUtilities.snackBar(this, getString(R.string.enter_pass))
//            return false
//        }
//
//        return true
//    }
//
//    fun googleLogin(){
//
//        val intent =Intent(this,GoogleLogin::class.java)
//        startActivityForResult(intent, ContantKotin.GOOGLE_DATA)
//
//    }
//
//    private fun fbLogin() {
//        val intent = Intent(this@LoginActivity, FirebaseFacebookLogin::class.java)
//        startActivityForResult(intent,ContantKotin.FACEBOOK_DATA)
//    }
//}
