package com.boyko.resultcftswagger.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.boyko.resultcftswagger.presenter.LoansPresenter
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.adapter.Adapter
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.ui.itemfragment.LoanItem
import com.boyko.resultcftswagger.util.InternetConnection
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.loans_fragment.*
private const val ARG_PARAM1 = "param1"

class Loans : Fragment() {

    private val api = Client.apiService
    private var sharedPref: SharedPreferences? = null
    lateinit var mLoanItemFragment: LoanItem
    lateinit var myAdapter: Adapter
    var listLoan = listOf<Loan>()
    private var param1: String? = null

    private var presenter: LoansPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
        sharedPref = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        Log.e("mytag", "Loans created ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.loans_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_req_loan_cond.setOnClickListener {

            presenter?.showCreateNewLoan()
        }
        fab.setOnClickListener {getLoansAll() }

            recycleViewCreate(listLoan)
            getLoansAll()


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

    fun getLoansAll() {
        if (isConnect()){
            val sharedPref = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val bearer: String? = sharedPref?.getString(KEY_NAME, null)
            bearer?.let {
                progressBar.setVisibility(View.VISIBLE)
                val call = api.getLoansAll(ACCEPT, it)
                call
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : Observer<List<Loan>> {

                        override fun onSubscribe(d: Disposable) {
                            Log.e("mytag", "onSubscribe " + d.toString())}
                        override fun onNext(listLoanCall: List<Loan>) {
                            progressBar.setVisibility(View.INVISIBLE)
                            listLoan = listLoanCall
                            myAdapter.update(listLoanCall)
                        }

                        override fun onError(e: Throwable) {
                            Log.e("mytag", "onError" + e.toString())}
                        override fun onComplete() {
                            Log.e("mytag", "onComplete")}
                    })
            }
        }

        else {
            progressBar.setVisibility(View.VISIBLE)
            Toast.makeText(context, getString(R.string.no_connection), Toast.LENGTH_LONG).show()
        }
    }
    private fun isConnect(): Boolean{
        return InternetConnection.checkConnection(context)
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
            const val PREFS_NAME = "Bearer"
            const val KEY_NAME = "Bearer"
            const val ACCEPT ="*/*"
            const val CONTENTTYPE ="application/json"
    }
//    override fun check_connect_and_run(f: () -> Unit){
//        if (isConnect())
//            f()
//        else {
//            progressOFF()
//            Toast.makeText(applicationContext, getString(R.string.no_connection), Toast.LENGTH_LONG).show()
//        }
//    }
}