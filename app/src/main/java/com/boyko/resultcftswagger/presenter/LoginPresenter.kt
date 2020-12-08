package com.boyko.resultcftswagger.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.boyko.resultcftswagger.LoginActivity
import com.boyko.resultcftswagger.models.LoggedInUser
import com.boyko.resultcftswagger.repositiry.LoginRepository
import com.boyko.resultcftswagger.ui.Login
import com.boyko.resultcftswagger.ui.Register

interface LoginPresenter {

    fun attachView(login: Login, register: Register, loginActivity: LoginActivity)

    fun detachView()

    fun onLoginDataUpdated(username: String, password: String, passwordrepeat: String)

    //login fragment
    fun onLoginButtonClicked(context: Context, intent: Intent, activity: Activity, userLoggedInUser: LoggedInUser, loginRepository: LoginRepository, s1: String, s2:String)

    fun clickToRegistration()

    //regestration fragment
    fun clickRegistration(context: Context, intent: Intent, activity: Activity, userLoggedInUser: LoggedInUser, loginRepository: LoginRepository, s1: String, s2:String)

    fun clickToLogin()




}