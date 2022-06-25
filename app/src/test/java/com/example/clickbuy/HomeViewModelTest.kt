package com.example.clickbuy

import android.os.Looper.getMainLooper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.clickbuy.home.viewmodel.HomeViewModel
import com.example.clickbuy.models.Brand
import com.example.clickbuy.models.Brands
import com.example.clickbuy.models.Repository
import com.example.clickbuy.productdetails.viewmodel.ProductDetailsViewModel
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import retrofit2.Response

@Config(sdk = [30])
@RunWith(AndroidJUnit4::class)
class HomeViewModelTest : TestCase() {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var dataRepository: Repository
    private lateinit var fakeRepository: FakeRepository
    val appContext =
        androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().context

    @Before
    public override fun
            setUp() {

        fakeRepository = FakeRepository()
        dataRepository = Repository.getInstance(
            fakeRepository, appContext
        )
    }
    @Test
    fun getAllBrands() {
        runBlocking {
            val viewModel = HomeViewModel(dataRepository)
            viewModel.getAllBrands()
            shadowOf(getMainLooper()).idle()
            val tasks = viewModel.brand.getOrAwaitValue()
            assertEquals(1, tasks.smart_collections.size)
        }
    }
}