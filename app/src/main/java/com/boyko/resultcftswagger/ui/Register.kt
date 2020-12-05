package com.boyko.resultcftswagger.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.boyko.resultcftswagger.InternetConnection
import com.boyko.resultcftswagger.R
import kotlinx.android.synthetic.main.login_fragment.*

/**
 * Created by minh98 on 17/08/2017.
 */
class Register : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View= inflater.inflate(R.layout.registr_fragment,container,false)

        val btnLogin = view.findViewById<Button>(R.id.btn_login)
        val btnReg = view.findViewById<Button>(R.id.btn_register)
        btnLogin.setOnClickListener{
            if(InternetConnection.checkConnection(context!!))
                showFragment(Login())
            else
                Toast.makeText(context, "Отсутствует соединение с сетью", Toast.LENGTH_LONG).show()
        }

        btnReg.setOnClickListener{
            showFragment(Login())
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}