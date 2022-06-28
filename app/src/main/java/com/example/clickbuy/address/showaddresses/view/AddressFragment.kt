package com.example.clickbuy.address.showaddresses.view

import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.clickbuy.R
import com.example.clickbuy.address.addaddresses.view.AddAddressFragment
import com.example.clickbuy.address.showaddresses.viewmodel.AddressViewModel
import com.example.clickbuy.address.showaddresses.viewmodel.AddressViewModelFactory
import com.example.clickbuy.models.CustomerAddress
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.util.ConnectionLiveData
import com.example.clickbuy.util.connectInternet
import com.example.clickbuy.util.isRTL
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


private const val TAG = "AddressFragment"

class AddressFragment : Fragment() {

    private lateinit var noInternetAnimation: LottieAnimationView
    private lateinit var enableConnection: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var arrowBackImageView: ImageView
    private lateinit var addressRecyclerView: RecyclerView
    private lateinit var addAddressButton: FloatingActionButton
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var addAddress: AppCompatButton
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var viewModelFactory: AddressViewModelFactory
    private lateinit var viewModel: AddressViewModel
    private var addressList: List<CustomerAddress> = emptyList()
    private lateinit var currentView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentView = view

        checkConnection()
        initView(view)
        checkRTL()
        swipeToDelete()
        initViewModel()
        observeViewModelGetAddress()
        observeViewModelIsRemoved()

        arrowBackImageView.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        addAddressButton.setOnClickListener {
            replaceFragment(AddAddressFragment())
        }

        addAddress.setOnClickListener {
            replaceFragment(AddAddressFragment())
        }

        enableConnection.setOnClickListener {
            connectInternet(requireContext())
        }
    }

    private fun checkConnection() {
        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                shimmerFrameLayout.visibility = View.VISIBLE
                shimmerFrameLayout.startShimmerAnimation()
                viewModel.getAllAddresses()
                noInternetAnimation.visibility = View.GONE
                enableConnection.visibility = View.GONE
                addAddressButton.visibility = View.VISIBLE
            } else {
                addressRecyclerView.visibility = View.GONE
                shimmerFrameLayout.visibility = View.GONE
                shimmerFrameLayout.stopShimmerAnimation()
                noInternetAnimation.visibility = View.VISIBLE
                enableConnection.visibility = View.VISIBLE
                addAddressButton.visibility = View.GONE
            }
        }
    }

    private fun swipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                Log.i(TAG, "onMove: ")
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.i(TAG, "onSwiped: ")
                showAlertDialog(viewHolder.adapterPosition)
            }

            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {

                RecyclerViewSwipeDecorator.Builder(
                    c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                ).addBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                ).addActionIcon(R.drawable.ic_delete).create().decorate()
                super.onChildDraw(
                    c, recyclerView, viewHolder, dX,
                    dY, actionState, isCurrentlyActive
                )
            }


        }).attachToRecyclerView(addressRecyclerView)
    }

    private fun initView(view: View) {
        addressRecyclerView = view.findViewById(R.id.recyclerView_address)
        addAddressButton = view.findViewById(R.id.add_address_floatingActionButton)
        shimmerFrameLayout = view.findViewById(R.id.shimmer_frame_layout)
        arrowBackImageView = view.findViewById(R.id.arrow_back_imageView)
        constraintLayout = view.findViewById(R.id.empty_address_constraintLayout)
        progressBar = view.findViewById(R.id.removing_progress_bar)
        addAddress = view.findViewById(R.id.add_address_button)
        addressAdapter = AddressAdapter()
        addressRecyclerView.layoutManager = LinearLayoutManager(view.context)
        addressRecyclerView.adapter = addressAdapter


        enableConnection = view.findViewById(R.id.enable_connection)
        noInternetAnimation = view.findViewById(R.id.no_internet_animation)

    }

    private fun checkRTL() {
        if (isRTL())
            arrowBackImageView.setImageResource(R.drawable.ic_arrow_right)
    }

    private fun initViewModel() {
        viewModelFactory = AddressViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(AddressViewModel::class.java)
    }

    private fun observeViewModelGetAddress() {
        viewModel.addresses.observe(viewLifecycleOwner) {
            Log.i(TAG, "observeViewModel: it.size------------> " + it.size)
            if (it.isNullOrEmpty()) {
                addressRecyclerView.visibility = View.GONE
                addAddressButton.visibility = View.GONE
                addressAdapter.setList(emptyList())
                constraintLayout.visibility = View.VISIBLE
            } else {
                addressRecyclerView.visibility = View.VISIBLE
                addAddressButton.visibility = View.VISIBLE
                addressAdapter.setList(it)
                constraintLayout.visibility = View.GONE
                addressList = it
            }

            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.visibility = View.GONE

        }
    }

    private fun observeViewModelIsRemoved() {
        viewModel.isRemoved.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.getAllAddresses()
            } else {
                addressAdapter.notifyDataSetChanged()
                showSnackBar()
            }
            progressBar.visibility = View.GONE
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .addToBackStack(null).commit()
    }

    private fun showAlertDialog(position: Int) {
        Log.i(TAG, "showAlertDialog")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_address))
            .setMessage(getString(R.string.delete_address_message))
            .setNegativeButton(getString(R.string.no_button)) { dialog: DialogInterface?, _: Int ->
                dialog!!.dismiss()
                addressAdapter.notifyDataSetChanged()
            }
            .setPositiveButton(getString(R.string.delete_button)) { dialog: DialogInterface?, _: Int ->
                dialog!!.dismiss()
                deleteAddress(position)
                progressBar.visibility = View.VISIBLE
            }.setCancelable(false).show()
    }

    private fun deleteAddress(position: Int) {
        val address = addressList[position]
        viewModel.deleteAddress(address.id!!)
    }

    private fun showSnackBar() {
        val snackBar = Snackbar.make(
            currentView.findViewById(R.id.ConstraintLayout_AddressFragment),
            getString(R.string.default_address),
            Snackbar.LENGTH_SHORT
        ).setActionTextColor(Color.WHITE)

        snackBar.view.setBackgroundColor(Color.RED)
        snackBar.show()
    }
}
