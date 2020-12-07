package com.boyko.resultcftswagger.ui


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.boyko.resultcftswagger.presenter.LoansPresenter
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.models.LoanConditions
import com.boyko.resultcftswagger.models.LoanRequest
import kotlinx.android.synthetic.main.fragment_create_new_loan.*

private const val ARG_PARAM1 = "param1"

class CreateNewLoan : Fragment() {

    private var presenter: LoansPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_new_loan, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        context?.let {
            btn_update
                    .setOnClickListener {
                        presenter?.loanConditionsRequest(context!!,  getString(R.string.no_connection))
                    }
            presenter?.loanConditionsRequest(context!!,  getString(R.string.no_connection))
        }
        context?.let {
            btn_send
                    .setOnClickListener {
                        presenter?.loanRequest(context!!, createLoanRequestObject(), presenter!!, getString(R.string.no_connection))
                    }
        }
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

    fun setFieldItemLoanFragment(loancond: LoanConditions?) {
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
    }
}