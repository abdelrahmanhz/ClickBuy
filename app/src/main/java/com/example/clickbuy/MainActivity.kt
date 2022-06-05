package com.example.clickbuy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< Updated upstream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        }
    }
=======
import android.util.Log
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.clickbuy.category.view.CategoryFragment
import com.example.clickbuy.databinding.ActivityMainBinding
import com.example.clickbuy.home.view.HomeFragment
import com.example.clickbuy.me.view.MeFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var meo: MeowBottomNavigation
    private val TAG = "MainActivity"
    private var fragmentShow: Int = 0
    private val ID_HOME = 1
    private val ID_CATEGORY = 2
    private val ID_PROFILE = 3
    private val homeFragment = HomeFragment()
    private val categoryFragment = CategoryFragment()
    private val meFragment = MeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        meo = findViewById(R.id.bottom_nav)

        meo.add(MeowBottomNavigation.Model(ID_HOME, R.drawable.home))
        meo.add(MeowBottomNavigation.Model(ID_CATEGORY, R.drawable.categories))
        meo.add(MeowBottomNavigation.Model(ID_PROFILE, R.drawable.profile))


        meo.setOnClickMenuListener {
            when (it.id) {
                ID_HOME -> {
                    fragmentShow = ID_HOME
                    replaceFragment(homeFragment)
                }
                ID_CATEGORY -> {
                    fragmentShow = ID_CATEGORY
                    replaceFragment(categoryFragment)
                }
                ID_PROFILE -> {
                    fragmentShow = ID_PROFILE
                    replaceFragment(meFragment)

                }
            }
        }

        meo.setOnShowListener { item ->
            fragmentShow = item.id
        }

        meo.show(ID_HOME, true)
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

}
>>>>>>> Stashed changes
