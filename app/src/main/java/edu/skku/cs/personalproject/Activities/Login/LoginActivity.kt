package edu.skku.cs.personalproject.Activities.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.lifecycleScope
import edu.skku.cs.personalproject.APIs.ApiClient
import edu.skku.cs.personalproject.Activities.Splash.SplashActivity
import edu.skku.cs.personalproject.DTOs.TokenManager
import edu.skku.cs.personalproject.databinding.ActivityLoginBinding

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import edu.skku.cs.personalproject.R

import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityLoginBinding

    private lateinit var tokenManager:
            TokenManager

    private lateinit var googleSignInClient: GoogleSignInClient

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
        Log.d("default_web_client_id", "${getString(R.string.default_web_client_id)}")
        val gso =

            GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            )

                .requestIdToken(
                    getString(
                        R.string.default_web_client_id
                    )
                )

                .requestEmail()

                .build()

        googleSignInClient =

            GoogleSignIn.getClient(
                this,
                gso
            )

        binding.googleLoginButton
            .setOnClickListener {

                signIn()
            }
    }

    private fun signIn() {

        val signInIntent =
            googleSignInClient.signInIntent

        startActivityForResult(
            signInIntent,
            1000
        )
    }

    private fun loginWithServer(
        idToken: String
    ) {

        lifecycleScope.launch {

            try {

                val response =

                    ApiClient
                        .create()
                        .googleLogin(
                            mapOf(
                                "idToken" to idToken
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

                    "서버 로그인 실패",

                    Toast.LENGTH_LONG

                ).show()
            }
        }
    }

    override fun onActivityResult(

        requestCode: Int,

        resultCode: Int,

        data: Intent?

    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == 1000) {

            val task =
                GoogleSignIn.getSignedInAccountFromIntent(
                    data
                )

            try {

                val account =
                    task.getResult(
                        ApiException::class.java
                    )

                val idToken =
                    account.idToken

                if (idToken != null) {

                    loginWithServer(idToken)
                }

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(

                    this,

                    "Google 로그인 실패",

                    Toast.LENGTH_SHORT

                ).show()
            }
        }
    }
}