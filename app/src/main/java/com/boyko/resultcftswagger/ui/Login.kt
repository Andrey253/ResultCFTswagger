package com.boyko.resultcftswagger.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.boyko.resultcftswagger.InternetConnection
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.LoggedInUser
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val PREFS_NAME = "Bearer"
private const val KEY_NAME = "Bearer"
private const val TAG = "mytag"

class Login : Fragment() {

    val api = Client.apiService
    var editor: SharedPreferences.Editor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View=inflater.inflate(R.layout.login_fragment,container,false)
        val btnLogin: Button =view.findViewById(R.id.btn_login)
        btnLogin.setOnClickListener{
            if(InternetConnection.checkConnection(context!!))
                login(editText_username.text.toString(),editText_password.text.toString())
            else
                Toast.makeText(context, "Отсутствует соединение с сетью", Toast.LENGTH_LONG).show()

        }
        return view
    }
    private fun login(username: String, password: String) {

        val user = LoggedInUser(username, password)
        Log.e("mytag", "LoggedInUser  $user")
        val call = api.postLogin(Loans.ACCEPT, Loans.CONTENTTYPE, user)

        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful) {

                    val bearer = response.body()
                    Log.e("mytag", "isSuccessful $bearer")
                    editor = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)?.edit()
                    editor?.putString(KEY_NAME, bearer)
                    editor?.apply()
                    showLoansFragment()
                } else {
                    val errorlogin = getString(R.string.error_login)
                    Toast.makeText(context, "$errorlogin", Toast.LENGTH_LONG).show()
                    Log.e("mytag", "No DATA, code = ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.e("mytag", "onFailure $t")
            }
        })
    }
    fun showLoansFragment() {
        fragmentManager?.beginTransaction()
                ?.addToBackStack(null)
                ?.setCustomAnimations(R.anim.left_in, R.anim.left_out)
                ?.replace(R.id.main_container, Loans())
                ?.commit()
    }
}