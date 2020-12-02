package com.boyko.resultcftswagger

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.models.LoggedInUser
import com.boyko.resultcftswagger.ui.LoanConditionsFragment
import com.boyko.resultcftswagger.ui.LoginFragment
import com.boyko.resultcftswagger.ui.SecondFragment
import kotlinx.android.synthetic.main.login_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val PREFS_NAME = "Bearer"
private const val KEY_NAME = "Bearer"
private const val TAG = "mytag"

class MainActivity : AppCompatActivity(), LoginFragment.onClickFragmentListener {
    override fun clicked() {

        login(editText_username.text.toString(), editText_password.text.toString())
    }

    val api = Client.apiService
    lateinit var mLoginFragment: LoginFragment
    lateinit var mSecondFragment: SecondFragment
    var editor: SharedPreferences.Editor? = null
    var tvFirst:TextView?=null
    var tvSecond:TextView?=null
    var tvFirstIsCheck=true


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        if (sharedPref.contains(KEY_NAME)){
            getLoansAll()
        } else{
            initFragment()
            showFirstFragment()

            tvFirst?.setOnClickListener {
                firstSelected()
                showFirstFragment() }

            tvSecond?.setOnClickListener {
                secondSelected()
                showSecondFragment() }
        }
    }

    private fun firstSelected(){
        tvFirst?.setBackgroundResource(R.drawable.bg_selected)
        tvSecond?.setBackgroundResource(R.drawable.bg_un_selected)
        tvFirstIsCheck=true
    }

    private fun secondSelected(){
        tvSecond?.setBackgroundResource(R.drawable.bg_selected)
        tvFirst?.setBackgroundResource(R.drawable.bg_un_selected)
        tvFirstIsCheck=false
    }

    private fun initFragment() {
        mLoginFragment=LoginFragment().apply { listener=this@MainActivity }
        mSecondFragment= SecondFragment()
    }

    private fun showFirstFragment() {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.left_in, R.anim.left_out)
                .replace(R.id.main_container, mLoginFragment, LoginFragment::class.java.name)
                .commit()
        firstSelected()
    }
    private fun showSecondFragment(){
        supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.right_in, R.anim.right_out, R.anim.left_in, R.anim.left_out)
                .replace(R.id.main_container, mSecondFragment, SecondFragment::class.java.name)
                .commit()
        secondSelected()
    }

    override fun onBackPressed() {
        if(!tvFirstIsCheck) {
            firstSelected()
            tvFirstIsCheck=true
        }
        super.onBackPressed()
    }
    private fun login(username: String, password: String) {

        val user = LoggedInUser(username, password)
        Log.e("mytag", "LoggedInUser  $user")
        val call = api.postLogin(LoanConditionsFragment.ACCEPT, LoanConditionsFragment.CONTENTTYPE, user)

        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful) {

                    val bearer = response.body()
                    Log.e("mytag", "isSuccessful $bearer")
                    editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
                    editor?.putString(KEY_NAME, bearer)
                    editor?.apply()
                    getLoansAll()
                } else {
                    Log.e("mytag", "No DATA, code = ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.e("mytag", "onFailure $t")
            }
        })
    }
    fun getLoansAll() {

        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val bearer = sharedPref.getString(KEY_NAME, null)
        val call = bearer?.let { api.getLoansAll(LoanConditionsFragment.ACCEPT, it) }
        call?.enqueue(object : Callback<List<Loan>> {
            override fun onResponse(call: Call<List<Loan>?>, response: Response<List<Loan>?>) {
                if (response.isSuccessful) {
                    val loansAll = response.body()
                    Log.e("mytag", "isSuccessful $loansAll")

                    var m = LoanConditionsFragment()
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.main_container, m, LoginFragment::class.java.name)
                            .commit()

                } else {
                    Log.e("mytag", "No DATA, code = ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Loan>?>, t: Throwable) {
                Log.e("mytag", "onFailure $t")
            }
        })
    }
}
