package com.boyko.resultcftswagger.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.boyko.resultcftswagger.presenter.LoansPresenter
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.models.LoanConditions
import com.boyko.resultcftswagger.models.LoanRequest
import com.boyko.resultcftswagger.ui.itemfragment.CreatedNewLoan
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_create_new_loan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private const val ARG_PARAM1 = "param1"

class CreateNewLoan : Fragment() {

    private val api = Client.apiService
    private var sharedPref: SharedPreferences? = null
    private var presenter: LoansPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = context!!.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_new_loan, container, false)
    }

    interface onClickFragmentListener{
        fun created_new_loan(param1: String)
        fun check_connect_and_run(f: () -> Unit)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_update  .setOnClickListener { get_LoanCond_ns()}
        btn_send    .setOnClickListener { get_LoanRequest() }
        get_LoanCond_ns()
    }

    private fun get_LoanRequest() {

        val bearer: String? = sharedPref?.getString(KEY_NAME, null)
        val call = bearer?.let { api.postGetLoans(ACCEPT, bearer, createLoanRequestObject()) }
        call?.enqueue(object : Callback<Loan?> {
            override fun onResponse(call: Call<Loan?>, response: Response<Loan?>) {
                if (response.isSuccessful) {
                    val fragment = CreatedNewLoan.newInstance(Gson().toJson(response.body()), presenter!!)
                    presenter?.showFragmentLeft(fragment)

                } else {
                    // обработать ошибки
                }
            }

            override fun onFailure(call: Call<Loan?>, t: Throwable) {
                // обработать ошибки
            }
        })
    }

    private fun createLoanRequestObject(): LoanRequest{
        return LoanRequest(
                amount = tv_new_amount.text.toString().toInt(),
                firstName = tv_new_firstname.text.toString(),
                lastName = tv_new_lastname.text.toString(),
                percent = tv_new_percent.text.toString().toDouble(),
                period = tv_new_period.text.toString().toInt(),
                phoneNumber = tv_new_phone.text.toString())
    }

    private fun get_LoanCond_ns() {

        val bearer: String? = sharedPref?.getString(KEY_NAME, null)
        val call = bearer?.let { api.getLoansConditions(ACCEPT, it) }
        call?.enqueue(object : Callback<LoanConditions?> {
            override fun onResponse(call: Call<LoanConditions?>, response: Response<LoanConditions?>) {
                if (response.isSuccessful) {
                    setFieldItemLoanFragment(response.body())
                } else {
                    // обработать ошибки
                }
            }

            override fun onFailure(call: Call<LoanConditions?>, t: Throwable) {
                // обработать ошибки
            }
        })
    }
    private fun setFieldItemLoanFragment(loancond: LoanConditions?) {
        tv_new_amount .setText(loancond?.maxAmount.toString())
        tv_new_percent.setText(loancond?.percent.toString())
        tv_new_period .setText(loancond?.period.toString())
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, loansPresenter: LoansPresenter) =
            CreateNewLoan().apply {
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
}