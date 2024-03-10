package com.example.workreminder.activities.reminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.example.workreminder.R
import com.example.workreminder.data.network.model.User
import com.example.workreminder.usecase.JsonAdapter
import com.example.workreminder.usecase.UserSharePreference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        val userJson = intent.getStringExtra(UserSharePreference.SHARE_NAME)
        viewModel.user = JsonAdapter<User>().toObject(userJson!!, User::class.java)
    }

    fun getLocalUser(): User {
        return viewModel.user!!
    }
}