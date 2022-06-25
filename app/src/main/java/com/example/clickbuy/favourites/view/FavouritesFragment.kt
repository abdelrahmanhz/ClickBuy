package com.example.clickbuy.favourites.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clickbuy.R
import com.example.clickbuy.databinding.FragmentFavouritesBinding
import com.example.clickbuy.favourites.adapters.FavouritesAdapter
import com.example.clickbuy.favourites.viewmodel.FavouritesViewModel
import com.example.clickbuy.favourites.viewmodel.FavouritesViewModelFactory
import com.example.clickbuy.models.Favourite
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.productdetails.view.ProductDetailsFragment



class FavouritesFragment : Fragment(), FavouritesFragmentInterface {

    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var viewModel: FavouritesViewModel
    private lateinit var viewModelFactory: FavouritesViewModelFactory
    private lateinit var favouritesAdapter: FavouritesAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var favorites = ArrayList<Favourite>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        initViewModel()
        initRecyclerView()
        getFavourites()
    }

    private fun setupUI() {
        binding.favHeader.rightDrawable.visibility = View.GONE
        binding.favHeader.titleTv.text = "Favourites"
        binding.favRecyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        // back
        binding.favHeader.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun initViewModel() {
        viewModelFactory = FavouritesViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(FavouritesViewModel::class.java)
        viewModel.getFavourites()
    }

    private fun initRecyclerView() {
        favouritesAdapter = FavouritesAdapter(ArrayList(), this)
        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.favRecyclerView.layoutManager = layoutManager
        binding.favRecyclerView.adapter = favouritesAdapter
    }

    private fun getFavourites() {
        viewModel.favourites.observe(requireActivity()) {
            if (!it.draft_orders.isNullOrEmpty()) {
                favorites = it.draft_orders as ArrayList<Favourite>
                displayFavourites(favorites)
            } else {
                binding.favRecyclerView.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.favEmptyImageView.visibility = View.VISIBLE
            }
        }

    }

    private fun displayFavourites(it: ArrayList<Favourite>) {
        favouritesAdapter.setFavourites(it)
        binding.favRecyclerView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun deleteFavouriteItem(favorite: Favourite, position: Int) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.apply {

            setTitle("Removing Alert")
            setMessage("Do you want to remove \"${favorite.line_items?.get(0)?.title}\" from your favourites?")

            setPositiveButton("Remove") { _, _ ->
                viewModel.deleteFavourite(favorite.id.toString())
                favorites.removeAt(position)
                favouritesAdapter.setFavourites(favorites)
                Toast.makeText(
                    context,
                    "Successfully removed!",
                    Toast.LENGTH_LONG).show()
                if (favorites.isEmpty()) {
                    binding.favRecyclerView.visibility = View.GONE
                    binding.favEmptyImageView.visibility = View.VISIBLE
                }
            }
            setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            show()
        }
    }

    override fun showFavouriteItemDetails(id: Long) {
        val favItemDetails = ProductDetailsFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, favItemDetails)
            .addToBackStack(null).commit()
        favItemDetails.setProductId(id.toString())
    }
}