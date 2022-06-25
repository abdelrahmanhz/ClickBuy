package com.example.clickbuy


import android.os.Looper.getMainLooper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.clickbuy.category.viewmodel.CategoryViewModel
import com.example.clickbuy.home.viewmodel.HomeViewModel
import com.example.clickbuy.models.Repository
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
class CategoryViewModelTest : TestCase() {
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
    fun getAllProductsForCategory() {
        runBlocking {
            val viewModel = CategoryViewModel(dataRepository)
            viewModel.getAllProducts("273053679755","","")
            shadowOf(getMainLooper()).idle()
            val tasks = viewModel.subCategory.getOrAwaitValue()
            assertEquals(1, tasks.products.size)
        }
    }
}