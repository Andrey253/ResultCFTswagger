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
import com.boyko.resultcftswagger.MainActivity
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.adapter.Adapter
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.ui.itemfragment.LoanItem
import com.google.gson.Gson
import kotlinx.android.synthetic.main.loans_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Loans : BaseFragment() {

    private val api = Client.apiService
    private var sharedPref: SharedPreferences? = null
    lateinit var mLoanItemFragment: LoanItem
    lateinit var myAdapter: Adapter
    var listLoan = listOf<Loan>()

    var listener: onClickFragmentListener?=null
        set(value) {field=value}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        send_getLoansAll()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.loans_fragment, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_req_loan_cond.setOnClickListener {listener?.create_new_loan()}
        fab.setOnClickListener { send_getLoansAll() }

        recycleCreate(listLoan)
    }

    fun send_getLoansAll() {
        if (isConnect())
            getLoansAll()
        else
            Toast.makeText(context, getString(R.string.no_connection), Toast.LENGTH_LONG).show()
    }

    fun getLoansAll() {
        val bearer: String? = sharedPref?.getString(KEY_NAME, null)
        bearer?.let {
            progressON()
            val call = api.getLoansAll(ACCEPT, it)
            call.enqueue(object : Callback<List<Loan>> {
                override fun onResponse(call: Call<List<Loan>?>, response: Response<List<Loan>?>) {
                    if (response.isSuccessful) {
                        progressOFF()
                        response.body()?.let {
                            listLoan = it
                            myAdapter.update(it)
                        }
                    } else {
                        // обработать ошибки
                    }
                }
                override fun onFailure(call: Call<List<Loan>?>, t: Throwable) {
                    // обработать ошибки
                }
            })
        }
    }
    private fun recycleCreate(listLoan: List<Loan>) {
        recyclerview.layoutManager = LinearLayoutManager(context)
        myAdapter = Adapter(listLoan, object : Adapter.Callback {
            override fun onItemClicked(item: Loan) {
                mLoanItemFragment = LoanItem.newInstance(Gson().toJson(item), "")
                showFragment(mLoanItemFragment)
            }
        })
        recyclerview.adapter = myAdapter
    }
    interface onClickFragmentListener{
        fun create_new_loan()
    }
}