package com.boyko.resultcftswagger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.di.LoansPresenterFactory
import com.boyko.resultcftswagger.presenter.LoansPresenter
import com.boyko.resultcftswagger.repositiry.LoginRepository
import com.boyko.resultcftswagger.ui.*
import com.boyko.resultcftswagger.ui.itemfragment.CreatedNewLoan
import com.boyko.resultcftswagger.ui.itemfragment.LoanItem

class ActivityLoans : AppCompatActivity() {

    private var loginRepository : LoginRepository? = null
    private var presenter       : LoansPresenter? = null

    lateinit var mLoans         : Loans
    lateinit var mCreateNewLoan : CreateNewLoan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loans)

        loginRepository = LoginRepository(applicationContext)
        initPresenter()
        presenter?.showFragmentLeft(mLoans)
    }

    private fun initPresenter() {

        presenter = LoansPresenterFactory.create(applicationContext, supportFragmentManager)

        mLoans         = Loans.newInstance          ("", presenter!!)
        mCreateNewLoan = CreateNewLoan.newInstance  ("", presenter!!)

        presenter?.attachView(mLoans, mCreateNewLoan)
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.main_loans_container)) {
            is Loans -> finish()
            is CreatedNewLoan -> {presenter?.showFragmentRight(mLoans)}
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