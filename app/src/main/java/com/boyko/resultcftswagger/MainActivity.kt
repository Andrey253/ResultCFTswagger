package com.boyko.resultcftswagger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.boyko.resultcftswagger.fragment.LoanConditionsFragment

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
        //val BEARER  ="Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBbmRyZXkyNTMiLCJleHAiOjE2MDg5ODcwOTh9.mcYvxzuELfSn_9I8KPzZel1nb5wcYr_d7yHl3ptC0bVf4kAEQVc1O5zKrHLD7ofO3r33xshr--tvrpqMi8fDJQ"
        val ACCEPT ="*/*"
        val CONTENTTYPE ="application/json"
    }

}