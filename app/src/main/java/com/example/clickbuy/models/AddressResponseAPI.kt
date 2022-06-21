package com.example.clickbuy.models

data class AddressResponseAPI(
    val results: List<Result>,
    val status: Status,
    val total_results: Int
)

data class Result(
    val components: Components,
    val formatted: String,
    val geometry: Geometry
)


data class Components(
    val _category: String,
    val _type: String,
    val city: String,
    val continent: String,
    val country: String,
    val neighbourhood: String,
    val country_code: String,
    val house_number: String,
    val place_of_worship: String,
    val postcode: String,
    val road: String,
    val state: String,
    val pitch: String,
    val place: String
)

data class Geometry(
    val lat: Double,
    val lng: Double
)

data class Status(
    val code: Int,
    val message: String
)