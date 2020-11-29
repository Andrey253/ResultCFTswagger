package com.boyko.resultcftswagger.api

import com.boyko.resultcftswagger.models.Loan
import com.boyko.resultcftswagger.models.LoanConditions
import retrofit2.http.POST
import com.boyko.resultcftswagger.models.LoggedInUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @POST("/login")
    fun postLogin(
            @Header ("accept") accept: String,
            @Header ("Content-Type") type: String,
            @Body user: LoggedInUser): Call<String>

    @GET("/loans/conditions")
    fun getLoansConditions(
            @Header ("accept") accept: String,
            @Header ("Authorization") auth: String) : Call<LoanConditions>

    @GET("/loans/all")
    fun getLoansAll(
            @Header ("accept") accept: String,
            @Header ("Authorization") auth: String) : Call<List<Loan>>

}