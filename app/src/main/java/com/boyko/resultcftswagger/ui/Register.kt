package com.boyko.resultcftswagger.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.boyko.resultcftswagger.R

/**
 * Created by minh98 on 17/08/2017.
 */
class Register : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View= inflater.inflate(R.layout.registr_fragment,container,false)
        return view
    }
}