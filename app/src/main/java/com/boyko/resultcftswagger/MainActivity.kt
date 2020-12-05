package com.boyko.resultcftswagger

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.models.UserEntity
import com.boyko.resultcftswagger.repositiry.LoginRepository
import com.boyko.resultcftswagger.ui.*
import com.boyko.resultcftswagger.ui.itemfragment.CreatedNewLoan
import com.boyko.resultcftswagger.ui.itemfragment.LoanItem
import com.google.gson.Gson
import kotlinx.android.synthetic.main.login_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity(),
        Login.onClickFragmentListener,
        Register.onClickFragmentListener,
        Loans.onClickFragmentListener,
        CreatedNewLoan.onClickFragmentListener,
        CreateNewLoan.onClickFragmentListener {

    lateinit var mLoginFragment: Login
    lateinit var mRegisterFragment: Register
    lateinit var mLoansFragment: Loans
    lateinit var mCreateNewLoanFragment: CreateNewLoan
    lateinit var mCreatedNewLoanFragment: CreatedNewLoan
    lateinit var loginRepository: LoginRepository

    var itemMenu: MenuItem? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginRepository = LoginRepository(applicationContext)
        initFragment()

        if (loginRepository.isAuthorized())
            showFragment_left(mLoansFragment)
        else showFragment_left(mLoginFragment)
    }

    private fun initFragment() {
        mLoginFragment = Login().apply { listener = this@MainActivity }
        mRegisterFragment = Register().apply { listener = this@MainActivity }
        mCreateNewLoanFragment = CreateNewLoan().apply { listener = this@MainActivity }
        mLoansFragment = Loans().apply { listener = this@MainActivity }
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.main_container)) {
            is Loans -> finish()
            is CreatedNewLoan -> {
                showFragment_right(mLoansFragment)
                mLoansFragment.getLoansAll()
            }
            is CreateNewLoan -> showFragment_right(mLoansFragment)
            is LoanItem -> showFragment_right(mLoansFragment)
            is Register -> showFragment_right(mLoginFragment)
            is Login -> finish()
            else -> super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        itemMenu = item
        when (item.itemId) {
            R.id.action_logout -> {
                loginRepository.logOut()
                finish()
            }
        }

        return when (item.itemId) {
            R.id.action_logout -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun click_to_Registration() {
        showFragment_left(mRegisterFragment)
    }

    override fun clickLogin() {
        if (isConnect())
            login_send_post(mLoansFragment, userCreate())
        else
            Toast.makeText(applicationContext, getString(R.string.no_connection), Toast.LENGTH_LONG).show()
    }

    override fun clickRegistration() {
        reg_send_post(mLoansFragment)
    }

    override fun click_to_Login() {
        showFragment_right(mLoginFragment)
    }

    override fun create_new_loan() {
        showFragment_left(mCreateNewLoanFragment)
    }

    override fun created_new_loan(fromGson: String) {
        mCreatedNewLoanFragment = CreatedNewLoan.newInstance(fromGson, "")
                .apply { listener = this@MainActivity }
        showFragment_left(mCreatedNewLoanFragment)
    }

    override fun click_to_main() {
        showFragment_left(mLoansFragment)
        mLoansFragment.getLoansAll()
    }

}
