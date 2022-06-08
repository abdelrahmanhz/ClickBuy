package com.example.clickbuy.me.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import com.example.clickbuy.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

private const val TAG = "ProfileEditFragment"

class ProfileEditFragment : Fragment() {

    lateinit var arrowBackImageView: ImageView
    lateinit var editImageView: ImageView
    lateinit var nameEditText: TextInputEditText
    lateinit var addressTextView: AutoCompleteTextView
    lateinit var genderTextView: AutoCompleteTextView
    lateinit var emailEditText: TextInputEditText
    lateinit var phoneNumberEditText: TextInputEditText
    lateinit var saveButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)

        arrowBackImageView = view.findViewById(R.id.arrow_back_imageView)
        editImageView = view.findViewById(R.id.edit_imageView)
        nameEditText = view.findViewById(R.id.name_textInputEditText)
        genderTextView = view.findViewById(R.id.gender_autoCompleteTextView)
        addressTextView = view.findViewById(R.id.address_autoCompleteTextView)
        emailEditText = view.findViewById(R.id.email_textInputEditText)
        phoneNumberEditText = view.findViewById(R.id.phone_textInputEditText)
        saveButton = view.findViewById(R.id.save_button)


        val genderList = listOf("Male", "Female")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            genderList
        )
        addressTextView.setAdapter(adapter)
        genderTextView.setAdapter(adapter)

        addressTextView.setOnItemClickListener { parent, view, position, id ->
            Log.i("TAG", "onCreateView: " + parent.getItemAtPosition(position).toString())
        }

        genderTextView.setOnItemClickListener { parent, view, position, id ->
            Log.i("TAG", "onCreateView: " + parent.getItemAtPosition(position).toString())
        }

        editImageView.setOnClickListener {
            nameEditText.isEnabled = true
            genderTextView.isEnabled = true
            addressTextView.isEnabled = true
            emailEditText.isEnabled = true
            phoneNumberEditText.isEnabled = true
            saveButton.visibility = View.VISIBLE
            editImageView.visibility = View.GONE
        }

        saveButton.setOnClickListener {
            nameEditText.isEnabled = false
            genderTextView.isEnabled = false
            addressTextView.isEnabled = false
            emailEditText.isEnabled = false
            phoneNumberEditText.isEnabled = false
            saveButton.visibility = View.GONE
            editImageView.visibility = View.VISIBLE
        }

        arrowBackImageView = view.findViewById(R.id.arrow_back_imageView)

        if (Locale.getDefault() == Locale.ENGLISH) {
            arrowBackImageView.setImageResource(R.drawable.ic_arrow_left)
            Log.i(TAG, "onCreateView: in if")
        }
        arrowBackImageView.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }

}