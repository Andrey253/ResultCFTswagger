package com.boyko.resultcftswagger.presenter

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.boyko.resultcftswagger.ActivityLoans
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.errors.ErrorsMake
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.models.LoanConditions
import com.boyko.resultcftswagger.models.LoanRequest
import com.boyko.resultcftswagger.repositiry.LoginRepository
import com.boyko.resultcftswagger.ui.CreateNewLoan
import com.boyko.resultcftswagger.ui.Loans
import com.boyko.resultcftswagger.ui.itemfragment.CreatedNewLoan
import com.boyko.resultcftswagger.ui.itemfragment.LoanItem
import com.boyko.resultcftswagger.util.InternetConnection
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.loans_fragment.*
import java.util.concurrent.TimeUnit


class LoansPresenterImpl(private val loginRepository: LoginRepository, val fragmentManager: FragmentManager) :
    LoansPresenter {

    private var mLoans              : Loans? = null
    private var viewLoanItem        : LoanItem? = null
    private var viewCreateNewLoan   : CreateNewLoan? = null
    private var errors = ErrorsMake()


    private val api = Client.apiService

    override fun attachView(
            viewLoans: Loans,
            viewCreateNewLoan: CreateNewLoan

    ) {
        this.mLoans             = viewLoans
       // this.viewLoanItem       = viewLoanItem
        this.viewCreateNewLoan  = viewCreateNewLoan
    }
    private fun sendErrors(context: Context, e: String) {
        val mainHandler = android.os.Handler(context.mainLooper)
        val runnable = Runnable {
            Toast.makeText(context, e,Toast.LENGTH_LONG).show()
        }
        mainHandler.post(runnable)
    }

    override fun detachView() {
        this.mLoans          = null
        this.viewLoanItem       = null
        this.viewCreateNewLoan  = null

    }
    override fun showFragmentLeft(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .addToBackStack(null)
            .setCustomAnimations(R.anim.left_in, R.anim.left_out)
            .replace(R.id.main_loans_container, fragment, fragment.javaClass.name)
            .commit()
    }
    override fun showFragmentRight(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .addToBackStack(null)
            .setCustomAnimations(R.anim.right_in, R.anim.right_out)
            .replace(R.id.main_loans_container, fragment, fragment.javaClass.name)
            .commit()
    }

    override fun showCreateNewLoan() {
        this.viewCreateNewLoan?.let { showFragmentLeft(it) }
    }

    override fun clickToMain() {
        mLoans?.let { showFragmentRight(it) }
    }

    override fun showItemLoan(loan: Loan) {
        showFragmentLeft(LoanItem.newInstance(Gson().toJson(loan)))
    }

    private fun isConnect(context: Context): Boolean{
        return InternetConnection.checkConnection(context)
    }

    override fun getAllLoans(context: Context, toast: String) {

        if (isConnect(context)){
            loginRepository.getBearer()?.let {
                mLoans?.progressBar?.setVisibility(View.VISIBLE)
                val call = api.getLoansAll(ActivityLoans.ACCEPT, it)
                call.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .timeout(7, TimeUnit.SECONDS)
                        .subscribe(object : Observer<List<Loan>> {
                            override fun onSubscribe(d: Disposable) { }
                            override fun onNext(listLoanCall: List<Loan>)
                            {
                                if (mLoans != null) mLoans?.progressBar?.setVisibility(View.INVISIBLE)
                                mLoans?.listLoan = listLoanCall
                                mLoans?.myAdapter?.update(listLoanCall)
                            }

                            override fun onError(e: Throwable)
                            {
                                if (mLoans != null) mLoans?.progressBar?.setVisibility(View.INVISIBLE)
                                sendErrors(context, errors.errorToString(e.message.toString()))
                                Log.e("mytag getAllLoans", e.message.toString())
                            }

                            override fun onComplete() {}
                        })
            }
        } else {
            if (mLoans != null) mLoans?.progressBar?.setVisibility(View.INVISIBLE)
            Toast.makeText(context, toast, Toast.LENGTH_LONG).show()
        }
    }
    override fun loanConditionsRequest(context: Context, toast: String) {
        if (isConnect(context)) {
            loginRepository.getBearer()?.let {
                val call = api.getLoansConditions(ActivityLoans.ACCEPT, it)
                call
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .timeout(7, TimeUnit.SECONDS)
                        .subscribe(object : Observer<LoanConditions> {
                            override fun onSubscribe(d: Disposable) {

                            }

                            override fun onNext(loanConditions: LoanConditions) {
                                viewCreateNewLoan?.setFieldItemLoanFragment(loanConditions)

                            }

                            override fun onError(e: Throwable) {

                            }

                            override fun onComplete() {

                            }
                        })
            }
        } else {

            Toast.makeText(context, toast, Toast.LENGTH_LONG).show()
        }
    }
    override fun loanRequest(context: Context, loanRequest: LoanRequest, presenter: LoansPresenter, toast: String) {
        if (isConnect(context)) {
            loginRepository.getBearer()?.let {
                val call = api.postGetLoans(ActivityLoans.ACCEPT, it, loanRequest)
                call
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .timeout(7, TimeUnit.SECONDS)
                        .subscribe(object : Observer<Loan> {
                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onNext(loan: Loan) {
                                val fragment = CreatedNewLoan.newInstance(Gson().toJson(loan), presenter)
                                presenter.showFragmentLeft(fragment)
                            }

                            override fun onError(e: Throwable) {
                            }

                            override fun onComplete() {
                            }
                        })
            }
        } else {
            Toast.makeText(context, toast, Toast.LENGTH_LONG).show()
        }
    }
}