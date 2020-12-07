package com.boyko.resultcftswagger.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.models.LoggedInUser
import com.boyko.resultcftswagger.repositiry.LoginRepository
import com.boyko.resultcftswagger.ui.CreateNewLoan
import com.boyko.resultcftswagger.ui.Loans
import com.boyko.resultcftswagger.ui.itemfragment.LoanItem
import com.boyko.resultcftswagger.util.InternetConnection
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.loans_fragment.*

class LoansPresenterImpl(private val loginUseCase: LoginRepository, val fragmentManager: FragmentManager) :
    LoansPresenter {

    private var viewLoans           : Loans? = null
    private var viewLoanItem        : LoanItem? = null
    private var viewCreateNewLoan   : CreateNewLoan? = null
    private var listLoan = listOf<Loan>()

    private val api = Client.apiService

    override fun attachView(
        viewLoans           : Loans,
        viewCreateNewLoan   : CreateNewLoan

    ) {
        this.viewLoans          = viewLoans
        this.viewLoanItem       = viewLoanItem
        this.viewCreateNewLoan  = viewCreateNewLoan

    }

    override fun detachView() {
        this.viewLoans          = null
        this.viewLoanItem       = null
        this.viewCreateNewLoan  = null

    }
    override fun showFragmentLeft(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .addToBackStack(null)
            .setCustomAnimations(R.anim.left_in, R.anim.left_out)
            .replace(R.id.main_loans_container,fragment, fragment.javaClass.name)
            .commit()
    }
    override fun showFragmentRight(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .addToBackStack(null)
            .setCustomAnimations(R.anim.right_in, R.anim.right_out)
            .replace(R.id.main_loans_container,fragment, fragment.javaClass.name)
            .commit()
    }

    override fun showCreateNewLoan() {
        this.viewCreateNewLoan?.let { showFragmentLeft(it) }
    }

    override fun clickToMain() {
        viewLoans?.let { showFragmentRight(it) }
        //check_connect_and_run { getLoansAll()}
    }

    override fun showItemLoan(loan: Loan) {
        showFragmentLeft(LoanItem.newInstance(Gson().toJson(loan)))
    }
    private fun isConnect(context: Context): Boolean{
        return InternetConnection.checkConnection(context)
    }
    override fun getAllLoans(context: Context, loginRepository: LoginRepository, s1: String, s2: String, toast: String) {
            if (isConnect(context)){
                val sharedPref = context.getSharedPreferences(Loans.PREFS_NAME, Context.MODE_PRIVATE)
                val bearer: String? = sharedPref?.getString(Loans.KEY_NAME, null)
                bearer?.let {
                    viewLoans?.progressBar?.setVisibility(View.VISIBLE)
                    val call = api.getLoansAll(Loans.ACCEPT, it)
                    call
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(object : Observer<List<Loan>> {

                                override fun onSubscribe(d: Disposable) {
                                    Log.e("mytag", "onSubscribe " + d.toString())}
                                override fun onNext(listLoanCall: List<Loan>) {
                                    viewLoans?.progressBar?.setVisibility(View.INVISIBLE)
                                    listLoan = listLoanCall
                                    viewLoans?.myAdapter?.update(listLoanCall)
                                }

                                override fun onError(e: Throwable) {
                                    Log.e("mytag", "onError" + e.toString())}
                                override fun onComplete() {
                                    Log.e("mytag", "onComplete")}
                            })
                }
            }

            else {
                viewLoans?.progressBar?.setVisibility(View.INVISIBLE)
                Toast.makeText(context,toast, Toast.LENGTH_LONG).show()
            }
    }
}