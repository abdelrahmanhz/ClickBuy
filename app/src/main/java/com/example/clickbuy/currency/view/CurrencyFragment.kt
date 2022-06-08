package com.example.clickbuy.currency.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.currency.viewmodel.CurrencyViewModel
import com.example.clickbuy.currency.viewmodel.CurrencyViewModelFactory
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.mainscreen.view.MainActivity
import com.google.android.material.snackbar.Snackbar
import java.util.*


class CurrencyFragment : Fragment() {
    private lateinit var arrowBackImageView: ImageView
    private lateinit var currencyRecyclerView: RecyclerView
    private lateinit var currencyAdapter: CurrencyAdapter
    private lateinit var viewModelFactory: CurrencyViewModelFactory
    private lateinit var viewModel: CurrencyViewModel
    private lateinit var currentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView: ")
        val view = inflater.inflate(R.layout.fragment_currency, container, false)
        currentView = view

        currencyRecyclerView = view.findViewById(R.id.recyclerView_currency)
        currencyAdapter = CurrencyAdapter(view.context)
        currencyRecyclerView.layoutManager = LinearLayoutManager(view.context)
        currencyRecyclerView.adapter = currencyAdapter

        arrowBackImageView = view.findViewById(R.id.arrow_back_imageView)

        if (Locale.getDefault() == Locale.ENGLISH) {
            arrowBackImageView.setImageResource(R.drawable.ic_arrow_left)
            Log.i(TAG, "onCreateView: in if")
        }
        arrowBackImageView.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        viewModelFactory = CurrencyViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CurrencyViewModel::class.java)

        viewModel.getCurrencies()

        viewModel.currencies.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                showSnackBar()
            } else {
                currencyAdapter.setList(it)
            }
        })


        return view
    }

    private fun showSnackBar() {
        var snackBar = Snackbar.make(
            currentView.findViewById(R.id.ConstraintLayout_CurrencyFragment),
            getString(R.string.no_currency_found),
            Snackbar.LENGTH_SHORT
        ).setActionTextColor(Color.WHITE)

        snackBar.view.setBackgroundColor(Color.RED)
        snackBar.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
        (requireActivity() as MainActivity).updateCurrency()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(TAG, "onDetach: ")
    }


}