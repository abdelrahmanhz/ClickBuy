package com.example.clickbuy

import android.os.Looper.getMainLooper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.clickbuy.models.Repository
import com.example.clickbuy.productdetails.viewmodel.ProductDetailsViewModel
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@Config(sdk = [30])
@RunWith(AndroidJUnit4::class)
class ProductDetailsViewModelTest: TestCase() {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var dataRepository: Repository
    private lateinit var fakeRepository: FakeRepository
    private val appContext =
        androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().context

    @Before
    public override fun setUp() {
        fakeRepository = FakeRepository()
        dataRepository = Repository.getInstance(
            fakeRepository, appContext
        )
    }

    @Test
    fun getProductById() {
        runBlocking {
            val productId = "6870134227083"
            val productTitleExpected = "ADIDAS | SUPERSTAR 80S"
            val viewModel = ProductDetailsViewModel(dataRepository)
            viewModel.getProductById(productId)
            shadowOf(getMainLooper()).idle()
            val task = viewModel.product.getOrAwaitValue()
            assertEquals(productTitleExpected, task.title)
        }
    }

    @Test
    fun isFavourite() {
        runBlocking {
            val productId = "40335555035275"
            val viewModel = ProductDetailsViewModel(dataRepository)
            viewModel.isFavourite(productId)
            shadowOf(getMainLooper()).idle()
            val task = viewModel.isFavAndId.getOrAwaitValue()
            assertEquals(true, task.second)
        }
    }
}