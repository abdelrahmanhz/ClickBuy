package com.example.clickbuy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.clickbuy.models.*
import com.example.clickbuy.network.RetrofitClient
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.experimental.categories.Categories
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
    var categoryExcpected: MutableList<Product> = mutableListOf()
    var addressExcpected: MutableList<CustomerAddress> = mutableListOf()
    var lineItemsExcpected: MutableList<BagItem> = mutableListOf()
    var noteAttributesExcpected: MutableList<NoteAttribute> = mutableListOf()

    lateinit var brand: Response<Brands>
    lateinit var category: Response<Products>
    lateinit var address: Response<CustomerAddresses>
    lateinit var bag:  Response<ShoppingBag>
    @Before
    public override fun
            setUp() {
        val appContext = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().context
        fakeRepository = FakeRepository()
        dataRepository = Repository.getInstance(fakeRepository, appContext)
    }
    @Test
    fun getAllBrandsTest() {
        runBlocking {
           brand = dataRepository.getAllBrands()
            brandExcpected.add(Brand(title = "ADIDAS"))
            assertEquals(brandExcpected[0].title, brand.body()?.smart_collections?.get(0)?.title)
        }
    }
    @Test
    fun getAllCategoriesTest() {
        runBlocking {
            category = dataRepository.getAllProducts("","","")
            categoryExcpected.add(Product(title = "product"))
            assertEquals(brandExcpected[0].title, brand.body()?.smart_collections?.get(0)?.title)
        }
    }
    @Test
    fun getAllAddresses(){
        runBlocking {
            address = dataRepository.getAllAddresses()
            addressExcpected.add(CustomerAddress(city = "Alexandri"))
            assertEquals(addressExcpected[0].city, address.body()?.addresses?.get(0)?.city)
        }
    }
    @Test
    fun getAllItemsInBag(){
        runBlocking {
            bag = dataRepository.getAllItemsInBag()
            lineItemsExcpected.add(BagItem(price = "10", quantity = 0, variant_id = 123456785))
            noteAttributesExcpected.add(NoteAttribute("0", "image"))
            assertEquals(lineItemsExcpected[0].price, bag.body()?.draft_order?.line_items?.get(0)?.price)
        }
    }
}