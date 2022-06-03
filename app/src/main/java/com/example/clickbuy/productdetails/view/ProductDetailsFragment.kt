package com.example.clickbuy.productdetails.view

import android.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.clickbuy.databinding.FragmentProductDetailsBinding
import com.example.clickbuy.models.Product
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.productdetails.adapters.ImagesViewPagerAdapter
import com.example.clickbuy.productdetails.adapters.ProductReviewsAdapter
import com.example.clickbuy.productdetails.viewmodel.ProductDetailsViewModel
import com.example.clickbuy.productdetails.viewmodel.ProductDetailsViewModelFactory

const val TAG = "ProductDetailsFragment"

class ProductDetailsFragment : Fragment()  {

    private lateinit var binding : FragmentProductDetailsBinding
    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var modelFactory: ProductDetailsViewModelFactory
    private lateinit var imagesAdapter: ImagesViewPagerAdapter

    private var sizes = mutableListOf<String>()
    private var colors = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        //setUpSpinners()
        setUpReviews()
        setUpViewModel()

    }

    private fun hideUIComponents() {
        binding.progressBar.visibility = View.VISIBLE
        binding.itemImagesViewPager.visibility = View.GONE
        binding.productInfo.productBottomSheet.visibility = View.GONE
        binding.cardView.visibility = View.GONE
    }

    private fun showUIComponent() {
        binding.progressBar.visibility = View.GONE
        binding.itemImagesViewPager.visibility = View.VISIBLE
        binding.productInfo.productBottomSheet.visibility = View.VISIBLE
        binding.cardView.visibility = View.VISIBLE
    }

    private fun setUpViewModel() {
        modelFactory = ProductDetailsViewModelFactory(Repository.getInstance(RetrofitClient.getInstance(), requireContext()))
        viewModel = ViewModelProvider(this, modelFactory).get(ProductDetailsViewModel::class.java)
        viewModel.getProductById("6870135046283")
        viewModel.product.observe(requireActivity()) {
            if (it != null) {
                Log.i("TAG", "product: $it")
                displayProduct(it)
                showUIComponent()
            }
        }
    }

    private fun setUpReviews() {
        binding.productInfo.reviewsRecyclerView.layoutManager = LinearLayoutManager(context)
        val reviewAdapter = ProductReviewsAdapter()
        binding.productInfo.reviewsRecyclerView.adapter = reviewAdapter
    }

    private fun setUpSpinners() {
        val sizeSpinner = binding.productInfo.sizeSpinner
        val colorSpinner = binding.productInfo.colorSpinner
        val sizesAdapter = activity?.applicationContext?.let {
            ArrayAdapter(
                it,
                R.layout.simple_spinner_item, sizes)
        }
        val colorsAdapter = activity?.applicationContext?.let {
            ArrayAdapter(
                it,
                R.layout.simple_spinner_item, colors)
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
        binding.productInfo.productTitle.text = product.title
        binding.productInfo.productDescTextView.text = product.body_html
        binding.productInfo.productAvailability.text = product.status
        binding.priceNumTextView.text = product.variants?.get(0)?.price ?: "No price available"
        binding.addToCartButton.text = if(product.status.equals("active")) "available" else "not available"

        // images
        product.images?.forEach { image -> imagesList.add(image.src)}
        imagesAdapter.setImages(imagesList)

        // spinners
        product.options?.get(0)?.values?.forEach { value -> sizes.add(value)}
        product.options?.get(1)?.values?.forEach { value -> colors.add(value)}
        setUpSpinners()

        // details
        binding.productInfo.detailsButton.setOnClickListener {
            binding.productInfo.productDescTextView.maxLines = if (binding.productInfo.productDescTextView.maxLines == 3) 8 else 3
        }

        // favorite
        binding.header.rightDrawable.setOnClickListener{
            TODO()
        }

        // back
        binding.header.backBtn.setOnClickListener {
            TODO()
        }

        // add to cart
        binding.addToCartButton.setOnClickListener{
            TODO()
        }
    }
}

