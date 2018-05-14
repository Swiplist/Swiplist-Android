package com.energy0124.swiplist.feature.model

import com.squareup.moshi.Json
import java.io.Serializable

//class User {
//    @Json(name = "_id") lateinit var id: String
//    lateinit var username: String
//    lateinit var email: String
//    var description: String? = null
//    var friends: List<String> = listOf<String>()
//    var items: List<String> = listOf<String>()
//    var games: List<Item> = listOf<Item>()
//    var anime: List<Item> = listOf<Item>()
//    var manga: List<Item> = listOf<Item>()
//    var iconUrl: String? = null
//    var mobileNumber: String? = null
//}

data class User(
        @Json(name = "_id") val id: String = "",
        val username: String = "",
        val email: String = "",
        val description: String? = null,
        val friends: List<String> = listOf<String>(),
        val items: List<String> = listOf<String>(),
        var games: List<Item> = listOf<Item>(),
        var anime: List<Item> = listOf<Item>(),
        var manga: List<Item> = listOf<Item>(),
        val iconUrl: String? = null,
        val mobileNumber: String? = null
) : Serializable