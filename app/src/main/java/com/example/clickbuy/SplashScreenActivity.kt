package com.example.clickbuy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.splashscreen.view.MainActivity
import com.example.clickbuy.splashscreen.viewmodel.MainActivityViewModel
import com.example.clickbuy.splashscreen.viewmodel.MainActivityViewModelFactory

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var logo: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var topAnim: Animation
    private lateinit var bottomAnim: Animation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash_screen)

        title = findViewById(R.id.title_textView)
        logo = findViewById(R.id.logo_imageView)
        description = findViewById(R.id.description_textView)

        topAnim = AnimationUtils.loadAnimation(this, R.anim.splash_screen_top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.splash_screen_bottom_animation)

        title.animation = topAnim
        logo.animation = bottomAnim
        description.animation = bottomAnim



        startActivity(Intent(this, MainActivity::class.java))
    }
}