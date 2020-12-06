package com.boyko.resultcftswagger.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.boyko.resultcftswagger.R

class Register : BaseFragment() {
    var listener: onClickFragmentListener?=null
        set(value) {field=value}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View= inflater.inflate(R.layout.registr_fragment,container,false)

        val btnLogin = view.findViewById<Button>(R.id.btn_login)
        val btnReg = view.findViewById<Button>(R.id.btn_register)
        btnLogin.setOnClickListener{
            listener?.click_to_Login()
        }

        btnReg.setOnClickListener{
            listener?.clickRegistration()
        }

        return view
    }
    interface onClickFragmentListener{
        fun clickRegistration()
        fun click_to_Login()
    }
}