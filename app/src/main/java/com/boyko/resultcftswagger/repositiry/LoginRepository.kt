package com.boyko.resultcftswagger.repositiry

import android.content.Context
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
        fun logOurt(){
            val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            sharedPref?.edit()?.clear()?.apply()
        }



}