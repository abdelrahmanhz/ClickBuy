package com.example.clickbuy.orders.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.clickbuy.R
import com.example.clickbuy.models.BagItem
import com.example.clickbuy.models.NoteAttribute
import com.example.clickbuy.models.ShoppingBag

private const val TAG = "AddressOrder"

class AddressOrderActivity : AppCompatActivity() {
    var bagList: List<BagItem> = emptyList()
    var imagesList: List<NoteAttribute> = emptyList()
    lateinit var totalAmountPrice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_order)

        val list = intent.getSerializableExtra("TEST") as ShoppingBag

        Log.i(TAG, "onCreate: list---------------> ${list.draft_order.note_attributes.size}")
        Log.i(TAG, "onCreate: list---------------> ${list.draft_order.line_items.size}")

        bagList = list.draft_order.line_items
        imagesList = list.draft_order.note_attributes
        totalAmountPrice = list.draft_order.subtotal_price
        replaceFragment(OrderAddressFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameOrderAddress, fragment).commit()
    }
}