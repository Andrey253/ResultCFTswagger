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
import com.boyko.resultcftswagger.InternetConnection
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.adapter.Adapter
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.repositiry.LoginRepository
import com.boyko.resultcftswagger.ui.itemfragment.LoanItem
import com.google.gson.Gson
import kotlinx.android.synthetic.main.loans_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [LoanConditionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Loans : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val api = Client.apiService
    private var sharedPref: SharedPreferences? = null
    lateinit var mLoanItemFragment: LoanItem
    lateinit var myAdapter: Adapter
    var listLoan = listOf<Loan>()
    var listener: onClickFragmentListener?=null
        set(value) {field=value}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        sharedPref = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if(isConnect())
            getLoansAll()
        else
            Toast.makeText(context, "Отсутствует соединение с сетью", Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.loans_fragment, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_get_loan.setOnClickListener {listener?.create_new_loan()}
        fab.setOnClickListener { getLoansAll() }

        recycleCreate(listLoan)
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
                        Log.e("mytag", "No DATA, code = ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<List<Loan>?>, t: Throwable) {
                    Log.e("mytag", "onFailure $t")
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoanConditionsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                Loans().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
    interface onClickFragmentListener{
        fun create_new_loan()
    }
}