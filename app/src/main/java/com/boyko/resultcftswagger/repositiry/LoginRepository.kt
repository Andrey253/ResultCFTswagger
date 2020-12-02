package com.boyko.resultcftswagger.repositiry

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.LoggedInUser
import com.boyko.resultcftswagger.ui.LoanConditionsFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private const val PREFS_NAME = "Bearer"
private const val KEY_NAME = "Bearer"
class LoginRepository (val user: String, val password: String, val c: Context){

    private var editor: SharedPreferences.Editor? = null
    private val api = Client.apiService

    fun login(username: String, password: String) {
        val sharedPref = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        val user = LoggedInUser(username, password)
        Log.e("mytag", "LoggedInUser  $user")
        val call = api.postLogin(LoanConditionsFragment.ACCEPT, LoanConditionsFragment.CONTENTTYPE, user)

        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful) {

                    val bearer = response.body()
                    Log.e("mytag", "isSuccessful $bearer")
                    editor?.putString(KEY_NAME, bearer)
                    editor?.apply()

                } else {
                    Log.e("mytag", "No DATA, code = ${response.code()}")
                }
            }
            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.e("mytag", "onFailure $t")
            }
        })
    }
}