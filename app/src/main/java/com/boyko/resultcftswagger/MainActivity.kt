package com.boyko.resultcftswagger

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.boyko.resultcftswagger.api.Client
import com.boyko.resultcftswagger.api.fragment.LoanConditionsFragment
import com.boyko.resultcftswagger.api.models.LoanConditions
import com.boyko.resultcftswagger.api.models.LoggedInUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, LoanConditionsFragment.newInstance("param1","param2"))
                .commit()
        }
    }



    companion object{
        val BEARER  ="Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBbmRyZXkyNTMiLCJleHAiOjE2MDg5ODcwOTh9.mcYvxzuELfSn_9I8KPzZel1nb5wcYr_d7yHl3ptC0bVf4kAEQVc1O5zKrHLD7ofO3r33xshr--tvrpqMi8fDJQ"
        val ACCEPT ="*/*"
    }

}