package com.example.clickbuy.productdetails.view

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.clickbuy.databinding.FragmentProductDetailsBinding
import com.example.clickbuy.models.Review


class ProductDetailsFragment : Fragment()  {

    private lateinit var binding : FragmentProductDetailsBinding

    private var sizes = arrayOf("S", "M", "L", "XL", "XXL", "XXXL")
    private var colors = arrayOf("White", "Black", "Gray", "Blue", "Red", "Yellow", "Green")

    private var reviews = listOf(
        Review("Ahmed S.", 3.7f, "Oversize.", 1),
        Review("Mohammed K.", 4.5f, "it’s a really nice t-shirts and i will definitely buy it again", 1),
        Review("Hanna A.", 5f, "Nice and loose.", 1),
        Review("Aya H.", 5f, "love this too so much! quality is great.", 1),
        Review("Asmaa H.", 2.8f, "nice material", 1),
        Review("Hager S.", 3.2f, "beautiful colors, looks just like the pictures.", 1),
        Review("Hala N.", 4.8f, "Very nice. Thanks for it!!", 1),
        Review("Naira R.", 4.8f, "Really good quality!! I absolutely love this!", 1),
    )

    private var imagesList = mutableListOf(
        "https://media.dior.com/couture/ecommerce/media/catalog/product/9/e/1594849779_043J615A0589_C980_E08_GHC.jpg?imwidth=800",
        "https://imgs.michaels.com/MAM/assets/1/726D45CA1C364650A39CD1B336F03305/img/91F89859AE004153A24E7852F8666F0F/10093625_r.jpg?fit=inside|540:540",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQCfnUYjuMS9mCi-fPniU6jY1GsSImntuEAOw&usqp=CAU"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)

        // images
        binding.itemImagesViewPager.adapter = ImagesViewPagerAdapter(imagesList)
        binding.itemImagesViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.productInfo.detailsButton.setOnClickListener {
            binding.productInfo.productDescTextView.maxLines = if (binding.productInfo.productDescTextView.maxLines == 3) 8 else 3
        }

        // spinners
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
        binding.productInfo.reviewsRecyclerView.layoutManager = LinearLayoutManager(context)
        val reviewAdapter = ProductReviewsAdapter(reviews)
        binding.productInfo.reviewsRecyclerView.adapter = reviewAdapter

        return binding.root
    }
}

