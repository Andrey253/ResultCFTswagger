package com.boyko.resultcftswagger

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.fragment.*
import com.boyko.resultcftswagger.models.LoggedInUser
import com.example.minh98.fragmentaddreplaceanimation.FirstFragment
import com.example.minh98.fragmentaddreplaceanimation.SecondFragment
import kotlinx.android.synthetic.main.login_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val PREFS_NAME = "Bearer"
private const val KEY_NAME = "Bearer"

class MainActivity : AppCompatActivity(), FirstFragment.onClickFragmentListener {
    override fun clicked() {

        login(idusername.text.toString(), idpassword.text.toString())
    }
    private var editor: SharedPreferences.Editor? = null
    private val api = Client.apiService
    lateinit var mFirstFragment: FirstFragment
    lateinit var mSecondFragment: SecondFragment
    var tvFirst:TextView?=null
    var tvSecond:TextView?=null
    var tvFirstIsCheck=true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFragment()
        showFirstFragment()
        tvFirst= findViewById(R.id.tvFirst) as TextView
        tvSecond= findViewById(R.id.tvSecond) as TextView

        tvFirst?.setOnClickListener {
            firstSelected()
            showFirstFragment() }

        tvSecond?.setOnClickListener {
            secondSelected()
            showSecondFragment() }
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
        mFirstFragment=FirstFragment().apply { listener=this@MainActivity }
        mSecondFragment= SecondFragment()
    }

    private fun showFirstFragment() {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.left_in, R.anim.left_out)
                .replace(R.id.main_layout,mFirstFragment,FirstFragment::class.java.name)
                .commit()
        firstSelected()
    }
    private fun showSecondFragment(){
        supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.right_in, R.anim.right_out, R.anim.left_in, R.anim.left_out)
                .replace(R.id.main_layout,mSecondFragment,SecondFragment::class.java.name)
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
        val sharedPref = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = sharedPref?.edit()
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
