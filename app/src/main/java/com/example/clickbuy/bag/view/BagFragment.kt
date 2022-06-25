package com.example.clickbuy.bag.view

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.clickbuy.R
import com.example.clickbuy.bag.viewmodel.BagViewModel
import com.example.clickbuy.bag.viewmodel.BagViewModelFactory
import com.example.clickbuy.models.*
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.orders.view.AddressOrderActivity
import com.example.clickbuy.util.*
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


private const val TAG = "BagFragment"

class BagFragment : Fragment(), UpdatingItemsAtBag {

    private lateinit var noInternetAnimation: LottieAnimationView
    private lateinit var enableConnection: TextView

    private lateinit var priceTextView: TextView
    private lateinit var checkoutButton: AppCompatButton
    private lateinit var arrowBackImageView: ImageView
    private lateinit var bagRecyclerView: RecyclerView
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var checkoutRelativeLayout: RelativeLayout
    private lateinit var emptyBagConstraintLayout: ConstraintLayout
    private lateinit var bagAdapter: BagAdapter
    private lateinit var viewModelFactory: BagViewModelFactory
    private lateinit var viewModel: BagViewModel
    private var bagList: List<BagItem> = emptyList()
    private var imagesList: List<NoteAttribute> = emptyList()
    private lateinit var progressBar: ProgressBar
    private lateinit var bagObject: ShoppingBag

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: -----------------------------> ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView: -----------------------------> ")
        val view = inflater.inflate(R.layout.fragment_bag, container, false)

        initUI(view)
        initViewModel()
        observeViewModel()
        swipeToDelete()
        checkRTL()

        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                shimmerFrameLayout.visibility = View.VISIBLE
                shimmerFrameLayout.startShimmerAnimation()
                viewModel.getAllItemsInBag()
                noInternetAnimation.visibility = View.GONE
                enableConnection.visibility = View.GONE
            } else {
                checkoutRelativeLayout.visibility = View.GONE
                emptyBagConstraintLayout.visibility = View.GONE
                progressBar.visibility = View.GONE
                bagRecyclerView.visibility = View.GONE
                shimmerFrameLayout.visibility = View.GONE
                shimmerFrameLayout.stopShimmerAnimation()
                noInternetAnimation.visibility = View.VISIBLE
                enableConnection.visibility = View.VISIBLE
            }
        }

        arrowBackImageView.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        checkoutButton.setOnClickListener {
            val intent = Intent(requireContext(), AddressOrderActivity::class.java)
            intent.putExtra("TEST", bagObject)
            startActivity(intent)
        }

        enableConnection.setOnClickListener {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
            } else {
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }*/
            connectInternet(requireContext())
        }
        return view
    }

    private fun initUI(view: View) {
        progressBar = view.findViewById(R.id.progress_bar)
        priceTextView = view.findViewById(R.id.price_textView)
        checkoutButton = view.findViewById(R.id.checkout_button)
        arrowBackImageView = view.findViewById(R.id.arrow_back_imageView)
        bagRecyclerView = view.findViewById(R.id.recyclerView_bag)
        shimmerFrameLayout = view.findViewById(R.id.shimmer_frame_layout)
        checkoutRelativeLayout = view.findViewById(R.id.checkout_relative_layout)
        emptyBagConstraintLayout = view.findViewById(R.id.empty_bag_constraintLayout)
        bagAdapter = BagAdapter(this)
        bagRecyclerView.layoutManager = LinearLayoutManager(view.context)
        bagRecyclerView.addItemDecoration(
            DividerItemDecoration(
                bagRecyclerView.context,
                (bagRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        bagRecyclerView.adapter = bagAdapter

        enableConnection = view.findViewById(R.id.enable_connection)
        noInternetAnimation = view.findViewById(R.id.no_internet_animation)
    }

    private fun initViewModel() {
        viewModelFactory = BagViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(BagViewModel::class.java)
    }

    private fun observeViewModel() {
        viewModel.shoppingBag.observe(viewLifecycleOwner) {
            Log.i(TAG, "observeViewModel: it----------------> $it")

            if (it != null && it.draft_order.note_attributes.isNotEmpty() && it.draft_order.line_items.isNotEmpty()) {
                bagRecyclerView.visibility = View.VISIBLE
                checkoutRelativeLayout.visibility = View.VISIBLE
                emptyBagConstraintLayout.visibility = View.GONE
                bagList = it.draft_order.line_items
                imagesList = it.draft_order.note_attributes
                bagAdapter.setList(it.draft_order.line_items, it.draft_order.note_attributes)
                priceTextView.text = calculatePrice(it.draft_order.subtotal_price)
                bagObject = it
            } else {
                bagAdapter.setList(emptyList(), emptyList())
                priceTextView.text = "0.0"
                checkoutRelativeLayout.visibility = View.GONE
                emptyBagConstraintLayout.visibility = View.VISIBLE
            }
            progressBar.visibility = View.GONE
            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.visibility = View.GONE


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


        }).attachToRecyclerView(bagRecyclerView)
    }

    private fun checkRTL() {
        if (isRTL())
            arrowBackImageView.setImageResource(R.drawable.ic_arrow_right)
    }

    override fun onQuantityIncreased(position: Int) {
        bagList[position].quantity++
        updateItemsInBag()
    }

    override fun onQuantityDecreased(position: Int) {
        if (bagList[position].quantity > 1) {
            bagList[position].quantity--
            updateItemsInBag()
        } else {
            showAlertDialog(position)
        }
    }

    private fun showAlertDialog(position: Int) {
        Log.i(TAG, "showAlertDialog")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_item))
            .setMessage(getString(R.string.delete_item_message))
            .setNegativeButton(
                getString(R.string.no_button)
            ) { dialog: DialogInterface?, _: Int ->
                dialog!!.dismiss()
                bagAdapter.notifyDataSetChanged()
            }
            .setPositiveButton(
                getString(R.string.delete_button)
            ) { dialog: DialogInterface?, _: Int ->
                dialog!!.dismiss()
                deleteItem(position)
            }
            .show()
    }

    private fun updateItemsInBag() {
        progressBar.visibility = View.VISIBLE
        Log.i(TAG, "updateItemsInBag: ")
        viewModel.updateItemsInBag(
            ShoppingBag(
                DraftOrder(
                    ConstantsValue.email,
                    ConstantsValue.draftOrderID.toLong(),
                    bagList,
                    imagesList
                )
            )
        )
    }

    private fun deleteItem(position: Int) {
        (bagList as ArrayList).remove(bagList[position])
        (imagesList as ArrayList).remove(imagesList[position])
        updateItemsInBag()
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: -----------------------------> ")
        shimmerFrameLayout.stopShimmerAnimation()
    }
}