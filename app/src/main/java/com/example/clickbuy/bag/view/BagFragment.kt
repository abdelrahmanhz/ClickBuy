package com.example.clickbuy.bag.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.models.Bag
import java.util.*


private const val TAG = "BagFragment"

class BagFragment : Fragment() {
    lateinit var arrowBackImageView: ImageView
    private lateinit var bagRecyclerView: RecyclerView
    private lateinit var bagAdapter: BagAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_bag, container, false)

        var bagList = arrayListOf<Bag>()
        bagList.add(Bag(R.drawable.bag_image, "Pizaa", "300", 5))
        bagList.add(Bag(R.drawable.bag_image, "shaowheckin", "300", 4))
        bagList.add(Bag(R.drawable.bag_image, "shawerma", "300", 3))
        bagList.add(Bag(R.drawable.bag_image, "burger", "300", 2))
        bagList.add(Bag(R.drawable.bag_image, "waffles", "300", 1))
        bagList.add(Bag(R.drawable.bag_image, "Pizaa", "300", 5))
        bagList.add(Bag(R.drawable.bag_image, "shaowheckin", "300", 4))
        bagList.add(Bag(R.drawable.bag_image, "shawerma", "300", 3))
        bagList.add(Bag(R.drawable.bag_image, "burger", "300", 2))
        bagList.add(Bag(R.drawable.bag_image, "waffles", "300", 1))

        bagRecyclerView = view.findViewById(R.id.recyclerView_bag)
        bagAdapter = BagAdapter(view.context)
        bagRecyclerView.layoutManager = LinearLayoutManager(view.context)
        bagRecyclerView.adapter = bagAdapter
        bagAdapter.setList(bagList)

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
                bagList.removeAt(viewHolder.adapterPosition)
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

}