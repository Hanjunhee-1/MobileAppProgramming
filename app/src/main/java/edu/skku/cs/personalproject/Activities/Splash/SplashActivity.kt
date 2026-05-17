package edu.skku.cs.personalproject.Activities.Splash

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import edu.skku.cs.personalproject.MainActivity
import edu.skku.cs.personalproject.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivitySplashBinding

    private val quotes = listOf(

        "당신이 잠을 자는 동안 누군가는 꿈을 이룹니다.",

        "늦었다고 생각될 때는 진짜 늦었습니다.",

        "지금 해야 할 일을 하지 않으면 미래의 자신이 후회합니다.",

        "시간은 누구에게나 공평하지만 결과는 공평하지 않습니다.",

        "오늘의 작은 행동이 미래의 가치를 만듭니다."
    )

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivitySplashBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        setupQuote()

        startAnimation()

        moveToMain()
    }

    private fun setupQuote() {

        binding.quoteText.text =
            quotes.random()
    }

    private fun startAnimation() {

        ObjectAnimator.ofFloat(

            binding.logoText,
            "alpha",
            0f,
            1f

        ).apply {

            duration = 1500

            start()
        }
    }

    private fun moveToMain() {

        binding.root.postDelayed({

            startActivity(

                Intent(
                    this,
                    MainActivity::class.java
                )
            )

            finish()

        }, 3000)
    }
}