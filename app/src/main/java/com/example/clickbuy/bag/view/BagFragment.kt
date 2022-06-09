package com.example.clickbuy.bag.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.bag.viewmodel.BagViewModel
import com.example.clickbuy.bag.viewmodel.BagViewModelFactory
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import java.util.*

private const val TAG = "BagFragment"

class BagFragment : Fragment() {
    lateinit var arrowBackImageView: ImageView
    private lateinit var bagRecyclerView: RecyclerView
    private lateinit var bagAdapter: BagAdapter
    private lateinit var viewModelFactory: BagViewModelFactory
    private lateinit var viewModel: BagViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_bag, container, false)


        initUI(view)
        initViewModel()

        viewModel.shoppingBag.observe(viewLifecycleOwner) {
            bagAdapter.setList(it.draft_order.line_items)
        }


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
             //  bagList.removeAt(viewHolder.adapterPosition)
                bagAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(bagRecyclerView)

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

    private fun initUI(view: View) {
        bagRecyclerView = view.findViewById(R.id.recyclerView_bag)
        bagAdapter = BagAdapter(view.context)
        bagRecyclerView.layoutManager = LinearLayoutManager(view.context)
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

}