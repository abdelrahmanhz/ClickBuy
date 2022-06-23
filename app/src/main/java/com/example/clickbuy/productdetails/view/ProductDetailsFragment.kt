package com.example.clickbuy.productdetails.view

import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.clickbuy.R as r
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.clickbuy.databinding.FragmentProductDetailsBinding
import com.example.clickbuy.models.*
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.productdetails.viewmodel.ProductDetailsViewModel
import com.example.clickbuy.productdetails.viewmodel.ProductDetailsViewModelFactory
import com.example.clickbuy.util.ConstantsValue
import com.example.clickbuy.util.calculatePrice

const val TAG = "ProductDetailsFragment"

class ProductDetailsFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var modelFactory: ProductDetailsViewModelFactory
    private lateinit var imagesAdapter: ImagesViewPagerAdapter

    private var sizes = mutableListOf<String>()
    private var colors = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()

    private lateinit var id: String
    private var variantId: Long? = null
    private var variantPosition: Int = 0
    private lateinit var product: Product

    private var isFavourite = false
    private var favId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        observeAddedToBag()

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
        if (ConstantsValue.isLogged) {
            binding.productDetailsHeader.rightDrawable.visibility = View.VISIBLE
        }
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
        viewModel.isFavourite(id)

        viewModel.product.observe(requireActivity()) {
            if (it != null) {
                Log.i("TAG", "product: $it")
                product = it
                viewModel.isFavAndId.observe(requireActivity()) { isFavAndId ->
                    favId = isFavAndId.first
                    isFavourite = isFavAndId.second
                    Log.i(TAG, "setUpViewModel: it-------------> $it")
                    Log.i(TAG, "setUpViewModel: isFavorite-----> $isFavourite")
                    binding.productDetailsHeader.rightDrawable
                        .setImageResource(if (isFavourite) (r.drawable.ic_favorite) else (r.drawable.ic_favorite_border))
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

    private fun observeAddedToBag() {
        viewModel.isAddedToCart.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(
                    requireContext(),
                    r.string.add_to_shopping_cart_success,
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                Toast.makeText(requireContext(), r.string.add_to_shopping_cart_fail, Toast.LENGTH_SHORT)
                    .show()
            }

            binding.addToCartButton.isEnabled = true
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

        sizesAdapter?.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        colorsAdapter?.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        sizeSpinner.onItemSelectedListener = this
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
        binding.priceNumTextView.text = calculatePrice(product.variants?.get(0)?.price!!)
        binding.addToCartButton.text =
            if (product.status.equals("active")) "Add to cart" else "not available"

        // variant id
        product.variants[0].id.also { variantId = it }

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
            Log.i(TAG, "displayProduct: isFavorite----->  $isFavourite")
            if (!isFavourite) {
                Log.i(TAG, "displayProduct: + variant_id = ${product.variants?.get(0)?.id}")
                val fav = FavouriteParent(
                    Favourite(
                        note = "fav",
                        line_items = listOf(
                            FavouriteLineItem(
                                variant_id = product.variants?.get(0)?.id,
                                quantity = 1
                            )
                        ),
                        note_attributes = listOf(
                            FavouriteNoteAttribute(
                                name = "image",
                                value = product.image.src
                            )
                        )
                    )
                )
                Log.i(TAG, "displayProduct: fav = $fav")
                viewModel.addFavourite(fav)
                isFavourite = true
                binding.productDetailsHeader.rightDrawable.setImageResource(r.drawable.ic_favorite)
            } else {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.apply {

                    setTitle(resources.getString(r.string.alert_title))
                    setMessage("Do you want to remove \"${product.title}\" from your favourites?")

                    setPositiveButton(resources.getString(r.string.remove)) { _, _ ->
                        Toast.makeText(
                            context,
                            "Successfully removed!",
                            Toast.LENGTH_LONG
                        ).show()
                        viewModel.deleteFavourite(favId)
                        isFavourite = false
                        binding.productDetailsHeader.rightDrawable.setImageResource(r.drawable.ic_favorite_border)
                    }
                    setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    show()
                }
            }
        }

        // back
        binding.productDetailsHeader.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // add to cart
        binding.addToCartButton.setOnClickListener {
            //variantId = product.variants[0].id
            Log.i(TAG, "addToCartButton: ")
            if (ConstantsValue.isLogged) {
                viewModel.addItemsInBag(product, variantPosition)
                binding.addToCartButton.isEnabled = false
            } else {
                //Log.i(TAG, "addToCartButton: $variantId")
                Toast.makeText(
                    requireContext(),
                    resources.getString(r.string.guest),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun setProductId(productId: String) {
        this.id = productId
        Log.i(TAG, "setVendorName: -------> $productId")
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        variantId = product.variants?.get(p2)!!.id
        variantPosition = p2
        Toast.makeText(
            requireContext(),
            "onItemSelected: $variantId",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}