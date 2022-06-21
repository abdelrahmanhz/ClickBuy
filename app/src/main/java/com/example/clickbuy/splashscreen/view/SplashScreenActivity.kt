package com.example.clickbuy.splashscreen.view

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.clickbuy.R
import com.example.clickbuy.mainscreen.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val TAG = "SplashScreenActivity"

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var logo: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var topAnim: Animation
    private lateinit var bottomAnim: Animation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash_screen)
        initUI()

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        }

    }

    private fun initUI() {
        title = findViewById(R.id.title_textView)
        logo = findViewById(R.id.logo_imageView)
        description = findViewById(R.id.description_textView)

        topAnim = AnimationUtils.loadAnimation(this, R.anim.splash_screen_top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.splash_screen_bottom_animation)

        title.animation = topAnim
        logo.animation = bottomAnim
        description.animation = bottomAnim
    }


}