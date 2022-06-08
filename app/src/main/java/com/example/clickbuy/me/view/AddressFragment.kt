package com.example.clickbuy.me.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.models.CustomerAddress
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

private const val TAG = "AddressFragment"

class AddressFragment : Fragment() {

    private lateinit var arrowBackImageView: ImageView
    private lateinit var addressRecyclerView: RecyclerView
    private lateinit var addAddressButton: FloatingActionButton
    private lateinit var addressAdapter: AddressAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_address, container, false)

        var addressList = arrayListOf<CustomerAddress>()
        addressList.add(
            CustomerAddress(
                "Test",
                "mahmoudia",
                "Damanhour",
                "Test,Behira, Egypt",
                "Egypt",
                "758",
                "EG", 145695815,
                true, "Behira",
                145695815, "Egypt",
                "Test Address", "Test,Behira, Egypt",
                "Behira", "Test,Behira, Egypt",
                "Test Address"
            )
        )

        addressList.add(
            CustomerAddress(
                "Test Address",
                "Test,Behira, Egypt",
                "Test Address",
                "Test,Behira, Egypt",
                "Test Address",
                "Test,Behira, Egypt",
                "Test Address", 145695815,
                true, "Test,Behira, Egypt",
                145695815, "Test,Behira, Egypt",
                "Test Address", "Test,Behira, Egypt",
                "Test Address", "Test,Behira, Egypt",
                "Test Address"
            )
        )
        addressList.add(
            CustomerAddress(
                "Test Address",
                "Test,Behira, Egypt",
                "Test Address",
                "Test,Behira, Egypt",
                "Test Address",
                "Test,Behira, Egypt",
                "Test Address", 145695815,
                true, "Test,Behira, Egypt",
                145695815, "Test,Behira, Egypt",
                "Test Address", "Test,Behira, Egypt",
                "Test Address", "Test,Behira, Egypt",
                "Test Address"
            )
        )
        addressList.add(
            CustomerAddress(
                "Test Address",
                "Test,Behira, Egypt",
                "Test Address",
                "Test,Behira, Egypt",
                "Test Address",
                "Test,Behira, Egypt",
                "Test Address", 145695815,
                true, "Test,Behira, Egypt",
                145695815, "Test,Behira, Egypt",
                "Test Address", "Test,Behira, Egypt",
                "Test Address", "Test,Behira, Egypt",
                "Test Address"
            )
        )
        addressList.add(
            CustomerAddress(
                "Test Address",
                "Test,Behira, Egypt",
                "Test Address",
                "Test,Behira, Egypt",
                "Test Address",
                "Test,Behira, Egypt",
                "Test Address", 145695815,
                true, "Test,Behira, Egypt",
                145695815, "Test,Behira, Egypt",
                "Test Address", "Test,Behira, Egypt",
                "Test Address", "Test,Behira, Egypt",
                "Test Address"
            )
        )
        addressList.add(
            CustomerAddress(
                "Test Address",
                "Test,Behira, Egypt",
                "Test Address",
                "Test,Behira, Egypt",
                "Test Address",
                "Test,Behira, Egypt",
                "Test Address", 145695815,
                true, "Test,Behira, Egypt",
                145695815, "Test,Behira, Egypt",
                "Test Address", "Test,Behira, Egypt",
                "Test Address", "Test,Behira, Egypt",
                "Test Address"
            )
        )




        addressRecyclerView = view.findViewById(R.id.recyclerView_address)
        addAddressButton = view.findViewById(R.id.add_address_floatingActionButton)
        addressAdapter = AddressAdapter(view.context)
        addressRecyclerView.layoutManager = LinearLayoutManager(view.context)
        addressRecyclerView.adapter = addressAdapter
        addressAdapter.setList(addressList)
        arrowBackImageView = view.findViewById(R.id.arrow_back_imageView)

        if (Locale.getDefault() == Locale.ENGLISH) {
            arrowBackImageView.setImageResource(R.drawable.ic_arrow_left)
            Log.i(TAG, "onCreateView: in if")
        }
        arrowBackImageView.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        addAddressButton.setOnClickListener {
            Toast.makeText(requireContext(), "Go to map or GPS", Toast.LENGTH_SHORT).show()
        }

        return view
    }

}
