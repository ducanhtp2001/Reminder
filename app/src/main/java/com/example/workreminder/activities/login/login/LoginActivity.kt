package com.example.workreminder.activities.login.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.workreminder.R
import com.example.workreminder.activities.login.register.RegisterActivity
import com.example.workreminder.activities.reminder.MainActivity
import com.example.workreminder.data.network.model.User
import com.example.workreminder.databinding.ActivityLoginBinding
import com.example.workreminder.usecase.JsonAdapter
import com.example.workreminder.usecase.PassWordValidator
import com.example.workreminder.usecase.UserSharePreference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var b: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        b.checkBoxRemember.isChecked = true

        fillOnStart()
        registerUserEvent()
        registerObserver()
    }

    private fun fillOnStart() {
        val share = viewModel.getSharePreference()
        val userJson = share.getString(UserSharePreference.SHARE_NAME,"")
        val user = JsonAdapter<User>().toObject(userJson!!, User::class.java)
        if (user != null) {
            b.apply {
                inputUser.setText(user.userName)
                inputPassword.setText(user.passWord)
            }
        }
    }

    private fun registerObserver() {
        viewModel.isLoginRequest.observe(this, Observer {
            b.loginProgressBar.visibility = if (it == true) View.VISIBLE else View.GONE
            if (!viewModel.isStart && b.loginProgressBar.visibility == View.GONE) {
                Toast.makeText(this, viewModel.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.isLoginSuccess.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                val userJson = JsonAdapter<User>().toJson(viewModel.localUser!!)
                intent.putExtra(UserSharePreference.SHARE_NAME, userJson)
                startActivity(intent)
            }
        })
    }

    private fun registerUserEvent() {
        b.apply {
            txtRegisterNewAccount.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            btnLogin.setOnClickListener {
                if (inputUser.text.isEmpty() || inputPassword.text!!.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Input your account", Toast.LENGTH_SHORT).show()
                } else {
                    if (PassWordValidator.validator(inputPassword.text.toString())) {
                        viewModel.login(User(userName = inputUser.text.toString(),
                                            passWord = inputPassword.text.toString()), checkBoxRemember.isChecked)
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid information input", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }
}