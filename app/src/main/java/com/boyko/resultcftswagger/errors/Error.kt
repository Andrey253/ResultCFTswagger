package com.boyko.resultcftswagger.errors

import android.content.Context
import android.util.Log
import android.widget.Toast

class Error (val context: Context){

        fun get(string: String?){
            if(string?.contains("The source did not signal an event for 10 seconds and has been terminated")==true)
                Toast.makeText(context,"Сервер не отвечал 10 секунд, повторите попытку позже", Toast.LENGTH_LONG).show()
            else Toast.makeText(context,"Неизвестная ошибка", Toast.LENGTH_LONG).show()
        }
}