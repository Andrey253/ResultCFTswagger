package com.boyko.resultcftswagger

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.boyko.resultcftswagger.di.LoginPresenterFactory
import com.boyko.resultcftswagger.presenter.LoginView
import com.boyko.resultcftswagger.repositiry.LoginRepository
import com.boyko.resultcftswagger.ui.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.registr_fragment.*
import com.boyko.resultcftswagger.ActivityLoans as ActivityLoa

class LoginActivity: AppCompatActivity(), LoginView{

    private val presenter by lazy {  LoginPresenterFactory.create(applicationContext, supportFragmentManager) }

    private val mLogin by lazy {  Login   .newInstance("", presenter!!) }
    private val mRegis by lazy {  Register.newInstance("", presenter!!) }

    lateinit var loginRepository: LoginRepository

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginRepository = LoginRepository(applicationContext)
        initPresenter()

        if (loginRepository.isAuthorized()){
            startActivity(Intent(this, ActivityLoa::class.java))
            this.finish()
        }
        else showFragment_left(mLogin)
    }

    private fun initPresenter() {

        presenter?.attachView(mLogin, mRegis, this)
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.main_container)) {
            is Register -> showFragment_left(mLogin)
            is Login -> finish()
            else -> super.onBackPressed()
        }
    }

    override fun showUsernameError() {
        editText_user_reg.error = getString(R.string.invalid_username)
    }
    override fun toggleRegButton(enable: Boolean) {
        btn_reg_register.isEnabled = enable
    }
    override fun showPasswordError() {
        editText_password_reg.error = getString(R.string.invalid_password)
    }
    override fun showPasswordRepeatError() {
        editText_password_repeat.error = getString(R.string.invalid_repeat_password)
    }

    fun showFragment_left(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .setCustomAnimations(R.anim.left_in, R.anim.left_out)
            .replace(R.id.main_container, fragment, fragment.javaClass.name)
            .commit()
    }
}