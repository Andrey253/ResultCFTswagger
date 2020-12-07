package com.boyko.resultcftswagger.presenter

interface LoginView {

    fun showUsernameError()

    fun showPasswordError()

    fun showPasswordRepeatError()

    fun toggleRegButton(enable: Boolean)

}