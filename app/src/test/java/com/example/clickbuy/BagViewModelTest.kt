package com.example.clickbuy

import android.os.Looper.getMainLooper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.clickbuy.bag.viewmodel.BagViewModel
import com.example.clickbuy.category.viewmodel.CategoryViewModel
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
class BagViewModelTest : TestCase() {
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
    fun getAllItemsInBags() {
        runBlocking {
            val viewModel = BagViewModel(dataRepository)
            viewModel.getAllItemsInBag()
            shadowOf(getMainLooper()).idle()
            val tasks = viewModel.shoppingBag.getOrAwaitValue()
            assertEquals(1, tasks.draft_order.line_items.size)
        }
    }
}