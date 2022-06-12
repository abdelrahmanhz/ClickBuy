package com.example.clickbuy.productdetails.view

import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clickbuy.R as r
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.clickbuy.databinding.FragmentProductDetailsBinding
import com.example.clickbuy.models.Favorite
import com.example.clickbuy.models.Product
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.productdetails.viewmodel.ProductDetailsViewModel
import com.example.clickbuy.productdetails.viewmodel.ProductDetailsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlin.math.log

const val TAG = "ProductDetailsFragment"

class ProductDetailsFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var modelFactory: ProductDetailsViewModelFactory
    private lateinit var imagesAdapter: ImagesViewPagerAdapter

    private var sizes = mutableListOf<String>()
    private var colors = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()
    private lateinit var  id :String
    private var isFavourite = false
    private var favorite = Favorite(0, "", "", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideUIComponents()
        setUpImagesPager()
        setUpReviews()
        setUpViewModel()

    }

    private fun hideUIComponents() {
        binding.progressBar.visibility = View.VISIBLE
        binding.itemImagesViewPager.visibility = View.GONE
        binding.productInfo.productBottomSheet.visibility = View.GONE
        binding.cardView.visibility = View.GONE
        binding.productDetailsHeader.rightDrawable.visibility = View.GONE
    }

    private fun showUIComponent() {
        binding.progressBar.visibility = View.GONE
        binding.itemImagesViewPager.visibility = View.VISIBLE
        binding.productInfo.productBottomSheet.visibility = View.VISIBLE
        binding.cardView.visibility = View.VISIBLE
        binding.productDetailsHeader.rightDrawable.visibility = View.VISIBLE
    }

    private fun setUpViewModel() {
        modelFactory = ProductDetailsViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )
        viewModel = ViewModelProvider(this, modelFactory)
            .get(ProductDetailsViewModel::class.java)
        viewModel.getProductById(id)
        viewModel.isFavourite(6870135046283)
        viewModel.product.observe(requireActivity()) {
            if (it != null) {
                Log.i("TAG", "product: $it")
                favorite = Favorite(it.id!!, it.title!!, it.variants!![0].price, it.image!!.src)
                viewModel.isFav.observe(requireActivity()) { it ->
                    isFavourite = it
                    Log.i(TAG, "setUpViewModel: it-------------> " + it)
                    Log.i(TAG, "setUpViewModel: isFavorite-----> " + isFavourite)
                }
                displayProduct(it)
                showUIComponent()
            } else {
                binding.progressBar.visibility = View.GONE
                binding.itemImagesViewPager.visibility = View.GONE
                binding.productInfo.productBottomSheet.visibility = View.GONE
                binding.cardView.visibility = View.GONE
                binding.productDetailsHeader.rightDrawable.visibility = View.GONE
                binding.productDetailsEmptyImageView.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpReviews() {
        binding.productInfo.reviewsRecyclerView.layoutManager = LinearLayoutManager(context)
        val reviewAdapter = ProductReviewsAdapter()
        binding.productInfo.reviewsRecyclerView.adapter = reviewAdapter
    }

     fun setIdProduct(id:String){
        this.id = id
        Log.i(TAG, "setIdProduct: " + id)
    }
    private fun setUpSpinners() {
        val sizeSpinner = binding.productInfo.sizeSpinner
        val colorSpinner = binding.productInfo.colorSpinner
        val sizesAdapter = activity?.applicationContext?.let {
            ArrayAdapter(
                it,
                R.layout.simple_spinner_item, sizes
            )
        }
        val colorsAdapter = activity?.applicationContext?.let {
            ArrayAdapter(
                it,
                R.layout.simple_spinner_item, colors
            )
        }
        sizeSpinner.adapter = sizesAdapter
        colorSpinner.adapter = colorsAdapter
    }

    private fun setUpImagesPager() {
        imagesAdapter = ImagesViewPagerAdapter(imagesList)
        binding.itemImagesViewPager.adapter = imagesAdapter
        binding.itemImagesViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    private fun displayProduct(product: Product) {
        binding.productDetailsHeader.rightDrawable.let {
            it.setImageResource(if (isFavourite) (r.drawable.ic_favorite) else (r.drawable.ic_favorite_border))
        }
        binding.productInfo.productTitle.text = product.title
        binding.productInfo.productDescTextView.text = product.body_html
        binding.productInfo.productAvailability.text = product.status
        binding.priceNumTextView.text = product.variants?.get(0)?.price ?: "No price available"
        binding.addToCartButton.text =
            if (product.status.equals("active")) "Add to cart" else "not available"

        // images
        product.images?.forEach { image -> imagesList.add(image.src) }
        imagesAdapter.setImages(imagesList)

        // spinners
        product.options?.get(0)?.values?.forEach { value -> sizes.add(value) }
        product.options?.get(1)?.values?.forEach { value -> colors.add(value) }
        setUpSpinners()

        // details
        binding.productInfo.detailsButton.setOnClickListener {
            binding.productInfo.productDescTextView.maxLines =
                if (binding.productInfo.productDescTextView.maxLines == 3) 8 else 3
        }

        // favorite
        binding.productDetailsHeader.rightDrawable.setOnClickListener {
            Log.i(TAG, "displayProduct: isFavorite----->  " + isFavourite)
            if (!isFavourite) {
                viewModel.addFavourite(favorite)
                isFavourite = true
                binding.productDetailsHeader.rightDrawable.setImageResource(r.drawable.ic_favorite)
            } else {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.apply {

                    setTitle("Removing Alert")
                    setMessage("Do you want to remove \"${favorite.title}\" from your favourites?")

                    setPositiveButton("Remove") { _, _ ->
                        viewModel.deleteFavourite(favorite.id)
                        isFavourite = false
                        binding.productDetailsHeader.rightDrawable.setImageResource(r.drawable.ic_favorite_border)
                        Snackbar.make(
                            binding.cardView,
                            "An Item deleted",
                            Snackbar.LENGTH_LONG
                        ).setAction("undo") {
                            viewModel.addFavourite(favorite)
                        }.show()
                    }
                    setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    show()
                }
            }
        }

        // back
        binding.productDetailsHeader.backBtn.setOnClickListener {
          //  TODO()
        }

        // add to cart
        binding.addToCartButton.setOnClickListener {
            //TODO()
        }
    }

    fun setProductId(productId: String) {
        this.id = productId
        Log.i(TAG, "setVendorName: -------> $productId")
    }

//    fun setProductIdFromCategory(productId: String) {
//        this.id = productId
//        Log.i(TAG, "setVendorName: -------> $productId")
//    }

}

