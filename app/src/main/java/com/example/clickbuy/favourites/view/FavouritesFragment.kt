package com.example.clickbuy.favourites.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clickbuy.R
import com.example.clickbuy.databinding.FragmentFavouritesBinding
import com.example.clickbuy.favourites.view.adapters.FavouritesAdapter
import com.example.clickbuy.favourites.viewmodel.FavouritesViewModel
import com.example.clickbuy.favourites.viewmodel.FavouritesViewModelFactory
import com.example.clickbuy.models.Favourite
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.productdetails.view.ProductDetailsFragment
import com.example.clickbuy.util.ConnectionLiveData



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
    ): View {
        binding = FragmentFavouritesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        initViewModel()
        initRecyclerView()
        checkInternetConnection()
        getFavourites()
    }

    private fun checkInternetConnection() {
        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                binding.favNoInternetAnimation.visibility = View.GONE
                binding.favEnableConnection.visibility = View.GONE
//                if(favorites.isEmpty()) binding.favEmptyAnimation.visibility = View.VISIBLE
                binding.favRecyclerView.visibility = View.VISIBLE
                viewModel.getFavourites()
            } else {
                binding.favNoInternetAnimation.visibility = View.VISIBLE
                binding.favEnableConnection.visibility = View.VISIBLE
                binding.favEmptyAnimation.visibility = View.GONE
                binding.favRecyclerView.visibility = View.GONE
            }
        }

        binding.favEnableConnection.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
            } else {
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
        }
    }

    private fun setupUI() {
        binding.favHeader.rightDrawable.visibility = View.GONE
        binding.favHeader.titleTv.text = getString(R.string.favourites)
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
                binding.favEmptyAnimation.visibility = View.VISIBLE
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

            setTitle(getString(R.string.alert_title))
            setMessage(getString(R.string.want_to_remove) + favorite.line_items[0].title + getString(R.string.from_favourites))

            setPositiveButton(getString(R.string.remove)) { _, _ ->
                viewModel.deleteFavourite(favorite.id.toString())
                favorites.removeAt(position)
                favouritesAdapter.setFavourites(favorites)
                Toast.makeText(
                    context,
                    getString(R.string.successfully_removed),
                    Toast.LENGTH_LONG).show()
                if (favorites.isEmpty()) {
                    binding.favRecyclerView.visibility = View.GONE
                    binding.favEmptyAnimation.visibility = View.VISIBLE
                }
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
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