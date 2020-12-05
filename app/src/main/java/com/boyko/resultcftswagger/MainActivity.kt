package com.boyko.resultcftswagger

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.boyko.resultcftswagger.repositiry.LoginRepository
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
    lateinit var loginRepository: LoginRepository
    var tvFirst:TextView?=null
    var tvSecond:TextView?=null
    var tvFirstIsCheck=true
    var itemMenu: MenuItem? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginRepository = LoginRepository(applicationContext)

        initFragment()

        if (loginRepository.isAuthorized())
            showLoansFragment()
        else showLoginFragment()
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
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_container)
        when(fragment)
        {
            is Loans -> finish()
            is CreatedNewLoan -> {
                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.left_in, R.anim.left_out)
                        .replace(R.id.main_container, mLoans)
                        .commit()
            }
            is LoanItem -> {
                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.left_in, R.anim.left_out)
                        .replace(R.id.main_container, mLoans)
                        .commit()
            }
            is Register -> {
                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.right_in, R.anim.right_out)
                        .replace(R.id.main_container, mLoginFragment)
                        .commit()
            }
            else -> super.onBackPressed()
        }
    }

    fun showLoansFragment() {
                    supportFragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .setCustomAnimations(R.anim.left_in, R.anim.left_out)
                            .replace(R.id.main_container, mLoans, "loan_tag")
                            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        itemMenu = item
        when (item.itemId) {
            R.id.action_logout -> {
                loginRepository.logOut()
                finish()
            }
        }
        return when (item.itemId) {
            R.id.action_logout -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}
