package com.boyko.resultcftswagger.di

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.boyko.resultcftswagger.presenter.LoansPresenter
import com.boyko.resultcftswagger.presenter.LoansPresenterImpl
import com.boyko.resultcftswagger.repositiry.LoginRepository

object LoansPresenterFactory {


    fun create(context: Context, fragmentManager: FragmentManager): LoansPresenter {
        val loginRepository = LoginRepository(context)

        return LoansPresenterImpl(loginRepository, fragmentManager)
    }
}