package com.example.clickbuy.bag.view

interface UpdatingItemsAtBag {
    fun onQuantityIncreased(position: Int)
    fun onQuantityDecreased(position: Int)
}