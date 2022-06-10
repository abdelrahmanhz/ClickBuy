package com.example.clickbuy.orders

import com.example.clickbuy.models.ItemImage
import com.example.clickbuy.models.LineItem

interface OrderDetailsInterface {
    fun showOrderDetails( lineItemList : List<LineItem> , itemImageList : List<ItemImage>)
}