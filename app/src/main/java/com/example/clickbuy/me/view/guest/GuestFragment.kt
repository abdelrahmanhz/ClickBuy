package com.example.clickbuy.me.view.guest

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.example.clickbuy.R
import com.example.clickbuy.authentication.view.AuthenticationActivity


class GuestFragment : Fragment() {

    // private lateinit var loginButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_guest, container, false)


        view.findViewById<AppCompatButton?>(R.id.login_button).setOnClickListener {
            //Here will direct you to the login activity
            startActivity(Intent(requireContext(), AuthenticationActivity::class.java))
        }

        /*loginButton.setOnClickListener {
            //Here will direct you to the login activity
        }*/
        return view
    }

}