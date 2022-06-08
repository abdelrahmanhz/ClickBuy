package com.example.clickbuy

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.clickbuy.mainscreen.view.MainActivity
import com.example.clickbuy.util.isInternetAvailable
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.provider.Settings
import android.view.Window

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var logo: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var topAnim: Animation
    private lateinit var bottomAnim: Animation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen)

        title = findViewById(R.id.title_textView)
        logo = findViewById(R.id.logo_imageView)
        description = findViewById(R.id.description_textView)

        topAnim = AnimationUtils.loadAnimation(this, R.anim.splash_screen_top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.splash_screen_bottom_animation)

        title.animation = topAnim
        logo.animation = bottomAnim
        description.animation = bottomAnim

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            if (isInternetAvailable(this@SplashScreenActivity))
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            else
                showSnackbar()
        }


    }

    private fun showSnackbar() {
        val snackBar = Snackbar.make(
            findViewById(R.id.splashScreenActivity_ConstraintLayout),
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        ).setActionTextColor(Color.WHITE)

        snackBar.setAction(getString(R.string.enable_connection)) {
            startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
        }
        snackBar.view.setBackgroundColor(Color.RED)
        snackBar.show()
    }


}