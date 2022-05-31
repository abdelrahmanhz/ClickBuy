//package com.example.clickbuy
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.viewpager2.widget.ViewPager2
//import com.example.clickbuy.databinding.FragmentTestBinding
//import com.example.clickbuy.productdetails.view.ImagesViewPagerAdapter
//
//
//class TestFragment : Fragment(), AdapterView.OnItemSelectedListener  {
//
//    private lateinit var binding : FragmentTestBinding
//    var country = arrayOf("India", "USA", "China", "Japan", "Other")
//    private var imagesList = mutableListOf(
//        "https://media.dior.com/couture/ecommerce/media/catalog/product/9/e/1594849779_043J615A0589_C980_E08_GHC.jpg?imwidth=800",
//        "https://imgs.michaels.com/MAM/assets/1/726D45CA1C364650A39CD1B336F03305/img/91F89859AE004153A24E7852F8666F0F/10093625_r.jpg?fit=inside|540:540",
//        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQCfnUYjuMS9mCi-fPniU6jY1GsSImntuEAOw&usqp=CAU"
//    )
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = FragmentTestBinding.inflate(inflater, container, false)
//        binding.itemImagesViewPager.adapter = ImagesViewPagerAdapter(imagesList)
//        binding.itemImagesViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//        binding.productInfo.detailsButton.setOnClickListener {
//            binding.productInfo.productDescTextView.maxLines = if (binding.productInfo.productDescTextView.maxLines == 3) 8  else 3
//        }
//        val spin = binding.productInfo.sizeSpinner
//        spin.onItemSelectedListener = this
//        val adapter = activity?.applicationContext?.let {
//            ArrayAdapter(
//                it,
//                android.R.layout.simple_spinner_item, country)
//        }
//        spin.adapter = adapter
//        return binding.root
//    }
//
//    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//        val type = country[p2]
//        Toast.makeText(activity,type, Toast.LENGTH_LONG).show()
//    }
//
//    override fun onNothingSelected(p0: AdapterView<*>?) {
//        TODO("Not yet implemented")
//    }
//}