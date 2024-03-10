package com.example.workreminder.activities.login.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.workreminder.R
import com.example.workreminder.activities.login.login.LoginActivity
import com.example.workreminder.data.network.model.User
import com.example.workreminder.databinding.ActivityRegisterBinding
import com.example.workreminder.usecase.PassWordValidator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    lateinit var b: ActivityRegisterBinding
    lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        b = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(b.root)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        registerUserEvent()
        registerObserver()
    }

    private fun registerObserver() {
        viewModel.isRegisterRequest.observe(this, Observer {
            b.registerProgressBar.visibility = if (it) View.VISIBLE else View.GONE
            b.btnRegister.isEnabled = !it
        })

        viewModel.isRegisterSuccess.observe(this, Observer {
            if (it) {
                Toast.makeText(this@RegisterActivity, viewModel.message, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            } else {
                if(!viewModel.isStart)
                    Toast.makeText(this@RegisterActivity, viewModel.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun registerUserEvent() {
        b.apply {
            btnRegister.setOnClickListener {
                if (inputUserRegister.text.isEmpty()
                    || inputPasswordRegister.text?.isEmpty() == true
                    || reInputPasswordRegister.text?.isEmpty() == true
                ) {
                    Toast.makeText(this@RegisterActivity, "Fill all field", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (PassWordValidator.compareAndValidatorPassword(
                            inputPasswordRegister.text.toString(),
                            reInputPasswordRegister.text.toString()
                        )
                    ) {
                        val user = User(
                            userName = inputUserRegister.text.toString(),
                            passWord = inputPasswordRegister.text.toString()
                        )
                        viewModel.register(user)
                    } else Toast.makeText(this@RegisterActivity, "Password confirmation does not match", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}