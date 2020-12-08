package com.boyko.resultcftswagger.presenter

import android.content.Context
import androidx.fragment.app.Fragment
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.models.LoanRequest
import com.boyko.resultcftswagger.ui.CreateNewLoan
import com.boyko.resultcftswagger.ui.Loans

interface LoansPresenter {

    fun attachView(viewLoans: Loans, viewCreateNewLoan: CreateNewLoan)

    fun detachView()

    fun showFragmentLeft(fragment: Fragment)

    fun showFragmentRight(fragment: Fragment)

    fun showCreateNewLoan()

    fun clickToMain()

    fun showItemLoan(loan: Loan)

    fun getAllLoans(context: Context, toast: String)

    fun loanConditionsRequest(context: Context, toast: String)

    fun loanRequest(context: Context, loanRequest: LoanRequest, presenter: LoansPresenter, toast: String)

}