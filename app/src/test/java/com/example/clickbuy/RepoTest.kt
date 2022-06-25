package com.example.clickbuy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.clickbuy.models.*
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
    fun getProductByID_fetchProduct_returnSpecificProduct(){
        runBlocking {
            val product = dataRepository.getProductById("6870134227083")
            val productExpected = Product(id = 6870134227083, title = "ADIDAS | SUPERSTAR 80S")
            assertEquals(productExpected.title, product.body()?.product?.title!!)
        }
    }

    @Test
    fun signIn_checkEmailAndPasswordMatching_returnResponseMessage(){
        runBlocking {
            val loggingMessage = dataRepository.signIn("ahmza@gmail.com", "12345674")
            val loggingMessageExpected = "Entered a wrong password"
            assertEquals(loggingMessageExpected, loggingMessage)
        }
    }

    @Test
    fun signUp_addNewCustomer_returnCustomerDetails(){
        runBlocking {
            val customer = fakeRepository.registerCustomer(CustomerParent(Customer(email = "test@test.com")))
            val customerExpected = Customer(email = "test@test.com")
            assertEquals(customerExpected.email, customer.body()?.customer!!.email)
        }
    }

    @Test
    fun getFavourites_fetchAllFavourites_returnAllFavourites(){
        runBlocking {
            val favourites = fakeRepository.getDraftOrders()
            val favouritesExpected = mutableListOf<Favourite>()
            favouritesExpected.add(Favourite(line_items = listOf(FavouriteLineItem(id = 30335555035274))))
            favouritesExpected.add(Favourite(line_items = listOf(FavouriteLineItem(id = 40335555035275))))
            assertNotSame(favouritesExpected[0].line_items[0].id, favourites.body()?.draft_orders?.get(0)?.line_items?.get(0)?.id)
            assertEquals(favouritesExpected[1].line_items[0].id, favourites.body()?.draft_orders?.get(0)?.line_items?.get(0)?.id)
        }
    }

    @Test
    fun addFavourite_modifyFavourite_returnAddedFavourite(){
        runBlocking {
            val favouriteExpected = FavouriteParent(Favourite(line_items = listOf(FavouriteLineItem(id = 30335555035274))))
            val favourite = fakeRepository.addFavourite(FavouriteParent(Favourite(line_items = listOf(FavouriteLineItem(id = 30335555035274)))))
            assertEquals(favouriteExpected.draft_order!!.line_items[0].id, favourite.body()?.draft_order?.line_items?.get(0)!!.id)
        }
    }

    @Test
    fun removeFavourite_checkFavouriteId(){
        runBlocking{
            val responseCodeExpected = 200
            val responseCode = fakeRepository.removeFavourite(id = "873157656715").code()
            assertEquals(responseCodeExpected, responseCode)
        }
    }
    @Test
    fun getAllCategoriesTest() {
        runBlocking {
            category = dataRepository.getAllProducts("","","")
            categoryExcpected.add(Product(title = "product"))
            assertEquals(categoryExcpected[0].title , category.body()?.products?.get(0)?.title)
            assertEquals(categoryExcpected[0].title, category.body()?.products?.get(0)?.title)
        }
    }
    @Test
    fun getAllAddresses(){
        runBlocking {
            address = dataRepository.getAllAddresses()
            addressExcpected.add(CustomerAddress(city = "Alexandria"))
            assertEquals(addressExcpected[0].city, address.body()?.addresses?.get(0)?.city)
        }
    }
    @Test
    fun getAllItemsInBag(){
        runBlocking {
            bag = dataRepository.getAllItemsInBag()
            lineItemsExcpected.add(BagItem(price = "100", quantity = 0, variant_id = 123456785))
            noteAttributesExcpected.add(NoteAttribute("0", "image"))
            assertEquals(lineItemsExcpected[0].price, bag.body()?.draft_order?.line_items?.get(0)?.price)
        }
    }
}