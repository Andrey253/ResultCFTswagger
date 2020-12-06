package com.boyko.resultcftswagger.ui

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.boyko.resultcftswagger.util.InternetConnection
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.LoggedInUser
import com.boyko.resultcftswagger.models.UserEntity
import com.boyko.resultcftswagger.repositiry.LoginRepository
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

    fun toastShow(toast: String?){
        Toast.makeText(applicationContext, toast, Toast.LENGTH_LONG).show()
    }
    
    fun login_send_post(fragment: Fragment, user: LoggedInUser, loginRepository: LoginRepository) {
        api.postLogin(ACCEPT, CONTENTTYPE, user)
                .enqueue(object : Callback<String?> {
                    override fun onResponse(call: Call<String?>, response: Response<String?>) {
                        if (response.isSuccessful) {
                            loginRepository.authorization(response.body())
                            showFragment_left(fragment)
                            toastShow("${user.name}, вы успешно авторизованы")
                        } else {
                            toastShow(getString(R.string.error_login))
                        }
                    }
                    override fun onFailure(call: Call<String?>, t: Throwable) {
                    }
                })
    }
    fun reg_send_post(fragment: Fragment, loginRepository: LoginRepository) {
        val userForReg = userForReg()
        api.postReg(ACCEPT, CONTENTTYPE, userForReg)
                .enqueue(object : Callback<UserEntity?> {
                    override fun onResponse(call: Call<UserEntity?>, response: Response<UserEntity?>) {
                        if (response.isSuccessful) {
                            toastShow("${response.body()?.name}, вы успешно зарегестрированы")
                            login_send_post(fragment, userForReg, loginRepository)
                        } else {
                            toastShow(response.errorBody().toString())
                        }
                    }
                    override fun onFailure(call: Call<UserEntity?>, t: Throwable) {}
                })
    }

    fun userCreate(): LoggedInUser {
        return     LoggedInUser(editText_username.text.toString(),editText_password.text.toString())
    }
    private fun userForReg(): LoggedInUser {
        return     LoggedInUser(editText_user_reg.text.toString(),editText_password_reg.text.toString())
    }

}