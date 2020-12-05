package com.boyko.resultcftswagger.repositiry

import android.content.Context
import android.content.SharedPreferences
import android.text.BoringLayout
import android.util.Log
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.LoggedInUser
import com.boyko.resultcftswagger.ui.BaseFragment
import com.boyko.resultcftswagger.ui.Loans

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private const val PREFS_NAME = "Bearer"
private const val KEY_NAME = "Bearer"
class LoginRepository (val context: Context){

        fun isAuthorized(): Boolean{
            val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPref?.contains(KEY_NAME)==true
        }

        fun logOut(){
            val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            sharedPref?.edit()?.clear()?.apply()
        }



}