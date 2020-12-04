package com.boyko.resultcftswagger

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.LoggedInUser
import com.boyko.resultcftswagger.ui.CreateNewLoanFragment
import com.boyko.resultcftswagger.ui.LoansFragment
import com.boyko.resultcftswagger.ui.LoginFragment
import com.boyko.resultcftswagger.ui.RegisterFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.loans_fragment.*
import kotlinx.android.synthetic.main.login_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val PREFS_NAME = "Bearer"
private const val KEY_NAME = "Bearer"
private const val TAG = "mytag"

class MainActivity : AppCompatActivity(){
//        , LoginFragment.onClickFragmentListener {
//    override fun clicked() {
//        Log.e("mytag", "editText_username  $editText_username")
//        login(editText_username.text.toString(), editText_password.text.toString())
//    }

    val api = Client.apiService
    lateinit var mLoginFragment: LoginFragment
    lateinit var mRegisterFragment: RegisterFragment
    lateinit var mLoans: LoansFragment
    var editor: SharedPreferences.Editor? = null
    var tvFirst:TextView?=null
    var tvSecond:TextView?=null
    var tvFirstIsCheck=true


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFragment()
        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            showLoginFragment()
        if (sharedPref.contains(KEY_NAME)){
            showLoansFragment()
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
        mLoginFragment=LoginFragment()//.apply { listener=this@MainActivity }
        mRegisterFragment= RegisterFragment()
        mLoans = LoansFragment.newInstance("","")
    }

    private fun showLoginFragment() {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.left_in, R.anim.left_out)
                .replace(R.id.main_container, mLoginFragment, LoginFragment::class.java.name)
                .commit()
        firstSelected()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_container)
        if (fragment is LoansFragment) {
            finish()
            return
        }
        super.onBackPressed()
    }

    fun showLoansFragment() {
                    supportFragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .setCustomAnimations(R.anim.left_in, R.anim.left_out)
                            .replace(R.id.main_container, mLoans)
                            .commit()
    }
    fun createNewLoan() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .setCustomAnimations(R.anim.left_in, R.anim.left_out)
            .replace(R.id.main_container, mLoans, CreateNewLoanFragment::class.java.name)
            .commit()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> logout()

        }
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    fun logout() {
        var sharedPref = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPref?.let {
            it.edit().clear().commit() }
        onBackPressed()
    }
}
