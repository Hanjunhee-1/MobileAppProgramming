package edu.skku.cs.personalproject.Activities.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.lifecycleScope
import edu.skku.cs.personalproject.APIs.ApiClient
import edu.skku.cs.personalproject.Activities.Splash.SplashActivity
import edu.skku.cs.personalproject.DTOs.Login.LoginRequest
import edu.skku.cs.personalproject.DTOs.TokenManager
import edu.skku.cs.personalproject.databinding.ActivityLoginBinding

import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityLoginBinding

    private lateinit var tokenManager:
            TokenManager

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityLoginBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        tokenManager =
            TokenManager(this)

        binding.loginButton.setOnClickListener {

            login()
        }
    }

    private fun login() {

        val id =
            binding.idEditText.text
                .toString()
                .trim()

        val password =
            binding.passwordEditText.text
                .toString()
                .trim()

        // validation
        if (id.isBlank()) {

            Toast.makeText(
                this,
                "아이디를 입력하세요",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (password.isBlank()) {

            Toast.makeText(
                this,
                "비밀번호를 입력하세요",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        lifecycleScope.launch {

            try {

                val response =
                    ApiClient
                        .create()
                        .login(
                            LoginRequest(
                                id,
                                password
                            )
                        )

                tokenManager.saveToken(
                    response.token
                )

                Toast.makeText(
                    this@LoginActivity,
                    "로그인 성공",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(
                        this@LoginActivity,
                        SplashActivity::class.java
                    )
                )

                finish()

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    this@LoginActivity,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}