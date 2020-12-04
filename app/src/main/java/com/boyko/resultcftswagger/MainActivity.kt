package com.boyko.resultcftswagger

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.boyko.resultcftswagger.ui.*
import com.boyko.resultcftswagger.ui.itemfragment.CreatedNewLoan
import com.boyko.resultcftswagger.ui.itemfragment.LoanItem

private const val PREFS_NAME = "Bearer"
private const val KEY_NAME = "Bearer"
private const val TAG = "mytag"
class MainActivity : AppCompatActivity(){

    lateinit var mLoginFragment: Login
    lateinit var mRegister: Register
    lateinit var mLoans: Loans
    lateinit var mCreateNewLoan: CreateNewLoan
    var tvFirst:TextView?=null
    var tvSecond:TextView?=null
    var tvFirstIsCheck=true
    var itemMenu: MenuItem? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFragment()
        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        if (sharedPref.contains(KEY_NAME))
            showLoansFragment()
        else showLoginFragment()
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
        mLoginFragment=Login()//.apply { listener=this@MainActivity }
        mRegister= Register()
        mLoans = Loans.newInstance("","")
        mCreateNewLoan = CreateNewLoan()
    }

    private fun showLoginFragment() {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.left_in, R.anim.left_out)
                .replace(R.id.main_container, mLoginFragment)
                .commit()
        firstSelected()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_container)
        when(fragment)
        {
            is Loans -> finish()
            is CreatedNewLoan -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, mLoans)
                        .commit()
            }
            is LoanItem -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, mLoans)
                        .commit()
            }
            else -> super.onBackPressed()
        }
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
                .replace(R.id.main_container, mCreateNewLoan)
                .commit()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        itemMenu = item
        when (item.itemId) {
            R.id.action_logout -> logout()
        }
        return when (item.itemId) {
            R.id.action_logout -> true
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
        finish()
    }
}
