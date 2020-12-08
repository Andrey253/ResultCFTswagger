package com.boyko.resultcftswagger.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.boyko.resultcftswagger.ActivityLoans
import com.boyko.resultcftswagger.presenter.LoansPresenter
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.adapter.Adapter
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.repositiry.LoginRepository
import com.boyko.resultcftswagger.ui.itemfragment.LoanItem
import com.boyko.resultcftswagger.util.InternetConnection
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.loans_fragment.*
private const val ARG_PARAM1 = "param1"

class Loans : Fragment() {

    lateinit var mLoanItemFragment: LoanItem
    lateinit var myAdapter: Adapter

    private var sharedPref: SharedPreferences? = null
    var listLoan = listOf<Loan>()
    private var param1: String? = null
    private var presenter: LoansPresenter? = null
    private var onCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("mytag", "onCreate Loans : Fragment()  ")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
        sharedPref = context?.getSharedPreferences(ActivityLoans.PREFS_NAME, Context.MODE_PRIVATE)

        onCreated = true

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.loans_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (onCreated){
            presenter?.getAllLoans(context!!,  getString(R.string.no_connection))
            onCreated =false
        }
        mLoanItemFragment = LoanItem()
        recycleViewCreate(listLoan)
        Log.e("mytag", "onActivityCreated Loans : Fragment()  ")
        btn_req_loan_cond.setOnClickListener {presenter?.showCreateNewLoan() }

        fab.setOnClickListener {context?.let {  presenter?.getAllLoans(context!!, getString(R.string.no_connection)) }}
        myAdapter.update(listLoan)
    }

    fun recycleViewCreate(listLoan: List<Loan>) {
        recyclerview.layoutManager = LinearLayoutManager(context)
        myAdapter = Adapter(listLoan, object : Adapter.Callback {
            override fun onItemClicked(item: Loan) {
                presenter?.showItemLoan(item)
            }
        })
        recyclerview.adapter = myAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, loansPresenter: LoansPresenter) =
            Loans().apply {
                presenter = loansPresenter
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}