package com.boyko.resultcftswagger.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.boyko.resultcftswagger.R


/**
 * Created by minh98 on 17/08/2017.
 */
class LoginFragment : Fragment() {
    var listener:onClickFragmentListener?=null
    set(value) {field=value}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View=inflater.inflate(R.layout.login_fragment,container,false)
        val btnNextFragment: Button =view.findViewById(R.id.button)
        btnNextFragment.setOnClickListener{
            listener?.clicked()
        }
        return view
    }

    interface onClickFragmentListener{
        fun clicked()
    }
}