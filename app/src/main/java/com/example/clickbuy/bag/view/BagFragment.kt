package com.example.clickbuy.bag.view

import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.bag.viewmodel.BagViewModel
import com.example.clickbuy.bag.viewmodel.BagViewModelFactory
import com.example.clickbuy.models.*
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.util.ConstantsValue
import com.example.clickbuy.util.isRTL
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder


private const val TAG = "BagFragment"

class BagFragment : Fragment(), UpdatingItemsAtBag {
    private lateinit var priceTextView: TextView
    private lateinit var checkoutButton: AppCompatButton
    private lateinit var arrowBackImageView: ImageView
    private lateinit var bagRecyclerView: RecyclerView
    private lateinit var bagAdapter: BagAdapter
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var relativeLayout: RelativeLayout
    private lateinit var viewModelFactory: BagViewModelFactory
    private lateinit var viewModel: BagViewModel
    private var bagList: List<BagItem> = emptyList()
    private var imagesList: List<NoteAttribute> = emptyList()
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bag, container, false)

        initUI(view)
        initViewModel()
        observeViewModel()
        swipeToDelete()
        checkRTL()

        arrowBackImageView.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        checkoutButton.setOnClickListener {
            //write here the code will convert you to the order activity to make the order
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
        relativeLayout = view.findViewById(R.id.relative_layout)
        bagAdapter = BagAdapter(this)
        bagRecyclerView.layoutManager = LinearLayoutManager(view.context)
        bagRecyclerView.addItemDecoration(
            DividerItemDecoration(
                bagRecyclerView.context,
                (bagRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        bagRecyclerView.adapter = bagAdapter
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

        viewModel.getAllItemsInBag()
    }

    private fun observeViewModel() {
        viewModel.shoppingBag.observe(viewLifecycleOwner) {
            Log.i(TAG, "observeViewModel: lineItems--------------> " + it.draft_order.line_items.size)
            Log.i(TAG, "observeViewModel: note_attributes--------> " + it.draft_order.note_attributes.size)
            if (it.draft_order.note_attributes.isNotEmpty() && it.draft_order.line_items.isNotEmpty()) {
                bagRecyclerView.visibility = View.VISIBLE
                relativeLayout.visibility = View.VISIBLE
                bagList = it.draft_order.line_items
                imagesList = it.draft_order.note_attributes
                bagAdapter.setList(it.draft_order.line_items, it.draft_order.note_attributes)
                priceTextView.text = it.draft_order.subtotal_price
            } else {
                Log.i(TAG, "observeViewModel: lineItems--------------> " + it.draft_order.line_items.size)
                Log.i(TAG, "observeViewModel: note_attributes--------> " + it.draft_order.note_attributes.size)
                bagAdapter.setList(emptyList()  , emptyList())
                priceTextView.text = "0.0"
                relativeLayout.visibility = View.GONE
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
                super.onChildDraw(
                    c, recyclerView, viewHolder, dX,
                    dY, actionState, isCurrentlyActive
                )
                val background = ColorDrawable(Color.RED)
                background.setBounds(
                    viewHolder.itemView.right,
                    viewHolder.itemView.top,
                    0,
                    viewHolder.itemView.bottom
                )
                background.draw(c)

                Log.i(TAG, "onChildDraw: ")
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

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmerAnimation()
    }
}