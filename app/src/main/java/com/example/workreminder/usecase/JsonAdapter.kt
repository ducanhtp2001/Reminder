package com.example.workreminder.usecase

import com.google.gson.Gson

class JsonAdapter<T>(){
    fun toJson(obj: T): String {
        val gson = Gson()
        return gson.toJson(obj)
    }

    fun toObject(obj: String, type: Class<T>): T? {
        val gson = Gson()
        return gson.fromJson(obj, type)
    }
}