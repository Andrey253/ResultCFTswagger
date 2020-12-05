package com.boyko.resultcftswagger.ui.itemfragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.ui.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_created_new_loan.*
import kotlinx.android.synthetic.main.fragment_loan_item.*
import kotlinx.android.synthetic.main.fragment_loan_item.tv_item_amount
import kotlinx.android.synthetic.main.fragment_loan_item.tv_item_name
import kotlinx.android.synthetic.main.fragment_loan_item.tv_item_percent
import kotlinx.android.synthetic.main.fragment_loan_item.tv_item_period
import kotlinx.android.synthetic.main.fragment_loan_item.tv_item_phone
import kotlinx.android.synthetic.main.fragment_loan_item.tv_item_state
import kotlinx.android.synthetic.main.fragment_loan_item.tv_item_state1
import kotlinx.android.synthetic.main.loans_fragment.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val APPROVED = "APPROVED"
private const val REJECTED = "REJECTED"
private const val REGISTERED = "REGISTERED"

/**
 * A simple [Fragment] subclass.
 * Use the [CreatedNewLoanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreatedNewLoan : BaseFragment() {
    // TODO: Rename and change types of parameters
    //lateinit var mLoans: Loans
    private var param1: String? = null
    private var param2: String? = null

    var listener: onClickFragmentListener?=null
        set(value) {field=value}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_created_new_loan, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e("mytag", "Вешаем слушатель")
        val btn = this.view?.findViewById<Button>(R.id.btn_to_main)
        btn?.setOnClickListener {
            listener?.click_to_main()}
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFieldItemLoanFragment()
    }
    fun showLoansFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()
                ?.addToBackStack(null)
                ?.setCustomAnimations(R.anim.left_in, R.anim.left_out)
                ?.replace(R.id.main_container, fragment)
                ?.commit()
    }
    private fun setFieldItemLoanFragment() {

        var loan = Gson().fromJson(param1, Loan::class.java)
        tv_item_name.text = "${loan?.firstName} ${loan?.lastName} "
        tv_item_phone.text = loan?.phoneNumber
        tv_item_state.text = loan?.state
        tv_item_amount.text = loan?.amount.toString()
        tv_item_percent.text = loan?.percent.toString()
        tv_item_period.text = loan?.period.toString()
        setColorStatus(loan?.state)
    }
    private fun setColorStatus(state: String?) {
        when(state){
            REGISTERED ->   {   tv_item_state.setBackgroundColor(Color.CYAN)
                tv_item_state1.setBackgroundColor(Color.CYAN)}
            APPROVED ->     {   tv_item_state.setBackgroundColor(Color.GREEN)
                tv_item_state1.setBackgroundColor(Color.GREEN)}
            REJECTED ->     {   tv_item_state.setBackgroundColor(Color.RED)
                tv_item_state1.setBackgroundColor(Color.RED)}

        }
    }
    interface onClickFragmentListener{
        fun click_to_main()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreatedNewLoanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                CreatedNewLoan().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}