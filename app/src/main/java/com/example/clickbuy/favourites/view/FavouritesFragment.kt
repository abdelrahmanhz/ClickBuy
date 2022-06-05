package com.example.clickbuy.favourites.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clickbuy.databinding.FragmentFavouritesBinding
import com.example.clickbuy.db.ConcreteLocalSource
import com.example.clickbuy.favourites.adapters.FavouritesAdapter
import com.example.clickbuy.favourites.viewmodel.FavouritesViewModel
import com.example.clickbuy.favourites.viewmodel.FavouritesViewModelFactory
import com.example.clickbuy.models.Favorite
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.google.android.material.snackbar.Snackbar


class FavouritesFragment : Fragment(), FavouritesFragmentInterface {

    private lateinit var binding : FragmentFavouritesBinding
    private lateinit var viewModel: FavouritesViewModel
    private lateinit var viewModelFactory: FavouritesViewModelFactory
    private lateinit var favouritesAdapter: FavouritesAdapter
    private lateinit var layoutManager: LinearLayoutManager


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
    }

    private fun initViewModel() {
        viewModelFactory = FavouritesViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                ConcreteLocalSource(requireContext()),
                requireContext()
            )
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(FavouritesViewModel::class.java)
    }

    private fun initRecyclerView() {
        favouritesAdapter = FavouritesAdapter(ArrayList(), this)
        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.favRecyclerView.layoutManager = layoutManager
        binding.favRecyclerView.adapter = favouritesAdapter
    }

    private fun getFavourites(){
        viewModel.favourites.observe(requireActivity()){
            if (!it.isNullOrEmpty()) {
                Log.i("TAG", "product: $it")
                displayFavourites(it)
            }
            else{
                binding.favRecyclerView.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.favEmptyImageView.visibility = View.VISIBLE
            }
        }
        viewModel.getFavourites()
    }

    private fun displayFavourites(it: List<Favorite>) {
        favouritesAdapter.setFavourites(it as ArrayList<Favorite>)
        binding.favRecyclerView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun deleteFavouriteItem(favorite: Favorite) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.apply {

            setTitle("Removing Alert")
            setMessage("Do you want to remove \"${favorite.title}\" from your favourites?")

            setPositiveButton("Remove"){ _, _ ->
                viewModel.deleteFavourite(favorite.id)
                Snackbar.make(
                    binding.favRecyclerView,
                    "An Item deleted",
                    Snackbar.LENGTH_LONG
                ).setAction("undo") {
                    viewModel.addFavourite(favorite)
                }.show()
            }
            setNegativeButton("Cancel"){ dialog, _ -> dialog.dismiss()}
            show()
        }
    }

    override fun showFavouriteItemDetails(id: Long) {
        TODO("Not yet implemented")
    }
}