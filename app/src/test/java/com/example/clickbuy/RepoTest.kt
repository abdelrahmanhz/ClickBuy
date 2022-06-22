package com.example.clickbuy


import android.os.Parcel
import android.os.Parcelable
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.clickbuy.models.*
import com.example.clickbuy.network.RetrofitClient
import com.google.ar.core.Config
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import retrofit2.Response

@Config(sdk = [30])
@RunWith(AndroidJUnit4::class)
class RepoTest() : TestCase(), Parcelable {
//    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var dataRepository: Repository
    private lateinit var fakeRepository: FakeRepository
    var brandExcpected: MutableList<Brand> = mutableListOf()
    lateinit var brand: Response<Brands>

    constructor(parcel: Parcel) : this() {

    }

    @Before
    public override fun
            setUp() {
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
            assertEquals(brandExcpected[0].title, brand.body()?.smart_collections?.get(0)?.title)
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
    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RepoTest> {
        override fun createFromParcel(parcel: Parcel): RepoTest {
            return RepoTest(parcel)
        }

        override fun newArray(size: Int): Array<RepoTest?> {
            return arrayOfNulls(size)
        }
    }

}