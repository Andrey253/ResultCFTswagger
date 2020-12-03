package com.boyko.resultcftswagger.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.adapter.Adapter
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.models.LoanConditions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import kotlinx.android.synthetic.main.loans_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
class LoansFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val api = Client.apiService
    private var sharedPref: SharedPreferences? = null
    lateinit var mLoanItemFragment: LoanItemFragment
    lateinit var mCreateNewLoanFragment: CreateNewLoanFragment

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
        return inflater.inflate(R.layout.loans_fragment, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_get_loan.setOnClickListener {requestLoanConditions(CreateNewLoanFragment())}
        fab.setOnClickListener { view ->
            getLoansAll()
        }

        getLoansAll()
    }

    fun getLoansAll() {

        var bearer: String? = sharedPref?.getString(KEY_NAME, null)
        bearer?.let {
            progressBar.setVisibility(View.VISIBLE)
            val call = api.getLoansAll(ACCEPT, it)
            bearer = null
            call.enqueue(object : Callback<List<Loan>> {
                override fun onResponse(call: Call<List<Loan>?>, response: Response<List<Loan>?>) {
                    if (response.isSuccessful) {
                        progressBar.setVisibility(View.INVISIBLE)
                        val loansAll = response.body()
                        Log.e("mytag", "isSuccessful $loansAll")

                        recyclerview.layoutManager = LinearLayoutManager(context)
                        val myAdapter = loansAll?.let {
                            Adapter(it, object : Adapter.Callback {
                                override fun onItemClicked(item: Loan) {
                                    //TODO Сюда придёт элемент, по которому кликнули. Можно дальше с ним работать
                                    Log.e("mytag", "Элемент списка = $item")
                                    mLoanItemFragment = LoanItemFragment.newInstance(Gson().toJson(item),"")
                                    showLoansFragment(mLoanItemFragment)
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
    }

    fun showLoansFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()
                ?.addToBackStack(null)
                ?.setCustomAnimations(R.anim.left_in, R.anim.left_out)
                ?.replace(R.id.main_container, fragment)
                ?.commit()
    }
    private fun requestLoanConditions(fragment: Fragment){
        fragmentManager?.beginTransaction()
            ?.addToBackStack(null)
            ?.setCustomAnimations(R.anim.right_in, R.anim.right_out, R.anim.left_in, R.anim.left_out)
            ?.replace(R.id.main_container, fragment)
            ?.commit()
    }

    companion object {
        const val ACCEPT ="*/*"
        const val CONTENTTYPE ="application/json"
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
                LoansFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}