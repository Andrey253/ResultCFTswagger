package com.boyko.resultcftswagger.ui

import android.view.View
import androidx.fragment.app.Fragment
import com.boyko.resultcftswagger.InternetConnection
import com.boyko.resultcftswagger.R
import kotlinx.android.synthetic.main.loans_fragment.*

abstract class BaseFragment: Fragment() {
    companion object{
        const val PREFS_NAME = "Bearer"
        const val KEY_NAME = "Bearer"
        const val ACCEPT ="*/*"
        const val CONTENTTYPE ="application/json"
    }

    fun showFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()
                ?.addToBackStack(null)
                ?.setCustomAnimations(R.anim.left_in, R.anim.left_out)
                ?.replace(R.id.main_container,fragment, fragment.javaClass.name)
                ?.commit()
    }
    fun isConnect(): Boolean{
        return InternetConnection.checkConnection(context!!)
    }
    fun progressON(){
        progressBar?.setVisibility(View.VISIBLE)
    }
    fun progressOFF(){
        progressBar?.setVisibility(View.INVISIBLE)
    }
}