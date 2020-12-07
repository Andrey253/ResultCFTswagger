package com.boyko.resultcftswagger.di

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.boyko.resultcftswagger.presenter.LoansPresenter
import com.boyko.resultcftswagger.presenter.LoansPresenterImpl
import com.boyko.resultcftswagger.presenter.LoginPresenter
import com.boyko.resultcftswagger.presenter.LoginPresenterImpl
import com.boyko.resultcftswagger.repositiry.LoginRepository

object LoginPresenterFactory {


    fun create(context: Context, fragmentManager: FragmentManager): LoginPresenter {
        val loginRepository = LoginRepository(context)

        return LoginPresenterImpl (loginRepository, fragmentManager)
    }
}