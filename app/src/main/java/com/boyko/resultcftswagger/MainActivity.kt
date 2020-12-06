package com.boyko.resultcftswagger

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.boyko.resultcftswagger.repositiry.LoginRepository
import com.boyko.resultcftswagger.ui.*
import com.boyko.resultcftswagger.ui.itemfragment.CreatedNewLoan
import com.boyko.resultcftswagger.ui.itemfragment.LoanItem
import kotlinx.android.synthetic.main.loans_fragment.*
import kotlinx.android.synthetic.main.login_fragment.*

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
        mLoansFragment = Loans().apply { listener = this@MainActivity}
        mRegisterFragment = Register().apply { listener = this@MainActivity }
        mCreateNewLoanFragment = CreateNewLoan().apply { listener = this@MainActivity }
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
    fun check_connect_and_run(f: () -> Unit){
        if (isConnect())
            f()
        else
            toastShow(getString(R.string.no_connection))
    }

    override fun clickLogin() {
        check_connect_and_run { login_send_post(mLoansFragment, userCreate(), loginRepository) }
    }

    override fun clickRegistration() {

        check_connect_and_run { reg_send_post(mLoansFragment, loginRepository) }
    }

    override fun click_to_Login() {
        showFragment_right(mLoginFragment)
    }

    override fun create_new_loan() {
        showFragment_left(mCreateNewLoanFragment)
    }

    override fun created_new_loan(fromGson: String) {
        mCreatedNewLoanFragment = CreatedNewLoan.newInstance(fromGson)
                .apply { listener = this@MainActivity }
        showFragment_left(mCreatedNewLoanFragment)
    }

    override fun click_to_main() {
        showFragment_left(mLoansFragment)
        mLoansFragment.send_getLoansAll()

    }
}