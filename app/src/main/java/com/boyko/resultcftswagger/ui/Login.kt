package com.boyko.resultcftswagger.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.boyko.resultcftswagger.R
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.registr_fragment.*

class Login : BaseFragment() {

    var listener:onClickFragmentListener?=null
        set(value) {field=value}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View=inflater.inflate(R.layout.login_fragment,container,false)
        val btnLogin = view.findViewById<Button>(R.id.btn_login)
        val btnReg = view.findViewById<Button>(R.id.btn_register)

        btnLogin.setOnClickListener{
            listener?.clickLogin()
        }

        btnReg.setOnClickListener{
            listener?.click_to_Registration()
        }
        return view
    }

    interface onClickFragmentListener{
        fun click_to_Registration()
        fun clickLogin()
    }
}