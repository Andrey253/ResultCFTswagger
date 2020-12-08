package com.boyko.resultcftswagger.repositiry

import android.content.Context
import com.boyko.resultcftswagger.ActivityLoans

class LoginRepository (val context: Context){

    fun isAuthorized(): Boolean {
        val sharedPref = context.getSharedPreferences(ActivityLoans.PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref?.contains(ActivityLoans.KEY_NAME) == true
    }

    fun logOut() {
        val sharedPref = context.getSharedPreferences(ActivityLoans.PREFS_NAME, Context.MODE_PRIVATE)
        sharedPref?.edit()?.remove(ActivityLoans.KEY_NAME)?.apply()
    }

    fun authorization(bearer: String?) {
        val editor = context.getSharedPreferences(ActivityLoans.PREFS_NAME, Context.MODE_PRIVATE)?.edit()
        editor?.putString(ActivityLoans.KEY_NAME, bearer)
        editor?.apply()
    }
}