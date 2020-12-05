package com.boyko.resultcftswagger.api

import com.boyko.resultcftswagger.models.*
import retrofit2.http.POST
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
    @POST("/registration")
    fun postReg(
            @Header ("accept") accept: String,
            @Header ("Content-Type") type: String,
            @Body user: LoggedInUser): Call<UserEntity>
    @POST("/loans")
    fun postGetLoans(
        @Header ("accept") accept: String,
        @Header ("Authorization") auth: String,
        @Body loanRequest: LoanRequest): Call<Loan>

    @GET("/loans/conditions")
    fun getLoansConditions(
            @Header ("accept") accept: String,
            @Header ("Authorization") auth: String) : Call<LoanConditions>

    @GET("/loans/all")
    fun getLoansAll(
            @Header ("accept") accept: String,
            @Header ("Authorization") auth: String) : Call<List<Loan>>

}