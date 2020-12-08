package com.boyko.resultcftswagger

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import com.boyko.resultcftswagger.di.LoansPresenterFactory
import com.boyko.resultcftswagger.presenter.LoansPresenter
import com.boyko.resultcftswagger.repositiry.LoginRepository
import com.boyko.resultcftswagger.ui.*
import com.boyko.resultcftswagger.ui.itemfragment.CreatedNewLoan
import com.boyko.resultcftswagger.ui.itemfragment.LoanItem
const val KEY_THEME = "prefs.theme"
const val THEME_UNDEFINED = -1
const val THEME_LIGHT = 0
const val THEME_DARK = 1


class ActivityLoans : AppCompatActivity() {

    private val mLoans          by lazy {  Loans        .newInstance("", presenter!!) }
    private val mCreateNewLoan  by lazy {  CreateNewLoan.newInstance("", presenter!!) }
    private var loginRepository : LoginRepository? = null
    private var presenter       : LoansPresenter? = null


    private val sharedPrefs by lazy {  getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loans)
        //initTheme()
        loginRepository = LoginRepository(applicationContext)
        initPresenter()
        presenter?.showFragmentLeft(mLoans)
        Log.e("mytag", "onCreate AcLs ")

    }

    private fun initPresenter() {

        presenter = LoansPresenterFactory.create(applicationContext, supportFragmentManager)


        presenter?.attachView(mLoans, mCreateNewLoan)

    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.main_loans_container)) {
            is Loans -> finish()
            is CreatedNewLoan -> {
                presenter?.showFragmentRight(mLoans)
                presenter?.getAllLoans(applicationContext, getString(R.string.no_connection))
            }
            is CreateNewLoan -> {presenter?.showFragmentRight(mLoans)}
            is LoanItem ->      {presenter?.showFragmentRight(mLoans)}
            else -> super.onBackPressed()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                loginRepository?.logOut()
                finish()
            }
            R.id.action_darck -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
            R.id.action_light -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
        }
        return when (item.itemId) {
            R.id.action_logout -> true
            R.id.action_darck -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    //////////Theme
    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveTheme(prefsMode)
    }
    private fun initTheme() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
//            themeSystem.visibility = View.VISIBLE
//        } else {
//            themeSystem.visibility = View.GONE
//        }
        when (getSavedTheme()) {
            THEME_LIGHT -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
            THEME_DARK -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
            THEME_UNDEFINED -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {

                }
            }
        }
    }

    private fun getSavedTheme() = sharedPrefs.getInt(KEY_THEME, THEME_UNDEFINED)
    private fun saveTheme(theme: Int) = sharedPrefs.edit().putInt(KEY_THEME, theme).apply()

    companion object{
        const val PREFS_NAME = "prefs"
        const val KEY_NAME = "Bearer"
        const val ACCEPT ="*/*"
    }
}