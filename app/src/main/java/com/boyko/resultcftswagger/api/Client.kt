package com.boyko.resultcftswagger.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Client {


    companion object {

        private const val ROOT_URL = "http://focusapp-env.eba-xm2atk2z.eu-central-1.elasticbeanstalk.com"

        private val retrofitInstance: Retrofit
        get() = Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val apiService: ApiService
            get() = retrofitInstance.create(ApiService::class.java)
        }
}