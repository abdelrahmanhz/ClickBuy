package com.example.clickbuy.bag.view

import com.example.clickbuy.models.BagItem
import com.example.clickbuy.models.NoteAttribute

interface ListOfBagInterface {
    fun setListOfBag(bagList: List<BagItem> , imagesList: List<NoteAttribute> )
}