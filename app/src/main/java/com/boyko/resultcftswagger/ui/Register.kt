package com.boyko.resultcftswagger.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import com.boyko.resultcftswagger.R
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.btn_login
import kotlinx.android.synthetic.main.login_fragment.view.*
import kotlinx.android.synthetic.main.registr_fragment.*

class Register : BaseFragment() {
    var listener: onClickFragmentListener?=null
        set(value) {field=value}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View= inflater.inflate(R.layout.registr_fragment,container,false)

        val btnLogin = view.findViewById<Button>(R.id.btn_reg_login)
        btnLogin.setOnClickListener{
            listener?.click_to_Login()
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    interface onClickFragmentListener{
        fun clickRegistration()
        fun click_to_Login()
    }
    fun showUsernameError() {
        editText_user_reg.error = getString(R.string.invalid_username)
    }
    fun toggleRegButton(enable: Boolean) {
        btn_reg_register.isEnabled = enable
    }
    fun showPasswordError() {
        editText_password_reg.error = getString(R.string.invalid_password)
    }
    fun showPasswordRepeatError() {
        editText_password_repeat.error = getString(R.string.invalid_repeat_password)
    }
    private fun initViews() {

        editText_user_reg.afterTextChanged {
            onLoginDataUpdated(
                    editText_user_reg.text.toString(),
                    editText_password_reg.text.toString(),
                    editText_password_repeat.text.toString()
            )
        }
        editText_password_reg.afterTextChanged {
            onLoginDataUpdated(
                    editText_user_reg.text.toString(),
                    editText_password_reg.text.toString(),
                    editText_password_repeat.text.toString()
            )
        }
        editText_password_repeat.apply {
            afterTextChanged {
                onLoginDataUpdated(
                        editText_user_reg.text.toString(),
                        editText_password_reg.text.toString(),
                        editText_password_repeat.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        listener?.clickRegistration()
                }
                false
            }

            btn_reg_register.setOnClickListener {
                listener?.clickRegistration()
            }
        }
    }

    private fun onLoginDataUpdated(username: String, password: String, passwordrepeat: String) {
        if (!isUserNameValid(username)) {
            showUsernameError()
            toggleRegButton(enable = false)
        } else if (!isPasswordValid(password)) {
            showPasswordError()
            toggleRegButton(enable = false)
        } else if (!isRepeatPasswordValid(password, passwordrepeat)) {
            showPasswordRepeatError()
            toggleRegButton(enable = false)
        }else {
            toggleRegButton(enable = true)
        }
    }
    private fun isUserNameValid(username: String): Boolean {
        return if (username.isNotBlank() && username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            false
        }
    }
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
    private fun isRepeatPasswordValid(password: String, repeatpassword: String): Boolean {
        return password.equals(repeatpassword)
    }
}
private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })

}