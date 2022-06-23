package com.example.clickbuy.me.view.guest

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.airbnb.lottie.LottieAnimationView
import com.example.clickbuy.R
import com.example.clickbuy.authentication.view.AuthenticationActivity
import com.example.clickbuy.util.ConnectionLiveData


class GuestFragment : Fragment() {

    private lateinit var enableConnection: AppCompatButton
    private lateinit var noInternetAnimation: LottieAnimationView
    private lateinit var welcomeTextView: TextView
    private lateinit var guestTextView: TextView
    private lateinit var loginButton: AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_guest, container, false)

        initUI(view)

        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                noInternetAnimation.visibility = View.GONE
                enableConnection.visibility = View.GONE
                welcomeTextView.visibility = View.VISIBLE
                guestTextView.visibility = View.VISIBLE
                loginButton.visibility = View.VISIBLE
            } else {
                noInternetAnimation.visibility = View.VISIBLE
                enableConnection.visibility = View.VISIBLE
                welcomeTextView.visibility = View.GONE
                guestTextView.visibility = View.GONE
                loginButton.visibility = View.GONE
            }
        }

        enableConnection.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
            } else {
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
        }

        loginButton.setOnClickListener {
            startActivity(Intent(requireContext(), AuthenticationActivity::class.java))
        }

        return view
    }

    private fun initUI(view: View) {
        welcomeTextView = view.findViewById(R.id.welcome_textView)
        guestTextView = view.findViewById(R.id.guest_textView)
        loginButton = view.findViewById(R.id.login_button)
        noInternetAnimation = view.findViewById(R.id.no_internet_animation)
        enableConnection = view.findViewById(R.id.enable_connection)
    }

}