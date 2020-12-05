package com.boyko.resultcftswagger.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.boyko.resultcftswagger.InternetConnection
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.models.LoanConditions
import com.boyko.resultcftswagger.models.LoanRequest
import com.boyko.resultcftswagger.ui.itemfragment.CreatedNewLoan
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_create_new_loan.*
import kotlinx.android.synthetic.main.fragment_loan_item.*
import kotlinx.android.synthetic.main.loans_fragment.*
import kotlinx.android.synthetic.main.login_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateNewLoanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateNewLoan : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val api = Client.apiService
    private var sharedPref: SharedPreferences? = null
    lateinit var mCreatedNew: CreatedNewLoan

    var listener: onClickFragmentListener?=null
        set(value) {field=value}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        sharedPref = context!!.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_new_loan, container, false)
    }
    interface onClickFragmentListener{
        fun created_new_loan(param1: String)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_update.setOnClickListener {getLoanConditions()}
        btn_send.setOnClickListener{
            if(isConnect())
                sendLoanRequest()
            else
                Toast.makeText(context, "Отсутствует соединение с сетью", Toast.LENGTH_LONG).show()
        }
        if(isConnect())
            getLoanConditions()
        else
            Toast.makeText(context, "Отсутствует соединение с сетью", Toast.LENGTH_LONG).show()

    }

    private fun sendLoanRequest() {

        val bearer: String? = sharedPref?.getString(KEY_NAME, null)
        val call = bearer?.let { api.postGetLoans(ACCEPT, bearer, createLoanRequestObject()) }
        call?.enqueue(object : Callback<Loan?> {
            override fun onResponse(call: Call<Loan?>, response: Response<Loan?>) {
                if (response.isSuccessful) {
                    val loancond = response.body()
                    Log.e("mytag", "isSuccessful loancond = $loancond")
                    //Создали новый займ
                    listener?.created_new_loan(Gson().toJson(loancond))
                } else {
                    Log.e("mytag", "No DATA, code = ${response.code()}")
                }
            }


            override fun onFailure(call: Call<Loan?>, t: Throwable) {
                Log.e("mytag", "onFailure $t")
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

    private fun getLoanConditions() {

        val bearer: String? = sharedPref?.getString(KEY_NAME, null)
        val call = bearer?.let { api.getLoansConditions(ACCEPT, it) }
        call?.enqueue(object : Callback<LoanConditions?> {
            override fun onResponse(call: Call<LoanConditions?>, response: Response<LoanConditions?>) {
                if (response.isSuccessful) {
                    val loancond = response.body()
                    Log.e("mytag", "isSuccessful $loancond")
                    setFieldItemLoanFragment(loancond)
                } else {
                    Log.e("mytag", "No DATA, code = ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoanConditions?>, t: Throwable) {
                Log.e("mytag", "onFailure $t")
            }
        })
    }
    private fun setFieldItemLoanFragment(loancond: LoanConditions?) {
        tv_new_amount.setText(loancond?.maxAmount.toString())
        tv_new_percent.setText(loancond?.percent.toString())
        tv_new_period.setText(loancond?.period.toString())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateNewLoanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                CreateNewLoan().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}