package com.boyko.resultcftswagger.errors

import android.util.Log

class ErrorsMake (){

    fun errorToString(str: String): String {
        if (str.contains("404") == true)
            return "Ошибка авторизации. \nНеверный логин или пароль."
        if (str.contains("event for 10 seconds") == true)
            return "Сервер не отвечал 10 секунд, повторите попытку позже"

        return str
    }
}