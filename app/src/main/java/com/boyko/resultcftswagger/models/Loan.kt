package com.boyko.resultcftswagger.models

data class Loan (

    val firstName : String,
    val lastName : String,
    val phoneNumber : String,
    val amount : Int,
    val percent : Double,
    val period : Int,
    val date : String,
    val state : String,
    val id : Int
)
