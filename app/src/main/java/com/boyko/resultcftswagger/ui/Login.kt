package com.boyko.resultcftswagger.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.boyko.resultcftswagger.ActivityLoans
import com.boyko.resultcftswagger.LoginActivity
import com.boyko.resultcftswagger.R
import com.boyko.resultcftswagger.models.LoggedInUser
import com.boyko.resultcftswagger.presenter.LoginPresenter
import com.boyko.resultcftswagger.repositiry.LoginRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login_fragment.*
private const val ARG_PARAM1 = "param1"

class Login : Fragment() {

    var presenterF: LoginPresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View=inflater.inflate(R.layout.login_fragment,container,false)
        val btnLogin = view.findViewById<Button>(R.id.btn_login)
        val btnReg = view.findViewById<Button>(R.id.btn_register)
        val loginRepository = context?.let { LoginRepository(it) }
        val intent = Intent(context, ActivityLoans::class.java)

        btnLogin.setOnClickListener{
            loginRepository?.let {
                presenterF?.clickLogin(context!!, intent, activity as LoginActivity, userCreate(), it, getString(R.string.authorization_successful), getString(R.string.error_login)) }
        }

        btnReg.setOnClickListener{
            presenterF?.clickToRegistration()
        }
        return view
    }
    fun userCreate(): LoggedInUser {
        return     LoggedInUser(editText_username.text.toString(),editText_password.text.toString())
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, presenter: LoginPresenter) =
            Login().apply {
                presenterF = presenter
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}