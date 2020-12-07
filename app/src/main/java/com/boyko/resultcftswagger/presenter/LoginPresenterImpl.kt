package com.boyko.resultcftswagger.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings.Global.getString
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.boyko.resultcftswagger.ActivityLoans
import com.boyko.resultcftswagger.LoginActivity
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.LoggedInUser
import com.boyko.resultcftswagger.models.UserEntity
import com.boyko.resultcftswagger.repositiry.LoginRepository
import com.boyko.resultcftswagger.ui.CreateNewLoan
import com.boyko.resultcftswagger.ui.Loans
import com.boyko.resultcftswagger.ui.Login
import com.boyko.resultcftswagger.ui.Register
import com.boyko.resultcftswagger.ui.itemfragment.LoanItem
import com.boyko.resultcftswagger.util.InternetConnection
import kotlinx.android.synthetic.main.registr_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
class LoginPresenterImpl(private val loginRepository: LoginRepository, private val fManager: FragmentManager): LoginPresenter{

    private var viewLogin : Login? = null
    private var viewRegis : Register? = null
    private var loginActivity : LoginActivity? = null
    private val api = Client.apiService

    //private var viewRegis : Register? = null


    override fun attachView(
        viewLogin         : Login,
        viewRegis         : Register,
        loginActivity : LoginActivity
        ) {
        this.viewLogin    = viewLogin
        this.viewRegis    = viewRegis
        this.loginActivity= loginActivity
    }

    override fun detachView() {
        this.viewLogin    = null
        this.viewRegis    = null
        this.loginActivity= null
    }


    override fun onLoginButtonClicked(username: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun clickLogin(
            context: Context,
            intent: Intent,
            activity: Activity,
            user: LoggedInUser,
            loginRepository: LoginRepository,
            s1: String, s2:String) {

        api.postLogin(LoginActivity.ACCEPT, LoginActivity.CONTENTTYPE, user)
                .enqueue(object : Callback<String?> {
                    override fun onResponse(call: Call<String?>, response: Response<String?>) {
                        if (response.isSuccessful) {

                            loginRepository.authorization(response.body())
                            context.startActivity(intent)
                            activity.finish()

                            Toast.makeText(context, (user.name + s1), Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, s2, Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<String?>, t: Throwable) {
                    }
                })

        //check_connect_and_run { login_send_post(intent, userCreate(), loginRepository) }
    }

    override fun clickRegistration(context: Context, intent: Intent, activity: Activity, userReg: LoggedInUser, loginRepository: LoginRepository, s1: String, s2:String) {
        api.postReg(LoginActivity.ACCEPT, LoginActivity.CONTENTTYPE, userReg)
                .enqueue(object : Callback<UserEntity?> {
                    override fun onResponse(call: Call<UserEntity?>, response: Response<UserEntity?>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, response.body()?.name + s1, Toast.LENGTH_LONG).show()
                            clickLogin(context, intent, activity, userReg, loginRepository, s1, s2)
                        } else {
                            Toast.makeText(context, s2, Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<UserEntity?>, t: Throwable) {}
                })
        //check_connect_and_run { reg_send_post(intent,loginRepository) }
    }
    override fun clickToLogin() {
        viewLogin?.let { showFragment_right(it) }
    }

    fun showFragment_right(fragment: Fragment) {
        fManager.beginTransaction()
            .addToBackStack(null)
            .setCustomAnimations(R.anim.right_in, R.anim.right_out)
            .replace(R.id.main_container,fragment, fragment.javaClass.name)
            .commit()
    }
    override fun clickToRegistration() {
        viewRegis?.let { showFragment_right(it) }
    }
//    fun check_connect_and_run(f: () -> Unit){
//        if (isConnect())
//            f()
//        else {
//            progressOFF()
//            Toast.makeText(applicationContext, getString(R.string.no_connection), Toast.LENGTH_LONG).show()
//        }
//    }
    override fun onLoginDataUpdated(username: String, password: String, passwordrepeat: String) {
    handleLoginResult(username, password, passwordrepeat)
    }

    private fun handleLoginResult(
        username: String,
        password: String,
        passwordrepeat: String
    ) {
        if (!isUserNameValid(username)) {
            viewRegis?.showUsernameError()
            viewRegis?.toggleRegButton(enable = false)
        } else if (!isPasswordValid(password)) {
            viewRegis?.showPasswordError()
            viewRegis?.toggleRegButton(enable = false)
        } else if (!isRepeatPasswordValid(password, passwordrepeat)) {
            viewRegis?.showPasswordRepeatError()
            viewRegis?.toggleRegButton(enable = false)
        } else {
            viewRegis?.toggleRegButton(enable = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.isNotBlank() && username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            false
        }
    }
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
    private fun isRepeatPasswordValid(password: String, repeatpassword: String): Boolean {
        return password.equals(repeatpassword)
    }
}
