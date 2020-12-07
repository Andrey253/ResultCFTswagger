package com.boyko.resultcftswagger.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.models.LoanRequest
import com.boyko.resultcftswagger.models.LoggedInUser
import com.boyko.resultcftswagger.repositiry.LoginRepository
import com.boyko.resultcftswagger.ui.CreateNewLoan
import com.boyko.resultcftswagger.ui.Loans
import com.boyko.resultcftswagger.ui.itemfragment.CreatedNewLoan
import com.boyko.resultcftswagger.ui.itemfragment.LoanItem

interface LoansPresenter {

    fun attachView(viewLoans: Loans,viewCreateNewLoan: CreateNewLoan)

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