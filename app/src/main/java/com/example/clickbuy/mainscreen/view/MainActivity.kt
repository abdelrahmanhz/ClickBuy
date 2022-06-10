package com.example.clickbuy.mainscreen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.clickbuy.R
import com.example.clickbuy.category.view.CategoryFragment
import com.example.clickbuy.databinding.ActivityMainBinding
import com.example.clickbuy.home.view.HomeFragment

import com.example.clickbuy.me.view.MeFragment
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.mainscreen.viewmodel.MainActivityViewModel
import com.example.clickbuy.mainscreen.viewmodel.MainActivityViewModelFactory
import com.example.clickbuy.util.ConstantsValue

class MainActivity : AppCompatActivity() {

    private lateinit var viewModelFactory: MainActivityViewModelFactory
    private lateinit var viewModel: MainActivityViewModel
    lateinit var binding: ActivityMainBinding
    private lateinit var meo: MeowBottomNavigation
    private val TAG = "MainActivity"
    private var fragmentShow: Int = 0
    private val ID_HOME = 1
    private val ID_CATEGORY = 2
    private val ID_PROFILE = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        viewModelFactory = MainActivityViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                this
            )
        )

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        viewModel.getQualifiedValueCurrency(ConstantsValue.to)

        viewModel.currencyConverter.observe(this, Observer {
            ConstantsValue.currencyValue = it.result
            Log.i(
                TAG,
                "onCreate:  ConstantsValue.currencyValue ----------> " + ConstantsValue.currencyValue
            )
            Log.i(TAG, "onCreate:  it.result ----------> " + it.result)
        })

        meo = findViewById(R.id.bottom_nav)



        meo.add(MeowBottomNavigation.Model(ID_HOME, R.drawable.home))
        meo.add(MeowBottomNavigation.Model(ID_CATEGORY, R.drawable.categories))
        meo.add(MeowBottomNavigation.Model(ID_PROFILE, R.drawable.profile))


        meo.setOnClickMenuListener {
            when (it.id) {
                ID_HOME -> {
                    fragmentShow = ID_HOME
                    replaceFragment(HomeFragment())
                }
                ID_CATEGORY -> {
                    fragmentShow = ID_CATEGORY
                    replaceFragment(CategoryFragment())
                }
                ID_PROFILE -> {
                    fragmentShow = ID_PROFILE
                    replaceFragment(MeFragment())

                }
            }
        }

        meo.setOnShowListener { item ->
            fragmentShow = item.id
        }

//        CoroutineScope(Dispatchers.Main).launch {
//            var response =
//                RetrofitClient.getInstance().getAllProducts()
//
//            Log.i(TAG, "onCreate: code---------------> " + response.code())
//            Log.i(TAG, "onCreate: body---------------> " + response.body())
//            Log.i(TAG, "onCreate: code---------------> " + response.body()?.products?.size)
//        }
        meo.show(ID_HOME, true)
//            Log.i(Companion.TAG, "onCreate: body----> " + response.body())
//            Log.i(Companion.TAG, "onCreate: products?.count()----> " + response.body()?.products?.count())
//            Log.i(Companion.TAG, "onCreate: title----> " + response.body()?.products?.get(0)?.product_type)
//
//            var subCateory =
//                RetrofitClient.getInstance().getAllSubCategoriesForSpecificCategory("273053679755")
//            Log.i(Companion.TAG, "onCreate: code---------------> " + subCateory.code())
//            Log.i(Companion.TAG, "onCreate: body----> " + subCateory.body())
//            Log.i(Companion.TAG, "onCreate: products?.count()----> " + subCateory.body()?.products?.count())
//            Log.i(
//                Companion.TAG,
//                "onCreate: product_type----> " + subCateory.body()?.products?.get(0)?.product_type
//            )
//
//            var categories = HashSet<String>()
//            for (i in 0..subCateory.body()?.products?.count()!! - 1) {
//                categories.add(subCateory.body()?.products?.get(i)?.product_type.toString())
//            }
//            Log.i(Companion.TAG, "onCreate: categories----> " + categories)
//
//            var subCategoryFilterion = HashSet<Product>()
//            for (i in 0..response.body()?.products?.count()!! - 1) {
//                if (response.body()?.products!!.get(i).product_type == "SHOES")
//                    Log.i(Companion.TAG, "SHxxx: " + response.body()?.products!!.get(i))
//            }
//
//
//            for (i in 0..response.body()?.products?.count()!! - 1) {
//                if (response.body()?.products!!.get(i).product_type == "T-SHIRTS")
//                    Log.i(Companion.TAG, "TSHxxx: " + response.body()?.products!!.get(i))
//            }
//
//
//
//            for (i in 0..response.body()?.products?.count()!! - 1) {
//                if (response.body()?.products!!.get(i).product_type == "ACCESSORIES")
//                    Log.i(Companion.TAG, "axxx: " + response.body()?.products!!.get(i))
//            }
//            var response =
//                RetrofitClient.getInstance().getAllBrandsRetro()
//            Log.i(Companion.TAG, "onCreate: code---------------> " + response.code())
//            Log.i(Companion.TAG, "onCreate: body----> " + response.body())
//            Log.i(Companion.TAG, "onCreate: products?.count()----> " + response.body()?.products?.count())

//
//        binding.bottomNavigation.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.homee -> {
//                    Log.i("TAG", "onCreate  home main activityyy: ")
//                    replaceFragment(HomeFragment())
//                }
//                R.id.category -> {
//                    Log.i("TAG", "onCreate  cat main activityyy: ")
//
//                    replaceFragment(CategoryFragment())
//                }
//
//                R.id.me ->{
//                    replaceFragment(MeFragment())
//                }
//
//                else -> Log.i("TAG", "onCreate: ttttttttttttttttt")
//
//            }
//            true
//        }

    }

    companion object {
        private const val TAG = "RetrofitClient"

    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }

    fun updateCurrency() {
        Log.i(TAG, "updateCurrency: ")
        viewModel.getQualifiedValueCurrency(ConstantsValue.to)
    }
}
