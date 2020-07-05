package com.envios.githubfinder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.envios.githubfinder.R


class SplashScreenActivity : AppCompatActivity() {

    private lateinit var imageView : ImageView
    private lateinit var anim : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        imageView = findViewById(R.id.iv_image)
        anim = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.fade_in
        )
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()

            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        imageView.startAnimation(anim)
    }
}

