package com.boyko.resultcftswagger.presenter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.repositiry.LoginRepository
import com.boyko.resultcftswagger.ui.CreateNewLoan
import com.boyko.resultcftswagger.ui.Loans
import com.boyko.resultcftswagger.ui.itemfragment.LoanItem
import com.google.gson.Gson

class LoansPresenterImpl(private val loginUseCase: LoginRepository, val fragmentManager: FragmentManager) :
    LoansPresenter {


    private var viewLoans           : Loans? = null
    private var viewLoanItem        : LoanItem? = null
    private var viewCreateNewLoan   : CreateNewLoan? = null


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
}