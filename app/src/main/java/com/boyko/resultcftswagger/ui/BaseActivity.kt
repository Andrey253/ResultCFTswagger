package com.boyko.resultcftswagger.ui

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.boyko.resultcftswagger.InternetConnection
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.LoggedInUser
import com.boyko.resultcftswagger.models.UserEntity
import kotlinx.android.synthetic.main.loans_fragment.*
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.registr_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseActivity: AppCompatActivity() {
    val api = Client.apiService
    companion object{
        const val PREFS_NAME = "Bearer"
        const val KEY_NAME = "Bearer"
        const val ACCEPT ="*/*"
        const val CONTENTTYPE ="application/json"
        const val LOGIN ="login"
        const val REGISTRATION ="registration"
    }

    fun showFragment_left(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.left_in, R.anim.left_out)
                .replace(R.id.main_container,fragment, fragment.javaClass.name)
                .commit()
    }
    fun showFragment_right(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.right_in, R.anim.right_out)
                .replace(R.id.main_container,fragment, fragment.javaClass.name)
                .commit()
    }
    fun isConnect(): Boolean{
        return InternetConnection.checkConnection(applicationContext)
    }

    fun progressON(){
        progressBar?.setVisibility(View.VISIBLE)
    }

    fun progressOFF(){
        progressBar?.setVisibility(View.INVISIBLE)
    }

    fun authorization(bearer: String?){
        val editor = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)?.edit()
        editor?.putString(BaseFragment.KEY_NAME, bearer)
        editor?.apply()
    }

    fun toastShow(toast: String?){
        Toast.makeText(applicationContext, toast, Toast.LENGTH_LONG).show()
    }
    
    fun login_send_post(fragment: Fragment, user: LoggedInUser) {
        api.postLogin(ACCEPT, CONTENTTYPE, user)
                .enqueue(object : Callback<String?> {
                    override fun onResponse(call: Call<String?>, response: Response<String?>) {
                        if (response.isSuccessful) {
                            authorization(response.body())
                            showFragment_left(fragment)
                        } else {
                            toastShow(getString(R.string.error_login))
                            Log.e("mytag", "No DATA, code = ${response.code()}")
                        }
                    }
                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        Log.e("mytag", "onFailure $t")
                    }
                })
    }
    fun reg_send_post(fragment: Fragment) {
        val userForReg = userForReg()
        api.postReg(ACCEPT, CONTENTTYPE, userForReg)
                .enqueue(object : Callback<UserEntity?> {
                    override fun onResponse(call: Call<UserEntity?>, response: Response<UserEntity?>) {
                        if (response.isSuccessful) {
                            toastShow("Привет, ${response.body()?.name}")
                            login_send_post(fragment, userForReg)
                        } else {
                            toastShow(response.errorBody().toString())
                            Log.e("mytag", "No DATA, code = ${response.code()}")
                            Log.e("mytag", "response.raw()= ${response.raw()}")
                            Log.e("mytag", "response.body()= ${response.body()}")
                            Log.e("mytag", "response.errorBody()= ${response.errorBody()}")

                        }
                    }
                    override fun onFailure(call: Call<UserEntity?>, t: Throwable) {
                        Log.e("mytag", "onFailure $t")
                    }
                })
    }

    fun userCreate(): LoggedInUser {
        return     LoggedInUser(editText_username.text.toString(),editText_password.text.toString())
    }
    private fun userForReg(): LoggedInUser {
        return     LoggedInUser(editText_user_reg.text.toString(),editText_password_reg.text.toString())
    }

}