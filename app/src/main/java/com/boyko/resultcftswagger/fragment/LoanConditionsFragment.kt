package com.boyko.resultcftswagger.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.boyko.resultcftswagger.MainActivity
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.adapter.Adapter
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.models.LoanConditions
import com.boyko.resultcftswagger.models.LoggedInUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.fragment_loan_conditions.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val PREFS_NAME = "Bearer"
private const val KEY_NAME = "Bearer"

/**
 * A simple [Fragment] subclass.
 * Use the [LoanConditionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoanConditionsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val api = Client.apiService
    private var sharedPref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

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
        return inflater.inflate(R.layout.fragment_loan_conditions, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_get_loan.setOnClickListener {getLoanConditions()}
        btn_get_loan_all.setOnClickListener {getLoansAll()}
        btn_login.setOnClickListener {login()}
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
            LoanConditionsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun getLoanConditions() {

        val bearer: String? = sharedPref?.getString(KEY_NAME, null)
        val call = bearer?.let { api.getLoansConditions(MainActivity.ACCEPT, it) }
        call?.enqueue(object : Callback<LoanConditions?> {
            override fun onResponse(call: Call<LoanConditions?>, response: Response<LoanConditions?>) {
                if (response.isSuccessful) {
                    val loancond = response.body()
                    Log.e("mytag", "isSuccessful $loancond")
                } else {
                    Log.e("mytag", "No DATA, code = ${response.code()}")
                }
            }
            override fun onFailure(call: Call<LoanConditions?>, t: Throwable) {
                Log.e("mytag", "onFailure $t")
            }
        })
    }

    fun getLoansAll() {

        val bearer: String? = sharedPref?.getString(KEY_NAME, null)
        val call = bearer?.let { api.getLoansAll(MainActivity.ACCEPT, it) }
        call?.enqueue(object : Callback<List<Loan>> {
            override fun onResponse(call: Call<List<Loan>?>, response: Response<List<Loan>?>) {
                if (response.isSuccessful) {
                    val loansAll = response.body()
                    Log.e("mytag", "isSuccessful $loansAll")

                    recyclerview.layoutManager = LinearLayoutManager(context)
                    val myAdapter = loansAll?.let {
                        Adapter(it, object : Adapter.Callback {
                            override fun onItemClicked(item: Loan) {
                                //TODO Сюда придёт элемент, по которому кликнули. Можно дальше с ним работать
                            }
                        })
                    }
                    recyclerview.adapter = myAdapter
                } else {
                    Log.e("mytag", "No DATA, code = ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Loan>?>, t: Throwable) {
                Log.e("mytag", "onFailure $t")
            }
        })
    }
    private fun login() {
        editor = sharedPref?.edit()
        val user = LoggedInUser("Andrey253", "ibmIBM")
        val call = api.postLogin(MainActivity.ACCEPT, MainActivity.CONTENTTYPE, user)

        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful) {

                    val bearer = response.body()
                    Log.e("mytag", "isSuccessful $bearer")
                    editor?.putString(KEY_NAME, bearer)
                    editor?.apply()

                } else {
                    Log.e("mytag", "No DATA, code = ${response.code()}")
                }
            }
            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.e("mytag", "onFailure $t")
            }
        })
    }
}