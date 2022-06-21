package com.example.clickbuy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.clickbuy.models.*
import com.example.clickbuy.network.RetrofitClient
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import retrofit2.Response

@Config(sdk = [30])
@RunWith(AndroidJUnit4::class)
class RepoTest : TestCase() {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var dataRepository: Repository
    private lateinit var fakeRepository: FakeRepository
    var brandExcpected: MutableList<Brand> = mutableListOf()
    lateinit var brand: Response<Brands>

    @Before
    public override fun setUp() {
        val appContext =
            androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().context

        dataRepository = Repository.getInstance(
            RetrofitClient.getInstance(), appContext
        )
        fakeRepository = FakeRepository()
    }
    @Test
    fun data_storeUser_returnUserDetailsStored() {
        runBlocking {
         //   val result = dataRepository.getAllBrands()
           brand = fakeRepository.getAllBrands()
            brandExcpected.add(Brand(title = "ADIDAS"))
            assertEquals(brandExcpected[0].title,brand)
        }
    }

//    fun getAllProducts(): Response<Products> {
//        var lineItems: MutableList<Product> = mutableListOf()
//        lineItems.add(Product(22,body_html = "dddd", product_type = "", image = null))
//        lineItems.add(Product(22, product_type = "", image = null))
//        lineItems.add(Product(22, product_type = "", image = null))
//        lineItems.add(Product(22, product_type = "", image = null))
//        lineItems.add(Product(22, product_type = "", image = null))
//        lineItems.add(Product(22, product_type = "", image = null))
//        lineItems.add(Product(22, product_type = "", image = null))
//        lineItems.add(Product(22, product_type = "", image = null))
//        lineItems.add(Product(22, product_type = "", image = null))
//        lineItems.add(Product(22, product_type = "", image = null))
//        lineItems.add(Product(22, product_type = "", image = null))
//        lineItems.add(Product(22, product_type = "", image = null))
//        lineItems.add(Product(22, product_type = "", image = null))
//        lineItems.add(Product(22, product_type = "", image = null))
//        lineItems.add(Product(22, product_type = "", image = null))
//        val products = Products(lineItems)
//        return Response.success(200, products)
//    }

    fun setAllBrands(): Response<Brands> {
        var brandItems: MutableList<Brand> = mutableListOf()
        brandItems.add(Brand(title = "ADIDAS"))
        return Response.success(200, Brands(brandItems))
    }
}