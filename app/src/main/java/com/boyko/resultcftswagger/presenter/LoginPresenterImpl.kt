package com.boyko.resultcftswagger.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.boyko.resultcftswagger.LoginActivity
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.errors.ErrorsMake
import com.boyko.resultcftswagger.models.LoggedInUser
import com.boyko.resultcftswagger.models.UserEntity
import com.boyko.resultcftswagger.repositiry.LoginRepository
import com.boyko.resultcftswagger.ui.Login
import com.boyko.resultcftswagger.ui.Register
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import java.util.logging.Handler

class LoginPresenterImpl(private val loginRepository: LoginRepository, private val fManager: FragmentManager) : LoginPresenter {

    private var mLogin : Login? = null
    private var mRegis : Register? = null
    private var mLoginActivity : LoginActivity? = null
    private var errors = ErrorsMake()
    private val api = Client.apiService

    override fun attachView(
            l : Login,
            r : Register,
            la: LoginActivity
    ) {
        this.mLogin         = l
        this.mRegis         = r
        this.mLoginActivity = la
    }

    override fun detachView() {
        this.mLogin        = null
        this.mRegis        = null
        this.mLoginActivity= null
    }

    override fun onLoginButtonClicked(
            context: Context,
            intentToStart: Intent,
            activityToFinish: Activity,
            userLoggedInUser: LoggedInUser,
            s1: String, s2: String) {

        api.postLogin(ACCEPT, CONTENTTYPE, userLoggedInUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .timeout(7, TimeUnit.SECONDS)
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(authKey: String) {
                        loginRepository.authorization(authKey)
                        context.startActivity(intentToStart)
                        activityToFinish.finish()
                        Toast.makeText(context, (userLoggedInUser.name + s1), Toast.LENGTH_LONG).show()
                    }

                    override fun onError(e: Throwable) {
                        sendErrors(context, errors.errorToString(e.message.toString()))
                    }

                    override fun onComplete() {
                    }
                })
    }

    private fun sendErrors(context: Context, e: String) {
        val mainHandler = android.os.Handler(context.mainLooper)
        val runnable = Runnable {
            Toast.makeText(context, e,Toast.LENGTH_LONG).show()
        }
        mainHandler.post(runnable)
    }

    override fun clickRegistration(context: Context, intent: Intent, activity: Activity, userLoggedInUser: LoggedInUser, s1: String, s2: String) {
        api.postReg(ACCEPT, CONTENTTYPE, userLoggedInUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .timeout(7, TimeUnit.SECONDS)
                .subscribe(object : Observer<UserEntity> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(userEntity: UserEntity) {
                        Toast.makeText(context, userEntity.name + s1, Toast.LENGTH_LONG).show()
                        onLoginButtonClicked(context, intent, activity, userLoggedInUser, s1, s2)
                    }

                    override fun onError(e: Throwable) {
                        sendErrors(context, errors.errorToString(e.message.toString()))
                        //Log.e("mytag", e.message.toString())
                        //Toast.makeText(context, errors.errorToString(e.message.toString()),Toast.LENGTH_LONG).show()
                    }

                    override fun onComplete() {
                    }
                })
    }

    override fun clickToLogin() {
        mLogin?.let { showFragment_right(it) }
    }

    fun showFragment_right(fragment: Fragment) {
        fManager.beginTransaction()
            .addToBackStack(null)
            .setCustomAnimations(R.anim.right_in, R.anim.right_out)
            .replace(R.id.main_container, fragment, fragment.javaClass.name)
            .commit()
    }
    override fun clickToRegistration() {
        mRegis?.let { showFragment_right(it) }
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
            mRegis?.showUsernameError()
            mRegis?.toggleRegButton(enable = false)
        } else if (!isPasswordValid(password)) {
            mRegis?.showPasswordError()
            mRegis?.toggleRegButton(enable = false)
        } else if (!isRepeatPasswordValid(password, passwordrepeat)) {
            mRegis?.showPasswordRepeatError()
            mRegis?.toggleRegButton(enable = false)
        } else {
            mRegis?.toggleRegButton(enable = true)
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
    companion object{
        const val ACCEPT ="*/*"
        const val CONTENTTYPE ="application/json"
    }
}
