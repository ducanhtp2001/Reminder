package com.example.workreminder.usecase

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class UserSharePreference @Inject constructor(val context: Context) {
    companion object {
        const val SHARE_KEY = "com.work-reminder"
        const val SHARE_NAME = "user"
    }

    public fun getSharePreference(): SharedPreferences {
        return context.getSharedPreferences(SHARE_KEY, Context.MODE_PRIVATE)
    }
}